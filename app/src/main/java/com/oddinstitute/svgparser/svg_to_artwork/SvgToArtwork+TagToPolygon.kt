package com.oddinstitute.svgparser.svg_to_artwork

import com.oddinstitute.svgparser.polygon.Polygon
import com.oddinstitute.svgparser.polygon.applySvgViewBox
import com.oddinstitute.svgparser.polygon.applySvgStyleT
import com.oddinstitute.svgparser.polygon.applySvgTransforms
import com.oddinstitute.svgparser.svg_elements.SvgStyle
import com.oddinstitute.svgparser.svg_elements.SvgTransform
import com.oddinstitute.svgparser.svg_tags.Tag

fun SvgToArtwork.tagToPolygons(tag: Tag): ArrayList<Polygon>
{
    // Group Level, we make it non-optional (even it initiated, the content might be null which is ok
    var styleByGroup: SvgStyle = SvgStyle()
    var transformByGroup: ArrayList<SvgTransform>? = null



    activeGroup?.let { g ->
        styleByGroup = consolidateStyles(g)

        g.transforms?.let {
            // group has transform
            transformByGroup = it
        }
    }


    // Local Level, we make it non-optional (even it initiated, the content might be null which is ok
    val styleByElement = consolidateStyles(tag)
    var transformByElement: ArrayList<SvgTransform>? = null

    tag.transforms?.let {
        transformByElement = it
    }






    val polygons :ArrayList<Polygon> = tag.decode()

    // this is the final style of a tag by combining tag level and group level
    // tag comes first and then the group
    val theStyle: SvgStyle = combineGroupAndTagStyles(styleByElement, styleByGroup)

    for (polygon in polygons)
        polygon.applySvgStyleT(theStyle)




//    all styles are consolidate dproperly
//    we are now working on the transforms
    val allTransforms: ArrayList<SvgTransform> = combineTransforms(transformByElement, transformByGroup)
    for (polygon in polygons)
        polygon.applySvgTransforms (allTransforms)



    // this is after everything else has been applied
    for (polygon in polygons)
        polygon.applySvgViewBox(scaleFactor, viewBoxOffset)


    return polygons
}