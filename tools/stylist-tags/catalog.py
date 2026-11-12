"""Reads the bundled catalogue out of the Kotlin sources into build/ai_test/catalog.json."""
import json
import pathlib
import re

ROOT = pathlib.Path(__file__).resolve().parents[2]
MOCK = ROOT / "app" / "src" / "main" / "java" / "com" / "curated" / "app" / "data" / "mock"
TARGET = pathlib.Path(__file__).resolve().parent / "catalog.json"

PIECE = re.compile(
    r'piece\("(?P<id>[^"]+)",\s*"(?P<brand>[^"]+)",\s*"(?P<name>(?:[^"\\]|\\.)*)",\s*(?P<price>\d+),\s*'
    r'R\.drawable\.(?P<image>\w+),\s*Category\.(?P<category>\w+),\s*"(?P<colour>[^"]*)"'
)
MOCK_PRODUCT = re.compile(
    r'id = "(?P<id>p_\w+)",\s*brandId = "(?P<brand>\w+)",\s*name = "(?P<name>[^"]+)",\s*pricePkr = (?P<price>\d+)'
    r'.*?ProductColor\("(?P<colour>[^"]+)".*?imageRes = R\.drawable\.(?P<image>\w+),\s*category = Category\.(?P<category>\w+)',
    re.S,
)


def load():
    products = []
    for name in ("BrandCatalog.kt", "StoreFeedCatalog.kt"):
        text = (MOCK / name).read_text(encoding="utf-8")
        products += [m.groupdict() for m in PIECE.finditer(text)]
    text = (MOCK / "MockCatalog.kt").read_text(encoding="utf-8")
    products += [m.groupdict() for m in MOCK_PRODUCT.finditer(text)]
    for product in products:
        product["price"] = int(product["price"])
    return products


if __name__ == "__main__":
    items = load()
    TARGET.write_text(json.dumps(items, ensure_ascii=False, indent=1), encoding="utf-8")
    counts = {}
    for item in items:
        counts[item["category"]] = counts.get(item["category"], 0) + 1
    print(len(items), "products", counts)
    for item in items:
        if item["category"] in ("Footwear", "Jewellery") or re.search(
            r"bag|clutch|tote|scarf|shawl|stole|earring|necklace|khussa|heel|sandal", item["name"], re.I
        ):
            print(f'{item["id"]:22} {item["category"]:9} {item["price"]:>6}  {item["name"]}')
