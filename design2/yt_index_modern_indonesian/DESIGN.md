---
name: YT Index Modern Indonesian
colors:
  surface: '#f8f9ff'
  surface-dim: '#cbdbf5'
  surface-bright: '#f8f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eff4ff'
  surface-container: '#e5eeff'
  surface-container-high: '#dce9ff'
  surface-container-highest: '#d3e4fe'
  on-surface: '#0b1c30'
  on-surface-variant: '#464554'
  inverse-surface: '#213145'
  inverse-on-surface: '#eaf1ff'
  outline: '#777586'
  outline-variant: '#c7c4d7'
  surface-tint: '#5148d7'
  primary: '#2a14b4'
  on-primary: '#ffffff'
  primary-container: '#4338ca'
  on-primary-container: '#c1beff'
  inverse-primary: '#c3c0ff'
  secondary: '#006a61'
  on-secondary: '#ffffff'
  secondary-container: '#86f2e4'
  on-secondary-container: '#006f66'
  tertiary: '#003e61'
  on-tertiary: '#ffffff'
  tertiary-container: '#005684'
  on-tertiary-container: '#8fcbff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#e3dfff'
  primary-fixed-dim: '#c3c0ff'
  on-primary-fixed: '#100069'
  on-primary-fixed-variant: '#372abf'
  secondary-fixed: '#89f5e7'
  secondary-fixed-dim: '#6bd8cb'
  on-secondary-fixed: '#00201d'
  on-secondary-fixed-variant: '#005049'
  tertiary-fixed: '#cce5ff'
  tertiary-fixed-dim: '#93ccff'
  on-tertiary-fixed: '#001d31'
  on-tertiary-fixed-variant: '#004b73'
  background: '#f8f9ff'
  on-background: '#0b1c30'
  surface-variant: '#d3e4fe'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 3.5rem
    fontWeight: '700'
    lineHeight: 4rem
    letterSpacing: -0.02em
  display-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 2.75rem
    fontWeight: '700'
    lineHeight: 3.25rem
    letterSpacing: -0.015em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 2rem
    fontWeight: '700'
    lineHeight: 2.5rem
    letterSpacing: -0.01em
  headline-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 1.625rem
    fontWeight: '700'
    lineHeight: 2.125rem
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 1.5rem
    fontWeight: '600'
    lineHeight: 2rem
  headline-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 1.25rem
    fontWeight: '600'
    lineHeight: 1.75rem
  title-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 1.125rem
    fontWeight: '600'
    lineHeight: 1.625rem
  title-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 1rem
    fontWeight: '600'
    lineHeight: 1.5rem
    letterSpacing: 0.005em
  title-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 0.875rem
    fontWeight: '600'
    lineHeight: 1.25rem
    letterSpacing: 0.01em
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 1rem
    fontWeight: '400'
    lineHeight: 1.6rem
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 0.875rem
    fontWeight: '400'
    lineHeight: 1.375rem
  body-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 0.75rem
    fontWeight: '400'
    lineHeight: 1.125rem
  label-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 0.875rem
    fontWeight: '600'
    lineHeight: 1.25rem
    letterSpacing: 0.01em
  label-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 0.75rem
    fontWeight: '600'
    lineHeight: 1rem
    letterSpacing: 0.02em
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 0.6875rem
    fontWeight: '500'
    lineHeight: 0.875rem
    letterSpacing: 0.03em
rounded:
  sm: 0.5rem
  DEFAULT: 1rem
  md: 1.5rem
  lg: 2rem
  xl: 3rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-tablet: 1.5rem
  gutter-desktop: 1.5rem
  margin: 1rem
  margin-tablet: 1.5rem
  margin-desktop: 2rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
  space-2xl: 3rem
---

## Brand & Style

This design system establishes a focused, calm, and highly utilitarian indexing experience tailored for Indonesian mobile users navigating deep video archives, timestamps, and curated learning roadmaps. 

### Brand Personality & Mood
- **Tenang & Terstruktur (Calm & Structured):** Reduces information fatigue through generous breathing room, structured metadata tiers, and soft contrast.
- **Modern & Berwibawa (Modern & Trustworthy):** Combines the systemic rigor of Material Design 3 with contemporary editorial clarity.
- **Responsif & Cepat (Responsive & Fluid):** Tactile micro-interactions and smooth container transformations convey precision and speed.

### Design Movement
**Material Design 3 (M3) Modernized:** Adheres to M3 foundational paradigms—dynamic container mapping, strict tonal elevation, and expressive pill-shaped interactive targets—while introducing crisp, cool-slate ambient lighting and refined typographic rhythm.

## Colors

The palette leverages a deep indigo primary coupled with a balanced teal accent to deliver an authoritative yet refreshing cataloging interface. The neutral foundation relies on slate undertones rather than flat achromatic grays.

### System Roles & Token Mapping (Light Mode Default)
- **Primary (`#4338CA` - Indigo 700):** Anchor actions, key filter highlights, active bottom navigation states, and primary CTA buttons.
- **On-Primary (`#FFFFFF`):** Text and icons on primary containers.
- **Primary Container (`#EEF2FF` - Indigo 50):** Selected navigation indicators, subtle chips, and highlighted metadata badges.
- **On-Primary Container (`#312E81` - Indigo 900):** Typography and glyphs placed within primary containers.
- **Secondary / Accent (`#0D9488` - Teal 600):** Secondary triggers, progress indicators, video timestamp bookmarks, and success markers.
- **Secondary Container (`#CCFBF1` - Teal 100):** Active category pills, video chapter tags, and subtle metric highlights.
- **On-Secondary Container (`#115E59` - Teal 800):** Typography inside teal accent containers.
- **Surface (`#F8FAFC` - Slate 50):** Baseline app background canvas.
- **Surface Container Low (`#F1F5F9` - Slate 100):** List items, quiet card containers, and search background fills.
- **Surface Container (`#FFFFFF`):** Standard elevated cards, modal sheets, and dialogue surfaces.
- **Surface Container High (`#E2E8F0` - Slate 200):** Hover surfaces, subtle divider lines, and active slider tracks.
- **Neutral Foreground (`#0F172A` - Slate 900):** High-emphasis titles, critical headlines, and primary labels.
- **Neutral Muted (`#64748B` - Slate 500):** Secondary metadata, channel names, duration indicators, and timestamp intervals.

## Typography

The type system is powered entirely by **Plus Jakarta Sans**, offering geometric clarity combined with subtle humanistic warmth ideal for Indonesian localized copy (which regularly features longer word lengths like *Penjelajahan*, *Pengaturan*, or *Direkomendasikan*).

### Typographic Hierarchy Rules
- **Display & Headlines:** Used for dashboard welcome headers, search result metrics, and indexed collection titles. Tight negative tracking keeps larger sizes cohesive.
- **Titles:** Primary label for video cards, playlist titles, and bottom sheet section headers.
- **Body:** Calibrated line-heights (minimum 1.5x) preserve horizontal scan-flow across detailed video descriptions, transcript highlights, and timestamp summaries.
- **Labels:** Applied across pill buttons, M3 navigation bars, status badges (*Terbaru*, *Tersimpan*, *Selesai*), and video duration overlays.

## Layout & Spacing

The layout utilizes a strict 4pt/8pt linear rhythmic scale designed primarily for handheld vertical viewports, scaling up smoothly to foldables and tablet splits.

### Mobile Grid & Safe Areas
- **Mobile (<600px):** 4-column fluid layout, `16px` outer canvas margins (`margin`), `16px` column gutters (`gutter`). Content strictly respects top status bars and bottom navigation inset areas (`padding-bottom: env(safe-area-inset-bottom) + 80px`).
- **Tablet (600px - 1024px):** 8-column layout, `24px` margins and gutters. Adapts from bottom navigation bar into an M3 Navigation Rail on the left edge.
- **Desktop / Wide Foldable (>1024px):** 12-column layout capped at `1280px` max-width. Navigation rail expands into an expanded navigation drawer.

### Content Spacing Rhythm
- **Micro Spacing (`space-xs` to `space-sm`):** Metadata chips, timestamp tags next to video icons, and input field inner paddings.
- **Component Spacing (`space-md` to `space-lg`):** Vertical separation between grouped list items, carousel peek offsets, and stacked cards.
- **Section Spacing (`space-xl` to `space-2xl`):** Major thematic divisions such as "Lanjutkan Menonton", "Indeks Topik", and "Rekomendasi Minggu Ini".

## Elevation & Depth

This design system adheres to Material Design 3's tonal surface elevation architecture, minimizing heavy drop shadows in favor of surface-fill shifts tinted with neutral slate and subtle indigo underglow.

### Elevation Levels
- **Level 0 (Dasar / Canvas - `#F8FAFC`):** App backdrop, search body canvas. No shadow.
- **Level 1 (Surface Container Low - `#F1F5F9`):** Inset filters, video cards in idle state, search input fields. Crisp border definition (`1px solid #E2E8F0`), no shadow.
- **Level 2 (Surface Container - `#FFFFFF`):** Active interactive cards, floating timestamp chips, expanded accordion panels. Subtle ambient shadow: `box-shadow: 0 2px 8px -2px rgba(15, 23, 42, 0.06), 0 1px 3px -1px rgba(15, 23, 42, 0.04)`.
- **Level 3 (Surface Container High / Navigation Bar):** Fixed bottom navigation bar and active bottom sheets. `box-shadow: 0 -4px 16px -4px rgba(15, 23, 42, 0.06)`.
- **Level 4 (Dialog & Modals):** Video timestamp editor, filter modals. Surface `#FFFFFF` with `box-shadow: 0 16px 32px -8px rgba(15, 23, 42, 0.12)`.

## Shapes

The design system embraces Material Design 3's high-curvature family, emphasizing pill-shaped elements for focal interactions and soft rounded containers for structural content to communicate an approachable, human-centric tone.

### Shape Geometry Application
- **Pill (Full Curvature / `9999px`):** Primary and secondary action buttons, filter chips, search bar container, slider thumbs, and bottom nav active pill indicators.
- **Extra Large (`rounded-xl` / `24px`):** Bottom sheets, modal dialogs, and video preview hero cards.
- **Large (`rounded-lg` / `16px`):** Video index cards, accordion panels, and thumbnail wrapper frames.
- **Medium (`rounded-md` / `12px`):** Text fields, menu dropdown popovers, and segmented controls.
- **Small (`rounded-sm` / `8px`):** Video duration tags, timestamp badges, and inline status chips.

## Components

All components are engineered to feature standard Indonesian copy and align with Material Design 3 interaction guidelines.

### Tombol (Buttons)
- **Filled Button (Utama):** Pill-shaped (`border-radius: 9999px`), background `primary` (`#4338CA`), label `on-primary` (`#FFFFFF`), `label-lg`. Height: `48px` to guarantee accessible mobile touch targets.
- **Tonal Button:** Background `primary-container` (`#EEF2FF`), text `on-primary-container` (`#312E81`). Used for secondary flow actions like *"Simpan Index"* or *"Tambah Bab"*.
- **Outlined Button:** Border `1px solid #CBD5E1`, text `primary`, surface transparent. Used for neutral actions like *"Batal"*.

### Kerip Filter (Filter Chips)
- Height `36px`, pill-shaped.
- **Unselected:** Border `1px solid #E2E8F0`, background transparent, text `#64748B` (`label-md`).
- **Selected:** Border none, background `secondary-container` (`#CCFBF1`), text `on-secondary-container` (`#115E59`), accompanied by a leading checkmark icon (`✓`).
- Labels: *"Semua"*, *"Ringkasan"*, *"Transkrip"*, *"Studi Kasus"*, *"Favorit"*.

### Navigasi Bawah (M3 Navigation Bar)
- Height: `80px` plus device safe inset. Surface: `Surface Container` (`#FFFFFF`) with Level 3 top ambient shadow.
- Three to four destinations:
  1. **Beranda** (Home icon)
  2. **Eksplorasi** (Compass/Search icon)
  3. **Indeks Saya** (Bookmark/Folder icon)
  4. **Profil** (User icon)
- **Active State Indicator:** Pill badge (`64px` width by `32px` height) in `primary-container` (`#EEF2FF`) surrounding the active icon in `primary` (`#4338CA`). Typography: `label-sm` in bold slate.

### Penggeser Durasi & Waktu (Smooth Sliders)
- Continuous track height: `6px`, rounded `9999px`.
- **Inactive Track:** `#E2E8F0` (Slate 200).
- **Active Track:** `#0D9488` (Teal 600).
- **Thumb:** `20px` circular pill in `#0D9488` with a `2px` white inner ring and subtle elevation shadow. Expands to `24px` on active press, displaying floating timestamp badge (e.g., *"12:45"*).

### Kolom Input Pencarian (Search Inputs)
- Pill-shaped height: `52px`. Surface: `Surface Container Low` (`#F1F5F9`).
- Leading search magnifying glass icon, trailing voice or clear button (`✕`).
- Placeholder text: *"Cari video, topik, atau kata kunci..."* in `#94A3B8`.
- Focus state: Outline `2px solid #4338CA` with `#FFFFFF` background.

### Kartu Indeks Video (Video Index Cards)
- Border-radius `16px`, background `Surface Container` (`#FFFFFF`), subtle border `1px solid #F1F5F9`.
- Video thumbnail nested with `12px` rounded corners and floating duration badge (`"14:20"` in `#0F172A` semi-transparent back).
- Contextual metadata layout: Title (`title-md`), Channel Name (`body-sm` `#64748B`), and pill tag for total timestamps created (e.g., *"8 Indeks Bab"*).

### Kotak Centang & Tombol Radio (Checkboxes & Radios)
- **Checkbox:** `20px` rounded square (`rounded: 6px`). Checked: Background `primary` with white check glyph.
- **Radio:** `20px` circle. Selected: Outer ring `primary`, inner dot `primary` (`10px`).