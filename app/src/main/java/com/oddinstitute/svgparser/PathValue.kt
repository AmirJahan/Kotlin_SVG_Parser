package com.oddinstitute.svgparser

import kotlinx.serialization.Serializable


// this is the values of a path for a polygon
// it holds on to an array of segments


@Serializable
class PathValue ()
{
    var segments: ArrayList<Segment> = arrayListOf()

    constructor(segs: ArrayList<Segment>): this ()
    {
        this.segments = segs
    }

    override fun toString(): String
    {
        var text = "\nNew Segment: \n"

        for (seg in segments)
        {
            text += "${seg.toString()}\n"
        }

        return text
    }


}
