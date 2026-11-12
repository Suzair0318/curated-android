"""Tags every catalogue product for the AI Stylist with a vision model.

The stylist only knows products that have tags, so run this again whenever products
are added to the catalogue (BrandCatalog.kt / StoreFeedCatalog.kt / MockCatalog.kt):

    python tag_products.py            tags the products that have no tags yet
    python tag_products.py --export   writes app/src/main/assets/stylist_tags.json

Each product costs about 8.5 neurons of the Cloudflare account in local.properties.
Results accumulate in tags_raw.json, so a run that stops half-way can simply be repeated.
Other options: --limit N, --ids a,b,c, --workers 4.
"""
import argparse
import base64
import concurrent.futures
import json
import pathlib
import re
import threading
import time

import cf
from catalog import load

HERE = pathlib.Path(__file__).resolve().parent
RAW = HERE / "tags_raw.json"
ASSET = cf.ROOT / "app" / "src" / "main" / "assets" / "stylist_tags.json"
MODEL = "@cf/google/gemma-4-26b-a4b-it"

TYPES = ["outfit", "top", "bottom", "outerwear", "scarf", "shoes", "bag", "jewellery", "other"]
AUDIENCES = ["women", "men", "kids"]
VIEWS = ["standing", "seated", "cropped", "flat"]
OCCASIONS = ["casual", "work", "party", "wedding", "festive"]
STYLES = ["desi", "western", "fusion"]
SEASONS = ["summer", "winter", "all"]

INSTRUCTIONS = """You label product photos for a Pakistani fashion shopping app.
Product name: "{name}". Shop section: {category}.
Look at the photo and reply with JSON only, with exactly these keys:
"type": what is being sold, one of outfit, top, bottom, outerwear, scarf, shoes, bag, jewellery, other.
  Trust the product name first ("Pants" is bottom even when the photo shows a whole look); use the photo when the name is unclear.
  outfit = a complete set that covers upper and lower body (shalwar kameez suit, 2 or 3 piece, co-ord set, dress, saree, abaya).
  top = shirt, kurta, kurti, blouse, sweater, t-shirt sold alone. bottom = trousers, pants, shalwar, jeans, skirt sold alone.
  outerwear = coat, jacket, waistcoat, blazer. scarf = scarf, shawl, stole or dupatta sold alone.
  other = anything else (wallet, perfume, belt, watch, fabric without a garment shown).
"for": one of women, men, kids.
"view": how the product is shown. standing = worn by a standing person or mannequin with the whole product clearly visible;
  seated = worn by someone sitting, lying or in a twisted pose; cropped = worn but partly cut off, hidden or a close-up;
  flat = the product alone without a person.
"occasions": list with one to three of casual, work, party, wedding, festive.
"style": one of desi, western, fusion.
"season": one of summer, winter, all.
"colour": main colour of the product in one or two words.
"desc": what the product looks like in at most 10 words, without the brand name."""


def tag(item):
    data = base64.b64encode(cf.reference_jpeg(cf.drawable(item["image"]), 384)).decode()
    text = INSTRUCTIONS.format(name=item["name"].replace('\\"', "'"), category=item["category"])
    payload = {
        "messages": [
            {
                "role": "user",
                "content": [
                    {"type": "text", "text": text},
                    {"type": "image_url", "image_url": {"url": f"data:image/jpeg;base64,{data}"}},
                ],
            }
        ],
        "max_tokens": 220,
        "temperature": 0,
        "chat_template_kwargs": {"enable_thinking": False},
    }
    last = None
    for attempt in range(4):
        try:
            status, body, _seconds = cf.run_json(MODEL, payload)
        except Exception as error:  # noqa: BLE001 - network hiccup: wait and try again
            last = "network: " + type(error).__name__
            time.sleep(5 * (attempt + 1))
            continue
        try:
            result = body["result"]
            content = result["choices"][0]["message"]["content"]
            match = re.search(r"\{.*\}", content, re.S)
            parsed = clean(json.loads(match.group(0)))
            parsed["neurons"] = round(result.get("usage", {}).get("neurons", 0), 2)
            return parsed
        except Exception as error:  # noqa: BLE001 - retried, then reported
            last = f"http {status}: {error}: {cf.brief(body, 200)}"
    raise RuntimeError(last)


def pick(value, allowed, default):
    value = str(value).strip().lower()
    return value if value in allowed else default


def clean(raw):
    occasions = raw.get("occasions") or []
    if isinstance(occasions, str):
        occasions = [occasions]
    occasions = [o for o in (str(v).strip().lower() for v in occasions) if o in OCCASIONS][:3]
    return {
        "type": pick(raw.get("type"), TYPES, "other"),
        "for": pick(raw.get("for"), AUDIENCES, "women"),
        "view": pick(raw.get("view"), VIEWS, "cropped"),
        "occasions": occasions or ["casual"],
        "style": pick(raw.get("style"), STYLES, "fusion"),
        "season": pick(raw.get("season"), SEASONS, "all"),
        "colour": str(raw.get("colour", "")).strip().lower()[:24],
        "desc": str(raw.get("desc", "")).strip()[:90],
    }


def export(done):
    compact = {}
    for product_id, t in done.items():
        compact[product_id] = {
            "t": t["type"],
            "f": t["for"],
            "v": t["view"],
            "o": t["occasions"],
            "s": t["style"],
            "w": t["season"],
            "c": t["colour"],
            "d": t["desc"],
        }
    ASSET.parent.mkdir(parents=True, exist_ok=True)
    ASSET.write_text(json.dumps(compact, ensure_ascii=False, separators=(",", ":")), encoding="utf-8")
    print("wrote", ASSET, len(compact), "products,", ASSET.stat().st_size, "bytes")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--limit", type=int, default=0)
    parser.add_argument("--ids", default="")
    parser.add_argument("--workers", type=int, default=4)
    parser.add_argument("--export", action="store_true")
    args = parser.parse_args()

    done = json.loads(RAW.read_text(encoding="utf-8")) if RAW.exists() else {}
    if args.export:
        export(done)
        return

    items = load()
    if args.ids:
        wanted = set(args.ids.split(","))
        items = [i for i in items if i["id"] in wanted]
    todo = [i for i in items if i["id"] not in done]
    if args.limit:
        todo = todo[: args.limit]
    print(f"{len(done)} done, tagging {len(todo)}")

    lock = threading.Lock()
    failures = []

    def work(item):
        try:
            tags = tag(item)
        except Exception as error:  # noqa: BLE001
            with lock:
                failures.append((item["id"], cf._scrub(str(error))[:200]))
            return
        with lock:
            done[item["id"]] = tags
            if len(done) % 20 == 0:
                RAW.write_text(json.dumps(done, ensure_ascii=False, indent=1), encoding="utf-8")
                print(len(done), "done", flush=True)

    with concurrent.futures.ThreadPoolExecutor(args.workers) as pool:
        list(pool.map(work, todo))
    RAW.write_text(json.dumps(done, ensure_ascii=False, indent=1), encoding="utf-8")
    spent = sum(t.get("neurons", 0) for t in done.values())
    print(f"finished: {len(done)} tagged, {len(failures)} failed, about {spent:.0f} neurons in total")
    for product_id, error in failures[:20]:
        print("  failed", product_id, error)


if __name__ == "__main__":
    main()
