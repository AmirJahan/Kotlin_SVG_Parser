package com.oddinstitute.svgparser.svg_tags.path_tag

import android.graphics.Matrix
import android.graphics.Path
import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.Segment
import com.oddinstitute.svgparser.SevenPieceArc
import kotlin.math.*

// Check input to Math.acos() in case rounding or other errors result in a val < -1 or > +1.
// For example, see the possible KitKat JIT error described in issue #62.
fun checkedArcCos(myVal: Double): Double
{
    return if (myVal < -1.0) Math.PI else if (myVal > 1.0) 0.0 else acos(myVal)
}


private fun arcToBeziers(angleStart: Double, angleExtent: Double): FloatArray
{
    val numPieces = ceil(abs(angleExtent) / (Math.PI * 2.0)).toInt() // (angleExtent / 90deg)
    val angleIncrement = angleExtent / numPieces

    // The length of each control point vector is given by the following formula.
    val controlLength = 4.0 / 3.0 * sin(angleIncrement / 2.0) / (1.0 + cos(angleIncrement / 2.0))

    val coords = FloatArray(numPieces * 6)


    var pos = 0
    for (i in 0 until numPieces)
    {
        var angle = angleStart + i * angleIncrement

        // Calculate the control vector at this angle
        var dx = cos(angle)
        var dy = sin(angle)

        // First control point
        coords[pos++] = (dx - controlLength * dy).toFloat()
        coords[pos++] = (dy + controlLength * dx).toFloat()

        // Second control point
        angle += angleIncrement
        dx = cos(angle)
        dy = sin(angle)
        coords[pos++] = (dx + controlLength * dy).toFloat()
        coords[pos++] = (dy - controlLength * dx).toFloat()

        // Endpoint of bezier
        coords[pos++] = dx.toFloat()
        coords[pos++] = dy.toFloat()
    }
    return coords
}


private fun arcTo(curPoint: PointF,
                  arc: SevenPieceArc): ArrayList<Segment>
{
    val segmentsArr : ArrayList<Segment> = arrayListOf()

    val x0 = curPoint.x
    val y0 = curPoint.y
    val xAxisRotation = arc.xAxisRotation

    val x = arc.x2
    val y = arc.y2

    var rx = arc.rx
    var ry = arc.ry

    val largeArcFlag = arc.largeArcFlag
    val sweepFlag = arc.sweepFlag


    if (x0 == x && y0 == y)
    {
        // If the endpoints (x, y) and (x0, y0) are identical, then this
        // is equivalent to omitting the elliptical arc segment entirely.
        // (behaviour specified by the spec)
        return segmentsArr
    }

    // Handle degenerate case (behaviour specified by the spec)
    if (rx == 0f || ry == 0f)
    {
        // we are here
        val lineSeg = Segment(PathType.Line, PointF(x, y))



        pather.lineTo(x, y)
        return
    }

    // Sign of the radii is ignored (behaviour specified by the spec)
    rx = abs(rx)
    ry = abs(ry)

    // Convert angle from degrees to radians
    val angleRad = Math.toRadians(xAxisRotation % 360.0)
    val cosAngle = cos(angleRad)
    val sinAngle = sin(angleRad)

    // We simplify the calculations by transforming the arc so that the origin is at the
    // midpoint calculated above followed by a rotation to line up the coordinate axes
    // with the axes of the ellipse.

    // Compute the midpoint of the line between the current and the end point
    val dx2 = (x0 - x) / 2.0
    val dy2 = (y0 - y) / 2.0

    // Step 1 : Compute (x1', y1')
    // x1,y1 is the midpoint vector rotated to take the arc's angle out of consideration
    val x1 = cosAngle * dx2 + sinAngle * dy2
    val y1 = -sinAngle * dx2 + cosAngle * dy2

    var rx_sq = (rx * rx).toDouble()
    var ry_sq = (ry * ry).toDouble()
    val x1_sq = x1 * x1
    val y1_sq = y1 * y1

    // Check that radii are large enough.
    // If they are not, the spec says to scale them up so they are.
    // This is to compensate for potential rounding errors/differences between SVG implementations.
    val radiiCheck = x1_sq / rx_sq + y1_sq / ry_sq
    if (radiiCheck > 0.99999)
    {
        val radiiScale = sqrt(radiiCheck) * 1.00001
        rx = (radiiScale * rx).toFloat()
        ry = (radiiScale * ry).toFloat()
        rx_sq = (rx * rx).toDouble()
        ry_sq = (ry * ry).toDouble()
    }

    // Step 2 : Compute (cx1, cy1) - the transformed centre point
    var sign: Double = if (largeArcFlag == sweepFlag) -1.0 else 1.0
    var sq = (rx_sq * ry_sq - rx_sq * y1_sq - ry_sq * x1_sq) / (rx_sq * y1_sq + ry_sq * x1_sq)
    sq = if (sq < 0) 0.0 else sq
    val coef = sign * sqrt(sq)
    val cx1 = coef * (rx * y1 / ry)
    val cy1 = coef * -(ry * x1 / rx)

    // Step 3 : Compute (cx, cy) from (cx1, cy1)
    val sx2 = (x0 + x) / 2.0
    val sy2 = (y0 + y) / 2.0
    val cx = sx2 + (cosAngle * cx1 - sinAngle * cy1)
    val cy = sy2 + (sinAngle * cx1 + cosAngle * cy1)

    // Step 4 : Compute the angleStart (angle1) and the angleExtent (dangle)
    val ux = (x1 - cx1) / rx
    val uy = (y1 - cy1) / ry
    val vx = (-x1 - cx1) / rx
    val vy = (-y1 - cy1) / ry
    var p: Double
    var n: Double

    // Angle betwen two vectors is +/- acos( u.v / len(u) * len(v))
    // Where '.' is the dot product. And +/- is calculated from the sign of the cross product (u x v)
    val TWO_PI = Math.PI * 2.0

    // Compute the start angle
    // The angle between (ux,uy) and the 0deg angle (1,0)
    n = sqrt(ux * ux + uy * uy)
    p = ux
    sign = if (uy < 0) -1.0 else 1.0

    var angleStart: Double = sign * acos(p / n) // No need for checkedArcCos() here. (p >= n) should always be true.

    // Compute the angle extent
    n = sqrt((ux * ux + uy * uy) * (vx * vx + vy * vy))
    p = ux * vx + uy * vy
    sign = if (ux * vy - uy * vx < 0) -1.0 else 1.0

    var angleExtent: Double = sign * checkedArcCos(p / n)

    // Catch angleExtents of 0, which will cause problems later in arcToBeziers
    if (angleExtent == 0.0)
    {
        pather.lineTo(x, y)
        return
    }
    if (!sweepFlag && angleExtent > 0)
    {
        angleExtent -= TWO_PI
    }
    else if (sweepFlag && angleExtent < 0)
    {
        angleExtent += TWO_PI
    }
    angleExtent %= TWO_PI
    angleStart %= TWO_PI

    // Many elliptical arc implementations including the Java2D and Android ones, only
    // support arcs that are axis aligned.  Therefore we need to substitute the arc
    // with bezier curves.  The following method call will generate the beziers for
    // a unit circle that covers the arc angles we want.
    val bezierPoints: FloatArray = arcToBeziers(angleStart, angleExtent)

    // Calculate a transformation matrix that will move and scale these bezier points to the correct location.
    val m = Matrix()
    m.postScale(rx, ry)
    m.postRotate(xAxisRotation) //todo is this correct
    m.postTranslate(cx.toFloat(), cy.toFloat())
    m.mapPoints(bezierPoints)

    // The last point in the bezier set should match exactly the last coord pair in the arc (ie: x,y). But
    // considering all the mathematical manipulation we have been doing, it is bound to be off by a tiny
    // fraction. Experiments show that it can be up to around 0.00002.  So why don't we just set it to
    // exactly what it ought to be.
    bezierPoints[bezierPoints.size - 2] = x
    bezierPoints[bezierPoints.size - 1] = y

    // Final step is to add the bezier curves to the path
    var i = 0
    while (i < bezierPoints.size)
    {
        pather.cubicTo(bezierPoints[i], bezierPoints[i + 1], bezierPoints[i + 2],
                       bezierPoints[i + 3], bezierPoints[i + 4], bezierPoints[i + 5])
        i += 6
    }
}