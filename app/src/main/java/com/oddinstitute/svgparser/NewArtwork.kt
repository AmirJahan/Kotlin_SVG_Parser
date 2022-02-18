package com.oddinstitute.svgparser

import java.util.*
import kotlin.collections.ArrayList


import android.graphics.Bitmap
import com.oddinstitute.svgparser.polygon.Polygon
import com.oddinstitute.svgparser.shapes.Object
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class NewArtwork()
{
    var title: String = ""
    var id: String = UUID.randomUUID().toString()
    var tags: ArrayList<String> = arrayListOf()





    var objects: ArrayList<Object> = arrayListOf()
//    var polygons: ArrayList<Polygon> = arrayListOf()








    @Transient
    var curFrame: Int = 0

    @Transient
    var thumb: Bitmap? = null
    var locked: Boolean = false

    var currentMotionBundleID: String = "1"


    // var motionBundles: ArrayList<MotionBundle> = arrayListOf()




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


