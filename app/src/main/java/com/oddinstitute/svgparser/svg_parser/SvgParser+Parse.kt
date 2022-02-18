package com.oddinstitute.svgparser.svg_parser

import android.util.Log
import com.oddinstitute.svgparser.Artwork
import com.oddinstitute.svgparser.tags.*
import com.oddinstitute.svgparser.tags.path_tag.PathTag
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream

fun SvgParser.parse(inputStream: InputStream): Artwork
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
//                            activeGroup = Tag(parser)
                            currentGroups.add(Tag(parser))
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

                            if (definitionState)
                                definitions.add(lineTag)
                            else
                            {
                                val polygons = lineTag.toPolygons(currentGroups, styles, scaleFactor, viewBoxOffset)
                                artwork.polygons.addAll(polygons)
                            }
                        }
                        "rect" ->
                        {
                            val rectTag = RectTag(parser)
                            if (definitionState)
                                definitions.add(rectTag)
                            else
                            {
                                val polygons = rectTag.toPolygons(currentGroups, styles, scaleFactor, viewBoxOffset)
                                artwork.polygons.addAll(polygons)
                            }
                        }
                        "polygon" ->
                        {
                            val polygonTag = PolyTag(parser, true)
                            if (definitionState)
                                definitions.add(polygonTag)
                            else
                            {
                                val polygons = polygonTag.toPolygons(currentGroups, styles, scaleFactor, viewBoxOffset)
                                artwork.polygons.addAll(polygons)
                            }
                        }
                        "polyline" ->
                        {
                            val polylineTag = PolyTag(parser) // default closed is false
                            if (definitionState)
                                definitions.add(polylineTag)
                            else
                            {
                                val polygons = polylineTag.toPolygons(currentGroups, styles, scaleFactor, viewBoxOffset)
                                artwork.polygons.addAll(polygons)
                            }
                        }
                        "circle" ->
                        {
                            val circleTag = CircleTag(parser)
                            if (definitionState)
                                definitions.add(circleTag)
                            else
                            {
                                val polygons = circleTag.toPolygons(currentGroups, styles, scaleFactor, viewBoxOffset)
                                artwork.polygons.addAll(polygons)
                            }
                        }
                        "ellipse" ->
                        {
                            val ellipseTag = OvalTag(parser)
                            if (definitionState)
                                definitions.add(ellipseTag)
                            else
                            {
                                val polygons = ellipseTag.toPolygons(currentGroups, styles, scaleFactor, viewBoxOffset)
                                artwork.polygons.addAll(polygons)
                            }
                        }
                        "path" ->
                        { // this is different. Doesn't use decode, uues a different type
                            val pathTag = PathTag(parser)
                            if (definitionState)
                                definitions.add(pathTag)
                            else
                            {
                                val polygons = pathTag.toPolygons(currentGroups, styles, scaleFactor, viewBoxOffset)
                                artwork.polygons.addAll(polygons)
                            }
                        }
                        "use" ->
                        {
                            // here, we pass the definitions into the ue tag
                            val useTag = UseTag(parser, definitions)
                            val resultTag = useTag.resultTag()
                            val polygons = resultTag.toPolygons(currentGroups, styles, scaleFactor, viewBoxOffset)
                            artwork.polygons.addAll(polygons)
                        }
                        "defs" -> definitionState = true


                        else -> Log.d(SvgParser::class.simpleName, "Wrong: $curTagName")
                    }
                }
                XmlPullParser.TEXT -> curTagText = parser.text // if there is text, read it
                XmlPullParser.END_TAG ->
                {

                    Log.d(SvgParser::class.simpleName, "At end: $curTagName")

                    when (curTagName)
                    {
                        "style" ->
                        {
                            val styleTag = StyleTag (curTagText)
                            this.styles = styleTag.decodeStyle()
                        }

                        // when we end the group, it should become inactive
                        "g" -> currentGroups.removeLast() //  activeGroup = null

                        "defs" -> definitionState = false
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


    // todo THESE HAVE TO BE ADDED LATER ON
    // this.makeDefaultMotionBundle()
    // findPivot()




    return artwork
}

