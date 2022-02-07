package com.oddinstitute.svgparser.svg_tags

import android.graphics.PointF
import com.oddinstitute.svgparser.*
import com.oddinstitute.svgparser.polygon.Polygon
import org.xmlpull.v1.XmlPullParser

class EllipseTag(val parser: XmlPullParser): Tag(parser)
{
    override fun decode(): Polygon
    {
        val segments: ArrayList<Segment> = arrayListOf()

        var cx = 0f
        var cy = 0f
        var rx = 0f
        var ry = 0f


        parser.getAttributeValue(null, "cx")?.let {
            cx = it.toFloat()
        }
        parser.getAttributeValue(null, "cy")?.let {
            cy = it.toFloat()
        }
        parser.getAttributeValue(null, "rx")?.let {
            rx = it.toFloat()
        }
        parser.getAttributeValue(null, "ry")?.let {
            ry = it.toFloat()
        }


        val moveSeg = Segment(PathType.Move)
        moveSeg.knot = PointF(cx - rx, cy)
        segments.add(moveSeg)


        // these are the seven pieces of an arc
        val sevenPieceArc1: SevenPieceArc = SevenPieceArc(rx, ry, 0, 1, 0, rx * 2f, 0f)
        val sevenPieceArc2: SevenPieceArc = SevenPieceArc(rx, ry, 0, 1, 0, -rx * 2f, 0f)


        // now, we have the seven piece, we have to convert it to the path
        // todo - we still have to do this
        segments.add(sevenPieceArc1.toSegment())
        segments.add(sevenPieceArc2.toSegment())

        val polygon: Polygon = Polygon()
        polygon.shapeNode.pathValue = PathValue(segments)
        polygon.closed = true
        return polygon
    }
}