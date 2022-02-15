package com.oddinstitute.svgparser.svg_tags.path_tag

import android.graphics.PointF
import com.oddinstitute.svgparser.PathValue
import com.oddinstitute.svgparser.polygon.Polygon
import com.oddinstitute.svgparser.clone


fun PathTag.dataToPolygon(singlePathPieceString: String): Polygon
{
    val delimiters: Array<Char> =
        arrayOf('M', 'm', 'L', 'l', 'C', 'c', 'S', 's', 'Q', 'q', 'T', 't', 'H', 'h', 'V', 'v', 'a', 'A')


    val thisPathPolygon = Polygon()

    var workingString = singlePathPieceString

    // we make them open by default.
    // for some reason in the init, i have it as true
    // not sure why
    thisPathPolygon.closed = false

    if (workingString.contains("Z"))
    {
        thisPathPolygon.closed = true
        workingString = workingString.replace("Z", "")
    }

    val piecesStringArr = arrayListOf(workingString)

    for (del in delimiters)
    {
        val temp = ArrayList<String>()
        temp.addAll(piecesStringArr.filterNotNull())
        piecesStringArr.clear()

        for (any in temp)
        {
            val thesePieces =
                any.split("(?=$del)".toRegex())
                        .toTypedArray()

            for (each in thesePieces)
                if (each.isNotEmpty())
                    piecesStringArr.add("$each")
        }
    }


    val pathValue = PathValue()
    var tempCurPoint = PointF() // is the previous location

    for (piece: String in piecesStringArr)
    {
        when (piece[0])
        {
            'M', 'm' -> pathValue.segments.add(movePiece(piece, tempCurPoint))
            'L', 'l', 'v', 'V', 'h', 'H' -> pathValue.segments.addAll(linePiece(piece, tempCurPoint) )// not relative
            'C', 'c' -> pathValue.segments.addAll(curvePiece(piece, tempCurPoint))
            'S', 's' -> pathValue.segments.addAll(smoothCurvePiece(piece, tempCurPoint, pathValue.segments.last()))
            'Q', 'q' -> pathValue.segments.addAll(quadPiece(piece, tempCurPoint))
            'T', 't' -> pathValue.segments.add(smoothQuadPiece(piece, tempCurPoint, pathValue.segments.last()))
            'a', 'A' -> pathValue.segments.addAll(arcPieces(piece, tempCurPoint)) // many pieces
        }

        if (pathValue.segments.count() != 0)
            tempCurPoint = pathValue.segments.last().knot
    }

    // this is the pathValue that determines the shape of the polygon
    thisPathPolygon.shapeNode.pathValue = pathValue.clone()

    // this is the path value that maintains a copy of the original shape
    thisPathPolygon.shapeNode.pathValueOrigin = pathValue.clone()

    return thisPathPolygon
}







