package com.oddinstitute.svgparser.polygon

import android.graphics.PointF
import com.oddinstitute.svgparser.Segment

fun Polygon.applySvgScale(scaleFactor: Float)
{
    val newSegments: ArrayList<Segment> = arrayListOf()

    for (segment in this.shapeNode.pathValue.segments)
    {
        val seg = Segment(segment.type)
        seg.knot = segment.knot * scaleFactor

        segment.cp1?.let {
            seg.cp1 = it * scaleFactor
        }

        segment.cp2?.let {
            seg.cp2 = it * scaleFactor
        }

        newSegments.add(seg)
    }

    this.shapeNode.pathValue.segments = newSegments

}


// todo remove. This code exists in main
operator fun PointF.times(other: Float) = PointF(this.x * other,
                                                 this.y * other)