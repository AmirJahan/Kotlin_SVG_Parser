package com.oddinstitute.svgparser.tags

import android.graphics.PointF
import com.oddinstitute.svgparser.*
import com.oddinstitute.svgparser.polygon.Polygon
import org.xmlpull.v1.XmlPullParser

class RectTag(val parser: XmlPullParser): Tag(parser) {
    // RECT
    var x = 0f
    var y = 0f
    var width = 0f
    var height = 0f

    // OVAL AND RECT
    var rx = 0f
    var ry = 0f

    init {
        // RECT
        parser.getAttributeValue(null, "x")?.let { x = it.toFloat() }
        parser.getAttributeValue(null, "y")?.let { y = it.toFloat() }
        parser.getAttributeValue(null, "width")?.let { width = it.toFloat() }
        parser.getAttributeValue(null, "height")?.let { height = it.toFloat() }

        // OVAL AND RECT
        parser.getAttributeValue(null, "rx")?.let { rx = it.toFloat() }
        parser.getAttributeValue(null, "ry")?.let { ry = it.toFloat() }
    }

    override fun toPolygon(): ArrayList<Polygon> {
        val segments: ArrayList<Segment> = arrayListOf()

        if (rx != 0.0f && ry == 0.0f) ry = rx
        if (ry != 0.0f && rx == 0.0f) rx = ry

//        segments.addAll(roundRectToPath(x1, y1, width, height, rx, ry))

        val moveSeg = Segment(PathType.Move, PointF(x, y + ry))
        segments.add(moveSeg)

        val topLeftArc = SevenPieceArc(rx, ry, 0f,
            largeArcFlag = false,
            sweepFlag = true,
            x2 = x + rx,
            y2 = y)

        val topLeftSegs = topLeftArc.toSegmentsObjCMethod(PointF(x, y + ry))
        segments.addAll(topLeftSegs)

        val topLine = Segment(PathType.Line)
        topLine.knot = PointF(x + width - rx, y)
        segments.add(topLine)

        val topRightArc = SevenPieceArc(rx, ry, 0f,
            largeArcFlag = false,
            sweepFlag = true,
            x2 = x + width,
            y2 = y + ry)
        val topRightSegs = topRightArc.toSegmentsObjCMethod(PointF(x + width - rx, y))
        segments.addAll(topRightSegs)

        val rightLine = Segment(PathType.Line)
        rightLine.knot = PointF(x + width, y + height - ry)
        segments.add(rightLine)

        val bottomRightArc = SevenPieceArc(rx, ry, 0f,
            largeArcFlag = false,
            sweepFlag = true,
            x2 = x + width - rx,
            y2 = y + height)
        val bottomRightSegs =
            bottomRightArc.toSegmentsObjCMethod(PointF(x + width, y + height - ry))
        segments.addAll(bottomRightSegs)

        val bottomLine = Segment(PathType.Line)
        bottomLine.knot = PointF(x + rx, y + height)
        segments.add(bottomLine)

        val bottomLeftArc = SevenPieceArc(rx, ry, 0f,
            largeArcFlag = false,
            sweepFlag = true,
            x2 = x,
            y2 = y + height - ry)
        val bottomLeftSegs = bottomLeftArc.toSegmentsObjCMethod(PointF(x + rx, y + height))
        segments.addAll(bottomLeftSegs)

        val polygon: Polygon = Polygon()
        polygon.shapeNode.pathValue = PathValue(segments)
        polygon.closed = true
        return arrayListOf(polygon)
    }
}
