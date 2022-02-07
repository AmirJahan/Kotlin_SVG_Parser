package com.oddinstitute.svgparser.svg_tags

import android.graphics.Color
import com.oddinstitute.svgparser.*
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

    open var transforms: ArrayList<SvgTransform>? = null

    open var style: SvgStyle? = null
    open var svgClass: String? = null // this is the only one that's a string. it makes sense (it's not a first tag)

    // this is generic, but only used in the actual tags
   // var parentGroup: GTag? = null


    constructor(parser: XmlPullParser): this()
    {
        parser.getAttributeValue(null, "fill")?.let {
            this.fill = SvgColor.ofRaw(it)
        }

        parser.getAttributeValue(null, "stroke")?.let {
            this.stroke = SvgColor.ofRaw(it)
        }

        parser.getAttributeValue(null, "stroke-width")?.let {
            this.strokeWidth = it.toFloat()
        }

        parser.getAttributeValue(null, "stroke-linecap")?.let {
            this.strokeLineCap = SvgLinecap.ofRaw(it)
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

        parser.getAttributeValue(null, "style")?.let {
            this.style = it.styleValueDecoder()
        }

        parser.getAttributeValue(null, "class")?.let {
            this.svgClass = it // build a class with the name that is on it
        }
    }

    open fun decode (): Polygon
    {
        // not sure if this is a right way
        return Polygon()
    }
}