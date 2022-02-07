package com.oddinstitute.svgparser.svg_tags

import android.graphics.PointF
import com.oddinstitute.svgparser.operators.cleanTags
import org.xmlpull.v1.XmlPullParser

// this is the only tag that is not a sub class of Tag
// because it doesn't really need that
class SvgTag(val parser: XmlPullParser)
{
    fun decode(): Pair<PointF, Float>
    {
        var viewBoxString = ""
        parser.getAttributeValue(null, "viewBox")?.let {

            viewBoxString = it
        }


        // general SVG cleaner
        val viewBoxCleaned = viewBoxString.cleanTags()

        val pieces =
            viewBoxCleaned.split("(?= )".toRegex())
                    .toTypedArray()


        val width = pieces[2].toFloat() - pieces[0].toFloat()
        val height = pieces[3].toFloat() - pieces[1].toFloat()

        var large: Float = height // actual height is the difference
        if (width > large) // actual width is the difference
            large = pieces[2].toFloat()


        val scaleFactor = 512.0f / large
        val offset = PointF(pieces[0].toFloat(), pieces[1].toFloat())

        return Pair(offset, scaleFactor)
    }
}

