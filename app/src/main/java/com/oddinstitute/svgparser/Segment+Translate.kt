package com.oddinstitute.svgparser

import android.graphics.PointF
import com.oddinstitute.svgparser.polygon.translate

// used for applying SVG Transformations
fun Segment.translate(offset: PointF) {
    this.knot.translate(offset)
    this.cp1?.translate(offset)
    this.cp2?.translate(offset)
}
