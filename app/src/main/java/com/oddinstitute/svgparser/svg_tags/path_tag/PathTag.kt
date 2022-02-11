package com.oddinstitute.svgparser.svg_tags.path_tag

import com.oddinstitute.svgparser.svg_tags.Tag
import com.oddinstitute.svgparser.operators.cleanTags
import com.oddinstitute.svgparser.polygon.Polygon
import org.xmlpull.v1.XmlPullParser

class PathTag(val parser: XmlPullParser) : Tag(parser)
{
    override fun decode(): ArrayList<Polygon>
    {
        val polygons: ArrayList<Polygon> = arrayListOf()
        var dString: String = ""

        parser.getAttributeValue(null, "d")?.let {
            dString = it
        }

        // the pathdata might make multiple polygons.
        val cleanPathString = dString.cleanTags()
                .replace("z", "Z|") // this is specific to paths. for paths, we break with "|".
                .replace("Z", "Z|")

        // HERE, WE BREAK THE string by |s
        val separatePolyPiecesStr =
            cleanPathString
                    .split("|")
                    .toTypedArray()

        val polygonsStringArr = arrayListOf<String>()
        for (any in separatePolyPiecesStr)
            if (any.isNotEmpty())
                polygonsStringArr.add(any)


        // by now, we have all the separate pieces in the SAME path
        // now, let's convert each into a polygon
        for (pathPieceStr in polygonsStringArr)
        {
            // this string should become an arraylist of PathPieces
            val polygon = dataToPolygon(pathPieceStr)
            polygons.add(polygon)
        }

        return polygons
    }
}