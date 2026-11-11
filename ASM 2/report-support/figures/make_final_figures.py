from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

BASE = Path(__file__).resolve().parent
OUT = BASE / "final"
OUT.mkdir(exist_ok=True)

F_REG = "C:/Windows/Fonts/segoeui.ttf"
F_SEMI = "C:/Windows/Fonts/seguisb.ttf"
F_BOLD = "C:/Windows/Fonts/segoeuib.ttf"

BG = "#F8F9FA"
INK = "#111827"
MUTED = "#4B5563"
BORDER = "#D1D5DB"
PALE = "#E5E7EB"


def font(path, size):
    return ImageFont.truetype(path, size)


def rounded(draw, box, radius, fill, outline=None, width=1):
    draw.rounded_rectangle(box, radius=radius, fill=fill, outline=outline, width=width)


def arrow_down(draw, x, y0, y1, color=INK, width=4):
    draw.line([(x, y0), (x, y1 - 12)], fill=color, width=width)
    draw.polygon([(x - 9, y1 - 14), (x + 9, y1 - 14), (x, y1)], fill=color)


def arrow_up_dashed(draw, x, y0, y1, color="#9CA3AF", width=3):
    # y0 is bottom, y1 is top
    y = y0
    while y > y1 + 10:
        draw.line([(x, y), (x, max(y - 10, y1 + 10))], fill=color, width=width)
        y -= 18
    draw.polygon([(x - 8, y1 + 12), (x + 8, y1 + 12), (x, y1)], fill=color)


def pill(draw, cx, cy, w, text, f, fill=PALE, fg=INK, h=46):
    rounded(draw, [cx - w / 2, cy - h / 2, cx + w / 2, cy + h / 2], h / 2, fill=fill)
    draw.text((cx, cy - 2), text, font=f, fill=fg, anchor="mm")


def make_architecture():
    W, H = 1600, 1120
    im = Image.new("RGB", (W, H), BG)
    d = ImageDraw.Draw(im)
    fb28, fb24, f18, f17 = font(F_BOLD, 28), font(F_BOLD, 24), font(F_REG, 18), font(F_REG, 17)
    f20 = font(F_REG, 20)

    d.text((W / 2, 42), "Unidirectional flow: user events travel down, StateFlow state comes back up",
           font=f20, fill=MUTED, anchor="mm")

    layers = [
        ("UI layer", "MainActivity + Jetpack Compose · CatalogueScreen, RentalScreen", "Jetpack Compose"),
        ("ViewModel layer", "CarRentalViewModel · selected car, search, sort, language", "StateFlow + coroutines"),
        ("Repository layer", "CarRentalRepository · credits, favourites, bookings", "MutableStateFlow"),
        ("Domain layer", "CarRentalDomain · validation, cost, search, sort", "Pure Kotlin · no Android"),
        ("Model layer", "Car, RentalBooking, SortOption · Parcelable", "Parcelable + Intent"),
    ]

    x0, x1 = 410, 1180
    y = 100
    box_h, gap = 118, 36
    centers = []
    for i, (title, sub, tech) in enumerate(layers):
        rounded(d, [x0, y, x1, y + box_h], 14, "#FFFFFF", BORDER, 2)
        rounded(d, [x0, y, x0 + 7, y + box_h], 3, INK)
        d.text((x0 + 28, y + 18), title, font=fb28, fill=INK)
        d.text((x0 + 28, y + 60), sub, font=f18, fill=MUTED)
        centers.append(y + box_h / 2)
        # technology pill
        pill(d, 1390, y + box_h / 2, 330, tech, f17)
        y += box_h + gap

    # flow arrows in each gap
    for i in range(len(layers) - 1):
        top = 100 + i * (box_h + gap) + box_h
        bottom = top + gap
        arrow_down(d, 760, top + 4, bottom - 2)
        arrow_up_dashed(d, 830, bottom - 2, top + 4)

    # legend
    lx, ly = 410, 940
    d.line([(lx, ly), (lx + 46, ly)], fill=INK, width=4)
    d.polygon([(lx + 46, ly - 7), (lx + 46, ly + 7), (lx + 58, ly)], fill=INK)
    d.text((lx + 70, ly - 12), "dependency / call", font=f18, fill=MUTED)
    d.line([(lx + 330, ly - 10), (lx + 376, ly - 10)], fill="#9CA3AF", width=3)
    d.polygon([(lx + 376, ly - 17), (lx + 376, ly - 3), (lx + 388, ly - 10)], fill="#9CA3AF")
    d.text((lx + 402, ly - 12), "StateFlow update", font=f18, fill=MUTED)

    d.text((W / 2, 1040),
           "Only the UI layer knows about Compose; the domain layer is pure Kotlin and unit-testable.",
           font=f18, fill=MUTED, anchor="mm")
    im.save(OUT / "figure_09_architecture.png")


def make_navigation():
    W, H = 1600, 830
    im = Image.new("RGB", (W, H), BG)
    d = ImageDraw.Draw(im)
    fb34, fb26, f24, f20, f18 = (
        font(F_BOLD, 34), font(F_BOLD, 26), font(F_SEMI, 24), font(F_REG, 20), font(F_REG, 18),
    )

    def activity_box(x0, x1, title, subtitle, bullets):
        y0, y1 = 190, 540
        rounded(d, [x0, y0, x1, y1], 18, "#FFFFFF", BORDER, 2)
        d.text((x0 + 34, y0 + 28), title, font=fb34, fill=INK)
        d.text((x0 + 36, y0 + 82), subtitle, font=f24, fill=MUTED)
        by = y0 + 146
        for b in bullets:
            d.ellipse([x0 + 38, by + 8, x0 + 48, by + 18], fill=INK)
            d.text((x0 + 60, by), b, font=f20, fill=INK)
            by += 38

    activity_box(70, 560, "MainActivity", "CatalogueScreen", [
        "Browse, search and sort cars",
        "Favourites and Active Rentals",
        "Credits, theme and language",
    ])
    activity_box(1040, 1530, "RentalActivity", "RentalScreen", [
        "Vehicle summary and rating",
        "Duration slider and live cost",
        "Booking, cancel and return",
    ])

    # top arrow: catalogue -> rental
    ay = 300
    d.line([(560, ay), (1028, ay)], fill=INK, width=5)
    d.polygon([(1028, ay - 11), (1028, ay + 11), (1044, ay)], fill=INK)
    d.text((800, 232), "Explicit Intent", font=fb26, fill=INK, anchor="mm")
    d.text((800, 272), "Car (Parcelable) + balance, theme and language", font=f18, fill=MUTED, anchor="mm")

    # bottom arrow: rental -> catalogue
    by = 452
    d.line([(1040, by), (582, by)], fill=INK, width=5)
    d.polygon([(582, by - 11), (582, by + 11), (566, by)], fill=INK)
    d.text((800, 500), "Booking result", font=fb26, fill=INK, anchor="mm")
    d.text((800, 542), "RESULT_OK (car ID + days) or RESULT_CANCELED", font=f18, fill=MUTED, anchor="mm")

    # badge
    rounded(d, [560, 630, 1040, 688], 29, PALE)
    d.text((800, 659), "Activity Result API · StartActivityForResult", font=f20, fill=INK, anchor="mm")

    d.text((W / 2, 748),
           "The result updates credits, bookings and the catalogue in the shared ViewModel.",
           font=f18, fill=MUTED, anchor="mm")
    im.save(OUT / "figure_10_navigation.png")


def annotate_catalogue():
    src = Image.open(BASE / "fig03_catalogue_light_raw.png").convert("RGB")
    im = src.copy()
    d = ImageDraw.Draw(im)
    f = font(F_BOLD, 52)
    points = [(600, 240), (620, 480), (930, 668), (930, 852), (95, 1200), (960, 2245)]
    for i, (x, y) in enumerate(points, start=1):
        d.ellipse([x - 31, y - 31, x + 31, y + 31], fill="#FFFFFF")
        d.ellipse([x - 26, y - 26, x + 26, y + 26], fill=INK)
        d.text((x, y - 2), str(i), font=f, fill="#FFFFFF", anchor="mm")
    d.line([(126, 1200), (160, 1200)], fill=INK, width=4)
    im.save(OUT / "figure_02_catalogue_light.png")


def trim_bottom(im, bg, tol=10, min_count=40):
    px = im.load()
    w, h = im.size
    br, bgc, bb = bg
    last = 0
    for y in range(h - 1, -1, -1):
        count = 0
        for x in range(0, w, 4):
            r, g, b = px[x, y]
            if abs(r - br) > tol or abs(g - bgc) > tol or abs(b - bb) > tol:
                count += 1
                if count >= min_count:
                    break
        if count >= min_count:
            last = y
            break
    return im.crop((0, 0, w, min(h, last + 36)))


def make_github():
    im = Image.open(BASE / "fig13_github_tall.png").convert("RGB")
    im = trim_bottom(im, (13, 17, 23), tol=12, min_count=30)
    im.save(OUT / "figure_01_github.png")


def make_tests():
    im = Image.open(BASE / "fig12_tests_raw.png").convert("RGB")
    im = trim_bottom(im, (255, 255, 255), tol=6, min_count=10)
    w, h = im.size
    canvas = Image.new("RGB", (w + 24, h + 24), "#FFFFFF")
    canvas.paste(im, (12, 12))
    d = ImageDraw.Draw(canvas)
    d.rectangle([0, 0, canvas.width - 1, canvas.height - 1], outline=BORDER, width=2)
    canvas.save(OUT / "figure_13_tests.png")


def phone_panel(path, label, target_w=880):
    im = Image.open(path).convert("RGB")
    w, h = im.size
    nw = target_w
    nh = round(h * nw / w)
    im = im.resize((nw, nh), Image.LANCZOS)
    label_h = 74
    panel = Image.new("RGB", (nw + 12, nh + label_h + 12), "#FFFFFF")
    panel.paste(im, (6, label_h + 6))
    d = ImageDraw.Draw(panel)
    d.rectangle([6, label_h + 6, nw + 5, nh + label_h + 5], outline=BORDER, width=2)
    d.text(((nw + 12) / 2, label_h / 2 + 2), label, font=font(F_BOLD, 32), fill=INK, anchor="mm")
    return panel


def make_pair(a_path, a_label, b_path, b_label, out_name):
    a = phone_panel(a_path, a_label)
    b = phone_panel(b_path, b_label)
    margin, gap = 36, 46
    W = margin * 2 + a.width + b.width + gap
    H = margin * 2 + max(a.height, b.height)
    canvas = Image.new("RGB", (W, H), "#EEF0F3")
    canvas.paste(a, (margin, margin))
    canvas.paste(b, (margin + a.width + gap, margin))
    canvas.save(OUT / out_name)


def make_singles():
    # figure 5 favourites and figure 6 rental remain single full screens in the document
    Image.open(BASE / "fig06_favourites_rented_raw.png").convert("RGB").save(OUT / "figure_05_favourites.png")
    Image.open(BASE / "fig07_rental_camry_raw.png").convert("RGB").save(OUT / "figure_06_rental.png")
    Image.open(BASE / "fig04_catalogue_dark_raw.png").convert("RGB").save(OUT / "figure_03_catalogue_dark.png")


def main():
    make_architecture()
    make_navigation()
    annotate_catalogue()
    make_github()
    make_tests()
    make_singles()
    make_pair(BASE / "fig05a_search_raw.png", '(a) Search for "Model"',
              BASE / "fig05b_sort_raw.png", "(b) Sorted by Lowest Price",
              "figure_04_search_sort.png")
    make_pair(BASE / "fig08a_cap_banner_raw.png", "(a) 400-credit cap exceeded",
              BASE / "fig08b_balance_banner_raw.png", "(b) Insufficient balance",
              "figure_07_validation.png")
    make_pair(BASE / "fig11a_vi_catalogue_raw.png", "(a) Catalogue in Vietnamese",
              BASE / "fig11b_vi_rental_raw.png", "(b) Rental screen in Vietnamese",
              "figure_08_vietnamese.png")
    make_pair(BASE / "fig09a_toast_confirmed_raw.png", "(a) Booking successful",
              BASE / "_fig09b_full.png", "(b) Favourites and Active Rentals",
              "figure_11_booking.png")
    make_pair(BASE / "fig10a_toast_cancelled_raw.png", "(a) Booking cancelled",
              BASE / "fig10b_toast_returned_raw.png", "(b) Honda Civic returned",
              "figure_12_cancel_return.png")

    for p in sorted(OUT.glob("*.png")):
        with Image.open(p) as im:
            print(p.name, im.size)


if __name__ == "__main__":
    main()
