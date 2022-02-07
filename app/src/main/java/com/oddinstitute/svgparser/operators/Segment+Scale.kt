package com.oddinstitute.svgparser

import android.graphics.PointF
import com.oddinstitute.svgparser.polygon.scale

// used for applying SVG Transformations
fun Segment.scale(scaleFactor: PointF, pivot: PointF)
{
    this.knot.scale(scaleFactor, pivot)
    this.cp1?.scale(scaleFactor, pivot)
    this.cp2?.scale(scaleFactor, pivot)
}
