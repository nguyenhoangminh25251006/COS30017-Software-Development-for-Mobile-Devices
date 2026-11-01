import os
import xml.etree.ElementTree as ET
import subprocess

def get_defs():
    return '''
    <defs>
        <style>
            @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700;900&amp;display=swap');
            text {
                font-family: 'Roboto', -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif;
                user-select: none;
            }
        </style>
        <!-- Card Elevation Shadow -->
        <filter id="card-shadow" x="-20%" y="-20%" width="140%" height="150%" filterUnits="userSpaceOnUse">
            <feDropShadow dx="0" dy="4" stdDeviation="5" flood-color="#000000" flood-opacity="0.10" />
        </filter>
        <filter id="canvas-card-shadow" x="-5%" y="-5%" width="110%" height="115%" filterUnits="userSpaceOnUse">
            <feDropShadow dx="0" dy="6" stdDeviation="12" flood-color="#0F172A" flood-opacity="0.08" />
        </filter>
        <filter id="phone-shadow" x="-10%" y="-5%" width="120%" height="115%" filterUnits="userSpaceOnUse">
            <feDropShadow dx="0" dy="12" stdDeviation="16" flood-color="#0F172A" flood-opacity="0.15" />
        </filter>
    </defs>
    '''

def status_bar_portrait(time_str="1:50"):
    return f'''
    <!-- Status Bar -->
    <g id="StatusBar" opacity="0.95">
        <!-- Time -->
        <text x="32" y="27" font-size="12" font-weight="700" fill="#212121">{time_str}</text>
        
        <!-- Settings Gear Icon -->
        <g transform="translate(68, 17) scale(0.75)">
            <path d="M 6 0 L 8 0 L 8.4 2.2 C 8.9 2.4 9.4 2.7 9.8 3.1 L 11.9 2.3 L 13.3 3.7 L 12.5 5.8 C 12.9 6.2 13.2 6.7 13.4 7.2 L 15.6 7.6 L 15.6 9.6 L 13.4 10 C 13.2 10.5 12.9 11 12.5 11.4 L 13.3 13.5 L 11.9 14.9 L 9.8 14.1 C 9.4 14.5 8.9 14.8 8.4 15 L 8 17.2 L 6 17.2 L 5.6 15 C 5.1 14.8 4.6 14.5 4.2 14.1 L 2.1 14.9 L 0.7 13.5 L 1.5 11.4 C 1.1 11 0.8 10.5 0.6 10 L -1.6 9.6 L -1.6 7.6 L 0.6 7.2 C 0.8 6.7 1.1 6.2 1.5 5.8 L 0.7 3.7 L 2.1 2.3 L 4.2 3.1 C 4.6 2.7 5.1 2.4 5.6 2.2 Z" fill="#424242"/>
            <circle cx="7" cy="8.6" r="3" fill="#F5F7FA"/>
        </g>
        
        <!-- Security Shield Icon -->
        <g transform="translate(86, 17) scale(0.75)">
            <path d="M 6 1 L 12 3.5 C 12 8 9.5 12 6 13.5 C 2.5 12 0 8 0 3.5 Z" fill="none" stroke="#424242" stroke-width="1.8"/>
            <path d="M 6 2.2 L 10.8 4.2 C 10.8 7.8 8.8 10.8 6 12.1 Z" fill="#424242"/>
        </g>
        
        <!-- Camera Cutout (Hole punch) -->
        <circle cx="180" cy="24" r="9" fill="#0A0B0C"/>
        <circle cx="180" cy="24" r="6.5" fill="#17181A"/>
        <circle cx="178.5" cy="22.5" r="2.2" fill="#2E3035"/>
        <circle cx="178" cy="22" r="0.8" fill="#5A606B"/>
        
        <!-- Cellular Signal Bars -->
        <g transform="translate(276, 18)">
            <rect x="0" y="8" width="2.5" height="4" rx="0.5" fill="#212121"/>
            <rect x="4" y="5" width="2.5" height="7" rx="0.5" fill="#212121"/>
            <rect x="8" y="2.5" width="2.5" height="9.5" rx="0.5" fill="#212121"/>
            <rect x="12" y="0" width="2.5" height="12" rx="0.5" fill="#212121"/>
        </g>
        
        <!-- Wi-Fi Icon -->
        <g transform="translate(298, 17)">
            <path d="M 0 3.5 C 3 0.8 7.5 0.8 10.5 3.5 L 9.3 4.8 C 6.9 2.7 3.6 2.7 1.2 4.8 Z" fill="#212121"/>
            <path d="M 2.4 6 C 4.2 4.4 6.3 4.4 8.1 6 L 6.9 7.3 C 5.7 6.2 4.8 6.2 3.6 7.3 Z" fill="#212121"/>
            <circle cx="5.25" cy="9.8" r="1.3" fill="#212121"/>
        </g>
        
        <!-- Battery Icon -->
        <g transform="translate(318, 18)">
            <rect x="0" y="0.5" width="18" height="11" rx="2.5" fill="none" stroke="#212121" stroke-width="1.6"/>
            <rect x="2" y="2.5" width="11" height="7" rx="1.2" fill="#212121"/>
            <path d="M 19 3.5 C 19.6 3.5 20 4 20 4.6 L 20 7.4 C 20 8 19.6 8.5 19 8.5 Z" fill="#212121"/>
        </g>
    </g>
    '''

def render_portrait_body(elem_text, zone_text, score_val, score_color, status_text, perform_disabled, deduct_disabled, time_str="1:50"):
    # Disabled styling in Material Design button with backgroundTint
    # The text becomes muted dark color with low contrast
    perform_text_color = "#18451B" if perform_disabled else "#FFFFFF"
    deduct_text_color = "#721A1A" if deduct_disabled else "#FFFFFF"
    
    return f'''
    <!-- Phone Outer Bezel -->
    <g filter="url(#phone-shadow)">
        <rect x="0" y="0" width="360" height="780" rx="38" fill="#1C1F23" stroke="#373B42" stroke-width="2.5"/>
        <rect x="2" y="2" width="356" height="776" rx="36" fill="none" stroke="#000000" stroke-width="1.5" opacity="0.5"/>
    </g>
    
    <!-- Top Speaker Grill -->
    <rect x="150" y="7" width="60" height="3" rx="1.5" fill="#373B42"/>
    
    <!-- Screen Display Area (Background #F5F7FA) -->
    <rect x="8" y="8" width="344" height="764" rx="30" fill="#F5F7FA"/>

    {status_bar_portrait(time_str)}

    <!-- App Title (Gymnastics App) -->
    <g id="AppHeader">
        <text x="180" y="82" font-size="24" font-weight="700" fill="#212121" text-anchor="middle">Gymnastics App</text>
    </g>

    <!-- Score Card Container -->
    <g id="CardScore" filter="url(#card-shadow)">
        <rect x="24" y="106" width="312" height="230" rx="16" fill="#FFFFFF"/>
        
        <!-- Element Counter -->
        <text x="180" y="145" font-size="19" font-weight="700" fill="#212121" text-anchor="middle">{elem_text}</text>
        
        <!-- Zone Indicator -->
        <text x="180" y="169" font-size="13.5" font-weight="400" fill="#616161" text-anchor="middle">{zone_text}</text>
        
        <!-- Score Display -->
        <text x="180" y="244" text-anchor="middle">
            <tspan font-size="68" font-weight="700" fill="{score_color}">{score_val}</tspan>
            <tspan font-size="22" font-weight="700" fill="#616161" dx="6"> / 20</tspan>
        </text>
        
        <!-- Score Label -->
        <text x="180" y="268" font-size="12.5" font-weight="400" fill="#757575" text-anchor="middle">Score</text>
        
        <!-- Status Message -->
        <text x="180" y="304" font-size="14.5" font-weight="700" fill="#212121" text-anchor="middle">{status_text}</text>
    </g>

    <!-- Buttons Section -->
    <g id="ButtonsGroup">
        <!-- Perform Element Button -->
        <g id="BtnPerform">
            <rect x="28" y="568" width="304" height="52" rx="26" fill="#2E7D32"/>
            <text x="180" y="601" font-size="15" font-weight="700" fill="{perform_text_color}" text-anchor="middle">Perform Element</text>
        </g>

        <!-- Deduction Button -->
        <g id="BtnDeduction">
            <rect x="28" y="632" width="304" height="52" rx="26" fill="#C62828"/>
            <text x="180" y="665" font-size="15" font-weight="700" fill="{deduct_text_color}" text-anchor="middle">Deduction (-2)</text>
        </g>

        <!-- Reset Button -->
        <g id="BtnReset">
            <rect x="28" y="696" width="304" height="52" rx="26" fill="#546E7A"/>
            <text x="180" y="729" font-size="15" font-weight="700" fill="#FFFFFF" text-anchor="middle">Reset Routine</text>
        </g>
    </g>

    <!-- Android Home Gesture Bar -->
    <rect x="140" y="756" width="80" height="4" rx="2" fill="#424242"/>
    '''

def render_portrait_screen_grouped(id_name, label_stage, badge_color, elem_text, zone_text, score_val, score_color, status_text, perform_disabled, deduct_disabled, time_str="1:50"):
    body = render_portrait_body(elem_text, zone_text, score_val, score_color, status_text, perform_disabled, deduct_disabled, time_str)
    return f'''
    <g id="{id_name}">
        <!-- Stage Label / Artboard Tag -->
        <rect x="0" y="-44" width="360" height="32" rx="6" fill="{badge_color}" fill-opacity="0.12"/>
        <circle cx="16" cy="-28" r="5" fill="{badge_color}"/>
        <text x="30" y="-23" font-size="13" font-weight="700" fill="{badge_color}">{label_stage}</text>

        {body}
    </g>
    '''

def render_landscape_body():
    return '''
    <!-- Phone Outer Bezel (Landscape) -->
    <g filter="url(#phone-shadow)">
        <rect x="0" y="0" width="780" height="360" rx="38" fill="#1C1F23" stroke="#373B42" stroke-width="2.5"/>
        <rect x="2" y="2" width="776" height="356" rx="36" fill="none" stroke="#000000" stroke-width="1.5" opacity="0.5"/>
    </g>
    
    <!-- Left Speaker Grill in Landscape -->
    <rect x="7" y="150" width="3" height="60" rx="1.5" fill="#373B42"/>

    <!-- Screen Area (#F5F7FA) -->
    <rect x="8" y="8" width="764" height="344" rx="30" fill="#F5F7FA"/>

    <!-- Landscape Status Bar -->
    <g id="LandscapeStatusBar" opacity="0.95">
        <text x="32" y="27" font-size="12" font-weight="700" fill="#212121">1:52</text>
        
        <!-- Gear -->
        <g transform="translate(68, 17) scale(0.75)">
            <path d="M 6 0 L 8 0 L 8.4 2.2 C 8.9 2.4 9.4 2.7 9.8 3.1 L 11.9 2.3 L 13.3 3.7 L 12.5 5.8 C 12.9 6.2 13.2 6.7 13.4 7.2 L 15.6 7.6 L 15.6 9.6 L 13.4 10 C 13.2 10.5 12.9 11 12.5 11.4 L 13.3 13.5 L 11.9 14.9 L 9.8 14.1 C 9.4 14.5 8.9 14.8 8.4 15 L 8 17.2 L 6 17.2 L 5.6 15 C 5.1 14.8 4.6 14.5 4.2 14.1 L 2.1 14.9 L 0.7 13.5 L 1.5 11.4 C 1.1 11 0.8 10.5 0.6 10 L -1.6 9.6 L -1.6 7.6 L 0.6 7.2 C 0.8 6.7 1.1 6.2 1.5 5.8 L 0.7 3.7 L 2.1 2.3 L 4.2 3.1 C 4.6 2.7 5.1 2.4 5.6 2.2 Z" fill="#424242"/>
            <circle cx="7" cy="8.6" r="3" fill="#F5F7FA"/>
        </g>
        <!-- Shield -->
        <g transform="translate(86, 17) scale(0.75)">
            <path d="M 6 1 L 12 3.5 C 12 8 9.5 12 6 13.5 C 2.5 12 0 8 0 3.5 Z" fill="none" stroke="#424242" stroke-width="1.8"/>
            <path d="M 6 2.2 L 10.8 4.2 C 10.8 7.8 8.8 10.8 6 12.1 Z" fill="#424242"/>
        </g>

        <!-- Signal Bars -->
        <g transform="translate(686, 18)">
            <rect x="0" y="8" width="2.5" height="4" rx="0.5" fill="#212121"/>
            <rect x="4" y="5" width="2.5" height="7" rx="0.5" fill="#212121"/>
            <rect x="8" y="2.5" width="2.5" height="9.5" rx="0.5" fill="#212121"/>
            <rect x="12" y="0" width="2.5" height="12" rx="0.5" fill="#212121"/>
        </g>
        <!-- Wi-Fi -->
        <g transform="translate(708, 17)">
            <path d="M 0 3.5 C 3 0.8 7.5 0.8 10.5 3.5 L 9.3 4.8 C 6.9 2.7 3.6 2.7 1.2 4.8 Z" fill="#212121"/>
            <path d="M 2.4 6 C 4.2 4.4 6.3 4.4 8.1 6 L 6.9 7.3 C 5.7 6.2 4.8 6.2 3.6 7.3 Z" fill="#212121"/>
            <circle cx="5.25" cy="9.8" r="1.3" fill="#212121"/>
        </g>
        <!-- Battery -->
        <g transform="translate(728, 18)">
            <rect x="0" y="0.5" width="18" height="11" rx="2.5" fill="none" stroke="#212121" stroke-width="1.6"/>
            <rect x="2" y="2.5" width="11" height="7" rx="1.2" fill="#212121"/>
            <path d="M 19 3.5 C 19.6 3.5 20 4 20 4.6 L 20 7.4 C 20 8 19.6 8.5 19 8.5 Z" fill="#212121"/>
        </g>
    </g>

    <!-- LEFT PANE: Info &amp; Score Display -->
    <g id="LandscapeLeftPane">
        <!-- App Title -->
        <text x="202" y="68" font-size="20" font-weight="700" fill="#212121" text-anchor="middle">Gymnastics App</text>
        
        <!-- Card Container -->
        <g id="LandscapeCardScore" filter="url(#card-shadow)">
            <rect x="32" y="86" width="340" height="230" rx="12" fill="#FFFFFF"/>
            
            <!-- Element Counter -->
            <text x="202" y="118" font-size="16" font-weight="700" fill="#212121" text-anchor="middle">Element: 0 / 10</text>
            
            <!-- Zone Indicator -->
            <text x="202" y="139" font-size="13" font-weight="400" fill="#616161" text-anchor="middle">Zone: Basic (Elem 1–3)</text>
            
            <!-- Score Display -->
            <text x="202" y="202" text-anchor="middle">
                <tspan font-size="52" font-weight="700" fill="#1976D2">0</tspan>
                <tspan font-size="20" font-weight="700" fill="#616161" dx="6"> / 20</tspan>
            </text>
            
            <!-- Score Label -->
            <text x="202" y="224" font-size="12" font-weight="400" fill="#757575" text-anchor="middle">Score</text>
            
            <!-- Status Message -->
            <text x="202" y="258" font-size="14" font-weight="700" fill="#212121" text-anchor="middle">Ready to start routine</text>
        </g>
    </g>

    <!-- RIGHT PANE: Action Buttons Stack -->
    <g id="LandscapeRightPane">
        <!-- Perform Button -->
        <g id="LandscapeBtnPerform">
            <rect x="396" y="86" width="348" height="54" rx="27" fill="#2E7D32"/>
            <text x="570" y="120" font-size="15" font-weight="700" fill="#FFFFFF" text-anchor="middle">Perform Element</text>
        </g>

        <!-- Deduction Button (Disabled in state 0) -->
        <g id="LandscapeBtnDeduction">
            <rect x="396" y="152" width="348" height="54" rx="27" fill="#C62828"/>
            <text x="570" y="186" font-size="15" font-weight="700" fill="#721A1A" text-anchor="middle">Deduction (-2)</text>
            
            <!-- Camera punch hole cutout rotated to right edge -->
            <circle cx="728" cy="179" r="10" fill="#0A0B0C"/>
            <circle cx="728" cy="179" r="6.5" fill="#17181A"/>
            <circle cx="726.5" cy="177.5" r="2.2" fill="#2E3035"/>
        </g>

        <!-- Reset Button -->
        <g id="LandscapeBtnReset">
            <rect x="396" y="218" width="348" height="54" rx="27" fill="#546E7A"/>
            <text x="570" y="252" font-size="15" font-weight="700" fill="#FFFFFF" text-anchor="middle">Reset Routine</text>
        </g>
    </g>

    <!-- Android Home Gesture Bar -->
    <rect x="348" y="342" width="84" height="4" rx="2" fill="#424242"/>
    '''

def render_landscape_grouped():
    body = render_landscape_body()
    return f'''
    <g id="Screen5_Rotation_Landscape">
        <!-- Stage Label / Artboard Tag -->
        <rect x="0" y="-44" width="780" height="32" rx="6" fill="#5E35B1" fill-opacity="0.12"/>
        <circle cx="16" cy="-28" r="5" fill="#5E35B1"/>
        <text x="30" y="-23" font-size="13" font-weight="700" fill="#5E35B1">Stage 5 | Rotation (Landscape Mode - State Restored on Orientation Change)</text>

        {body}
    </g>
    '''

def render_spec_card():
    return '''
    <g id="Design_System_Specifications" transform="translate(840, 0)">
        <!-- Spec Card Header Badge -->
        <rect x="0" y="-44" width="780" height="32" rx="6" fill="#00897B" fill-opacity="0.12"/>
        <circle cx="16" cy="-28" r="5" fill="#00897B"/>
        <text x="30" y="-23" font-size="13" font-weight="700" fill="#00897B">Specification &amp; Design System Tokens</text>

        <!-- Outer Card -->
        <rect x="0" y="0" width="780" height="360" rx="20" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="1.5" filter="url(#card-shadow)"/>
        
        <g transform="translate(32, 28)">
            <text x="0" y="16" font-size="20" font-weight="700" fill="#1E293B">Gymnastics App Component &amp; Rule Specifications</text>
            <text x="0" y="38" font-size="13" fill="#64748B">Android Kotlin Architecture &amp; State Management Reference</text>

            <!-- Difficulty Zones &amp; Score Color Palette -->
            <text x="0" y="76" font-size="14" font-weight="700" fill="#334155">Difficulty Zones &amp; Scoring Progression</text>
            
            <!-- Zone 1: Basic -->
            <rect x="0" y="88" width="220" height="64" rx="10" fill="#EBF4FE" stroke="#BED8FA" stroke-width="1"/>
            <circle cx="20" cy="110" r="8" fill="#1976D2"/>
            <text x="36" y="114" font-size="13" font-weight="700" fill="#1976D2">Basic Zone</text>
            <text x="36" y="132" font-size="11.5" fill="#475569">Elements 1–3 &#8226; +1 pt each</text>

            <!-- Zone 2: Intermediate -->
            <rect x="236" y="88" width="220" height="64" rx="10" fill="#EAF7EC" stroke="#BCE6C1" stroke-width="1"/>
            <circle cx="256" cy="110" r="8" fill="#388E3C"/>
            <text x="272" y="114" font-size="13" font-weight="700" fill="#388E3C">Intermediate Zone</text>
            <text x="272" y="132" font-size="11.5" fill="#475569">Elements 4–7 &#8226; +2 pts each</text>

            <!-- Zone 3: Advanced -->
            <rect x="472" y="88" width="220" height="64" rx="10" fill="#FEF3EB" stroke="#FED2B7" stroke-width="1"/>
            <circle cx="492" cy="110" r="8" fill="#E65100"/>
            <text x="508" y="114" font-size="13" font-weight="700" fill="#E65100">Advanced Zone</text>
            <text x="508" y="132" font-size="11.5" fill="#475569">Elements 8–10 &#8226; +3 pts each</text>

            <!-- Deduction &amp; Logic Rules -->
            <text x="0" y="184" font-size="14" font-weight="700" fill="#334155">Button State Rules &amp; Deduction Logic</text>

            <g transform="translate(0, 196)">
                <!-- Rule 1 -->
                <rect x="0" y="0" width="344" height="96" rx="10" fill="#F8FAFC" stroke="#E2E8F0" stroke-width="1"/>
                <text x="16" y="24" font-size="13" font-weight="700" fill="#0F172A">Deduction Rule (-2 points)</text>
                <text x="16" y="44" font-size="12" fill="#64748B">&#8226; Only enabled after 1st element completed</text>
                <text x="16" y="62" font-size="12" fill="#64748B">&#8226; Once pressed: stops further performance</text>
                <text x="16" y="80" font-size="12" fill="#64748B">&#8226; Min score clamped at 0, max score 20</text>

                <!-- Rule 2 -->
                <rect x="360" y="0" width="344" height="96" rx="10" fill="#F8FAFC" stroke="#E2E8F0" stroke-width="1"/>
                <text x="376" y="24" font-size="13" font-weight="700" fill="#0F172A">Orientation Change / Rotation</text>
                <text x="376" y="44" font-size="12" fill="#64748B">&#8226; Preserves currentElement, score &amp; deduction</text>
                <text x="376" y="62" font-size="12" fill="#64748B">&#8226; Switches to 2-column landscape layout</text>
                <text x="376" y="80" font-size="12" fill="#64748B">&#8226; onSaveInstanceState &amp; onRestoreInstanceState</text>
            </g>
        </g>
    </g>
    '''

def build_full_artboard_svg():
    return f'''<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1780 1480" width="1780" height="1480" fill="none">
    {get_defs()}

    <!-- Canvas Background -->
    <rect width="1780" height="1480" fill="#F1F5F9" />

    <!-- FIGMA ARTBOARD HEADER -->
    <g id="Canvas_Header" transform="translate(60, 48)">
        <rect x="0" y="0" width="1660" height="74" rx="14" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="1.5" filter="url(#canvas-card-shadow)"/>
        <!-- App Icon Badge -->
        <rect x="18" y="15" width="44" height="44" rx="10" fill="#1976D2"/>
        <text x="40" y="44" font-size="22" font-weight="700" fill="#FFFFFF" text-anchor="middle">G</text>
        
        <text x="76" y="35" font-size="18" font-weight="700" fill="#0F172A">Gymnastics Scoring App — Full UI State Flows for Figma</text>
        <text x="76" y="52" font-size="12.5" fill="#64748B">COS30017 / SWE30009 Assignment 1 &#8226; Pixel-perfect vector recreation of Stage 1, Stage 2, Stage 3, Deduction &amp; Rotation</text>

        <!-- Status Badges -->
        <rect x="1460" y="22" width="100" height="28" rx="14" fill="#E0F2FE"/>
        <text x="1510" y="41" font-size="12" font-weight="700" fill="#0369A1" text-anchor="middle">5 STATES</text>
        <rect x="1570" y="22" width="72" height="28" rx="14" fill="#DCFCE7"/>
        <text x="1606" y="41" font-size="12" font-weight="700" fill="#15803D" text-anchor="middle">FIGMA</text>
    </g>

    <!-- ROW 1: 4 PORTRAIT SCREENS (y = 200) -->
    <!-- Screen 1: Stage 1 (Basic Zone) -->
    <g transform="translate(60, 200)">
        {render_portrait_screen_grouped(
            id_name="Screen1_Stage1_Basic",
            label_stage="Stage 1 | Basic Zone (Initial State)",
            badge_color="#1976D2",
            elem_text="Element: 0 / 10",
            zone_text="Zone: Basic (Elem 1–3)",
            score_val="0",
            score_color="#1976D2",
            status_text="Ready to start routine",
            perform_disabled=False,
            deduct_disabled=True,
            time_str="1:50"
        )}
    </g>

    <!-- Screen 2: Stage 2 (Intermediate Zone) -->
    <g transform="translate(480, 200)">
        {render_portrait_screen_grouped(
            id_name="Screen2_Stage2_Intermediate",
            label_stage="Stage 2 | Intermediate Zone (Elem 6/10)",
            badge_color="#388E3C",
            elem_text="Element: 6 / 10",
            zone_text="Zone: Intermediate (Elem 4–7, 2pts)",
            score_val="9",
            score_color="#388E3C",
            status_text="Routine in progress",
            perform_disabled=False,
            deduct_disabled=False,
            time_str="1:50"
        )}
    </g>

    <!-- Screen 3: Stage 3 (Advanced Zone) -->
    <g transform="translate(900, 200)">
        {render_portrait_screen_grouped(
            id_name="Screen3_Stage3_Advanced",
            label_stage="Stage 3 | Advanced Zone (Elem 8/10)",
            badge_color="#E65100",
            elem_text="Element: 8 / 10",
            zone_text="Zone: Advanced (Elem 8–10, 3pts)",
            score_val="14",
            score_color="#E65100",
            status_text="Routine in progress",
            perform_disabled=False,
            deduct_disabled=False,
            time_str="1:50"
        )}
    </g>

    <!-- Screen 4: Deduction Taken -->
    <g transform="translate(1320, 200)">
        {render_portrait_screen_grouped(
            id_name="Screen4_Deduction",
            label_stage="Deduction | Penalty Taken (-2 pts)",
            badge_color="#C62828",
            elem_text="Element: 4 / 10",
            zone_text="Zone: Intermediate (Elem 4–7, 2pts)",
            score_val="3",
            score_color="#388E3C",
            status_text="Deduction taken - Performance stopped",
            perform_disabled=True,
            deduct_disabled=True,
            time_str="1:51"
        )}
    </g>

    <!-- ROW 2: LANDSCAPE (ROTATION) + DESIGN SYSTEM TOKENS (y = 1070) -->
    <g transform="translate(60, 1070)">
        {render_landscape_grouped()}
        {render_spec_card()}
    </g>
</svg>
'''

def build_single_screen_svg(screen_type):
    if screen_type == "stage1":
        body = render_portrait_body("Element: 0 / 10", "Zone: Basic (Elem 1–3)", "0", "#1976D2", "Ready to start routine", False, True, "1:50")
        width, height = 360, 780
    elif screen_type == "stage2":
        body = render_portrait_body("Element: 6 / 10", "Zone: Intermediate (Elem 4–7, 2pts)", "9", "#388E3C", "Routine in progress", False, False, "1:50")
        width, height = 360, 780
    elif screen_type == "stage3":
        body = render_portrait_body("Element: 8 / 10", "Zone: Advanced (Elem 8–10, 3pts)", "14", "#E65100", "Routine in progress", False, False, "1:50")
        width, height = 360, 780
    elif screen_type == "deduction":
        body = render_portrait_body("Element: 4 / 10", "Zone: Intermediate (Elem 4–7, 2pts)", "3", "#388E3C", "Deduction taken - Performance stopped", True, True, "1:51")
        width, height = 360, 780
    elif screen_type == "rotation":
        body = render_landscape_body()
        width, height = 780, 360
    else:
        raise ValueError("Invalid screen type")

    return f'''<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 {width} {height}" width="{width}" height="{height}" fill="none">
    {get_defs()}
    {body}
</svg>
'''

if __name__ == '__main__':
    # 1. Full canvas
    full_svg = build_full_artboard_svg()
    # Validate XML
    ET.fromstring(full_svg)
    print("Full SVG XML validation passed!")

    with open('file.svg', 'w', encoding='utf-8') as f:
        f.write(full_svg)
    with open('gymnastics_app_screens.svg', 'w', encoding='utf-8') as f:
        f.write(full_svg)
    print("file.svg and gymnastics_app_screens.svg saved!")

    # 2. Individual screens
    os.makedirs('figma_exports', exist_ok=True)
    screens = [
        ('stage1', 'figma_exports/stage_1_basic.svg'),
        ('stage2', 'figma_exports/stage_2_intermediate.svg'),
        ('stage3', 'figma_exports/stage_3_advanced.svg'),
        ('deduction', 'figma_exports/stage_4_deduction.svg'),
        ('rotation', 'figma_exports/stage_5_rotation_landscape.svg'),
    ]
    for stype, filename in screens:
        svg_content = build_single_screen_svg(stype)
        ET.fromstring(svg_content)
        with open(filename, 'w', encoding='utf-8') as f:
            f.write(svg_content)
        print(f"Saved {filename} (valid XML)!")
