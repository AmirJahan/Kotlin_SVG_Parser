package com.oddinstitute.svgparser.shapes

import android.graphics.PointF


open class Object
{

}


class Rectangle: Object ()
{
    var p: PointF = PointF() // position
    var s: PointF = PointF() // size

    var r: Float = 0f // rounded corner
}