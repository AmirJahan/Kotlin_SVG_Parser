package com.oddinstitute.svgparser.svg_to_artwork

import com.oddinstitute.svgparser.svg_elements.SvgStyle

fun SvgToArtwork.combineGroupTagStyles(tagStyle: SvgStyle, gStyle: SvgStyle): SvgStyle
{
    val outStyle: SvgStyle = SvgStyle()

    if (tagStyle.fill != null)      outStyle.fill = tagStyle.fill
    else if (gStyle.fill != null)   outStyle.fill = gStyle.fill

    if (tagStyle.stroke != null)      outStyle.stroke = tagStyle.stroke
    else if (gStyle.stroke != null)   outStyle.stroke = gStyle.stroke

    if (tagStyle.strokeWidth != null)      outStyle.strokeWidth = tagStyle.strokeWidth
    else if (gStyle.strokeWidth != null)   outStyle.strokeWidth = gStyle.strokeWidth

    if (tagStyle.fillRule != null)      outStyle.fillRule = tagStyle.fillRule
    else if (gStyle.fillRule != null)   outStyle.fillRule = gStyle.fillRule

    if (tagStyle.clipRule != null)      outStyle.clipRule = tagStyle.clipRule
    else if (gStyle.clipRule != null)   outStyle.clipRule = gStyle.clipRule

    if (tagStyle.strokeLineCap != null)      outStyle.strokeLineCap = tagStyle.strokeLineCap
    else if (gStyle.strokeLineCap != null)   outStyle.strokeLineCap = gStyle.strokeLineCap

    return outStyle
}