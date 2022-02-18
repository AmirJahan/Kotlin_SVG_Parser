package com.oddinstitute.svgparser.tags

import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.PathValue
import com.oddinstitute.svgparser.polygon.Polygon
import com.oddinstitute.svgparser.Segment
import com.oddinstitute.svgparser.operators.cleanTags
import org.xmlpull.v1.XmlPullParser

// if it is polygon, it will be closed
class PolyTag(val parser: XmlPullParser, var closed: Boolean = false) : Tag(parser)
{
    // POLYGON
    var points = ""

    init
    {
        // POLYGON
        parser.getAttributeValue(null, "points")?.let { points = it }
    }

    override fun toPolygon(): ArrayList<Polygon>
    {
        // for polygon, we assume the values are always paired
        // like x,y x2,y2 x3,y3 and so on
        // split character is "space"

        val polygon: Polygon = Polygon()
        polygon.closed = closed
        val segments: ArrayList<Segment> = arrayListOf()

        var polygonsCleaned = points.cleanTags()

        // it turns our polylines can have either SPACES or COMMAS to separate pairs
        // for instance 20,20,40,25,60,40 is the same as 20,20 40,25 60,40
        // to eliminate this problem, we replace SPACES with COMMAS

        polygonsCleaned = polygonsCleaned.replace(" ", ",")
        val pointsComponents = polygonsCleaned.split(",")

        // we go through these every 2
        for (i in 0 until pointsComponents.count() step 2)
        {
            val xValue = pointsComponents[i].toFloat()
            val yValue = pointsComponents[i + 1].toFloat()

            val segment = Segment()

            if (i == 0) segment.type = PathType.Move // the first point, we move there
            else segment.type = PathType.Line // otherwise, we draw a line


            segment.knot = PointF(xValue, yValue)

            // at the end, add to the segments
            segments.add(segment)
        }


        // create a polygon where its shape node ha s apath value
        // built upon the recently found segments
        polygon.shapeNode.pathValue = PathValue(segments)


        return arrayListOf(polygon)
    }
}