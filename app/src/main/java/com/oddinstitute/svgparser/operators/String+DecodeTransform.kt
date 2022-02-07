package com.oddinstitute.svgparser.operators

import com.oddinstitute.svgparser.svg_elements.SvgTransform
import com.oddinstitute.svgparser.svg_elements.SvgTransformType


fun String.decodeTransformTranslate (): SvgTransform
{
    val translate : SvgTransform = SvgTransform(SvgTransformType.TRANSLATE)

    val translateText = this.replace("translate", "")
            .replace("(", "")
            .replace(")", "")

    val translateComponents = translateText.split(",")

    if (translateComponents.count() == 1) // just X
        translate.x = translateComponents[0].toFloat()
    else if (translateComponents.count() == 2)
    {
        translate.x = translateComponents[0].toFloat()
        translate.y = translateComponents[1].toFloat()
    }

    return translate
}


fun String.decodeTransformScale (): SvgTransform
{
    val scale : SvgTransform = SvgTransform(SvgTransformType.SCALE)

    val scaleText = this.replace("scale", "")
            .replace("(", "")
            .replace(")", "")

    val scaleComponents = scaleText.split(",")

    if (scaleComponents.count() == 1) // just X -> Y is X
    {
        scale.x = scaleComponents[0].toFloat()
        scale.y = scale.x
    }
    else if (scaleComponents.count() == 2)
    {
        scale.x = scaleComponents[0].toFloat()
        scale.y = scaleComponents[1].toFloat()
    }

    return scale
}

fun String.decodeTransformRotate (): SvgTransform
{
    val rotate : SvgTransform = SvgTransform(SvgTransformType.ROTATE)

    val rotateText = this.replace("rotate", "")
            .replace("(", "")
            .replace(")", "")

    val rotateComponents = rotateText.split(",")

    if (rotateComponents.count() == 1) // just angle
        rotate.angle = rotateComponents[0].toFloat()
    else if (rotateComponents.count() == 3)
    {
        rotate.angle = rotateComponents[0].toFloat()
        rotate.cx = rotateComponents[1].toFloat()
        rotate.cy = rotateComponents[2].toFloat()
    }

    return rotate
}


fun String.decodeTransform (): ArrayList<SvgTransform>
{
    val transforms : ArrayList<SvgTransform> = arrayListOf()

    val cleanString = this.cleanTags()

    val transformComponents = cleanString.split(" ")

    for (each in transformComponents)
    {
        when
        {
            each.contains("translate") -> transforms.add(each.decodeTransformTranslate())
            each.contains("rotate") -> transforms.add(each.decodeTransformRotate())
            each.contains("scale") -> transforms.add(each.decodeTransformScale())
        }
    }

    return transforms
}