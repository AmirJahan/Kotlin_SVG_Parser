package com.oddinstitute.svgparser.svg_tags.path_tag

import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.Segment


fun PathTag.linePiece(piece: String, curPoint: PointF): Segment
{
    val line = Segment(PathType.Line)

    val str = piece
            .replace("L,", "")
            .replace("L", "")
            .replace("l,", "")
            .replace("l", "")
            .replace("V,", "")
            .replace("V", "")
            .replace("v,", "")
            .replace("v", "")
            .replace("H,", "")
            .replace("H", "")
            .replace("h,", "")
            .replace("h", "")



    val points = str.split(",")


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