---
name: Luminous Material Index
colors:
  surface: '#0b1326'
  surface-dim: '#0b1326'
  surface-bright: '#31394d'
  surface-container-lowest: '#060e20'
  surface-container-low: '#131b2e'
  surface-container: '#171f33'
  surface-container-high: '#222a3d'
  surface-container-highest: '#2d3449'
  on-surface: '#dae2fd'
  on-surface-variant: '#c6c5d5'
  inverse-surface: '#dae2fd'
  inverse-on-surface: '#283044'
  outline: '#908f9e'
  outline-variant: '#454653'
  surface-tint: '#bdc2ff'
  primary: '#bdc2ff'
  on-primary: '#131e8c'
  primary-container: '#818cf8'
  on-primary-container: '#101b8a'
  inverse-primary: '#4953bc'
  secondary: '#44e2cd'
  on-secondary: '#003731'
  secondary-container: '#03c6b2'
  on-secondary-container: '#004d44'
  tertiary: '#c0c1ff'
  on-tertiary: '#1000a9'
  tertiary-container: '#8689ff'
  on-tertiary-container: '#0f00a2'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#e0e0ff'
  primary-fixed-dim: '#bdc2ff'
  on-primary-fixed: '#000767'
  on-primary-fixed-variant: '#2f3aa3'
  secondary-fixed: '#62fae3'
  secondary-fixed-dim: '#3cddc7'
  on-secondary-fixed: '#00201c'
  on-secondary-fixed-variant: '#005047'
  tertiary-fixed: '#e1e0ff'
  tertiary-fixed-dim: '#c0c1ff'
  on-tertiary-fixed: '#07006c'
  on-tertiary-fixed-variant: '#2f2ebe'
  background: '#0b1326'
  on-background: '#dae2fd'
  surface-variant: '#2d3449'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 40px
    fontWeight: '800'
    lineHeight: 48px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.01em
  headline-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 26px
    fontWeight: '700'
    lineHeight: 34px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
  headline-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.02em
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 11px
    fontWeight: '700'
    lineHeight: 14px
    letterSpacing: 0.04em
rounded:
  sm: 0.5rem
  DEFAULT: 1rem
  md: 1.5rem
  lg: 2rem
  xl: 3rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-mobile: 0.75rem
  margin: 1rem
  margin-tablet: 1.5rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

This design system expresses a focused, high-precision indexing and cataloging experience tailored for power mobile users on Android. Drawing directly from Material Design 3 (Material You) with an expressive, OLED-optimized dark canvas, the interface combines technical clarity with midnight-grade visual luxury. 

The aesthetic marries deep charcoal and midnight ink planes with crisp, luminous neon accents in electric indigo and surgical teal. It balances utilitarian catalog density with comfortable touch ergonomics, invoking a sense of calm mastery, night-friendly legibility, and modern algorithmic elegance.

## Colors

The palette is tuned specifically for OLED energy efficiency and zero eye strain in dark environments. 

- **Surface Architecture:**
  - Base canvas (`surface`): `#0f172a` (Slate deep background).
  - Primary container (`surface-container-low`): `#111827` (Midnight charcoal).
  - Elevated card container (`surface-container`): `#1e293b`.
  - High-elevation or modal tier (`surface-container-high`): `#1f2937`.
  - Tonal accent container (`surface-tinted`): `#1e1b4b` (Subtle midnight indigo wash).

- **Accents:**
  - `primary` (`#818cf8`): Expressive luminous indigo for primary actions, floating action buttons (FABs), and key interactive states.
  - `secondary` (`#2dd4bf`): Energetic teal for highlights, active indicator badges, timeline markers, and completed index states.
  - `tertiary` (`#6366f1` / `#14b8a6`): Auxiliary anchors, selected states, and progressive metrics.

- **Content & Borders:**
  - `text-primary`: `#f8fafc` (Ultra-high contrast, WCAG AAA against base layers).
  - `text-secondary`: `#94a3b8` (Muted metadata and structural descriptions).
  - `outline`: `#334155` (Soft structural boundaries that retain shape without drawing visual distraction).

## Typography

Typographic scale is powered exclusively by Plus Jakarta Sans. Its geometric clarity and open counters offer pristine scanning speeds across vertical feeds, index lists, and dense timestamp metadata.

- **Headlines:** Set with deliberate tight tracking to anchor video titles, channel summaries, and index headers.
- **Body:** Standardized at 14px/20px (`body-md`) for content summaries, transcripts, and index notes to balance information density with effortless scanning.
- **Labels:** Uppercase styling is avoided for long strings; micro tags and timestamps use `label-sm` with slight positive tracking to ensure fast recognition against dark backgrounds.

## Layout & Spacing

The layout is built around an 8pt rhythmic grid optimized for standard handheld Android viewports:

- **Mobile Viewports (<600dp):** Fluid single-column feed. Canvas edge margin is fixed at `1rem` (16dp). Internal horizontal content gutters default to `0.75rem` (12dp) for horizontal carousels and card groupings.
- **Tablet & Foldables (600dp - 840dp+):** Dual-pane master-detail arrangement. Outer margin extends to `1.5rem` (24dp) with 16dp column gutters.
- **Touch Targets:** All interactive elements maintain a strict minimum bounding box of 48x48dp, even when visual pill containers occupy smaller heights.

## Elevation & Depth

In keeping with Material 3 Dark specifications, elevation is defined primarily by **tonal layering** and **subtle boundary definition** rather than harsh directional drop shadows:

- **Surface Tiers:**
  - **Level 0 (Canvas):** `#0f172a` — Underlying screen background.
  - **Level 1 (Cards, Feed items):** `#1e293b` with a 1px border of `#334155` at 60% opacity.
  - **Level 2 (Modals, Dropdowns, Floating Action Bars):** `#1f2937` with an ambient glow shadow: `0 8px 24px -4px rgba(15, 23, 42, 0.8), 0 0 1px 1px rgba(51, 65, 85, 0.4)`.
  - **Level 3 (Popovers & System Sheets):** Midnight tint `#1e1b4b` combined with an extra-diffused indigo ambient glow: `0 16px 32px -8px rgba(99, 102, 241, 0.12)`.

## Shapes

The design system embraces the friendly, organic, yet disciplined shape language of Material You:

- **Buttons, Filter Chips, Badges:** Fully rounded pills (`roundedness: 3` / 9999px border-radius).
- **Cards, Index Modules, Video Panels:** Standardized at `rounded-xl` (24dp / 1.5rem) to evoke an ergonomic, handheld device container.
- **Bottom Sheets & Modal Surfaces:** Upper boundaries feature 28dp rounded curves, harmonizing with Android gesture bars.

## Components

- **Buttons:**
  - *Primary Button:* Solid `#818cf8` fill with `#0f172a` text (`label-lg`), pill shape (h: 48dp), zero border. Active state applies a luminous `#6366f1` overlay.
  - *Tonal / Secondary Button:* Background `#1e1b4b`, text `#2dd4bf`, pill shape, 1px `#334155` border.
  - *Outlined / Ghost Button:* Transparent background, `#94a3b8` text, pill shape, 1px `#334155` border.

- **Chips & Filter Pills:**
  - Height 32dp, pill radius.
  - *Inactive:* Surface `#111827`, border 1px `#334155`, text `#94a3b8`.
  - *Active:* Surface `#2dd4bf` with text `#0f172a` (`label-md`), or indigo tint `#1e1b4b` with text `#818cf8` and border `#818cf8`.

- **Cards (Video & Index Containers):**
  - Background `#1e293b`, 1px border `#334155`, radius 24dp, internal padding `space-md` (16dp).
  - Video preview frames inside cards utilize 16dp corner radius. Hover/press triggers an ambient teal or indigo stroke shift.

- **Inputs & Search Bar:**
  - Material 3 search bar: Full pill contour (h: 56dp), background `#111827`, border 1px `#334155`.
  - Focused state transitions border to `#818cf8` with an inner luminous bloom. Placeholder text in `#94a3b8`.

- **Checkboxes & Selection Controls:**
  - Rounded-sm (6dp) for checkboxes, circular for radios.
  - Checked: Filled with `#2dd4bf`, contrasting checkmark in `#0f172a`.
  - Unchecked: 2px border `#334155` on transparent background.

- **Timestamps & Index Pins:**
  - Micro-pills embedded over thumbnail media. Background: `rgba(15, 23, 42, 0.85)` with backdrop blur, text `#2dd4bf` (`label-sm`), 1px border `rgba(51, 65, 85, 0.6)`.