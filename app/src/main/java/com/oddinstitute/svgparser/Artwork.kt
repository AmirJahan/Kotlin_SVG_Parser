package com.oddinstitute.svgparser

import java.util.*
import kotlin.collections.ArrayList


import android.graphics.Bitmap
import com.oddinstitute.svgparser.polygon.Polygon
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


// this is a large main class
// it comes from moush



@Serializable
class Artwork ()
{
    var polygons: ArrayList<Polygon> = arrayListOf()
    var id: String = UUID.randomUUID().toString()
    var tags: ArrayList<String> = arrayListOf()

    @Transient
    var curFrame: Int = 0

    @Transient
    var thumb: Bitmap? = null
    var locked: Boolean = false

    var currentMotionBundleID: String = "1"


    // var motionBundles: ArrayList<MotionBundle> = arrayListOf()

    init {
        // this is for when we read from the disk. From SVG, this is done at the construct
        for (polygon in this.polygons) {
            polygon.shapeNode.fillColorOrig = polygon.shapeNode.fillColor
            polygon.shapeNode.strokeColorOrig = polygon.shapeNode.strokeColor
            polygon.shapeNode.strokeWidthOrig = polygon.shapeNode.strokeWidth
            polygon.shapeNode.pathValueOrigin = polygon.shapeNode.pathValue.clone()
        }
    }


    // @Transient var artworkMotion: Motion = Motion("Artwork Motion")

    // var transformNode: TransformNode = TransformNode()

    @Transient
    var selected: Boolean = false

    var hidden: Boolean = false

    @Transient
    var hideForLayerThumbs: Boolean = false


    /*
    constructor(svgFile: SvgFile) : this()  {
        construct (svgFile)
        // this is for saving right at the very beginning
//        saveMotionBundleBeforeNewOrSwitch()
    }

     */
}


