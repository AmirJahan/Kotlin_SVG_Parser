package com.oddinstitute.svgparser.tags

import android.graphics.PointF
import com.oddinstitute.svgparser.*
import com.oddinstitute.svgparser.polygon.Polygon
import org.xmlpull.v1.XmlPullParser


class CircleTag (val parser: XmlPullParser): Tag(parser)
{
    // CIRCLE ONLY
    var r = 0f
    // CIRCLE and OVAL
    var cx = 0f
    var cy = 0f


    init
    {
        // CIRCLE and OVAL
        parser.getAttributeValue(null, "cx")?.let { cx = it.toFloat() }
        parser.getAttributeValue(null, "cy")?.let { cy = it.toFloat() }




        // CIRCLE ONLY
        parser.getAttributeValue(null, "r")?.let { r = it.toFloat() }
    }



    override fun toPolygon(): ArrayList<Polygon>
    {
        val segments: ArrayList<Segment> = arrayListOf()

        val moveSeg = Segment(PathType.Move)
        moveSeg.knot = PointF(cx - r, cy)
        segments.add(moveSeg)


        // XCODE METHOD
        // FIXME this works, converts a circle into 4 arcs
        // for now, I am choosing to use the 4 arc method

        // these are the seven pieces of an arc
        val sevenPieceArc1: SevenPieceArc = SevenPieceArc(r, r,
                                                          0f,
                                                          largeArcFlag = false,
                                                          sweepFlag = false,
                                                          x2 = cx + r,
                                                          y2 = cy)

        val sevenPieceArc2: SevenPieceArc = SevenPieceArc(r, r,
                                                          0f,
                                                          largeArcFlag = true,
                                                          sweepFlag = false,
                                                          x2 = cx - r,
                                                          y2 = cy)




        // THIS IS CURRENTLY THE WORKING ONE THAT CONVERTS to 4 PIECES
        // first from Move draw to half
        val piece1Segments = sevenPieceArc1.toSegmentsObjCMethod(PointF(cx - r, cy))
//
//        // then from the end of that half, draw back
        val piece2Segments = sevenPieceArc2.toSegmentsObjCMethod(PointF(cx + r, cy))



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
