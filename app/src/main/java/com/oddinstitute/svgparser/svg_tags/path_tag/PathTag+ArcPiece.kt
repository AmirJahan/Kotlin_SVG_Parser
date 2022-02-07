package com.oddinstitute.svgparser.svg_tags.path_tag

import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.Segment
import com.oddinstitute.svgparser.SevenPieceArc
import kotlin.math.*


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


fun PathTag.arcPieces(piece: String, curPoint: PointF): ArrayList<Segment>?
{
    val str = piece
            .replace(" ", ",")
            .replace("a", "")
            .replace("A", "")
            .replace(",,", ",")
            .replace(" ", ",")

    val points = str.split(",")

    // if we are relative, we find the actual value
    // if not, we just add zero
    var intCurPoint = PointF() // 0, 0
    if (piece[0] == 'a') // relative
        intCurPoint = curPoint





    if (points.count() != 7)
        return null



    val rx = points[0].toFloat()
    val ry = points[1].toFloat()

    val xAxisRotation = points[2].toInt()
    val largeArcFlag_fA = points[3].toInt()
    val sweepFlag_fS = points[4].toInt()

    val x2 = points[5].toFloat()
    val y2 = points[6].toFloat()


    val s = SevenPieceArc(rx, ry, xAxisRotation, largeArcFlag_fA, sweepFlag_fS, x2, y2)


    return sevenPieceToSegments(s, intCurPoint)

}

fun PathTag.sevenPieceToSegments(arc: SevenPieceArc,
                                 curPoint: PointF): ArrayList<Segment>
{
    val outSegments = ArrayList<Segment>()
//
//        if (prevSegment.type == PathType.Arc)
//        {
//            x2 += curPoint.x
//            y2 += curPoint.y
//        }

    val x0 = curPoint.x
    val y0 = curPoint.y
    val xAxisRotation = arc.xAxisRotation

    val x = arc.x2
    val y = arc.y2

    var rx = arc.rx
    var ry = arc.ry

    val largeArcFlag = arc.largeArcFlag
    val sweepFlag = arc.sweepFlag


    // Convert angle from degrees to radians
    val TwoPI: Float = (PI * 2.0).toFloat()
    val sinAngle: Float = sin(xAxisRotation * TwoPI / 360)
    val cosAngle: Float = cos(xAxisRotation * TwoPI / 360)


    val x1Prime: Float = cosAngle * (x0 - x) / 2 + sinAngle * (y0 - y) / 2
    val y1Prime: Float = -sinAngle * (x0 - x) / 2 + cosAngle * (y0 - y) / 2

    if (x1Prime == 0.0f && y1Prime == 0.0f)
        return outSegments

    // Sign of the radii is ignored (behaviour specified by the spec)
    rx = abs(rx)
    ry = abs(ry)


    val lambda: Float = x1Prime.pow(2) / rx.pow(2) + y1Prime.pow(2) / ry.pow(2)

    if (lambda > 1)
    {
        rx *= sqrt(lambda)
        ry *= sqrt(lambda)
    }

    // Step 2: Compute (cx′, cy′)

    val rxSq: Float = rx.pow(2)
    val rySq: Float = ry.pow(2)
    val x1PrimeSq: Float = x1Prime.pow(2)
    val y1PrimeSq: Float = y1Prime.pow(2)

    var radicant: Float = (rxSq * rySq) - (rxSq * y1PrimeSq) - (rySq * x1PrimeSq)

    if (radicant < 0)
        radicant = 0f

    radicant /= (rxSq * y1PrimeSq) + (rySq * x1PrimeSq)

    // where the + sign is chosen if fA ≠ fS, and the − sign is chosen if fA = fS.
    radicant = sqrt(radicant) * (if (largeArcFlag == sweepFlag) -1; else 1)

    val xCenterPrime: Float = radicant * rx / ry * y1Prime
    val yCenterPrime: Float = radicant * -ry / rx * x1Prime


    // Step 3: Compute (cx, cy) from (cx′, cy′)
    val centerX: Float = cosAngle * xCenterPrime - sinAngle * yCenterPrime + (x0 + x) / 2
    val centerY: Float = sinAngle * xCenterPrime + cosAngle * yCenterPrime + (y0 + y) / 2


    // Step 4: Compute θ1 and Δθ
    val vec1_x: Float = (x1Prime - xCenterPrime) / rx
    val vec1_y: Float = (y1Prime - yCenterPrime) / ry

    val vec2_x: Float = (-x1Prime - xCenterPrime) / rx
    val vec2_y: Float = (-y1Prime - yCenterPrime) / ry

    var ang1: Float = vectorAngle(1f, 0f, vec1_x, vec1_y)
    var ang2: Float = vectorAngle(vec1_x, vec1_y, vec2_x, vec2_y)

    if (!sweepFlag && ang2 > 0)
        ang2 -= TwoPI // mod 360

    if (sweepFlag && ang2 < 0)
        ang2 += TwoPI // mod 360

    val segments: Int = maxOf(ceil(abs(ang2) / (TwoPI / 4.0)).toInt(), 1)

    ang2 /= segments


    for (i in 0 until segments)
    {
        val a: Float = 4.0f / 3.0f * tan(ang2 / 4.0f);

        val x1: Float = cos(ang1)
        val y1: Float = sin(ang1)
        val x2: Float = cos(ang1 + ang2)
        val y2: Float = sin(ang1 + ang2)

        val cp1: PointF =
            mapToEllipse(x1 - y1 * a, y1 + x1 * a, rx, ry, cosAngle, sinAngle, centerX, centerY)
        val cp2: PointF =
            mapToEllipse(x2 + y2 * a, y2 - x2 * a, rx, ry, cosAngle, sinAngle, centerX, centerY)
        val knot: PointF = mapToEllipse(x2, y2, rx, ry, cosAngle, sinAngle, centerX, centerY)

        val arcSegment = Segment(PathType.Arc)

        arcSegment.knot = knot
        arcSegment.cp1 = cp1
        arcSegment.cp2 = cp2

        outSegments.add(arcSegment)

//            CGPathAddCurveToPoint(_path, NULL, p1.x, p1.y, p2.x, p2.y, p.x, p.y);
//            _lastControlPoint = p2;

        ang1 += ang2;
    }

    return outSegments
}


fun PathTag.vectorAngle(ux: Float, uy: Float, vx: Float, vy: Float): Float
{
    val sign: Float = if (ux * vy - uy * vx < 0) -1f; else 1f
    val umag: Float = sqrt(ux * ux + uy * uy)
    val vmag: Float = sqrt(ux * ux + uy * uy)
    val dot: Float = ux * vx + uy * vy

    var div = dot / (umag * vmag)

    if (div > 1)
        div = 1f


    if (div < -1)
        div = -1f


    return sign * acos(div)
}

fun PathTag.mapToEllipse(x: Float,
                         y: Float,
                         rx: Float,
                         ry: Float,
                         cosPhi: Float,
                         sinPhi: Float,
                         centerX: Float,
                         centerY: Float): PointF
{
    val xx = x * rx
    val yy = y * ry

    val xp: Float = cosPhi * xx - sinPhi * yy
    val yp: Float = sinPhi * xx + cosPhi * yy

    return PointF(xp + centerX, yp + centerY)
}