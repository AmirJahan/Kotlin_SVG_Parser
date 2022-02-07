package com.oddinstitute.svgparser.svg_tags.path_tag

import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.Segment


fun PathTag.smoothCurvePiece(piece: String, curPoint: PointF, prevSegment: Segment): Segment
{
    // here, if the first letter was -, then we have like ("s,-4,17.8,-8.9,17.8")
    // so, we check and clean for both "s," and "s"

    val str = piece.replace("s,", "")
            .replace("S,", "")
            .replace("s", "")
            .replace("S", "")
            .replace(",,", ",")
            .replace(" ", ",")

    val points = str.split(",")

    // if we are relative, we find the actual value
    // if not, we just add zero
    var intCurPoint = PointF() // 0, 0
    if (piece[0] == 's') // relative
        intCurPoint = curPoint

    val smooth = Segment(PathType.SmoothCurve)
    smooth.cp2 = PointF(points[0].toFloat() + intCurPoint.x,
                        points[1].toFloat() + intCurPoint.y)

    smooth.knot = PointF(points[2].toFloat() + intCurPoint.x,
                         points[3].toFloat() + intCurPoint.y)


    /**
     * S
     * Draws a cubic Bézier curve from the current point to (x,y).
     * The first control point is assumed to be the reflection
     * of the second control point on the previous command relative to the current point.
     * If there is no previous command or if the previous command was not an C, c, S or s,
     * assume the first control point is coincident with the current point.
     */

    if (prevSegment.type == PathType.Curve || prevSegment.type == PathType.SmoothCurve)
    {
        var prevCp2X = 0f
        var prevCp2Y = 0f

        prevSegment.cp2?.let {
            prevCp2X = it.x
            prevCp2Y = it.y
        }


        // these reflections are irrelevant of the relative or not
        // they have to consider the "curPoint", not the "intCurPoint"
        val xReflectionOfSecondCP =
            2 * curPoint.x - prevCp2X // + curX
        val yReflectionOfSecondCP =
            2 * curPoint.y - prevCp2Y // + curY
        smooth.cp1 = PointF(xReflectionOfSecondCP, yReflectionOfSecondCP)
    }
    else
    {
        smooth.cp1 = PointF(prevSegment.knot.x,
                            prevSegment.knot.y)
    }

    return smooth
}