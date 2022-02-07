package com.oddinstitute.svgparser.svg_to_artwork

import com.oddinstitute.svgparser.svg_elements.SvgStyle
import com.oddinstitute.svgparser.svg_tags.Tag


// this function takes three possible styles from a tag and compabines them
// assuming that the highest priority is the Local level (e.g., fill="red")
// the second priority is the style (e.g., style="fill:red")
// and last priority is the class (e.g., class="myStyle")



// todo
/*
when we apply a class to a path,
        it should not override group
        I think it currently does override group

 */

fun SvgToArtwork.combineStyles(tag: Tag): SvgStyle
{
    var resClass: SvgStyle = SvgStyle() // if there's a class
    var resStyle: SvgStyle = SvgStyle() // if there's a style
    val resInline: SvgStyle = SvgStyle() // if there are inline values


    tag.svgClass?.let { svgClass ->
        // there is a class
        // let's find out the style
        this.styles?.let { styles ->
            styles[svgClass]?.let {
                resClass = it
            }
        }
    }

    tag.style?.let {
        resStyle = it
    }

    tag.fill?.let { resInline.fill = it }
    tag.stroke?.let { resInline.stroke = it }
    tag.strokeWidth?.let { resInline.strokeWidth = it }
    tag.fillRule?.let { resInline.fillRule = it }
    tag.clipRule?.let {
        resInline.clipRule = it }
    tag.strokeLineCap?.let { resInline.strokeLineCap = it }



    // NOW, LET'S COMBINE
    // inline has -> direct
    // inline doesn't, style has
    // inline doesn't, style doesn't, class has

    val outStyle: SvgStyle = SvgStyle()

    when
    {
        resInline.fill != null -> outStyle.fill = resInline.fill
        resStyle.fill != null -> outStyle.fill = resStyle.fill
        resClass.fill != null -> outStyle.fill = resClass.fill
    }

    when
    {
        resInline.stroke != null -> outStyle.stroke = resInline.stroke
        resStyle.stroke != null -> outStyle.stroke = resStyle.stroke
        resClass.stroke != null -> outStyle.stroke = resClass.stroke
    }

    when
    {
        resInline.strokeWidth != null -> outStyle.strokeWidth = resInline.strokeWidth
        resStyle.strokeWidth != null -> outStyle.strokeWidth = resStyle.strokeWidth
        resClass.strokeWidth != null -> outStyle.strokeWidth = resClass.strokeWidth
    }

    when
    {
        resInline.fillRule != null -> outStyle.fillRule = resInline.fillRule
        resStyle.fillRule != null -> outStyle.fillRule = resStyle.fillRule
        resClass.fillRule != null -> outStyle.fillRule = resClass.fillRule
    }

    when
    {
        resInline.clipRule != null -> outStyle.clipRule = resInline.clipRule
        resStyle.clipRule != null -> outStyle.clipRule = resStyle.clipRule
        resClass.clipRule != null -> outStyle.clipRule = resClass.clipRule
    }

    when
    {
        resInline.strokeLineCap != null -> outStyle.strokeLineCap = resInline.strokeLineCap
        resStyle.strokeLineCap != null -> outStyle.strokeLineCap = resStyle.strokeLineCap
        resClass.strokeLineCap != null -> outStyle.strokeLineCap = resClass.strokeLineCap
    }

    return outStyle
}

