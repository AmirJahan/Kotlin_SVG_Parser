package com.oddinstitute.svgparser.operators

// this cleaning can only be used for Style, d of Path and polylines
fun String.cleanTags(): String
{
    val newString = this.trimIndent()
            .trimStart()
            .trimEnd()
            .trim()
            .replace("\n", " ")

    for (i in 0..9)
    {
        newString.replace("-$i", ",-$i")
    }

    newString
            .replace("\\s+".toRegex(), " ")
    newString.replace(", ", ",")


    val delimiters: Array<Char> =
        arrayOf('M', 'm', 'L', 'l', 'C', 'c', 'S', 's', 'Q', 'q', 'T', 't', 'H', 'h', 'V', 'v', 'a', 'A')

    for (char in delimiters)
        newString.replace("$char,", "$char")


    newString.replace(" ,-", " -") // for poly lines
    newString.replace("l,-r", "l-r") // for styles
    newString.replace("p,-r", "p-r") // for styles clip rule
    newString.replace("e,-l", "e-l") // for styles stroke - linecap
    newString.replace("e,-w", "e-w") // for styles stroke - width


    return newString
}