package com.oddinstitute.svgparser

import android.graphics.PointF
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
class Segment()
{
    lateinit var type: PathType

    constructor(type: PathType): this ()
    {
        this.type = type
    }


    constructor(t: PathType, k: PointF, c1: PointF?, c2: PointF?) : this (t)
    {
        type = t
        knot = k
        cp1 = c1
        cp2 = c2
    }

    @Transient
    var knotDrawn: PointF = PointF()
        /* FIREBASE @Exclude */
        get() { return field }

    @Transient
    var cp1Drawn: PointF? = null
        /* FIREBASE @Exclude */
        get() { return field }

    @Transient
    var cp2Drawn: PointF? = null
        /* FIREBASE @Exclude */
        get() { return field }

    @Serializable(with = PointFAsStringSerializer::class)
    var knot: PointF = PointF()

    @Serializable(with = PointFAsStringSerializer::class)
    var cp1: PointF? = null

    @Serializable(with = PointFAsStringSerializer::class)
    var cp2: PointF? = null


    override fun toString(): String
    {
        var text = "Type: $type\n"

        text += "knot: ${knot}\n"

        cp1?.let { text += "cp1: $it\n" }
        cp2?.let { text += "cp2: $it\n" }

        return text
    }





    @Transient
    var knotPath: CornerPath? = null
        /* FIREBASE @Exclude */
        get() { return field }

    @Transient
    var cp1Path: CornerPath? = null
        /* FIREBASE @Exclude */
        get() { return field }

    @Transient
    var cp2Path: CornerPath? = null
        /* FIREBASE @Exclude */
        get() { return field }

    @Transient
    var selected: Boolean = false
        /* FIREBASE @Exclude */
        get() { return field }
}