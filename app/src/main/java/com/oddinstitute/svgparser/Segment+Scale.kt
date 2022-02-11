package com.oddinstitute.svgparser

import android.graphics.PointF
import com.oddinstitute.svgparser.polygon.scale
import com.oddinstitute.svgparser.polygon.scaleThis

// used for applying SVG Transformations
fun Segment.scale(scaleFactor: PointF, pivot: PointF)
{
    this.knot.scaleThis(scaleFactor, pivot)
    this.cp1?.scaleThis(scaleFactor, pivot)
    this.cp2?.scaleThis(scaleFactor, pivot)
}
