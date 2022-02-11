package com.oddinstitute.svgparser.operators

import com.oddinstitute.svgparser.svg_elements.SvgMatrixTransform
import com.oddinstitute.svgparser.svg_elements.SvgTransform
import com.oddinstitute.svgparser.svg_elements.SvgTransformType


fun String.decodeTransformTranslate (): SvgTransform
{
    val translate : SvgTransform = SvgTransform(SvgTransformType.TRANSLATE)

    val translateText = this.replace("translate", "")
            .replace("(", "")
            .replace(")", "")
            .trimStart().trimEnd().trim() // we trim the end if there was extra spaces at the end of the tranasform xy
            .replace(" ", ",")

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



fun String.decodeTransformMatrix (): SvgTransform
{
    val trans : SvgTransform = SvgTransform(SvgTransformType.MATRIX)

    val scaleText = this.replace("matrix", "")
            .replace("(", "")
            .replace(")", "")
            .trimStart().trimEnd().trim() // we trim the end if there was extra spaces at the end of the tranasform xy
            .replace(" ", ",")

    val matrixComponents = scaleText.split(",")

    val a = matrixComponents[0].toFloat()
    val b = matrixComponents[1].toFloat()
    val c = matrixComponents[2].toFloat()
    val d = matrixComponents[3].toFloat()
    val e = matrixComponents[4].toFloat()
    val f = matrixComponents[5].toFloat()

    trans.matrix = SvgMatrixTransform(a, b, c, d, e, f)

    return trans
}

fun String.decodeTransformScale (): SvgTransform
{
    val scale : SvgTransform = SvgTransform(SvgTransformType.SCALE)

    val scaleText = this.replace("scale", "")
            .replace("(", "")
            .replace(")", "")
            .trimStart().trimEnd().trim() // we trim the end if there was extra spaces at the end of the tranasform xy
            .replace(" ", ",")

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
            .trimStart().trimEnd().trim() // we trim the end if there was extra spaces at the end of the tranasform xy
            .replace(" ", ",")

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

    var cleanString = this.cleanTags()


    var tempMinusString = ""
    for (i in cleanString.indices)
    {
        if (cleanString[i] == ' ')
        {
            tempMinusString += if (cleanString[i+1] == 's' || cleanString[i+1] == 't' || cleanString[i+1] == 'r')
                "|" // this is a space between different transformations
            else
                " " // this is the space between x, and y and r and ....
        }
        else
            tempMinusString += cleanString[i] // otherwise add the character
    }
    cleanString = tempMinusString // this is now the working string




    val transformComponents = cleanString.split("|")
//    we are splitin by space,
//    there is between x and y

    for (each in transformComponents)
    {
        when
        {
            each.contains("translate") -> transforms.add(each.decodeTransformTranslate())
            each.contains("rotate") -> transforms.add(each.decodeTransformRotate())
            each.contains("scale") -> transforms.add(each.decodeTransformScale())
            each.contains("matrix") -> transforms.add(each.decodeTransformMatrix())
        }
    }

    return transforms
}