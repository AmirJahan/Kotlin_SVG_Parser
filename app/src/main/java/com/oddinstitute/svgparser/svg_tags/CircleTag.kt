package com.oddinstitute.svgparser.svg_tags

import android.graphics.PointF
import com.oddinstitute.svgparser.*
import com.oddinstitute.svgparser.polygon.Polygon
import org.xmlpull.v1.XmlPullParser

class CircleTag (val parser: XmlPullParser): Tag(parser)
{
    override fun decode(): Polygon
    {
        val segments: ArrayList<Segment> = arrayListOf()

        var cx = 0f
        var cy = 0f
        var r = 0f

        parser.getAttributeValue(null, "cx")?.let {
            cx = it.toFloat()
        }

        parser.getAttributeValue(null, "cy")?.let {
            cy = it.toFloat()
        }

        parser.getAttributeValue(null, "r")?.let {
            r = it.toFloat()
        }

        val moveSeg = Segment(PathType.Move)
        moveSeg.knot = PointF(cx - r, cy)
        segments.add(moveSeg)

        // these are the seven pieces of an arc
        val sevenPieceArc1: SevenPieceArc = SevenPieceArc(r, r, 0, 1, 0, r * 2f, 0f)
        val sevenPieceArc2: SevenPieceArc = SevenPieceArc(r, r, 0, 1, 0, -r * 2f, 0f)


        // now, we have the seven piece, we have to convert it to the path
        segments.add(sevenPieceArc1.toSegment())
        segments.add(sevenPieceArc2.toSegment())


        val polygon: Polygon = Polygon()
        polygon.shapeNode.pathValue = PathValue(segments)

        // todo do we need to make this closed?
        polygon.closed = false


        return polygon
    }
}