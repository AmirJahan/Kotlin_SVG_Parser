package com.oddinstitute.svgparser.svg_elements

import android.graphics.Color

class SvgStyle()
{
    var fill: Color? = null
    var stroke: Color? = null
    var strokeWidth: Float? = null

    var fillRule: SvgFillRule? = null
    var clipRule: SvgClipRule? = null
    var strokeLineCap: SvgLinecap? = null // butt | round | square
    var strokeDashArray: Float? = null // butt | round | square
    var strokeLineJoin: SvgStrokeLineJoin? = null // butt | round | square


    // this function receives a style string and decodes it to actual SvgStyles
    constructor(sText: String) : this()
    {
//        val svgStyle: SvgStyle = SvgStyle()

        // in style, we have both the Equal sign and the colon,
        // we replace equals with a colon
        val components = sText
                .replace("=", ":")
                .replace("\\s+".toRegex(), "") // remove multiple spaces
                .split(";")

        for (each in components)
        {
            val keyVal = each.split(":")
            if (keyVal.count() < 2)
                continue

            val key = keyVal[0]
            val value = keyVal[1]

            when (key)
            {
                "fill" -> this.fill = SvgColor.ofRaw(value)
                "stroke" -> this.stroke = SvgColor.ofRaw(value)
                "stroke-width" -> this.strokeWidth = value.toFloat()
                "fill-rule" -> this.fillRule = SvgFillRule.ofRaw(value) // gets from enum
                "clip-rule" -> this.clipRule = SvgClipRule.ofRaw(value)
                "stroke-linecap" -> this.strokeLineCap = SvgLinecap.ofRaw(value)
                "stroke-dasharray" -> this.strokeDashArray = value.toFloat()
                "stroke-linejoin" -> this.strokeLineJoin = SvgStrokeLineJoin.valueOf(value)
            }
        }
    }
}