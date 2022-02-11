package com.oddinstitute.svgparser.svg_tags.path_tag

import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.Segment

fun PathTag.movePiece(piece: String, curPoint: PointF): Segment
{
    val str = piece.replace("m", "")
            .replace("M", "")
            .replace(" ", ",")
            .replace(",,", ",") // it's possible to get two commas



    val points = str
            .split(",")
    val move = Segment(PathType.Move)


    // if we are relative, we find the actual value based on the cur point
    // this is simply a shortcut, when relative, we use curPoint, when not, we don't
    // if not, we just add zero
    val intCurPoint = curPoint * (piece[0] == 'm').toFloat()

    move.knot = PointF(points[0].toFloat() + intCurPoint.x,
                       points[1].toFloat() + intCurPoint.y)

    return move
}


// todo these two exist
operator fun PointF.times(other: Float) = PointF(this.x * other,
                                                 this.y * other)
fun Boolean.toFloat() = if (this) 1f else 0f
