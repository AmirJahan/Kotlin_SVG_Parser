package com.oddinstitute.svgparser.polygon

import android.graphics.Paint
import android.graphics.Path
import com.oddinstitute.svgparser.ShapeNode
import kotlinx.serialization.Serializable


// polygon is a main class coming from Moush

@Serializable
class Polygon ()
{
    var shapeNode: ShapeNode = ShapeNode()
    var closed: Boolean = true


    // TODO
    // these variables are new
    var strokeLineCap : Paint.Cap = Paint.Cap.ROUND
    var fillType: Path.FillType = Path.FillType.EVEN_ODD
    // Clip rule is not currently supported in this parser
    var clipRule: Path.FillType = Path.FillType.EVEN_ODD



    // TODO - All these have to be uncommented

    /*

    @Transient
    var curFrame: Int = 0
        @Exclude get() { return field }

    @Transient
    var artworkLocation: PointF = PointF()
        @Exclude get() { return field }

    @Transient
    var artworkRotation: Float = 0f
        @Exclude get() { return field }

    @Transient
    var artworkScale: PointF = PointF(1f, 1f)
        @Exclude get() { return field }

    @Transient
    var artworkPivot: PointF = PointF()
        @Exclude get() { return field }

    @Transient
    var artworkAlpha: Float = 1f
        @Exclude get() { return field }

    @Transient
    var thumb: Bitmap? = null


    var locked: Boolean = false

    var selected: Boolean = false

    var hidden: Boolean = false

    @Transient
    var hideForLayerThumbs: Boolean = false
        @Exclude get() { return field }


    // thi path is used in the SVG draw app.  We should see if we still need it
    @Transient
    var mainPath: Path = Path()
        @Exclude get() { return field }

    @Transient
    var newGuidesPath: Path? = null
        @Exclude get() { return field }

    @Transient
    // this is the playable motion that is aggregated for playback time
    var motion: Motion = Motion("Polygon Motion")
        @Exclude get() { return field }

     */
}

