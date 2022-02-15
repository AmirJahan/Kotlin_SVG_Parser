package com.oddinstitute.svgparser.svg_tags.path_tag

import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.Segment


fun PathTag.quadPiece(piece: String, curPoint: PointF): ArrayList<Segment>
{
    val segments = arrayListOf<Segment>()
    val str = piece
            .replace("q", "")
            .replace("Q", "")
            .replace(" ", ",")
            .replace(",,", ",") // it's possible to get two commas

    // quads have 4 pieces, SO
    val points = str
            .split(",")

    // if we are relative, we find the actual value based on the cur point
    // this is simply a shortcut, when relative, we use curPoint, when not, we don't
    // if not, we just add zero
    var intCurPoint = curPoint * (piece[0] == 'q').toFloat()

//    quad.cp1 = PointF(points[0].toFloat() + intCurPoint.x,
//                      points[1].toFloat() + intCurPoint.y)
//
//    quad.knot =
//        PointF(points[2].toFloat() + intCurPoint.x,
//               points[3].toFloat() + intCurPoint.y)


    for (i in 0 until points.count() step 4)
    {
        val quad = Segment(PathType.Quad)

        // if we are relative, we find the actual value based on the cur point
        // this is simply a shortcut, when relative, we use curPoint, when not, we don't
        // if not, we just add zero
        if (piece[0] == 'q') // all relative
        {
            intCurPoint = curPoint

            // after the first piece, we should take the previous location
            if (segments.count() > 0)
                intCurPoint = segments.last().knot
        }

        quad.cp1 = PointF(points[i].toFloat() + intCurPoint.x,
                          points[i+1].toFloat() + intCurPoint.y)

        quad.knot =
            PointF(points[i+2].toFloat() + intCurPoint.x,
                   points[i+3].toFloat() + intCurPoint.y)


        segments.add(quad)
    }


    return segments
}