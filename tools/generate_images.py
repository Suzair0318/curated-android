"""Generate images with Cloudflare Workers AI (FLUX.1 schnell) and save them to a folder.

    python tools\\generate_images.py "a maroon embroidered kurta on a mannequin, studio photo" --count 4
    python tools\\generate_images.py --file prompts.txt --count 2

The account id and token are read from local.properties (CF_ACCOUNT_ID, CF_API_TOKEN).
Each image is one request; the free plan allows 10,000 Neurons a day and requests past
that fail until 00:00 UTC.
"""
import argparse
import base64
import json
import os
import re
import sys
import time
import urllib.error
import urllib.request

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
MODEL = "@cf/black-forest-labs/flux-1-schnell"


def credentials():
    props = {}
    with open(os.path.join(ROOT, "local.properties"), encoding="utf-8") as f:
        for line in f:
            if "=" in line and not line.lstrip().startswith("#"):
                key, value = line.split("=", 1)
                props[key.strip()] = value.strip()
    account, token = props.get("CF_ACCOUNT_ID"), props.get("CF_API_TOKEN")
    if not account or not token:
        sys.exit("CF_ACCOUNT_ID and CF_API_TOKEN must both be set in local.properties")
    return account, token


def generate(account, token, prompt, steps):
    # The model takes no seed; it picks a new one for every request, so repeats differ.
    request = urllib.request.Request(
        f"https://api.cloudflare.com/client/v4/accounts/{account}/ai/run/{MODEL}",
        data=json.dumps({"prompt": prompt, "steps": steps}).encode("utf-8"),
        headers={"Authorization": f"Bearer {token}", "Content-Type": "application/json"},
    )
    try:
        with urllib.request.urlopen(request, timeout=120) as response:
            body = json.load(response)
    except urllib.error.HTTPError as error:
        detail = error.read().decode("utf-8", "replace")
        raise RuntimeError(f"HTTP {error.code}: {detail[:400]}") from None
    if not body.get("success") or "image" not in (body.get("result") or {}):
        raise RuntimeError(f"unexpected response: {json.dumps(body)[:400]}")
    return base64.b64decode(body["result"]["image"])


def extension(data):
    if data[:8] == b"\x89PNG\r\n\x1a\n":
        return "png"
    if data[:4] == b"RIFF" and data[8:12] == b"WEBP":
        return "webp"
    return "jpg"


def main():
    parser = argparse.ArgumentParser(description="Generate images with Cloudflare Workers AI.")
    parser.add_argument("prompt", nargs="?", help="what to draw")
    parser.add_argument("--file", help="text file with one prompt per line")
    parser.add_argument("--count", type=int, default=1, help="images per prompt (default 1)")
    parser.add_argument("--steps", type=int, default=4, help="quality steps, 1-8 (default 4)")
    parser.add_argument("--out", default=os.path.join(ROOT, "generated-images"), help="output folder")
    args = parser.parse_args()

    prompts = [args.prompt] if args.prompt else []
    if args.file:
        with open(args.file, encoding="utf-8-sig") as f:  # Notepad may add a byte-order mark
            prompts += [line.strip() for line in f if line.strip()]
    if not prompts:
        parser.error("give a prompt or --file")

    account, token = credentials()
    os.makedirs(args.out, exist_ok=True)
    stamp = time.strftime("%Y%m%d_%H%M%S")
    done = failed = 0
    for p, prompt in enumerate(prompts, 1):
        slug = re.sub(r"[^a-z0-9]+", "-", prompt.lower()).strip("-")[:40] or "image"
        for n in range(1, args.count + 1):
            try:
                data = generate(account, token, prompt, args.steps)
            except RuntimeError as error:
                failed += 1
                print(f"FAILED prompt {p} image {n}: {error}")
                continue
            path = os.path.join(args.out, f"{stamp}_{p:02d}_{slug}_{n}.{extension(data)}")
            with open(path, "wb") as f:
                f.write(data)
            done += 1
            print(f"saved {path} ({len(data) // 1024} KB)")
    print(f"{done} saved, {failed} failed")
    sys.exit(1 if failed else 0)


if __name__ == "__main__":
    main()
