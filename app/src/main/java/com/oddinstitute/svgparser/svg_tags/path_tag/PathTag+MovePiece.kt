package com.oddinstitute.svgparser.svg_tags.path_tag

import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.Segment

fun PathTag.movePiece(piece: String, curPoint: PointF): Segment
{
    val str = piece.replace("m,", "")
            .replace("M,", "")
            .replace("m", "")
            .replace("M", "")


    val points = str.split(",")
    val move = Segment(PathType.Move)


    // if we are relative, we find the actual value
    // if not, we just add zero
    var intCurPoint = PointF() // 0, 0
    if (piece[0] == 'm') // relative
        intCurPoint = curPoint


    move.knot = PointF(points[0].toFloat() + intCurPoint.x,
                       points[1].toFloat() + intCurPoint.y)

    return move
}