import json
import os

with open("figma_assets/car_images.json", "r") as f:
    images = json.load(f)

OUTPUT_DIR = "figma_assets"

def get_status_bar(dark=False):
    fill = "#FFFFFF" if dark else "#111827"
    return f"""
    <g id="StatusBar">
        <text x="32" y="32" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="600" fill="{fill}">9:41</text>
        <g transform="translate(330, 20)" fill="{fill}">
            <path d="M0 11h3V9H0v2zm4 0h3V7H4v4zm4 0h3V4H8v7zm4 0h3V1h-3v10z"/>
            <path d="M19 2a12.9 12.9 0 00-8.5 3.2l1.4 1.4A10.9 10.9 0 0119 4a10.9 10.9 0 017.1 2.6l1.4-1.4A12.9 12.9 0 0019 2zm0 4a8.9 8.9 0 00-5.7 2.1l1.4 1.4A6.9 6.9 0 0119 8a6.9 6.9 0 014.3 1.5l1.4-1.4A8.9 8.9 0 0019 6zm0 4a4.9 4.9 0 00-2.8 1l2.8 3.5 2.8-3.5A4.9 4.9 0 0019 10z"/>
            <rect x="36" y="2" width="22" height="11" rx="3" fill="none" stroke="{fill}" stroke-width="1.2"/>
            <rect x="38" y="4" width="15" height="7" rx="1.5" fill="{fill}"/>
            <path d="M59 5.5v4" stroke="{fill}" stroke-width="1.2" stroke-linecap="round"/>
        </g>
    </g>
    """

def get_status_bar_landscape(dark=False):
    fill = "#FFFFFF" if dark else "#111827"
    return f"""
    <g id="StatusBarLandscape">
        <text x="36" y="24" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="600" fill="{fill}">9:41</text>
        <g transform="translate(810, 14)" fill="{fill}">
            <path d="M0 10h3V8H0v2zm4 0h3V6H4v4zm4 0h3V3H8v7zm4 0h3V0h-3v10z"/>
            <path d="M19 1a12.9 12.9 0 00-8.5 3.2l1.4 1.4A10.9 10.9 0 0119 3a10.9 10.9 0 017.1 2.6l1.4-1.4A12.9 12.9 0 0019 1zm0 4a8.9 8.9 0 00-5.7 2.1l1.4 1.4A6.9 6.9 0 0119 7a6.9 6.9 0 014.3 1.5l1.4-1.4A8.9 8.9 0 0019 5zm0 4a4.9 4.9 0 00-2.8 1l2.8 3.5 2.8-3.5A4.9 4.9 0 0019 9z"/>
            <rect x="36" y="1" width="22" height="11" rx="3" fill="none" stroke="{fill}" stroke-width="1"/>
            <rect x="38" y="3" width="15" height="7" rx="1.5" fill="{fill}"/>
            <path d="M59 4.5v4" stroke="{fill}" stroke-width="1" stroke-linecap="round"/>
        </g>
    </g>
    """

def get_nav_bar(dark=False):
    fill = "#FFFFFF" if dark else "#111827"
    return f"""
    <g id="NavigationBar">
        <rect x="145" y="876" width="122" height="4" rx="2" fill="{fill}" opacity="0.4"/>
    </g>
    """

def get_nav_bar_landscape(dark=False):
    fill = "#FFFFFF" if dark else "#111827"
    return f"""
    <g id="NavigationBarLandscape">
        <rect x="385" y="400" width="122" height="4" rx="2" fill="{fill}" opacity="0.4"/>
    </g>
    """

def render_stars(x, y, rating, star_color="#EAB308", text_color="#111827", star_size=13):
    stars_svg = []
    curr_x = x
    for i in range(1, 6):
        if rating >= i:
            stars_svg.append(f'<path d="M6.5 0l2 4 4.5.7-3.3 3.2.8 4.6L6.5 10.3 2.5 12.5l.8-4.6L0 4.7l4.5-.7 2-4z" transform="translate({curr_x}, {y}) scale({star_size/13})" fill="{star_color}"/>')
        elif rating >= i - 0.5:
            stars_svg.append(f'<path d="M6.5 0l2 4 4.5.7-3.3 3.2.8 4.6L6.5 10.3 2.5 12.5l.8-4.6L0 4.7l4.5-.7 2-4z" transform="translate({curr_x}, {y}) scale({star_size/13})" fill="{star_color}" opacity="0.5"/>')
        else:
            stars_svg.append(f'<path d="M6.5 0l2 4 4.5.7-3.3 3.2.8 4.6L6.5 10.3 2.5 12.5l.8-4.6L0 4.7l4.5-.7 2-4z" transform="translate({curr_x}, {y}) scale({star_size/13})" fill="{star_color}" opacity="0.25"/>')
        curr_x += star_size + 2
    
    text_svg = f'<text x="{curr_x + 4}" y="{y + star_size - 1}" font-family="-apple-system, BlinkMacSystemFont, \'Segoe UI\', Roboto, sans-serif" font-size="12" font-weight="600" fill="{text_color}">{rating:.1f}</text>'
    return "".join(stars_svg) + text_svg


# ==========================================
# SCREEN 1: Home Catalogue (Light Mode)
# ==========================================
def generate_screen1_home_light():
    return f"""<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 412 892" width="412" height="892">
    <defs>
        <clipPath id="clipHeroImg"><rect x="20" y="262" width="372" height="175" rx="18"/></clipPath>
        <clipPath id="clipPop1"><rect x="20" y="700" width="174" height="90" rx="14"/></clipPath>
        <clipPath id="clipPop2"><rect x="202" y="700" width="174" height="90" rx="14"/></clipPath>
        <clipPath id="clipFav1"><rect x="20" y="112" width="70" height="50" rx="10"/></clipPath>
        <filter id="shadow" x="-5%" y="-5%" width="110%" height="110%"><feDropShadow dx="0" dy="2" stdDeviation="4" flood-opacity="0.06"/></filter>
    </defs>

    <rect width="412" height="892" fill="#F8F9FA"/>

    {get_status_bar(dark=False)}

    <g id="TopAppBar">
        <text x="20" y="72" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="22" font-weight="800" fill="#111827">Car Rental</text>
        
        <rect x="180" y="52" width="105" height="28" rx="14" fill="#111827"/>
        <text x="190" y="70" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#F3F4F6">Credits: 500</text>
        
        <circle cx="310" cy="66" r="14" fill="#F1F3F5"/>
        <text x="303" y="70" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="700" fill="#111827">EN</text>

        <g transform="translate(342, 54)">
            <rect width="48" height="24" rx="12" fill="#E5E7EB"/>
            <circle cx="12" cy="12" r="9" fill="#FFFFFF" filter="url(#shadow)"/>
            <circle cx="12" cy="12" r="3.5" fill="#EAB308"/>
        </g>
    </g>

    <g id="FavouritesSection">
        <text x="20" y="104" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#111827">Your Favourites</text>
        <text x="352" y="104" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#EF4444">(1)</text>

        <g>
            <rect x="20" y="112" width="180" height="50" rx="10" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
            <image href="{images['toyota_camry']}" x="20" y="112" width="70" height="50" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipFav1)"/>
            <text x="96" y="132" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="700" fill="#111827">Toyota Camry</text>
            <text x="96" y="148" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#111827">50 credits</text>
            <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" transform="translate(178, 128) scale(0.65)" fill="#EF4444"/>
        </g>
    </g>

    <g id="SearchAndSort">
        <rect x="20" y="174" width="270" height="42" rx="10" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <path d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0016 9.5 6.5 6.5 0 109.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" transform="translate(30, 185) scale(0.9)" fill="#6B7280"/>
        <text x="56" y="200" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" fill="#9CA3AF">Search car name or model...</text>

        <rect x="298" y="174" width="94" height="42" rx="10" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <text x="312" y="199" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="600" fill="#111827">Rating</text>
        <path d="M7 10l5 5 5-5z" transform="translate(366, 186) scale(0.9)" fill="#111827"/>
    </g>

    <g id="SortChips">
        <rect x="20" y="226" width="76" height="26" rx="13" fill="#111827"/>
        <text x="34" y="243" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#FFFFFF">★ Rating</text>

        <rect x="104" y="226" width="76" height="26" rx="13" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <text x="120" y="243" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="500" fill="#6B7280">Newest</text>

        <rect x="188" y="226" width="98" height="26" rx="13" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <text x="200" y="243" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="500" fill="#6B7280">Lowest Price</text>
    </g>

    <g id="HeroCard" filter="url(#shadow)">
        <rect x="20" y="262" width="372" height="400" rx="18" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        
        <image href="{images['toyota_camry']}" x="20" y="262" width="372" height="175" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipHeroImg)"/>
        
        <circle cx="44" cy="350" r="16" fill="#111827" opacity="0.7"/>
        <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z" transform="translate(34, 340) scale(0.85)" fill="#FFFFFF"/>

        <circle cx="368" cy="350" r="16" fill="#111827" opacity="0.7"/>
        <path d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z" transform="translate(358, 340) scale(0.85)" fill="#FFFFFF"/>

        <rect x="334" y="274" width="46" height="22" rx="11" fill="#111827" opacity="0.8"/>
        <text x="345" y="289" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">1 / 5</text>

        <circle cx="50" cy="285" r="16" fill="#FFFFFF" opacity="0.9"/>
        <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" transform="translate(41, 276) scale(0.75)" fill="#EF4444"/>

        <text x="36" y="462" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="18" font-weight="800" fill="#111827">Toyota Camry</text>
        <text x="36" y="478" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="500" fill="#6B7280">Camry SE • 2022 • 24,500 km</text>

        <text x="290" y="462" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="18" font-weight="800" fill="#111827">50</text>
        <text x="312" y="462" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">credits/day</text>

        <g transform="translate(36, 492)">
            <rect x="0" y="0" width="76" height="30" rx="15" fill="#F3F4F6"/>
            <text x="10" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">2.5L 4-Cyl</text>

            <rect x="84" y="0" width="58" height="30" rx="15" fill="#F3F4F6"/>
            <text x="96" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">5 Seats</text>

            <rect x="150" y="0" width="70" height="30" rx="15" fill="#F3F4F6"/>
            <text x="160" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">Gasoline</text>

            <rect x="228" y="0" width="96" height="30" rx="15" fill="#F3F4F6"/>
            <text x="238" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">8-Speed Auto</text>
        </g>

        <text x="36" y="546" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" fill="#4B5563">A reliable and fuel-efficient sedan designed for comfortable daily driving with smooth handling and modern safety features.</text>

        <line x1="36" y1="575" x2="376" y2="575" stroke="#F1F3F5" stroke-width="1"/>

        <g transform="translate(36, 595)">
            <text x="0" y="24" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">Rating: </text>
            {render_stars(42, 12, 4.8, "#EAB308", "#111827", 14)}

            <rect x="230" y="0" width="110" height="38" rx="19" fill="#111827"/>
            <text x="268" y="24" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#FFFFFF">Rent</text>
        </g>
    </g>

    <g id="PopularSection">
        <text x="20" y="688" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="800" fill="#111827">Popular Cars</text>
        <text x="340" y="688" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">5 available</text>

        <g id="PopCard1">
            <rect x="20" y="700" width="174" height="156" rx="14" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
            <image href="{images['tesla_model3']}" x="20" y="700" width="174" height="90" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipPop1)"/>
            <text x="30" y="806" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#111827">Tesla Model 3</text>
            <text x="30" y="822" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">75 credits/day</text>
            <text x="30" y="842" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#EAB308">★ 4.9</text>
            <rect x="120" y="822" width="64" height="26" rx="13" fill="#111827"/>
            <text x="140" y="839" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">Rent</text>
        </g>

        <g id="PopCard2">
            <rect x="202" y="700" width="174" height="156" rx="14" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
            <image href="{images['hyundai_tucson']}" x="202" y="700" width="174" height="90" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipPop2)"/>
            <text x="212" y="806" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#111827">Hyundai Tucson</text>
            <text x="212" y="822" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">45 credits/day</text>
            <text x="212" y="842" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#EAB308">★ 4.5</text>
            <rect x="302" y="822" width="64" height="26" rx="13" fill="#111827"/>
            <text x="322" y="839" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">Rent</text>
        </g>
    </g>

    {get_nav_bar(dark=False)}
</svg>"""

# ==========================================
# SCREEN 2: Home Catalogue (Dark Mode)
# ==========================================
def generate_screen2_home_dark():
    return f"""<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 412 892" width="412" height="892">
    <defs>
        <clipPath id="clipHeroImgDark"><rect x="20" y="262" width="372" height="175" rx="18"/></clipPath>
        <clipPath id="clipPop1Dark"><rect x="20" y="700" width="174" height="90" rx="14"/></clipPath>
        <clipPath id="clipPop2Dark"><rect x="202" y="700" width="174" height="90" rx="14"/></clipPath>
        <clipPath id="clipFav1Dark"><rect x="20" y="112" width="70" height="50" rx="10"/></clipPath>
    </defs>

    <rect width="412" height="892" fill="#000000"/>

    {get_status_bar(dark=True)}

    <g id="TopAppBar">
        <text x="20" y="72" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="22" font-weight="800" fill="#FFFFFF">Car Rental</text>
        
        <rect x="180" y="52" width="105" height="28" rx="14" fill="#FFFFFF"/>
        <text x="190" y="70" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#000000">Credits: 500</text>
        
        <circle cx="310" cy="66" r="14" fill="#1C1C1E"/>
        <text x="303" y="70" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="700" fill="#FFFFFF">EN</text>

        <g transform="translate(342, 54)">
            <rect width="48" height="24" rx="12" fill="#FFFFFF"/>
            <circle cx="36" cy="12" r="9" fill="#000000"/>
            <path d="M37 9a4 4 0 11-4 4 3 3 0 004-4z" fill="#FFFFFF"/>
        </g>
    </g>

    <g id="FavouritesSection">
        <text x="20" y="104" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#FFFFFF">Your Favourites</text>
        <text x="352" y="104" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#EF4444">(1)</text>

        <g>
            <rect x="20" y="112" width="180" height="50" rx="10" fill="#121214" stroke="#27272A" stroke-width="1"/>
            <image href="{images['toyota_camry']}" x="20" y="112" width="70" height="50" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipFav1Dark)"/>
            <text x="96" y="132" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="700" fill="#FFFFFF">Toyota Camry</text>
            <text x="96" y="148" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#9CA3AF">50 credits</text>
            <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" transform="translate(178, 128) scale(0.65)" fill="#EF4444"/>
        </g>
    </g>

    <g id="SearchAndSort">
        <rect x="20" y="174" width="270" height="42" rx="10" fill="#121214" stroke="#27272A" stroke-width="1"/>
        <path d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0016 9.5 6.5 6.5 0 109.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" transform="translate(30, 185) scale(0.9)" fill="#9CA3AF"/>
        <text x="56" y="200" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" fill="#6B7280">Search car name or model...</text>

        <rect x="298" y="174" width="94" height="42" rx="10" fill="#121214" stroke="#27272A" stroke-width="1"/>
        <text x="312" y="199" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="600" fill="#FFFFFF">Rating</text>
        <path d="M7 10l5 5 5-5z" transform="translate(366, 186) scale(0.9)" fill="#FFFFFF"/>
    </g>

    <g id="SortChips">
        <rect x="20" y="226" width="76" height="26" rx="13" fill="#FFFFFF"/>
        <text x="34" y="243" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#000000">★ Rating</text>

        <rect x="104" y="226" width="76" height="26" rx="13" fill="#121214" stroke="#27272A" stroke-width="1"/>
        <text x="120" y="243" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="500" fill="#9CA3AF">Newest</text>

        <rect x="188" y="226" width="98" height="26" rx="13" fill="#121214" stroke="#27272A" stroke-width="1"/>
        <text x="200" y="243" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="500" fill="#9CA3AF">Lowest Price</text>
    </g>

    <g id="HeroCard">
        <rect x="20" y="262" width="372" height="400" rx="18" fill="#121214" stroke="#27272A" stroke-width="1"/>
        
        <image href="{images['toyota_camry']}" x="20" y="262" width="372" height="175" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipHeroImgDark)"/>
        
        <circle cx="44" cy="350" r="16" fill="#000000" opacity="0.8"/>
        <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z" transform="translate(34, 340) scale(0.85)" fill="#FFFFFF"/>

        <circle cx="368" cy="350" r="16" fill="#000000" opacity="0.8"/>
        <path d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z" transform="translate(358, 340) scale(0.85)" fill="#FFFFFF"/>

        <rect x="334" y="274" width="46" height="22" rx="11" fill="#000000" opacity="0.8"/>
        <text x="345" y="289" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">1 / 5</text>

        <circle cx="50" cy="285" r="16" fill="#1C1C1E" opacity="0.9"/>
        <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" transform="translate(41, 276) scale(0.75)" fill="#EF4444"/>

        <text x="36" y="462" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="18" font-weight="800" fill="#FFFFFF">Toyota Camry</text>
        <text x="36" y="478" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="500" fill="#9CA3AF">Camry SE • 2022 • 24,500 km</text>

        <text x="290" y="462" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="18" font-weight="800" fill="#FFFFFF">50</text>
        <text x="312" y="462" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#9CA3AF">credits/day</text>

        <g transform="translate(36, 492)">
            <rect x="0" y="0" width="76" height="30" rx="15" fill="#1C1C1E"/>
            <text x="10" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#FFFFFF">2.5L 4-Cyl</text>

            <rect x="84" y="0" width="58" height="30" rx="15" fill="#1C1C1E"/>
            <text x="96" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#FFFFFF">5 Seats</text>

            <rect x="150" y="0" width="70" height="30" rx="15" fill="#1C1C1E"/>
            <text x="160" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#FFFFFF">Gasoline</text>

            <rect x="228" y="0" width="96" height="30" rx="15" fill="#1C1C1E"/>
            <text x="238" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#FFFFFF">8-Speed Auto</text>
        </g>

        <text x="36" y="546" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" fill="#9CA3AF">A reliable and fuel-efficient sedan designed for comfortable daily driving with smooth handling and modern safety features.</text>

        <line x1="36" y1="575" x2="376" y2="575" stroke="#1C1C1E" stroke-width="1"/>

        <g transform="translate(36, 595)">
            <text x="0" y="24" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#9CA3AF">Rating: </text>
            {render_stars(42, 12, 4.8, "#EAB308", "#FFFFFF", 14)}

            <rect x="230" y="0" width="110" height="38" rx="19" fill="#FFFFFF"/>
            <text x="268" y="24" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#000000">Rent</text>
        </g>
    </g>

    <g id="PopularSection">
        <text x="20" y="688" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="800" fill="#FFFFFF">Popular Cars</text>
        <text x="340" y="688" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#9CA3AF">5 available</text>

        <g id="PopCard1">
            <rect x="20" y="700" width="174" height="156" rx="14" fill="#121214" stroke="#27272A" stroke-width="1"/>
            <image href="{images['tesla_model3']}" x="20" y="700" width="174" height="90" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipPop1Dark)"/>
            <text x="30" y="806" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#FFFFFF">Tesla Model 3</text>
            <text x="30" y="822" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#9CA3AF">75 credits/day</text>
            <text x="30" y="842" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#EAB308">★ 4.9</text>
            <rect x="120" y="822" width="64" height="26" rx="13" fill="#FFFFFF"/>
            <text x="140" y="839" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#000000">Rent</text>
        </g>

        <g id="PopCard2">
            <rect x="202" y="700" width="174" height="156" rx="14" fill="#121214" stroke="#27272A" stroke-width="1"/>
            <image href="{images['hyundai_tucson']}" x="202" y="700" width="174" height="90" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipPop2Dark)"/>
            <text x="212" y="806" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#FFFFFF">Hyundai Tucson</text>
            <text x="212" y="822" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#9CA3AF">45 credits/day</text>
            <text x="212" y="842" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#EAB308">★ 4.5</text>
            <rect x="302" y="822" width="64" height="26" rx="13" fill="#FFFFFF"/>
            <text x="322" y="839" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#000000">Rent</text>
        </g>
    </g>

    {get_nav_bar(dark=True)}
</svg>"""

# ==========================================
# SCREEN 3: Search & Filter Active (Tesla)
# ==========================================
def generate_screen3_search_active():
    return f"""<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 412 892" width="412" height="892">
    <defs>
        <clipPath id="clipHeroImgTesla"><rect x="20" y="262" width="372" height="175" rx="18"/></clipPath>
        <filter id="shadowS3" x="-5%" y="-5%" width="110%" height="110%"><feDropShadow dx="0" dy="2" stdDeviation="4" flood-opacity="0.06"/></filter>
    </defs>

    <rect width="412" height="892" fill="#F8F9FA"/>

    {get_status_bar(dark=False)}

    <g id="TopAppBar">
        <text x="20" y="72" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="22" font-weight="800" fill="#111827">Car Rental</text>
        
        <rect x="180" y="52" width="105" height="28" rx="14" fill="#111827"/>
        <text x="190" y="70" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#F3F4F6">Credits: 500</text>
        
        <circle cx="310" cy="66" r="14" fill="#F1F3F5"/>
        <text x="303" y="70" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="700" fill="#111827">EN</text>

        <g transform="translate(342, 54)">
            <rect width="48" height="24" rx="12" fill="#E5E7EB"/>
            <circle cx="12" cy="12" r="9" fill="#FFFFFF"/>
            <circle cx="12" cy="12" r="3.5" fill="#EAB308"/>
        </g>
    </g>

    <g id="SearchAndSort">
        <rect x="20" y="112" width="270" height="42" rx="10" fill="#FFFFFF" stroke="#111827" stroke-width="1.5"/>
        <path d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0016 9.5 6.5 6.5 0 109.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" transform="translate(30, 123) scale(0.9)" fill="#111827"/>
        <text x="56" y="138" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="600" fill="#111827">Tesla</text>
        
        <circle cx="270" cy="133" r="10" fill="#E5E7EB"/>
        <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z" transform="translate(263, 126) scale(0.6)" fill="#4B5563"/>

        <rect x="298" y="112" width="94" height="42" rx="10" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <text x="312" y="137" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="600" fill="#111827">Rating</text>
        <path d="M7 10l5 5 5-5z" transform="translate(366, 124) scale(0.9)" fill="#111827"/>
    </g>

    <g id="ResultBadge">
        <rect x="20" y="166" width="140" height="26" rx="13" fill="#E5E7EB"/>
        <text x="32" y="183" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#111827">1 vehicle matching</text>
    </g>

    <g id="HeroCardTesla" filter="url(#shadowS3)">
        <rect x="20" y="206" width="372" height="420" rx="18" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        
        <image href="{images['tesla_model3']}" x="20" y="206" width="372" height="185" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipHeroImgTesla)"/>
        
        <rect x="334" y="218" width="46" height="22" rx="11" fill="#111827" opacity="0.8"/>
        <text x="345" y="233" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">1 / 1</text>

        <circle cx="50" cy="229" r="16" fill="#FFFFFF" opacity="0.9"/>
        <path d="M16.5 3c-1.74 0-3.41.81-4.5 2.09C10.91 3.81 9.24 3 7.5 3 4.42 3 2 5.42 2 8.5c0 3.78 3.4 6.86 8.55 11.54L12 21.35l1.45-1.32C18.6 15.36 22 12.28 22 8.5 22 5.42 19.58 3 16.5 3zm-4.4 15.55l-.1.1-.1-.1C7.14 14.24 4 11.39 4 8.5 4 6.5 5.5 5 7.5 5c1.54 0 3.04.99 3.57 2.36h1.87C13.46 5.99 14.96 5 16.5 5c2 0 3.5 1.5 3.5 3.5 0 2.89-3.14 5.74-7.9 10.05z" transform="translate(41, 220) scale(0.75)" fill="#6B7280"/>

        <text x="36" y="416" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="18" font-weight="800" fill="#111827">Tesla Model 3</text>
        <text x="36" y="434" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="500" fill="#6B7280">Long Range Dual Motor • 2023 • 12,000 km</text>

        <text x="290" y="416" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="18" font-weight="800" fill="#111827">75</text>
        <text x="312" y="416" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">credits/day</text>

        <g transform="translate(36, 452)">
            <rect x="0" y="0" width="86" height="30" rx="15" fill="#F3F4F6"/>
            <text x="10" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">Dual Motors</text>

            <rect x="94" y="0" width="58" height="30" rx="15" fill="#F3F4F6"/>
            <text x="106" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">5 Seats</text>

            <rect x="160" y="0" width="70" height="30" rx="15" fill="#F3F4F6"/>
            <text x="170" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">Electric</text>

            <rect x="238" y="0" width="96" height="30" rx="15" fill="#F3F4F6"/>
            <text x="248" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">Single-Speed</text>
        </g>

        <text x="36" y="510" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" fill="#4B5563">High-performance all-electric luxury sedan featuring instant torque, Autopilot highway assist, and ultra-low running costs.</text>

        <line x1="36" y1="545" x2="376" y2="545" stroke="#F1F3F5" stroke-width="1"/>

        <g transform="translate(36, 565)">
            <text x="0" y="24" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">Rating: </text>
            {render_stars(42, 12, 4.9, "#EAB308", "#111827", 14)}

            <rect x="230" y="0" width="110" height="38" rx="19" fill="#111827"/>
            <text x="268" y="24" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#FFFFFF">Rent</text>
        </g>
    </g>

    {get_nav_bar(dark=False)}
</svg>"""

# ==========================================
# SCREEN 4: Rental Details (Valid 3 Days)
# ==========================================
def generate_screen4_rental_valid():
    return f"""<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 412 892" width="412" height="892">
    <defs>
        <clipPath id="clipRentalDetail"><rect x="20" y="96" width="372" height="190" rx="18"/></clipPath>
        <filter id="shadowR4" x="-5%" y="-5%" width="110%" height="110%"><feDropShadow dx="0" dy="2" stdDeviation="4" flood-opacity="0.06"/></filter>
    </defs>

    <rect width="412" height="892" fill="#F8F9FA"/>

    {get_status_bar(dark=False)}

    <g id="TopAppBarRental">
        <circle cx="38" cy="62" r="18" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z" transform="translate(26, 50)" fill="#111827"/>
        <text x="74" y="68" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="18" font-weight="800" fill="#111827">Rental Details</text>

        <rect x="282" y="48" width="110" height="28" rx="14" fill="#111827"/>
        <text x="294" y="66" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">Balance: 500</text>
    </g>

    <g id="VehicleDetailImage">
        <image href="{images['toyota_camry']}" x="20" y="96" width="372" height="190" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipRentalDetail)"/>
    </g>

    <g transform="translate(20, 308)">
        <text x="0" y="20" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="20" font-weight="800" fill="#111827">Toyota Camry</text>
        <text x="0" y="40" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="500" fill="#6B7280">Camry SE • 2022 • Celestial Silver</text>
        
        <g transform="translate(0, 48)">
            {render_stars(0, 0, 4.8, "#EAB308", "#111827", 14)}
        </g>
    </g>

    <g transform="translate(20, 388)">
        <rect x="0" y="0" width="86" height="28" rx="14" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <text x="14" y="18" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#111827">⛽ Gasoline</text>

        <rect x="94" y="0" width="94" height="28" rx="14" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <text x="108" y="18" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#111827">⚙️ 8-Speed Auto</text>

        <rect x="196" y="0" width="76" height="28" rx="14" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <text x="210" y="18" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#111827">💺 5 Seats</text>

        <rect x="280" y="0" width="92" height="28" rx="14" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <text x="292" y="18" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#111827">⚡ 16.5 kmpl</text>
    </g>

    <g id="CalculationCard" filter="url(#shadowR4)">
        <rect x="20" y="432" width="372" height="136" rx="16" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        
        <text x="38" y="462" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="600" fill="#6B7280">Daily Rental Rate</text>
        <text x="320" y="462" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#111827">50 credits/day</text>

        <text x="38" y="492" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="600" fill="#6B7280">Selected Duration</text>
        <text x="328" y="492" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#111827">3 Days</text>

        <line x1="38" y1="510" x2="374" y2="510" stroke="#F1F3F5" stroke-width="1"/>

        <text x="38" y="542" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="800" fill="#111827">Total Rental Cost</text>
        <text x="286" y="542" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="16" font-weight="800" fill="#111827">150 Credits</text>
    </g>

    <g id="DurationSlider" filter="url(#shadowR4)">
        <rect x="20" y="582" width="372" height="126" rx="16" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        
        <text x="38" y="612" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#111827">Rental Duration</text>
        <rect x="306" y="596" width="70" height="24" rx="12" fill="#111827"/>
        <text x="324" y="612" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">3 Days</text>

        <rect x="38" y="648" width="336" height="6" rx="3" fill="#E5E7EB"/>
        <rect x="38" y="648" width="112" height="6" rx="3" fill="#111827"/>
        <circle cx="150" cy="651" r="11" fill="#111827" stroke="#FFFFFF" stroke-width="3" filter="url(#shadowR4)"/>

        <text x="38" y="682" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">1 Day</text>
        <text x="330" y="682" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">7 Days</text>
    </g>

    <g id="ActionButtons" transform="translate(20, 780)">
        <rect x="0" y="0" width="180" height="50" rx="14" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1.5"/>
        <text x="44" y="31" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="700" fill="#111827">Cancel Booking</text>

        <rect x="192" y="0" width="180" height="50" rx="14" fill="#111827"/>
        <text x="234" y="31" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="700" fill="#FFFFFF">Confirm Booking</text>
    </g>

    {get_nav_bar(dark=False)}
</svg>"""

# ==========================================
# SCREEN 5: Rental Details (Error > 400 Credits)
# ==========================================
def generate_screen5_rental_error():
    return f"""<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 412 892" width="412" height="892">
    <defs>
        <clipPath id="clipRentalBMW"><rect x="20" y="96" width="372" height="190" rx="18"/></clipPath>
        <filter id="shadowR5" x="-5%" y="-5%" width="110%" height="110%"><feDropShadow dx="0" dy="2" stdDeviation="4" flood-opacity="0.06"/></filter>
    </defs>

    <rect width="412" height="892" fill="#F8F9FA"/>

    {get_status_bar(dark=False)}

    <g id="TopAppBarRental">
        <circle cx="38" cy="62" r="18" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z" transform="translate(26, 50)" fill="#111827"/>
        <text x="74" y="68" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="18" font-weight="800" fill="#111827">Rental Details</text>

        <rect x="282" y="48" width="110" height="28" rx="14" fill="#111827"/>
        <text x="294" y="66" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">Balance: 500</text>
    </g>

    <g id="VehicleDetailImage">
        <image href="{images['bmw_3series']}" x="20" y="96" width="372" height="190" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipRentalBMW)"/>
    </g>

    <g transform="translate(20, 308)">
        <text x="0" y="20" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="20" font-weight="800" fill="#111827">BMW 3 Series</text>
        <text x="0" y="40" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="500" fill="#6B7280">330i xDrive • 2024 • Black Sapphire</text>
        
        <g transform="translate(0, 48)">
            {render_stars(0, 0, 4.7, "#EAB308", "#111827", 14)}
        </g>
    </g>

    <g id="CalculationCard" filter="url(#shadowR5)">
        <rect x="20" y="388" width="372" height="136" rx="16" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        
        <text x="38" y="418" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="600" fill="#6B7280">Daily Rental Rate</text>
        <text x="320" y="418" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#111827">90 credits/day</text>

        <text x="38" y="448" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="600" fill="#6B7280">Selected Duration</text>
        <text x="328" y="448" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#DC2626">5 Days</text>

        <line x1="38" y1="466" x2="374" y2="466" stroke="#F1F3F5" stroke-width="1"/>

        <text x="38" y="498" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="800" fill="#111827">Total Rental Cost</text>
        <text x="286" y="498" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="16" font-weight="800" fill="#DC2626">450 Credits</text>
    </g>

    <g id="DurationSlider" filter="url(#shadowR5)">
        <rect x="20" y="538" width="372" height="116" rx="16" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        
        <text x="38" y="568" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#111827">Rental Duration</text>
        <rect x="306" y="552" width="70" height="24" rx="12" fill="#DC2626"/>
        <text x="324" y="568" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">5 Days</text>

        <rect x="38" y="598" width="336" height="6" rx="3" fill="#E5E7EB"/>
        <rect x="38" y="598" width="224" height="6" rx="3" fill="#DC2626"/>
        <circle cx="262" cy="601" r="11" fill="#DC2626" stroke="#FFFFFF" stroke-width="3" filter="url(#shadowR5)"/>

        <text x="38" y="632" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">1 Day</text>
        <text x="330" y="632" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">7 Days</text>
    </g>

    <g id="ErrorBanner">
        <rect x="20" y="670" width="372" height="74" rx="14" fill="#FEF2F2" stroke="#FCA5A5" stroke-width="1.5"/>
        <circle cx="48" cy="707" r="14" fill="#DC2626"/>
        <path d="M1 21h22L12 2 1 21zm12-3h-2v-2h2v2zm0-4h-2v-4h2v4z" transform="translate(37, 696) scale(0.9)" fill="#FFFFFF"/>
        
        <text x="74" y="700" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="700" fill="#991B1B">Limit Exceeded</text>
        <text x="74" y="718" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" fill="#B91C1C">Total cost (450 credits) exceeds individual</text>
        <text x="74" y="732" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" fill="#B91C1C">rental limit of 400 credits.</text>
    </g>

    <g id="ActionButtons" transform="translate(20, 780)">
        <rect x="0" y="0" width="180" height="50" rx="14" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1.5"/>
        <text x="44" y="31" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="700" fill="#111827">Cancel Booking</text>

        <rect x="192" y="0" width="180" height="50" rx="14" fill="#9CA3AF"/>
        <text x="234" y="31" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="700" fill="#FFFFFF">Confirm Booking</text>
    </g>

    {get_nav_bar(dark=False)}
</svg>"""

# ==========================================
# SCREEN 6: Booking Confirmed & Rented Active
# ==========================================
def generate_screen6_booking_success():
    return f"""<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 412 892" width="412" height="892">
    <defs>
        <clipPath id="clipHeroImgTeslaS6"><rect x="20" y="262" width="372" height="175" rx="18"/></clipPath>
        <clipPath id="clipRentedCamry"><rect x="20" y="688" width="90" height="70" rx="10"/></clipPath>
        <filter id="shadowS6" x="-5%" y="-5%" width="110%" height="110%"><feDropShadow dx="0" dy="2" stdDeviation="4" flood-opacity="0.06"/></filter>
    </defs>

    <rect width="412" height="892" fill="#F8F9FA"/>

    {get_status_bar(dark=False)}

    <g id="TopAppBar">
        <text x="20" y="72" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="22" font-weight="800" fill="#111827">Car Rental</text>
        
        <rect x="180" y="52" width="105" height="28" rx="14" fill="#111827"/>
        <text x="190" y="70" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#22C55E">Credits: 350</text>
        
        <circle cx="310" cy="66" r="14" fill="#F1F3F5"/>
        <text x="303" y="70" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="700" fill="#111827">EN</text>

        <g transform="translate(342, 54)">
            <rect width="48" height="24" rx="12" fill="#E5E7EB"/>
            <circle cx="12" cy="12" r="9" fill="#FFFFFF"/>
            <circle cx="12" cy="12" r="3.5" fill="#EAB308"/>
        </g>
    </g>

    <g id="ToastFeedback" filter="url(#shadowS6)">
        <rect x="20" y="96" width="372" height="48" rx="12" fill="#111827"/>
        <circle cx="44" cy="120" r="10" fill="#22C55E"/>
        <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z" transform="translate(36, 112) scale(0.7)" fill="#FFFFFF"/>
        <text x="64" y="125" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="600" fill="#FFFFFF">Booking successful! 150 credits deducted for 3 day(s).</text>
    </g>

    <g id="SearchAndSort" transform="translate(0, -10)">
        <rect x="20" y="166" width="270" height="42" rx="10" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <path d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0016 9.5 6.5 6.5 0 109.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" transform="translate(30, 177) scale(0.9)" fill="#6B7280"/>
        <text x="56" y="192" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" fill="#9CA3AF">Search car name or model...</text>

        <rect x="298" y="166" width="94" height="42" rx="10" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <text x="312" y="191" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="600" fill="#111827">Rating</text>
        <path d="M7 10l5 5 5-5z" transform="translate(366, 178) scale(0.9)" fill="#111827"/>
    </g>

    <g id="HeroCardTesla" filter="url(#shadowS6)">
        <rect x="20" y="218" width="372" height="434" rx="18" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        
        <image href="{images['tesla_model3']}" x="20" y="218" width="372" height="175" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipHeroImgTeslaS6)"/>
        
        <rect x="334" y="230" width="46" height="22" rx="11" fill="#111827" opacity="0.8"/>
        <text x="345" y="245" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">1 / 4</text>

        <text x="36" y="418" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="18" font-weight="800" fill="#111827">Tesla Model 3</text>
        <text x="36" y="434" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="500" fill="#6B7280">Long Range Dual Motor • 2023 • 12,000 km</text>

        <text x="290" y="418" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="18" font-weight="800" fill="#111827">75</text>
        <text x="312" y="418" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">credits/day</text>

        <g transform="translate(36, 448)">
            <rect x="0" y="0" width="86" height="30" rx="15" fill="#F3F4F6"/>
            <text x="10" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">Dual Motors</text>

            <rect x="94" y="0" width="58" height="30" rx="15" fill="#F3F4F6"/>
            <text x="106" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">5 Seats</text>

            <rect x="160" y="0" width="70" height="30" rx="15" fill="#F3F4F6"/>
            <text x="170" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">Electric</text>
        </g>

        <text x="36" y="506" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" fill="#4B5563">High-performance all-electric luxury sedan featuring instant torque and Autopilot highway assist.</text>

        <line x1="36" y1="535" x2="376" y2="535" stroke="#F1F3F5" stroke-width="1"/>

        <g transform="translate(36, 555)">
            <text x="0" y="24" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">Rating: </text>
            {render_stars(42, 12, 4.9, "#EAB308", "#111827", 14)}

            <rect x="230" y="0" width="110" height="38" rx="19" fill="#111827"/>
            <text x="268" y="24" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#FFFFFF">Rent</text>
        </g>
    </g>

    <g id="ActiveRentalsSection">
        <text x="20" y="676" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="800" fill="#111827">Active Rentals</text>
        <text x="352" y="676" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#22C55E">(1 active)</text>

        <g id="RentedCard" filter="url(#shadowS6)">
            <rect x="20" y="688" width="372" height="78" rx="14" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
            <image href="{images['toyota_camry']}" x="26" y="694" width="84" height="66" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipRentedCamry)"/>
            
            <text x="122" y="718" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="700" fill="#111827">Toyota Camry</text>
            <text x="122" y="736" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="500" fill="#6B7280">3 days rental • 150 credits</text>
            <text x="122" y="752" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="700" fill="#22C55E">● Active Booking</text>

            <rect x="290" y="708" width="90" height="38" rx="10" fill="#FEF2F2" stroke="#FCA5A5" stroke-width="1"/>
            <text x="312" y="732" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="700" fill="#DC2626">Return</text>
        </g>
    </g>

    {get_nav_bar(dark=False)}
</svg>"""

# ==========================================
# SCREEN 7: Vietnamese Localisation (Tiếng Việt)
# ==========================================
def generate_screen7_vietnamese():
    return f"""<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 412 892" width="412" height="892">
    <defs>
        <clipPath id="clipHeroImgVN"><rect x="20" y="262" width="372" height="175" rx="18"/></clipPath>
        <clipPath id="clipPop1VN"><rect x="20" y="700" width="174" height="90" rx="14"/></clipPath>
        <clipPath id="clipPop2VN"><rect x="202" y="700" width="174" height="90" rx="14"/></clipPath>
        <filter id="shadowVN" x="-5%" y="-5%" width="110%" height="110%"><feDropShadow dx="0" dy="2" stdDeviation="4" flood-opacity="0.06"/></filter>
    </defs>

    <rect width="412" height="892" fill="#F8F9FA"/>

    {get_status_bar(dark=False)}

    <g id="TopAppBar">
        <text x="20" y="72" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="22" font-weight="800" fill="#111827">Thuê Xe Ô Tô</text>
        
        <rect x="180" y="52" width="105" height="28" rx="14" fill="#111827"/>
        <text x="190" y="70" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#F3F4F6">Số dư: 500</text>
        
        <circle cx="310" cy="66" r="14" fill="#111827"/>
        <text x="303" y="70" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="700" fill="#FFFFFF">VI</text>

        <g transform="translate(342, 54)">
            <rect width="48" height="24" rx="12" fill="#E5E7EB"/>
            <circle cx="12" cy="12" r="9" fill="#FFFFFF"/>
            <circle cx="12" cy="12" r="3.5" fill="#EAB308"/>
        </g>
    </g>

    <g id="FavouritesSection">
        <text x="20" y="104" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#111827">Xe Yêu Thích Của Bạn</text>
        <text x="352" y="104" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#EF4444">(1)</text>

        <g>
            <rect x="20" y="112" width="180" height="50" rx="10" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
            <image href="{images['toyota_camry']}" x="20" y="112" width="70" height="50" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipHeroImgVN)"/>
            <text x="96" y="132" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="700" fill="#111827">Toyota Camry</text>
            <text x="96" y="148" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#111827">50 điểm</text>
            <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" transform="translate(178, 128) scale(0.65)" fill="#EF4444"/>
        </g>
    </g>

    <g id="SearchAndSort">
        <rect x="20" y="174" width="270" height="42" rx="10" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <path d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0016 9.5 6.5 6.5 0 109.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" transform="translate(30, 185) scale(0.9)" fill="#6B7280"/>
        <text x="56" y="200" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" fill="#9CA3AF">Tìm tên hoặc dòng xe...</text>

        <rect x="298" y="174" width="94" height="42" rx="10" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <text x="312" y="199" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="600" fill="#111827">Đánh giá</text>
        <path d="M7 10l5 5 5-5z" transform="translate(366, 186) scale(0.9)" fill="#111827"/>
    </g>

    <g id="HeroCard" filter="url(#shadowVN)">
        <rect x="20" y="238" width="372" height="424" rx="18" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        
        <image href="{images['toyota_camry']}" x="20" y="238" width="372" height="175" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipHeroImgVN)"/>
        
        <rect x="334" y="250" width="46" height="22" rx="11" fill="#111827" opacity="0.8"/>
        <text x="345" y="265" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">1 / 5</text>

        <text x="36" y="438" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="18" font-weight="800" fill="#111827">Toyota Camry</text>
        <text x="36" y="454" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="500" fill="#6B7280">Đời 2022 • Đã đi 24.500 km</text>

        <text x="290" y="438" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="18" font-weight="800" fill="#111827">50</text>
        <text x="312" y="438" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">điểm/ngày</text>

        <g transform="translate(36, 468)">
            <rect x="0" y="0" width="76" height="30" rx="15" fill="#F3F4F6"/>
            <text x="10" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">2.5L 4-Cyl</text>

            <rect x="84" y="0" width="58" height="30" rx="15" fill="#F3F4F6"/>
            <text x="96" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">5 Chỗ</text>

            <rect x="150" y="0" width="70" height="30" rx="15" fill="#F3F4F6"/>
            <text x="160" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">Xăng</text>

            <rect x="228" y="0" width="96" height="30" rx="15" fill="#F3F4F6"/>
            <text x="238" y="19" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">Tự Động 8 Cấp</text>
        </g>

        <text x="36" y="522" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" fill="#4B5563">Dòng xe sedan bền bỉ, tiết kiệm nhiên liệu, mang lại trải nghiệm lái êm ái với các tính năng an toàn hiện đại.</text>

        <line x1="36" y1="550" x2="376" y2="550" stroke="#F1F3F5" stroke-width="1"/>

        <g transform="translate(36, 570)">
            <text x="0" y="24" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">Đánh giá: </text>
            {render_stars(55, 12, 4.8, "#EAB308", "#111827", 14)}

            <rect x="230" y="0" width="110" height="38" rx="19" fill="#111827"/>
            <text x="254" y="24" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#FFFFFF">Thuê Xe</text>
        </g>
    </g>

    <g id="PopularSection">
        <text x="20" y="688" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="800" fill="#111827">Xe Phổ Biến</text>
        <text x="330" y="688" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">5 xe sẵn sàng</text>

        <g id="PopCard1">
            <rect x="20" y="700" width="174" height="156" rx="14" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
            <image href="{images['tesla_model3']}" x="20" y="700" width="174" height="90" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipPop1VN)"/>
            <text x="30" y="806" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#111827">Tesla Model 3</text>
            <text x="30" y="822" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">75 điểm/ngày</text>
            <text x="30" y="842" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#EAB308">★ 4.9</text>
            <rect x="110" y="822" width="74" height="26" rx="13" fill="#111827"/>
            <text x="126" y="839" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">Thuê Xe</text>
        </g>

        <g id="PopCard2">
            <rect x="202" y="700" width="174" height="156" rx="14" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
            <image href="{images['hyundai_tucson']}" x="202" y="700" width="174" height="90" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipPop2VN)"/>
            <text x="212" y="806" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#111827">Hyundai Tucson</text>
            <text x="212" y="822" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">45 điểm/ngày</text>
            <text x="212" y="842" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#EAB308">★ 4.5</text>
            <rect x="292" y="822" width="74" height="26" rx="13" fill="#111827"/>
            <text x="308" y="839" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">Thuê Xe</text>
        </g>
    </g>

    {get_nav_bar(dark=False)}
</svg>"""

# ==========================================
# SCREEN 8: Home Catalogue (Landscape Orientation)
# ViewBox: 892 x 412 (Adaptive side-by-side layout)
# ==========================================
def generate_screen8_catalogue_landscape():
    return f"""<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 892 412" width="892" height="412">
    <defs>
        <clipPath id="clipLsHero"><rect x="0" y="0" width="220" height="135" rx="14"/></clipPath>
        <clipPath id="clipLsPop1"><rect x="0" y="0" width="140" height="90" rx="10"/></clipPath>
        <clipPath id="clipLsPop2"><rect x="0" y="0" width="140" height="90" rx="10"/></clipPath>
        <filter id="shadowLs" x="-5%" y="-5%" width="110%" height="110%"><feDropShadow dx="0" dy="2" stdDeviation="4" flood-opacity="0.06"/></filter>
    </defs>

    <rect width="892" height="412" fill="#F8F9FA"/>

    {get_status_bar_landscape(dark=False)}

    <!-- Top App Bar in Landscape -->
    <g id="TopAppBarLandscape">
        <text x="36" y="58" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="20" font-weight="800" fill="#111827">Car Rental</text>
        
        <!-- Orientation Indicator Badge -->
        <rect x="156" y="42" width="112" height="22" rx="11" fill="#F3F4F6"/>
        <text x="166" y="57" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="700" fill="#4B5563">🔄 Landscape</text>

        <!-- Right actions -->
        <rect x="620" y="38" width="105" height="28" rx="14" fill="#111827"/>
        <text x="630" y="56" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">Credits: 500</text>

        <circle cx="748" cy="52" r="14" fill="#F1F3F5"/>
        <text x="741" y="56" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="700" fill="#111827">EN</text>

        <!-- Switch -->
        <g transform="translate(778, 40)">
            <rect width="44" height="24" rx="12" fill="#E5E7EB"/>
            <circle cx="12" cy="12" r="9" fill="#FFFFFF"/>
            <circle cx="12" cy="12" r="3.5" fill="#EAB308"/>
        </g>
    </g>

    <!-- Landscape Main 2-Panel Content Area -->
    <!-- LEFT PANEL: Account Card & Search & Sort Controls (width: 320px) -->
    <g id="LeftPanel" transform="translate(36, 78)">
        <!-- Account Balance Card -->
        <g id="AccountBalanceCard">
            <rect x="0" y="0" width="310" height="95" rx="14" fill="#111827"/>
            <text x="18" y="24" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="700" fill="#9CA3AF" letter-spacing="1">AVAILABLE CREDITS</text>
            <text x="18" y="58" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="30" font-weight="900" fill="#FFFFFF">500</text>
            <text x="80" y="58" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="600" fill="#9CA3AF">credits</text>

            <rect x="18" y="68" width="110" height="18" rx="9" fill="#374151"/>
            <text x="26" y="81" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="9" font-weight="700" fill="#22C55E">● 5 cars ready</text>
        </g>

        <!-- Search & Sort Row -->
        <g transform="translate(0, 108)">
            <!-- Search Field -->
            <rect x="0" y="0" width="205" height="38" rx="10" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
            <path d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0016 9.5 6.5 6.5 0 109.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" transform="translate(10, 10) scale(0.8)" fill="#6B7280"/>
            <text x="32" y="24" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" fill="#9CA3AF">Search car...</text>

            <!-- Sort Dropdown -->
            <rect x="215" y="0" width="95" height="38" rx="10" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
            <text x="226" y="24" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#111827">Rating</text>
            <path d="M7 10l5 5 5-5z" transform="translate(285, 12) scale(0.7)" fill="#111827"/>
        </g>

        <!-- Favourites & Active Rentals Mini-Dashboard in Left Column -->
        <g transform="translate(0, 158)">
            <!-- Favourites Header -->
            <text x="0" y="16" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="700" fill="#111827">Favourites (1)</text>

            <rect x="0" y="24" width="310" height="52" rx="10" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
            <image href="{images['toyota_camry']}" x="6" y="28" width="60" height="44" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipLsPop1)"/>
            <text x="74" y="44" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#111827">Toyota Camry</text>
            <text x="74" y="60" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#6B7280">50 credits • ★ 4.8</text>
            
            <circle cx="288" cy="50" r="10" fill="#FEE2E2"/>
            <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" transform="translate(281, 44) scale(0.55)" fill="#EF4444"/>

            <!-- Active Rentals Header -->
            <text x="0" y="100" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="700" fill="#111827">Active Rentals (0)</text>
            <rect x="0" y="108" width="310" height="34" rx="8" fill="#F3F4F6"/>
            <text x="14" y="130" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="500" fill="#6B7280">No active rented vehicles.</text>
        </g>
    </g>

    <!-- RIGHT PANEL: Popular Vehicles Carousel / Cards (width: 500px) -->
    <g id="RightPanel" transform="translate(366, 78)">
        <g id="PopularSectionHeader">
            <text x="0" y="16" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="800" fill="#111827">Popular Vehicles</text>
            <text x="125" y="16" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">• 5 available</text>

            <!-- Navigation Controls Arrow in Landscape -->
            <circle cx="430" cy="12" r="14" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
            <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z" transform="translate(423, 5) scale(0.7)" fill="#111827"/>

            <circle cx="468" cy="12" r="14" fill="#111827"/>
            <path d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z" transform="translate(461, 5) scale(0.7)" fill="#FFFFFF"/>
        </g>

        <!-- Landscape Popular Vehicle Cards (Side by side horizontal swipe) -->
        <!-- Card 1: Toyota Camry -->
        <g id="LandscapeCard1" transform="translate(0, 30)" filter="url(#shadowLs)">
            <rect x="0" y="0" width="236" height="270" rx="16" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
            <image href="{images['toyota_camry']}" x="0" y="0" width="236" height="135" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipLsHero)"/>

            <!-- Heart Favourite Button -->
            <circle cx="212" cy="22" r="13" fill="#FFFFFF" opacity="0.9"/>
            <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" transform="translate(204, 15) scale(0.65)" fill="#EF4444"/>

            <text x="14" y="158" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="800" fill="#111827">Toyota Camry</text>
            <text x="14" y="174" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="500" fill="#6B7280">Camry SE • 2022 • 24,500 km</text>

            <text x="14" y="196" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="16" font-weight="800" fill="#111827">50</text>
            <text x="36" y="196" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#6B7280">credits/day</text>

            <!-- Star Rating in Landscape Card -->
            <g transform="translate(14, 206)">
                {render_stars(0, 0, 4.8, "#EAB308", "#111827", 12)}
            </g>

            <!-- Rent Button -->
            <rect x="14" y="228" width="208" height="32" rx="16" fill="#111827"/>
            <text x="104" y="249" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="700" fill="#FFFFFF">Rent</text>
        </g>

        <!-- Card 2: Tesla Model 3 -->
        <g id="LandscapeCard2" transform="translate(252, 30)" filter="url(#shadowLs)">
            <rect x="0" y="0" width="236" height="270" rx="16" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
            <image href="{images['tesla_model3']}" x="0" y="0" width="236" height="135" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipLsHero)"/>

            <circle cx="212" cy="22" r="13" fill="#FFFFFF" opacity="0.9"/>
            <path d="M16.5 3c-1.74 0-3.41.81-4.5 2.09C10.91 3.81 9.24 3 7.5 3 4.42 3 2 5.42 2 8.5c0 3.78 3.4 6.86 8.55 11.54L12 21.35l1.45-1.32C18.6 15.36 22 12.28 22 8.5 22 5.42 19.58 3 16.5 3zm-4.4 15.55l-.1.1-.1-.1C7.14 14.24 4 11.39 4 8.5 4 6.5 5.5 5 7.5 5c1.54 0 3.04.99 3.57 2.36h1.87C13.46 5.99 14.96 5 16.5 5c2 0 3.5 1.5 3.5 3.5 0 2.89-3.14 5.74-7.9 10.05z" transform="translate(204, 15) scale(0.65)" fill="#6B7280"/>

            <text x="14" y="158" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="800" fill="#111827">Tesla Model 3</text>
            <text x="14" y="174" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="500" fill="#6B7280">Long Range • 2023 • 12,000 km</text>

            <text x="14" y="196" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="16" font-weight="800" fill="#111827">75</text>
            <text x="36" y="196" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#6B7280">credits/day</text>

            <g transform="translate(14, 206)">
                {render_stars(0, 0, 4.9, "#EAB308", "#111827", 12)}
            </g>

            <rect x="14" y="228" width="208" height="32" rx="16" fill="#111827"/>
            <text x="104" y="249" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="700" fill="#FFFFFF">Rent</text>
        </g>
    </g>

    {get_nav_bar_landscape(dark=False)}
</svg>"""

# ==========================================
# SCREEN 9: Rental Details (Landscape Dual-Pane)
# ViewBox: 892 x 412 (Adaptive side-by-side layout)
# ==========================================
def generate_screen9_rental_landscape():
    return f"""<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 892 412" width="892" height="412">
    <defs>
        <clipPath id="clipLsRentalImg"><rect x="0" y="0" width="370" height="150" rx="16"/></clipPath>
        <filter id="shadowLsR" x="-5%" y="-5%" width="110%" height="110%"><feDropShadow dx="0" dy="2" stdDeviation="4" flood-opacity="0.06"/></filter>
    </defs>

    <rect width="892" height="412" fill="#F8F9FA"/>

    {get_status_bar_landscape(dark=False)}

    <!-- Top App Bar Landscape with Back Button -->
    <g id="TopAppBarRentalLandscape">
        <circle cx="50" cy="50" r="16" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>
        <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z" transform="translate(39, 39) scale(0.9)" fill="#111827"/>
        <text x="78" y="56" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="18" font-weight="800" fill="#111827">Rental Details</text>

        <!-- Orientation Badge -->
        <rect x="220" y="40" width="112" height="22" rx="11" fill="#F3F4F6"/>
        <text x="230" y="55" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="700" fill="#4B5563">🔄 Landscape</text>

        <!-- Credits Badge -->
        <rect x="735" y="38" width="120" height="26" rx="13" fill="#111827"/>
        <text x="748" y="55" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">Balance: 500</text>
    </g>

    <!-- 2-COLUMN DUAL-PANE LAYOUT -->
    <!-- LEFT COLUMN: Selected Vehicle Summary Card (width: 380px) -->
    <g id="LeftColumnVehicle" transform="translate(36, 76)" filter="url(#shadowLsR)">
        <rect x="0" y="0" width="380" height="310" rx="18" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>

        <!-- Vehicle Image -->
        <image href="{images['toyota_camry']}" x="5" y="5" width="370" height="145" preserveAspectRatio="xMidYMid slice" clip-path="url(#clipLsRentalImg)"/>

        <!-- Vehicle Info -->
        <text x="18" y="174" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="16" font-weight="800" fill="#111827">Toyota Camry</text>
        <text x="18" y="192" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="500" fill="#6B7280">Camry SE • 2022 • Celestial Silver</text>

        <!-- Spec Badges Row -->
        <g transform="translate(18, 204)">
            <rect x="0" y="0" width="76" height="24" rx="12" fill="#F3F4F6"/>
            <text x="10" y="16" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">Gasoline</text>

            <rect x="82" y="0" width="88" height="24" rx="12" fill="#F3F4F6"/>
            <text x="92" y="16" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">8-Speed Auto</text>

            <rect x="176" y="0" width="68" height="24" rx="12" fill="#F3F4F6"/>
            <text x="186" y="16" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">5 Seats</text>

            <rect x="250" y="0" width="84" height="24" rx="12" fill="#F3F4F6"/>
            <text x="260" y="16" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#111827">16.5 kmpl</text>
        </g>

        <!-- Description -->
        <text x="18" y="248" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" fill="#4B5563">A reliable, fuel-efficient sedan designed for daily comfort with smooth handling.</text>

        <!-- Rating -->
        <g transform="translate(18, 270)">
            <text x="0" y="16" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">Rating: </text>
            {render_stars(44, 4, 4.8, "#EAB308", "#111827", 13)}
        </g>
    </g>

    <!-- RIGHT COLUMN: Rental Duration Slider, Cost Breakdown & Action Buttons (width: 420px) -->
    <g id="RightColumnControls" transform="translate(436, 76)">
        <!-- Duration & Pricing Card -->
        <g id="DurationPricingCard" filter="url(#shadowLsR)">
            <rect x="0" y="0" width="420" height="230" rx="18" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1"/>

            <!-- Calculation Row -->
            <text x="20" y="32" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="600" fill="#6B7280">Daily Rental Rate</text>
            <text x="310" y="32" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#111827">50 credits/day</text>

            <text x="20" y="58" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="600" fill="#6B7280">Selected Duration</text>
            <text x="350" y="58" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#111827">3 Days</text>

            <line x1="20" y1="74" x2="400" y2="74" stroke="#F1F3F5" stroke-width="1"/>

            <text x="20" y="100" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="14" font-weight="800" fill="#111827">Total Rental Cost</text>
            <text x="315" y="100" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="16" font-weight="800" fill="#111827">150 Credits</text>

            <!-- Duration Slider Header -->
            <text x="20" y="136" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="12" font-weight="700" fill="#111827">Select Rental Duration</text>
            <rect x="330" y="120" width="70" height="22" rx="11" fill="#111827"/>
            <text x="348" y="135" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="700" fill="#FFFFFF">3 Days</text>

            <!-- Slider Track -->
            <rect x="20" y="162" width="380" height="6" rx="3" fill="#E5E7EB"/>
            <rect x="20" y="162" width="126" height="6" rx="3" fill="#111827"/>
            <circle cx="146" cy="165" r="10" fill="#111827" stroke="#FFFFFF" stroke-width="3" filter="url(#shadowLsR)"/>

            <text x="20" y="192" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">1 Day</text>
            <text x="358" y="192" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="11" font-weight="600" fill="#6B7280">7 Days</text>

            <!-- Remaining Credits Preview -->
            <rect x="20" y="202" width="380" height="20" rx="4" fill="#F8F9FA"/>
            <text x="28" y="216" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="10" font-weight="600" fill="#4B5563">Remaining balance after rental: 350 credits (500 - 150)</text>
        </g>

        <!-- Action Buttons Row -->
        <g id="LandscapeActionButtons" transform="translate(0, 252)">
            <rect x="0" y="0" width="195" height="46" rx="12" fill="#FFFFFF" stroke="#E5E7EB" stroke-width="1.5"/>
            <text x="48" y="28" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#111827">Cancel Booking</text>

            <rect x="215" y="0" width="205" height="46" rx="12" fill="#111827"/>
            <text x="264" y="28" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" font-size="13" font-weight="700" fill="#FFFFFF">Confirm Booking</text>
        </g>
    </g>

    {get_nav_bar_landscape(dark=False)}
</svg>"""

# ==========================================
# Run all generators
# ==========================================
screens = [
    ("01_home_catalogue_light.svg", generate_screen1_home_light),
    ("02_home_catalogue_dark.svg", generate_screen2_home_dark),
    ("03_search_filter_active.svg", generate_screen3_search_active),
    ("04_rental_details_valid.svg", generate_screen4_rental_valid),
    ("05_rental_details_error.svg", generate_screen5_rental_error),
    ("06_booking_success_rented.svg", generate_screen6_booking_success),
    ("07_home_catalogue_vietnamese.svg", generate_screen7_vietnamese),
    ("08_home_catalogue_landscape.svg", generate_screen8_catalogue_landscape),
    ("09_rental_details_landscape.svg", generate_screen9_rental_landscape),
]

for filename, func in screens:
    filepath = os.path.join(OUTPUT_DIR, filename)
    with open(filepath, "w", encoding="utf-8") as f:
        f.write(func())
    print(f"Generated: {filename}")

print("All 9 SVG screens (including Landscape Orientation) generated successfully!")
