package com.oddinstitute.svgparser.svg_elements

import android.graphics.Path

enum class SvgFillRule(val text: String)
{
    EVENODD("evenodd"),
    NONZERO("nonzero");


    fun toRaw() = enumToRaw[this]
    fun toType() = enumToType[this]

    companion object
    {
        val rawToEnum = mapOf("evenodd" to EVENODD,
                              "nonzero" to NONZERO)
        val enumToRaw = rawToEnum.entries.associate { (k, v) -> v to k }
        fun ofRaw(raw: String): SvgFillRule? = rawToEnum[raw]


        val typeToEnum = mapOf<Path.FillType, SvgFillRule>(Path.FillType.EVEN_ODD to EVENODD,
                                                           Path.FillType.INVERSE_EVEN_ODD to NONZERO)
        val enumToType = typeToEnum.entries.associate { (k, v) -> v to k }



    }
}
