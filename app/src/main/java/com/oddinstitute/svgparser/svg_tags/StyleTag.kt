package com.oddinstitute.svgparser.svg_tags

import com.oddinstitute.svgparser.*
import com.oddinstitute.svgparser.operators.cleanTags
import com.oddinstitute.svgparser.svg_elements.*


class StyleTag : Tag()
{
    // this tag reads the styles
    // associated with a SVG file


    fun decodeStyle(styleText: String): HashMap<String, SvgStyle>
    {
        val stylesMap: HashMap<String, SvgStyle> = hashMapOf()


        // this section replaces the period at the beginning of styles with a "^", so
        // we can use that to split the style
        var styleString: String = ""
        for (i in 0 until styleText.count())
        {
            var char = styleText[i] // this character

            if (char == '.') // is a period
            {
                if (i + 1 < styleText.count()) // there's next
                {
                    if (!styleText[i + 1].isDigit()) // if the next one is not digit
                    {
                        // this is the starting ., let's replace
                        char = '^'
                    }
                }
            }

            styleString += char
        }


        val pieces =
            styleString.split("^")
                    .toTypedArray()

        for (any in pieces)
        {
            val cleanPiece = any.cleanTags()

            if (cleanPiece == "")
                continue


            val content =
                cleanPiece.split("{")
                        .toTypedArray()

            val name = content[0]


            val values: String = content[1].replace("}", "")


            // here, we separate the value decoder, because some elements might have
            // inline styles. We need the function for those as well.
            val svgStyle: SvgStyle = values.styleValueDecoder()


            stylesMap[name] = svgStyle
        }


        return stylesMap
    }

}

// this function receives a style string and decodes it to actual SvgStyles
fun String.styleValueDecoder(): SvgStyle
{
    val svgStyle: SvgStyle = SvgStyle()

    val components = this.split(";")

    for (each in components)
    {
        val keyVal = each.split(":")
        if (keyVal.count() < 2)
            continue

        val key = keyVal[0]
        val value = keyVal[1]

        if (key == "fill") svgStyle.fill = SvgColor.ofRaw(value)
        if (key == "stroke") svgStyle.stroke = SvgColor.ofRaw(value)
        if (key == "stroke-width") svgStyle.strokeWidth = value.toFloat()
        if (key == "fill-rule") svgStyle.fillRule = SvgFillRule.ofRaw(value) // gets from enum
        if (key == "clip-rule") svgStyle.clipRule = SvgClipRule.ofRaw(value)
        if (key == "stroke-linecap") svgStyle.strokeLineCap = SvgLinecap.ofRaw(value)
    }

    return svgStyle
}
