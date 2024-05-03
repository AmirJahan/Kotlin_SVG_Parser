package com.oddinstitute.svgparser

import android.graphics.PointF

// cloning a path value
fun PathValue.clone(): PathValue {
    val outPathValue = PathValue()
    for (segment in this.segments) {
        val knot = PointF(segment.knot.x, segment.knot.y)

        var cp1: PointF? = null
        segment.cp1?.let {
            cp1 = PointF(it.x, it.y)
        }

        var cp2: PointF? = null
        segment.cp2?.let {
            cp2 = PointF(it.x, it.y)
        }

        val singlePieceKey = Segment(segment.type, knot, cp1, cp2)
        outPathValue.segments.add(singlePieceKey)
    }
    return outPathValue
}
