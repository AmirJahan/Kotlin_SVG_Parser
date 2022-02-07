package com.oddinstitute.svgparser

// rx ry x-axis-rotation large-arc-flag sweep-flag x y
data class SevenPieceArc(var rx: Float,
                          var ry: Float,
                          val xAxisRotation: Float,
                          val largeArcFlag: Boolean,
                          val sweepFlag: Boolean,
                          val x2: Float,
                          val y2: Float)
{
    fun toSegment (): Segment
    {
        val segment = Segment(PathType.Arc)

        // todo




        return segment
    }
}