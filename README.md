# Kotlin SVG Parser

An Android application that parses SVG files and renders them as vector graphics on a custom canvas, with a comparison view against the Sharp SVG library.

## Overview

This project demonstrates SVG parsing from scratch in Kotlin, converting SVG XML into drawable vector shapes with full support for paths, shapes, styling, and transformations.

## Features

### SVG Element Support
- **Shapes**: Paths, Rectangles, Circles, Ellipses, Polygons, Polylines, Lines
- **Groups**: `<g>` tag support with nested elements
- **Text**: Text element rendering
- **Use References**: `<use>` tag support

### Path Commands
- Move (M/m), Line (L/l, H/h, V/v)
- Cubic Bezier (C/c, S/s)
- Quadratic Bezier (Q/q, T/t)
- Arc (A/a), Close (Z/z)

### Styling
- Fill colors and fill rules (even-odd, non-zero)
- Stroke properties (color, width, line-cap, line-join, dash arrays)
- Style attributes and CSS class styling

### Transformations
- Matrix transforms
- Scale factors
- ViewBox handling with automatic scaling

## Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Kotlin |
| Platform | Android (Min SDK 27, Target SDK 31) |
| Build | Gradle 7.0.3 |
| Serialization | kotlinx-serialization 1.1.0 |
| Reference Lib | Sharp 1.1.0 |

## Project Structure

```
app/src/main/java/com/oddinstitute/svgparser/
├── MainActivity.kt           # UI with navigation
├── Artwork.kt                # Top-level SVG container
├── ShapeNode.kt              # Shape styling and path data
├── Polygon.kt                # Individual drawable shape
├── svg_parser/
│   ├── SvgParser.kt          # Parser state
│   └── SvgParser+Parse.kt    # Parsing implementation
├── tags/
│   ├── Tag.kt                # Base tag class
│   ├── PathTag.kt            # Path parsing
│   └── ...                   # Shape-specific tags
├── svg_elements/
│   ├── SvgStyle.kt           # Style parsing
│   ├── SvgColor.kt           # Color parsing
│   └── SvgTransform.kt       # Transform handling
└── temp_draw/
    └── DrawView.kt           # Custom Android canvas view
```

## Usage

```kotlin
// Load SVG from assets
val parser = SvgParser()
val inputStream = assets.open("mysvg.svg")
val artwork = parser.parse(inputStream)
inputStream.close()

// Render to custom view
drawView.redraw(artwork)

// Access parsed data
artwork.polygons  // ArrayList<Polygon>
artwork.title = "My SVG"
```

## Building

```bash
# Clone and open in Android Studio
# Sync Gradle files

./gradlew assembleDebug
```

## Test Assets

The project includes 200+ test SVG icons in `/app/src/main/assets/magicons/` for testing the parser against various SVG patterns.

## Navigation

- **Next/Previous** buttons cycle through available SVG files
- Side-by-side comparison: Custom parser output vs Sharp library reference

---


