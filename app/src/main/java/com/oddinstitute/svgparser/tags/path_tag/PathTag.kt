package com.oddinstitute.svgparser.tags.path_tag

import com.oddinstitute.svgparser.tags.Tag
import com.oddinstitute.svgparser.operators.cleanTags
import com.oddinstitute.svgparser.polygon.Polygon
import org.xmlpull.v1.XmlPullParser

class PathTag(val parser: XmlPullParser) : Tag(parser)
{
    // PATH
    var d = ""


    init
    {
        // PATH
        parser.getAttributeValue(null, "d")?.let { d = it }
    }


    override fun toPolygon(): ArrayList<Polygon>
    {
        val polygons: ArrayList<Polygon> = arrayListOf()


        // the pathdata might make multiple polygons.
        // however, in order to get the fill type, we should ignore that
//        val cleanPathString = dString.cleanTags()
//                .replace("z", "Z|") // this is specific to paths. for paths, we break with "|".
//                .replace("Z", "Z|")
//
//        // HERE, WE BREAK THE string by |s
//        val separatePolyPiecesStr =
//            cleanPathString
//                    .split("|")
//                    .toTypedArray()
//
//
//
//        val polygonsStringArr = arrayListOf<String>()
//        for (any in separatePolyPiecesStr)
//            if (any.isNotEmpty())
//                polygonsStringArr.add(any)
//
//
//        // by now, we have all the separate pieces in the SAME path
//        // now, let's convert each into a polygon
//        for (pathPieceStr in polygonsStringArr)
//        {
//            // this string should become an arraylist of PathPieces
//            val polygon = dataToPolygon(pathPieceStr)
//            polygons.add(polygon)
//        }



        // we use this
        val singlePolyString = d.cleanTags()
                .replace("z", "")
                .replace("Z", "")
        val polygon = dataToPolygon(singlePolyString)
        polygons.add(polygon)


        return polygons
    }
}