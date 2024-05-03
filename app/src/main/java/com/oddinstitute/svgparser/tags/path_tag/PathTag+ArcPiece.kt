package com.oddinstitute.svgparser.tags.path_tag

import android.graphics.PointF
import com.oddinstitute.svgparser.Segment
import com.oddinstitute.svgparser.SevenPieceArc
import com.oddinstitute.svgparser.operators.toBoolean
import com.oddinstitute.svgparser.operators.toFloat
import com.oddinstitute.svgparser.toSegmentsJavaMethod

//  x1 y1 x2 y2 fA fS rx ry φ
/*
(x1, y1) are the absolute coordinates of the current point on the path,
obtained from the last two parameters of the previous path command.

(rx, ry) are the radii of the ellipse (also known as its semi-major and semi-minor axes).

φ is the angle from the x-axis of the current coordinate system to the x-axis of the ellipse.

fA is the large arc flag, and is 0 if an arc spanning less than or equal to 180 degrees is chosen,
 or 1 if an arc spanning greater than 180 degrees is chosen.

fS is the sweep flag, and is 0 if the line joining center to arc sweeps through decreasing angles,
 or 1 if it sweeps through increasing angles.

(x2, y2) are the absolute coordinates of the final point of the arc.


(cx, cy) are the coordinates of the center of the ellipse.

θ is the angle around the arc that the point (x, y) lies at, and ranges from:
    θ1 which is the start angle of the elliptical arc prior to the stretch and rotate operations.
    θ2 which is the end angle of the elliptical arc prior to the stretch and rotate operations.
    Δθ which is the difference between these two angles.
 */

fun PathTag.arcPieces(piece: String, curPoint: PointF): ArrayList<Segment> {
    val str = piece
        .replace("a", "")
        .replace("A", "")
        .replace(" ", ",")
        .replace(",,", ",") // it's possible to get two commas
                            // at this level, you can't move this to CLEAN

    val points = str
        .split(",")

    // if we are relative, we find the actual value
    // if not, we just add zero
//    var intCurPoint = PointF() // 0, 0
//    if (piece[0] == 'a') // relative
//        intCurPoint = curPoint

    if (points.count() != 7)
        return ArrayList<Segment>()

    val rx = points[0].toFloat()
    val ry = points[1].toFloat()

    val xAxisRotation = points[2].toFloat()
    val largeArcFlag_fA = points[3].toInt().toBoolean()
    val sweepFlag_fS = points[4].toInt().toBoolean()

    // if we are relative, we find the actual value based on the cur point
    // this is simply a shortcut, when relative, we use curPoint, when not, we don't
    // if not, we just add zero
    val intCurPoint = curPoint * (piece[0] == 'a').toFloat()

    val x2 = points[5].toFloat() + intCurPoint.x
    val y2 = points[6].toFloat() + intCurPoint.y

    val s = SevenPieceArc(rx, ry, xAxisRotation, largeArcFlag_fA, sweepFlag_fS, x2, y2)

    val segments: ArrayList<Segment> = s.toSegmentsJavaMethod(curPoint)

    return segments
//    return sevenPieceToSegments(s, intCurPoint)
}
