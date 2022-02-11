package com.oddinstitute.svgparser.svg_elements



data class SvgMatrixTransform(val a: Float,
                              val b: Float,
                              val c: Float,
                              val d: Float,
                              val e: Float,
                              val f: Float)
{
    fun clone (): SvgMatrixTransform
    {
        return SvgMatrixTransform (a, b, c, d, e, f)
    }
}


class SvgTransform(val type: SvgTransformType)
{
    // we use the same structure for translate, rotate and scale
    // for that, all values are optional
    var x: Float = 0f
    var y: Float = 0f

    var angle: Float = 0f
    var cx: Float = 0f
    var cy: Float = 0f

    var matrix: SvgMatrixTransform? = null




    fun clone (): SvgTransform
    {
        val svgTransform = SvgTransform(this.type)
        svgTransform.x = this.x
        svgTransform.y = this.y
        svgTransform.angle = this.angle
        svgTransform.cx = this.cx
        svgTransform.cy = this.cy

        svgTransform.matrix = this.matrix?.clone()




        return svgTransform
    }
}