package com.oddinstitute.svgparser.svg_to_artwork

import com.oddinstitute.svgparser.polygon.Polygon
import com.oddinstitute.svgparser.polygon.applySvgScale
import com.oddinstitute.svgparser.polygon.applySvgStyleT
import com.oddinstitute.svgparser.polygon.applySvgTransforms
import com.oddinstitute.svgparser.svg_elements.SvgStyle
import com.oddinstitute.svgparser.svg_elements.SvgTransform
import com.oddinstitute.svgparser.svg_tags.Tag

fun SvgToArtwork.tagToPolygon(tag: Tag): Polygon
{
    // Group Level, we make it non-optional (even it initiated, the content might be null which is ok
    var styleByGroup: SvgStyle = SvgStyle()
    var transformByGroup: ArrayList<SvgTransform>? = null

    activeGroup?.let { g ->
        styleByGroup = combineStyles(g)

        g.transforms?.let {
            // group has transform
            transformByGroup = it
        }
    }


    // Local Level, we make it non-optional (even it initiated, the content might be null which is ok
    val styleByTag = combineStyles(tag)
    var transformByTag: ArrayList<SvgTransform>? = null

    tag.transforms?.let {
        transformByTag = it
    }


    // decode the tag into an actual polygon
    val polygon: Polygon = tag.decode()

    // this is the final style of a tag by combining tag level and group level
    // tag comes first and then the group
    val theStyle: SvgStyle = combineGroupTagStyles(styleByTag, styleByGroup)
    polygon.applySvgStyleT(theStyle)


    val allTransforms: ArrayList<SvgTransform> = combineTransforms(transformByTag, transformByGroup)
    polygon.applySvgTransforms (allTransforms)

    // this is after everything else has been applied
    polygon.applySvgScale(scaleFactor)


    return polygon
}