package com.oddinstitute.svgparser.svg_tags.path_tag

import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.Segment
import com.oddinstitute.svgparser.operators.toFloat


fun PathTag.linePiece(piece: String, curPoint: PointF): Segment
{
    val line = Segment(PathType.Line)

    val str = piece
            .replace("L", "")
            .replace("l", "")
            .replace("V", "")
            .replace("v", "")
            .replace("H", "")
            .replace("h", "")
            .replace(" ", ",")
            .replace(",,", ",") // it's possible to get two commas




    val points = str
            .split(",")


    // if we are relative, we find the actual value based on the cur point
    // this is simply a shortcut, when relative, we use curPoint, when not, we don't
    // if not, we just add zero
    val intCurPoint = curPoint * (  (piece[0] == 'l').toFloat() +
                                    (piece[0] == 'h').toFloat() +
                                    (piece[0] == 'v').toFloat()) // this combination is either zero or one


    when (piece[0])
    {
        // line to new x and y
        'L' -> line.knot = PointF(points[0].toFloat(), points[1].toFloat())

        // line from current to x and y
        'l' -> line.knot =
            PointF(points[0].toFloat() + curPoint.x, points[1].toFloat() + curPoint.y)

        // line from current point horizontally relative to the new x
        'h' -> line.knot = PointF(points[0].toFloat() + curPoint.x, curPoint.y)

        // line from current point, horizontally to the new x value (not relative)
        'H' -> line.knot = PointF(points[0].toFloat(), curPoint.y)


        // THESE TWO ARE ALSO points[0], because there is only one value after H or V
        // line from current point vertically relative to the new y
        'v' -> line.knot = PointF(curPoint.x, points[0].toFloat() + curPoint.y)

        // line from current point vertically to the new y value (not relative)
        'V' -> line.knot = PointF(curPoint.x, points[0].toFloat())
    }


    return line
}