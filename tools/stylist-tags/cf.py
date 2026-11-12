"""Tiny Cloudflare Workers AI client used by tag_products.py.

Reads CF_ACCOUNT_ID / CF_API_TOKEN from local.properties and never prints them.
"""
import base64
import io
import json
import pathlib
import time

import requests
from PIL import Image

ROOT = pathlib.Path(__file__).resolve().parents[2]
DRAWABLES = ROOT / "app" / "src" / "main" / "res" / "drawable-nodpi"
OUT = pathlib.Path(__file__).resolve().parent / "out"


def _props():
    values = {}
    for line in (ROOT / "local.properties").read_text(encoding="utf-8").splitlines():
        if "=" in line and not line.lstrip().startswith("#"):
            key, value = line.split("=", 1)
            values[key.strip()] = value.strip()
    return values


_P = _props()
_BASE = f"https://api.cloudflare.com/client/v4/accounts/{_P['CF_ACCOUNT_ID']}/ai/run/"
_HEADERS = {"Authorization": f"Bearer {_P['CF_API_TOKEN']}"}


def _scrub(text):
    return text.replace(_P["CF_API_TOKEN"], "<token>").replace(_P["CF_ACCOUNT_ID"], "<account>")


def run_json(model, payload, timeout=120):
    """POST a JSON body; returns (status, parsed json or text, seconds)."""
    start = time.time()
    response = requests.post(_BASE + model, headers=_HEADERS, json=payload, timeout=timeout)
    return _result(response, start)


def run_multipart(model, fields, images=(), timeout=180):
    """POST a multipart form. `images` is a list of (field name, jpeg bytes)."""
    start = time.time()
    files = [(name, (None, str(value))) for name, value in fields.items()]
    files += [(name, (f"{name}.jpg", data, "image/jpeg")) for name, data in images]
    response = requests.post(_BASE + model, headers=_HEADERS, files=files, timeout=timeout)
    return _result(response, start)


def _result(response, start):
    seconds = round(time.time() - start, 1)
    try:
        body = response.json()
    except ValueError:
        body = _scrub(response.text[:2000])
    return response.status_code, body, seconds


def drawable(name):
    """Path of a bundled drawable by resource name (any extension)."""
    matches = sorted(DRAWABLES.glob(name + ".*"))
    if not matches:
        raise FileNotFoundError(name)
    return matches[0]


def reference_jpeg(path, box=512):
    """Product photo fitted inside box x box on white, as JPEG bytes."""
    image = Image.open(path).convert("RGB")
    image.thumbnail((box, box), Image.LANCZOS)
    canvas = Image.new("RGB", (box, box), "white")
    canvas.paste(image, ((box - image.width) // 2, (box - image.height) // 2))
    buffer = io.BytesIO()
    canvas.save(buffer, "JPEG", quality=90)
    return buffer.getvalue()


def save_image(result_body, name):
    """Writes result.image (base64) to out/<name>.jpg; returns the path or None."""
    try:
        encoded = result_body["result"]["image"]
    except (KeyError, TypeError):
        return None
    image = Image.open(io.BytesIO(base64.b64decode(encoded))).convert("RGB")
    OUT.mkdir(exist_ok=True)
    path = OUT / f"{name}.jpg"
    image.save(path, "JPEG", quality=92)
    return path


def brief(body, limit=600):
    """Short printable form of a response without the image payload."""
    if isinstance(body, dict):
        clone = json.loads(json.dumps(body))
        result = clone.get("result")
        if isinstance(result, dict) and isinstance(result.get("image"), str):
            result["image"] = f"<base64 {len(result['image'])} chars>"
        return _scrub(json.dumps(clone, ensure_ascii=False))[:limit]
    return str(body)[:limit]
