package com.oddinstitute.svgparser

import com.oddinstitute.svgparser.tags.path_tag.PathTag
import kotlin.math.*

// rx ry x-axis-rotation large-arc-flag sweep-flag x y
data class SevenPieceArc(
    var rx: Float,
    var ry: Float,
    val xAxisRotation: Float,
    val largeArcFlag: Boolean,
    val sweepFlag: Boolean,
    val x2: Float,
    val y2: Float
)

fun PathTag.arcToBeziers(angleStart: Double, angleExtent: Double): FloatArray {
    val numPieces = ceil(abs(angleExtent) / (Math.PI * 2.0)).toInt() // (angleExtent / 90deg)
    val angleIncrement = angleExtent / numPieces

    // The length of each control point vector is given by the following formula.
    val controlLength = 4.0 / 3.0 * sin(angleIncrement / 2.0) / (1.0 + cos(angleIncrement / 2.0))

    val coords = FloatArray(numPieces * 6)

    var pos = 0
    for (i in 0 until numPieces) {
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
