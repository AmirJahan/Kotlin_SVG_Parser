package com.oddinstitute.svgparser.polygon

import android.graphics.PointF
import androidx.core.graphics.minus
import com.oddinstitute.svgparser.Segment

fun Polygon.applySvgViewBox(scaleFactor: Float, offset: PointF)
{
//    val newSegments: ArrayList<Segment> = arrayListOf()

    this.shapeNode.strokeWidth *= scaleFactor
    this.dashArray?.let {  this.dashArray = it * scaleFactor }


    for (segment in this.shapeNode.pathValue.segments)
    {
        segment.knot.offset(-offset.x, -offset.y)
        segment.cp1?.offset(-offset.x, -offset.y)
        segment.cp2?.offset(-offset.x, -offset.y)


        segment.knot.scale(scaleFactor, PointF())
        segment.cp1?.scale(scaleFactor, PointF())
        segment.cp2?.scale(scaleFactor, PointF())
    }
}


// todo remove. This code exists in main
operator fun PointF.times(other: Float) = PointF(this.x * other,
                                                 this.y * other)