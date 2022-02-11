package com.oddinstitute.svgparser.svg_to_artwork

import com.oddinstitute.svgparser.svg_elements.SvgStyle

fun SvgToArtwork.combineGroupAndTagStyles(elemStyle: SvgStyle, gStyle: SvgStyle): SvgStyle
{
    val outStyle: SvgStyle = SvgStyle()

    // we call all of these on order
    // so elements takes priority over the group
    gStyle.fill?.let { outStyle.fill = it }
    elemStyle.fill?.let { outStyle.fill = it }

    gStyle.stroke?.let { outStyle.stroke = it }
    elemStyle.stroke?.let { outStyle.stroke = it }

    gStyle.stroke?.let { outStyle.stroke = it }
    elemStyle.stroke?.let { outStyle.stroke = it }

    gStyle.strokeWidth?.let { outStyle.strokeWidth = it }
    elemStyle.strokeWidth?.let { outStyle.strokeWidth = it }

    gStyle.fillRule?.let { outStyle.fillRule = it }
    elemStyle.fillRule?.let { outStyle.fillRule = it }

    gStyle.clipRule?.let { outStyle.clipRule = it }
    elemStyle.clipRule?.let { outStyle.clipRule = it }

    gStyle.strokeLineCap?.let { outStyle.strokeLineCap = it }
    elemStyle.strokeLineCap?.let { outStyle.strokeLineCap = it }

    return outStyle
}