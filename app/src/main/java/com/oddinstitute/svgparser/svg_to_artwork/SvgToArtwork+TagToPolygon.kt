package com.oddinstitute.svgparser.svg_to_artwork

import android.graphics.PointF
import com.oddinstitute.svgparser.operators.roundTwoDecimals
import com.oddinstitute.svgparser.polygon.Polygon
import com.oddinstitute.svgparser.polygon.applySvgViewBox
import com.oddinstitute.svgparser.polygon.applySvgStyle
import com.oddinstitute.svgparser.polygon.applySvgTransforms
import com.oddinstitute.svgparser.svg_elements.SvgStyle
import com.oddinstitute.svgparser.svg_elements.SvgTransform
import com.oddinstitute.svgparser.svg_tags.Tag

fun SvgToArtwork.tagToPolygons(tag: Tag): ArrayList<Polygon>
{
    // Group Level, we make it non-optional (even it initiated, the content might be null which is ok
    var styleByGroup: SvgStyle = SvgStyle()
    var transformByGroup: ArrayList<SvgTransform>? = null


    if (currentGroups.count() == 1)
    {
        styleByGroup = consolidateStyles(currentGroups[0])

        currentGroups[0].transforms?.let {
            transformByGroup = it
        }
//        g.transforms?.let {
//            // group has transform
//            transformByGroup = it
//        }
    }
    else if (currentGroups.count() > 1)
    {
        styleByGroup = consolidateStyles(currentGroups[0])

        currentGroups[0].transforms?.let {
            transformByGroup = it
        }

        for (i in 1 until currentGroups.count() ) // from the first
        {
            val nextGroupStyle = consolidateStyles(currentGroups[i])
            styleByGroup = combinePrePostStyles(nextGroupStyle, styleByGroup)


            currentGroups[i].transforms?.let {
                transformByGroup = if (transformByGroup != null)
                    combineTransforms(it, transformByGroup)
                else
                    it
            }
        }
    }


//    activeGroup?.let { g ->
//        styleByGroup = consolidateStyles(g)
//
//        g.transforms?.let {
//            // group has transform
//            transformByGroup = it
//        }
//    }


    // Local Level, we make it non-optional (even it initiated, the content might be null which is ok
    val styleByElement = consolidateStyles(tag)
    var transformByElement: ArrayList<SvgTransform>? = null

    tag.transforms?.let {
        transformByElement = it
    }






    val polygons :ArrayList<Polygon> = tag.decode()

    // this is the final style of a tag by combining tag level and group level
    // tag comes first and then the group
    val theStyle: SvgStyle = combinePrePostStyles(styleByGroup, styleByElement)

    for (polygon in polygons)
        polygon.applySvgStyle(theStyle)




//    all styles are consolidated properly
//    we are now working on the transforms
    val allTransforms: ArrayList<SvgTransform> = combineTransforms(transformByElement, transformByGroup)
    for (polygon in polygons)
        polygon.applySvgTransforms (allTransforms)



    // this is after everything else has been applied
    for (polygon in polygons)
    {
        polygon.applySvgViewBox(scaleFactor, viewBoxOffset)
        polygon.roundTwoDecimals()
    }



    return polygons
}

fun Polygon.roundTwoDecimals ()
{

    for (seg in this.shapeNode.pathValue.segments)
    {
        seg.knot.roundTwoDecimals()
        seg.cp1?.roundTwoDecimals()
        seg.cp2?.roundTwoDecimals()
    }
}

fun PointF.roundTwoDecimals()
{
    this.x = this.x.roundTwoDecimals()
    this.y = this.y.roundTwoDecimals()
}
