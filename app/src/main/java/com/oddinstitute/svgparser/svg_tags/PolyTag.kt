package com.oddinstitute.svgparser.svg_tags

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
    override fun decode(): Polygon
    {
        val polygon: Polygon = Polygon()
        polygon.closed = closed
        val segments: ArrayList<Segment> = arrayListOf()

        parser.getAttributeValue(null, "points")?.let {

            val polygonsCleaned = it.cleanTags()
            val pointsComponents = polygonsCleaned.split(" ")


            for (i in 0 until pointsComponents.count())
            {
                val eachPoint = pointsComponents[i]
                val xyComponent = eachPoint.split(",")


                val segment = Segment()

                if (i == 0) segment.type = PathType.Move // the first point, we move there
                else segment.type = PathType.Line // otherwise, we draw a line


                segment.knot = PointF(xyComponent[0].toFloat(), xyComponent[1].toFloat())

                // at the end, add to the segments
                segments.add(segment)
            }


            // create a polygon where its shape node ha s apath value
            // built upon the recently found segments
            polygon.shapeNode.pathValue = PathValue(segments)
        }

        return polygon
    }
}