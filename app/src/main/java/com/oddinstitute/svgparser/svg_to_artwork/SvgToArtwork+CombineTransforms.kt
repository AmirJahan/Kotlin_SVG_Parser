package com.oddinstitute.svgparser.svg_to_artwork

import com.oddinstitute.svgparser.svg_elements.SvgTransform

// here, we read inner and outer Transforms. Inner gets priority
fun SvgToArtwork.combineTransforms(innerTransforms: ArrayList<SvgTransform>?,
                                   outerTransforms: ArrayList<SvgTransform>?): ArrayList<SvgTransform>
{
    val resTransforms : ArrayList<SvgTransform> = arrayListOf()

    // let's add transforms backwards
    // then for the group
    outerTransforms?.let {
//        for (i in 0 until it.count())
//            resTransforms.add(it[it.count() - i - 1]) //

        resTransforms.addAll(it)
    }


    // first for the inner
    innerTransforms?.let {
//        for (i in 0 until it.count())
//            resTransforms.add(it[it.count() - i - 1]) //


        resTransforms.addAll(it)

    }


    return resTransforms
}