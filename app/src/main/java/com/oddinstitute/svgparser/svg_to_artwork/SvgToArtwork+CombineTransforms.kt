package com.oddinstitute.svgparser.svg_to_artwork

import com.oddinstitute.svgparser.svg_elements.SvgTransform

fun SvgToArtwork.combineTransforms(tagTransforms: ArrayList<SvgTransform>?,
                                   gTransforms: ArrayList<SvgTransform>?): ArrayList<SvgTransform>
{
    val outTransforms : ArrayList<SvgTransform> = arrayListOf()

    // let's add transforms backwards

    // first for the tag
    tagTransforms?.let {
        for (i in 0 until it.count())
            outTransforms.add(it[it.count() - i - 1]) //
    }

    // then for the group
    gTransforms?.let {
        for (i in 0 until it.count())
            outTransforms.add(it[it.count() - i - 1]) //
    }

    return outTransforms
}