package com.oddinstitute.svgparser.svg_to_artwork

import com.oddinstitute.svgparser.svg_elements.SvgStyle
import com.oddinstitute.svgparser.svg_tags.Tag


// this function takes three possible levels of elements, style and class for any tag
// and returns the consolidated one based on the following:
// Style Overrides class
// Class overrides individual elements

fun SvgToArtwork.consolidateStyles(tag: Tag): SvgStyle
{
    var styleLevel: SvgStyle = SvgStyle() // if there's a style
    var classLevel: SvgStyle = SvgStyle() // if there's a class
    val elemeLevel: SvgStyle = SvgStyle() // if there are inline values


    tag.svgClass?.let { svgClass ->
        // there is a class
        // let's find out the style
        this.styles?.let { styles ->
            styles[svgClass]?.let {
                classLevel = it
            }
        }
    }

    tag.style?.let {
        styleLevel = it
    }

    tag.fill?.let { elemeLevel.fill = it }
    tag.stroke?.let { elemeLevel.stroke = it }
    tag.strokeWidth?.let { elemeLevel.strokeWidth = it }
    tag.fillRule?.let { elemeLevel.fillRule = it }
    tag.clipRule?.let { elemeLevel.clipRule = it }
    tag.strokeLineCap?.let { elemeLevel.strokeLineCap = it }
    tag.strokeDashArray?.let { elemeLevel.strokeDashArray = it }
    tag.strokeLineJoin?.let { elemeLevel.strokeLineJoin = it }



    // NOW, LET'S COMBINE
    // inline has -> direct
    // inline doesn't, style has
    // inline doesn't, style doesn't, class has

    val outStyle: SvgStyle = SvgStyle()


    // we call these IN Order. So, if there is a class, it'll override elements
    // and if there is a style, it'll override everything

    elemeLevel.fill?.let { outStyle.fill = it }
    classLevel.fill?.let { outStyle.fill = it }
    styleLevel.fill?.let { outStyle.fill = it }


    elemeLevel.stroke?.let { outStyle.stroke = it }
    classLevel.stroke?.let { outStyle.stroke = it }
    styleLevel.stroke?.let { outStyle.stroke = it }

    elemeLevel.strokeWidth?.let { outStyle.strokeWidth = it }
    classLevel.strokeWidth?.let { outStyle.strokeWidth = it }
    styleLevel.strokeWidth?.let { outStyle.strokeWidth = it }


    elemeLevel.fillRule?.let { outStyle.fillRule = it }
    classLevel.fillRule?.let { outStyle.fillRule = it }
    styleLevel.fillRule?.let { outStyle.fillRule = it }


    elemeLevel.clipRule?.let { outStyle.clipRule = it }
    classLevel.clipRule?.let { outStyle.clipRule = it }
    styleLevel.clipRule?.let { outStyle.clipRule = it }



    elemeLevel.strokeLineCap?.let { outStyle.strokeLineCap = it }
    classLevel.strokeLineCap?.let { outStyle.strokeLineCap = it }
    styleLevel.strokeLineCap?.let { outStyle.strokeLineCap = it }

    elemeLevel.strokeLineJoin?.let { outStyle.strokeLineJoin = it }
    classLevel.strokeLineJoin?.let { outStyle.strokeLineJoin = it }
    styleLevel.strokeLineJoin?.let { outStyle.strokeLineJoin = it }


    elemeLevel.strokeDashArray?.let { outStyle.strokeDashArray = it }
    classLevel.strokeDashArray?.let { outStyle.strokeDashArray = it }
    styleLevel.strokeDashArray?.let { outStyle.strokeDashArray = it }


    return outStyle
}

