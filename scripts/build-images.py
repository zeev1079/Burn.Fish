#!/usr/bin/env python3
"""Regenerate every whale-image variant the site uses from a single source.

Usage:
    python3 scripts/build-images.py [path/to/source.jpg]

Default source is docs/img/whale-source.jpg. Drops into docs/img/:
    whale.jpg          (1664x936 hero/background, optimised)
    whale.webp         (same size, ~10x smaller)
    og-image.jpg       (1200x630 social card)
    favicon-32.png
    favicon-192.png
    favicon-512.png
"""
from __future__ import annotations

import os
import sys
from PIL import Image

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
IMG_DIR = os.path.join(ROOT, "docs", "img")

HERO_SIZE = (1664, 936)
OG_SIZE = (1200, 630)
FAVICON_SIZES = (32, 192, 512)
BG_COLOR = (11, 26, 46, 255)


def cover_resize(img: Image.Image, target: tuple[int, int]) -> Image.Image:
    """Resize-and-crop `img` so it exactly fills `target` (CSS background-size: cover)."""
    tw, th = target
    sw, sh = img.size
    scale = max(tw / sw, th / sh)
    nw, nh = int(round(sw * scale)), int(round(sh * scale))
    resized = img.resize((nw, nh), Image.Resampling.LANCZOS)
    left = (nw - tw) // 2
    top = (nh - th) // 2
    return resized.crop((left, top, left + tw, top + th))


def main(src_path: str) -> None:
    if not os.path.isfile(src_path):
        sys.exit(f"source image not found: {src_path}")

    src = Image.open(src_path).convert("RGB")
    print(f"source: {src_path}  size={src.size}")

    hero = cover_resize(src, HERO_SIZE)
    hero_jpg = os.path.join(IMG_DIR, "whale.jpg")
    hero_webp = os.path.join(IMG_DIR, "whale.webp")
    hero.save(hero_jpg, "JPEG", quality=82, optimize=True, progressive=True)
    hero.save(hero_webp, "WEBP", quality=80, method=6)
    print(f"  -> whale.jpg  {os.path.getsize(hero_jpg) // 1024}KB")
    print(f"  -> whale.webp {os.path.getsize(hero_webp) // 1024}KB")

    og = cover_resize(src, OG_SIZE)
    og_path = os.path.join(IMG_DIR, "og-image.jpg")
    og.save(og_path, "JPEG", quality=85, optimize=True, progressive=True)
    print(f"  -> og-image.jpg {os.path.getsize(og_path) // 1024}KB")

    src_rgba = Image.open(src_path).convert("RGBA")
    for size in FAVICON_SIZES:
        canvas = Image.new("RGBA", (size, size), BG_COLOR)
        thumb = src_rgba.copy()
        thumb.thumbnail((size, size), Image.Resampling.LANCZOS)
        canvas.alpha_composite(thumb, ((size - thumb.width) // 2, (size - thumb.height) // 2))
        out = os.path.join(IMG_DIR, f"favicon-{size}.png")
        canvas.save(out, "PNG", optimize=True)
        print(f"  -> favicon-{size}.png {os.path.getsize(out) // 1024}KB")


if __name__ == "__main__":
    src = sys.argv[1] if len(sys.argv) > 1 else os.path.join(IMG_DIR, "whale-source.jpg")
    main(src)
