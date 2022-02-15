package com.oddinstitute.svgparser.svg_elements

import android.graphics.Color

class SvgStyle
{
    var fill: Color? = null
    var stroke: Color? = null
    var strokeWidth: Float? = null

    var fillRule: SvgFillRule? = null
    var clipRule: SvgClipRule? = null
    var strokeLineCap: SvgLinecap? = null // butt | round | square
    var strokeDashArray: Float? = null // butt | round | square
    var strokeLineJoin: SvgStrokeLineJoin? = null // butt | round | square


    override fun toString(): String
    {
        var string = ""

        strokeWidth?.let { string += "Width: $it" }

        return  string
    }
}