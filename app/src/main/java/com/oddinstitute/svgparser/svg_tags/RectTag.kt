package com.oddinstitute.svgparser.svg_tags

import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.PathValue
import com.oddinstitute.svgparser.polygon.Polygon
import com.oddinstitute.svgparser.Segment
import org.xmlpull.v1.XmlPullParser

class RectTag (val parser: XmlPullParser): Tag(parser)
{
    override fun decode(): Polygon
    {
        val segments: ArrayList<Segment> = arrayListOf()

        var x1 = 0f
        var y1 = 0f
        var x2 = 0f
        var y2 = 0f

        parser.getAttributeValue(null, "x")?.let {
            x1 = it.toFloat()
        }

        parser.getAttributeValue(null, "y")?.let {
            y1 = it.toFloat()
        }


        val moveSeg = Segment(PathType.Move)
        moveSeg.knot = PointF(x1, y1)
        segments.add(moveSeg)



        parser.getAttributeValue(null, "width")?.let {
            x2 = it.toFloat() + x1
        }

        val line1Seg = Segment(PathType.Line)
        line1Seg.knot = PointF(x2, y1)
        segments.add(line1Seg)


        parser.getAttributeValue(null, "height")?.let {
            y2 = it.toFloat() + y1
        }

        val line2Seg = Segment(PathType.Line)
        line2Seg.knot = PointF(x2, y2)
        segments.add(line2Seg)


        val line3Seg = Segment(PathType.Line)
        line3Seg.knot = PointF(x2 - x1, y2)
        segments.add(line3Seg)

        val polygon: Polygon = Polygon()
        polygon.shapeNode.pathValue = PathValue(segments)
        polygon.closed = true

        return polygon
    }
}