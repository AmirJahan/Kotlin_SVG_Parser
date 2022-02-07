package com.oddinstitute.svgparser.svg_to_artwork

import com.oddinstitute.svgparser.svg_tags.StyleTag

fun SvgToArtwork.styleTag()
{
    val style = StyleTag ()

    curTagText?.let {
        // here, we have the style texts, we should make the actual styles

        // what are we gonna do with this
        this.styles = style.decodeStyle(it)
    }
}