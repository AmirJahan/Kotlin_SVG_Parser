package com.oddinstitute.svgparser.svg_tags.path_tag

import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.Segment


fun PathTag.curvePiece(piece: String, curPoint: PointF): Segment
{
    val str = piece
            .replace("c,", "")
            .replace("C,", "")
            .replace("c", "")
            .replace("C", "")
            .replace(",,", ",")
            .replace(" ", ",")



    val points = str.split(",")
    val curve = Segment(PathType.Curve)


    // if we are relative, we find the actual value
    // if not, we just add zero
    var intCurPoint = PointF() // 0, 0
    if (piece[0] == 'c') // relative
        intCurPoint = curPoint

    curve.cp1 = PointF(points[0].toFloat() + intCurPoint.x,
                       points[1].toFloat() + intCurPoint.y)

    curve.cp2 = PointF(points[2].toFloat() + intCurPoint.x,
                       points[3].toFloat() + intCurPoint.y)

    curve.knot = PointF(points[4].toFloat() + intCurPoint.x,
                        points[5].toFloat() + intCurPoint.y)

    return curve
}