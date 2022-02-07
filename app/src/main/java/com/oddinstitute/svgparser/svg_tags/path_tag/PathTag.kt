package com.oddinstitute.svgparser.svg_tags.path_tag

import com.oddinstitute.svgparser.svg_tags.Tag
import com.oddinstitute.svgparser.operators.cleanTags
import com.oddinstitute.svgparser.polygon.Polygon
import org.xmlpull.v1.XmlPullParser

class PathTag(val parser: XmlPullParser) : Tag(parser)
{

    override fun decode(): Polygon
    {
        val polygons: ArrayList<Polygon> = arrayListOf()
        var dString: String = ""

        parser.getAttributeValue(null, "d")?.let {
            dString = it
        }

        // the pathdata might make multiple polygons.
        val cleanPathString = dString.cleanTags()


        // HERE, WE BREAK THE string by Zs
        val separatePolyPiecesStr =
            cleanPathString
                    .split("(?<=Z)".toRegex())
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


        // here when we have multiple separate polygons
        // we still merge them together
        // I think this has been the intention of SVG
        // if you have a "z" in the middle of path d string
        // it means the polygon closes, yet another part of the same polygon begins
        // for that:

        val outPolygon = polygons.first()

        if (polygons.count() > 1)
            for (i in 1 until polygons.count()) // from 2nd onwards
                outPolygon.shapeNode.pathValue.segments.addAll(polygons[i].shapeNode.pathValue.segments)

        return outPolygon
    }

}





