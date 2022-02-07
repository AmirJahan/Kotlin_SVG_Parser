package com.oddinstitute.svgparser.svg_tags.path_tag

import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.Segment


fun PathTag.quadPiece(piece: String, curPoint: PointF): Segment
{
    val str = piece
            .replace(" ", ",")
            .replace("q,", "")
            .replace("Q,", "")
            .replace("q", "")
            .replace("Q", "")
            .replace(",,", ",")
            .replace(" ", ",")

    val points = str.split(",")
    val quad = Segment(PathType.Quad)

    // if we are relative, we find the actual value
    // if not, we just add zero
    var intCurPoint = PointF() // 0, 0
    if (piece[0] == 'q') // relative
        intCurPoint = curPoint

    quad.cp1 = PointF(points[0].toFloat() + intCurPoint.x,
                      points[1].toFloat() + intCurPoint.y)

    quad.knot =
        PointF(points[2].toFloat() + intCurPoint.x,
               points[3].toFloat() + intCurPoint.y)

    return quad
}