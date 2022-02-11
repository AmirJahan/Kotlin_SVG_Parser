package com.oddinstitute.svgparser.svg_tags

import android.graphics.PointF
import com.oddinstitute.svgparser.*
import com.oddinstitute.svgparser.polygon.Polygon
import org.xmlpull.v1.XmlPullParser

class OvalTag(val parser: XmlPullParser): Tag(parser)
{
    override fun decode(): ArrayList<Polygon>
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
//        var curPoint = PointF(cx - rx, cy) // set the current point





        // XCODE METHOD
        // FIXME this works, converts a circle into 4 arcs
        // for now, I am choosing to use the 4 arc method

        // these are the seven pieces of an arc
        val sevenPieceArc1: SevenPieceArc = SevenPieceArc(rx, ry,
                                                          0f,
                                                          largeArcFlag = false,
                                                          sweepFlag = false,
                                                          x2 = cx + rx,
                                                          y2 = cy)

        val sevenPieceArc2: SevenPieceArc = SevenPieceArc(rx, ry,
                                                          0f,
                                                          largeArcFlag = true,
                                                          sweepFlag = false,
                                                          x2 = cx - rx,
                                                          y2 = cy)




        // THIS IS CURRENTLY THE WORKING ONE THAT CONVERTS to 4 PIECES
        // first from Move draw to half
        val piece1Segments = sevenPieceArc1.toSegmentsObjCMethod(PointF(cx - rx, cy))
//
//        // then from the end of that half, draw back
        val piece2Segments = sevenPieceArc2.toSegmentsObjCMethod(PointF(cx + rx, cy))



        // THIS IS THE OLD JAVA METHOD THAT CONVERTS to 2 PIECES
//        val piece1Segments = sevenPieceArc1.toSegmentsJavaMethod(PointF(cx - r, cy))
//        val piece2Segments = sevenPieceArc2.toSegmentsJavaMethod(PointF(cx + r, cy))


        segments.addAll(piece1Segments)
        segments.addAll(piece2Segments)






        val polygon: Polygon = Polygon()
        polygon.shapeNode.pathValue = PathValue(segments)

        // todo do we need to make this closed?
        polygon.closed = true


        return arrayListOf(polygon)
    }
}