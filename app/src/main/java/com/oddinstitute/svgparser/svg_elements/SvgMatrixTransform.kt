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
