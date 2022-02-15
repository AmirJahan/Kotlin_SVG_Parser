package com.oddinstitute.svgparser.svg_to_artwork

import com.oddinstitute.svgparser.svg_elements.SvgStyle


// here, we read inner and outer styles. Inner gets priority
fun SvgToArtwork.combinePrePostStyles(innerStyle: SvgStyle,
                                      outerStyle: SvgStyle): SvgStyle
{
    val resStyle: SvgStyle = SvgStyle()

    // we call all of these on order
    // so elements takes priority over the group
    outerStyle.fill?.let { resStyle.fill = it }
    innerStyle.fill?.let { resStyle.fill = it }

    outerStyle.stroke?.let { resStyle.stroke = it }
    innerStyle.stroke?.let { resStyle.stroke = it }

    outerStyle.stroke?.let { resStyle.stroke = it }
    innerStyle.stroke?.let { resStyle.stroke = it }

    outerStyle.strokeWidth?.let { resStyle.strokeWidth = it }
    innerStyle.strokeWidth?.let { resStyle.strokeWidth = it }

    outerStyle.fillRule?.let { resStyle.fillRule = it }
    innerStyle.fillRule?.let { resStyle.fillRule = it }

    outerStyle.clipRule?.let { resStyle.clipRule = it }
    innerStyle.clipRule?.let { resStyle.clipRule = it }

    outerStyle.strokeLineCap?.let { resStyle.strokeLineCap = it }
    innerStyle.strokeLineCap?.let { resStyle.strokeLineCap = it }

    outerStyle.strokeDashArray?.let { resStyle.strokeDashArray = it }
    innerStyle.strokeDashArray?.let { resStyle.strokeDashArray = it }

    outerStyle.strokeLineJoin?.let { resStyle.strokeLineJoin = it }
    innerStyle.strokeLineJoin?.let { resStyle.strokeLineJoin = it }

    return resStyle
}