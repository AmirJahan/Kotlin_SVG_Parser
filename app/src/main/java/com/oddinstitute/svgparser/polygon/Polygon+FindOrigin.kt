package com.oddinstitute.svgparser.polygon

import android.graphics.PointF


fun Polygon.findOrigin(): PointF
{
    var origX: Float = Float.MAX_VALUE
    var origY: Float = Float.MAX_VALUE

    for (seg in this.shapeNode.pathValue.segments)
    {
        if (seg.knot.x < origX)
            origX = seg.knot.x

        if (seg.knot.y < origY)
            origY = seg.knot.y


        seg.cp1?.let {
            if (it.x < origX)
                origX = it.x

            if (it.y < origY)
                origY = it.y
        }

        seg.cp2?.let {
            if (it.x < origX)
                origX = it.x

            if (it.y < origY)
                origY = it.y
        }
    }

    return PointF(origX, origY)
}