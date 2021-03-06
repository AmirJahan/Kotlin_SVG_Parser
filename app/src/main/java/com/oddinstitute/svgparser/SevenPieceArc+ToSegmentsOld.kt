package com.oddinstitute.svgparser

import android.graphics.PointF
import kotlin.math.*

fun SevenPieceArc.toSegmentsObjCMethod(curPoint: PointF): ArrayList<Segment>
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
    val xAxisRotation = this.xAxisRotation

    val x = this.x2
    val y = this.y2

    var rx = this.rx
    var ry = this.ry

    val largeArcFlag = this.largeArcFlag
    val sweepFlag = this.sweepFlag


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

        val arcSegment = Segment(PathType.Curve)

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


fun SevenPieceArc.vectorAngle(ux: Float, uy: Float, vx: Float, vy: Float): Float
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

fun SevenPieceArc.mapToEllipse(x: Float,
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