package com.oddinstitute.svgparser.svg_tags

import android.graphics.Color
import com.oddinstitute.svgparser.*
import com.oddinstitute.svgparser.operators.decodeTransform
import com.oddinstitute.svgparser.polygon.Polygon
import com.oddinstitute.svgparser.svg_elements.*
import org.xmlpull.v1.XmlPullParser

// base tag class for everyone
// it cacluates ALL the common tags
open class Tag()
{
    open var fill: Color? = null
    open var stroke: Color? = null
    open var strokeWidth: Float? = null

    open var fillRule: SvgFillRule? = null
    open var clipRule: SvgClipRule? = null
    open var strokeLineCap: SvgLinecap? = null // butt | round | square
    open var strokeLineJoin: SvgStrokeLineJoin? = null
    open var strokeDashArray: Float? = null // butt | round | square

    open var transforms: ArrayList<SvgTransform>? = null

    open var style: SvgStyle? = null
    open var svgClass: String? = null // this is the only one that's a string. it makes sense (it's not a first tag)

    // this is generic, but only used in the actual tags
   // var parentGroup: GTag? = null


    constructor(parser: XmlPullParser): this()
    {
        parser.getAttributeValue(null, "fill")?.let {
            if (it != "none")
                this.fill = SvgColor.ofRaw(it)
        }

        parser.getAttributeValue(null, "stroke")?.let {
            if (it != "none")
            {
                this.stroke = SvgColor.ofRaw(it)
                this.strokeWidth = 1.0f // if there's a stroke color, then there's a stroke
                // in the next code, this might be updated with the actual stroke width
            }
        }

        parser.getAttributeValue(null, "stroke-width")?.let {
            this.strokeWidth = it.toFloat()
        }

        parser.getAttributeValue(null, "stroke-linecap")?.let {
            this.strokeLineCap = SvgLinecap.ofRaw(it)
        }

        parser.getAttributeValue(null, "stroke-dasharray")?.let {
            this.strokeDashArray = it.toFloat()
        }

        parser.getAttributeValue(null, "transform")?.let {
            this.transforms = it.decodeTransform()
        }

        parser.getAttributeValue(null, "fill-rule")?.let {
            this.fillRule = SvgFillRule.ofRaw(it)
        }

        parser.getAttributeValue(null, "clip-rule")?.let {
            this.clipRule = SvgClipRule.ofRaw(it)
        }

        parser.getAttributeValue(null, "stroke-linejoin")?.let {
            this.strokeLineJoin = SvgStrokeLineJoin.ofRaw(it)
        }

        parser.getAttributeValue(null, "style")?.let {
            this.style = it.styleValueDecoder()
        }

        parser.getAttributeValue(null, "class")?.let {
            this.svgClass = it // build a class with the name that is on it
        }
    }


    // this function returns an array, because for the paths, we might have more than one piece
    // others return singular objects, but path might return multiple objects
    open fun decode (): ArrayList<Polygon>
    {
        return arrayListOf()
    }
}