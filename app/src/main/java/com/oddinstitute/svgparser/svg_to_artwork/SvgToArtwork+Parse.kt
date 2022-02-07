package com.oddinstitute.svgparser.svg_to_artwork

import android.util.Log
import com.oddinstitute.svgparser.Artwork
import com.oddinstitute.svgparser.polygon.Polygon
import com.oddinstitute.svgparser.svg_tags.*
import com.oddinstitute.svgparser.svg_tags.path_tag.PathTag
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream

fun SvgToArtwork.parse(inputStream: InputStream): Artwork
{
    val artwork: Artwork = Artwork()
    try
    {
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val parser: XmlPullParser = factory.newPullParser()
        parser.setInput(inputStream, null)
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            parser.name?.let {
                curTagName = parser.name
//                    Log.d(XmlToArtwork::class.simpleName, "curTagName: ${curTagName!!}")
            }

            when (eventType)
            {
                XmlPullParser.START_TAG ->
                {
                    when (curTagName)
                    {
                        "g" ->
                        {
                            // we set the active group
                            // here, we make a tag with parser
                            // and put it in the active group
                            activeGroup = Tag(parser)
                        }
                        "svg" -> {
                            val svgTag = SvgTag(parser)
                            val decodedViewBox = svgTag.decode()
                            scaleFactor = decodedViewBox.second
                            viewBoxOffset = decodedViewBox.first
                        } // must be in start, because its end is the end of the document
                        "line" ->
                        {
                            val lineTag = LineTag(parser)
                            val polygon = tagToPolygon(lineTag)
                            artwork.polygons.add(polygon)
                        }
                        "rect" ->
                        {
                            val rectTag = PolyTag(parser)
                            val polygon = tagToPolygon(rectTag)
                            artwork.polygons.add(polygon)
                        }
                        "polygon" ->
                        {
                            val polygonTag = PolyTag(parser, true)
                            val polygon: Polygon = tagToPolygon(polygonTag)
                            artwork.polygons.add(polygon)
                        }
                        "polyline" ->
                        {
                            val polylineTag = PolyTag(parser) // default closed is false
                            val polygon: Polygon = tagToPolygon(polylineTag)
                            artwork.polygons.add(polygon)
                        }
                        "circle" ->
                        {
                            val circleTag = CircleTag(parser)
                            val polygon: Polygon = tagToPolygon(circleTag)
                            artwork.polygons.add(polygon)
                        }
                        "ellipse" ->
                        {
                            val ellipseTag = EllipseTag(parser)
                            val polygon: Polygon = tagToPolygon(ellipseTag)
                            artwork.polygons.add(polygon)
                        }
                        "path" ->
                        { // this is different. Doesn't use decode, uues a different type
                            val pathTag = PathTag(parser)
                            val polygon = tagToPolygon(pathTag)
                            artwork.polygons.add(polygon)
                        }


                        else -> Log.d(SvgToArtwork::class.simpleName, "Wrong: $curTagName")
                    }
                }
                XmlPullParser.TEXT -> curTagText = parser.text // if there is text, read it
                XmlPullParser.END_TAG ->
                {

                    Log.d(SvgToArtwork::class.simpleName, "At end: $curTagName")

                    when (curTagName)
                    {
                        "style" -> styleTag() // this must be at the end

                        // when we end the group, it should become inactive
                        "g" -> activeGroup = null
                    }

                    curTagName = null
                }
            }

            eventType = parser.next()
        }

    } catch (e: XmlPullParserException)
    {
        e.printStackTrace()
    } catch (e: IOException)
    {
        e.printStackTrace()
    }


    // this is temp for debugging
    val polygon = artwork.polygons[0]


    // we reverse this here, because when we are drawing them on
    // screen, we do it from bottom to top
    artwork.polygons.reverse()


    // todo THESE HAVE TO BE ADDED LATER ON
    // this.makeDefaultMotionBundle()
    // findPivot()

    return artwork
}

