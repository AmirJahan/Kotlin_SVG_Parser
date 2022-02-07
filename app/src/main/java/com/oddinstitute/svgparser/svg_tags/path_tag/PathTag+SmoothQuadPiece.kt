package com.oddinstitute.svgparser.svg_tags.path_tag

import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.Segment


fun PathTag.smoothQuadPiece(piece: String, curPoint: PointF, prevSegment: Segment): Segment
{
    val str = piece
            .replace(" ", ",")
            .replace("t,", "")
            .replace("T,", "")
            .replace("t", "")
            .replace("T", "")
            .replace(",,", ",")
            .replace(" ", ",")

    val points = str.split(",")

    // if we are relative, we find the actual value
    // if not, we just add zero
    var intCurPoint = PointF() // 0, 0
    if (piece[0] == 't') // relative
        intCurPoint = curPoint



    val smoothQuad = Segment(PathType.SmoothQuad)
    smoothQuad.knot = PointF(points[0].toFloat() + intCurPoint.x,
                             points[1].toFloat() + intCurPoint.y)

    /**
     * T
     * Draws a quadratic Bézier curve from the current point to (x,y).
     * The control point is assumed to be the reflection of the control point
     * on the previous command relative to the current point.
     * If there is no previous command or if the previous command was not a Q, q, T or t,
     * assume the control point is coincident with the current point
     */

    if (prevSegment.type == PathType.Quad || prevSegment.type == PathType.SmoothQuad)
    {
        var prevCp1X = 0f
        var prevCp1Y = 0f

        prevSegment.cp1?.let {
            prevCp1X = it.x
            prevCp1Y = it.y
        }


        // these reflections are irrelevant of the relative or not
        // they have to consider the "curPoint", not the "intCurPoint"
        val xReflectionOfFirstCP =
            2 * curPoint.x - prevCp1X //+ curX
        val yReflectionOfFirstCP =
            2 * curPoint.y - prevCp1Y //+ curY

        smoothQuad.cp1 = PointF(xReflectionOfFirstCP, yReflectionOfFirstCP)
    }
    else
    {
        smoothQuad.cp1 =
            PointF(prevSegment.knot.x, prevSegment.knot.y)
    }

    return smoothQuad
}