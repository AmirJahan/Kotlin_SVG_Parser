package com.oddinstitute.svgparser.tags

import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.PathValue
import com.oddinstitute.svgparser.polygon.Polygon
import com.oddinstitute.svgparser.Segment
import org.xmlpull.v1.XmlPullParser

class LineTag(val parser: XmlPullParser) : Tag(parser)
{
    // LINE
    var x1 = 0f
    var y1 = 0f
    var x2 = 0f
    var y2 = 0f

    init
    {
        // LINE
        parser.getAttributeValue(null, "x1")?.let { x1 = it.toFloat() }
        parser.getAttributeValue(null, "y1")?.let { y1 = it.toFloat() }
        parser.getAttributeValue(null, "x2")?.let { x2 = it.toFloat() }
        parser.getAttributeValue(null, "y2")?.let { y2 = it.toFloat() }
    }

    override fun toPolygon(): ArrayList<Polygon>
    {
        val segments: ArrayList<Segment> = arrayListOf()


        val moveSeg = Segment(PathType.Move)
        moveSeg.knot = PointF(x1, y1)
        segments.add(moveSeg)

        val lineSeg = Segment(PathType.Line)
        lineSeg.knot = PointF(x2, y2)
        segments.add(lineSeg)


        val polygon: Polygon = Polygon()
        polygon.shapeNode.pathValue = PathValue(segments)
        polygon.closed = false

        return arrayListOf(polygon)
    }
}
