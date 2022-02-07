package com.oddinstitute.svgparser

import android.graphics.Color
import androidx.core.graphics.toColor

class ShapeNode
{
    var fillColorR: Float = 0f
    var fillColorG: Float = 0f
    var fillColorB: Float = 0f
    var fillColorA: Float = 0f


    // fill color after Artwork Transparency has been applied
    @Transient
    var filColorApplied: Color = Color.TRANSPARENT.toColor()
        /* @Exclude  FIREBASE */
        get()
        {
            return field
        }


    // these colors were Ints.
    // we changed them to colors
    @Transient
    var fillColor: Color = Color.TRANSPARENT.toColor()
        set(newColor)
        {
            field = newColor
            fillColorA = newColor.alpha()
            fillColorR = newColor.red()
            fillColorG = newColor.green()
            fillColorB = newColor.blue()

            filColorApplied = newColor
        }
        /* @Exclude  FIREBASE */
        get()
        {
            //            Log.d(MyTag, "Getting Fill Color: $col")

            return Color.valueOf(fillColorA,
                                 fillColorR,
                                 fillColorG,
                                 fillColorB)
        }


    var strokeColorR: Float = 0f
    var strokeColorG: Float = 0f
    var strokeColorB: Float = 0f
    var strokeColorA: Float = 0f

    @Transient
    var strokeColorApplied: Color = Color.TRANSPARENT.toColor()
        /* @Exclude  FIREBASE */
        get()
        {
            return field
        }

    @Transient
    var strokeColor: Color = Color.TRANSPARENT.toColor()
        set(newColor)
        {
            field = newColor
            strokeColorA = newColor.alpha()
            strokeColorR = newColor.red()
            strokeColorG = newColor.green()
            strokeColorB = newColor.blue()
            strokeColorApplied = newColor
        }
        /* @Exclude  FIREBASE */
        get()
        {
            return Color.valueOf(strokeColorA,
                                 strokeColorR,
                                 strokeColorG,
                                 strokeColorB)
        }

    var strokeWidth: Float = 0f


    var pathValue: PathValue = PathValue()


    // these 4 are the internal values of the polygon
    // they are maintained for when we add new motion bundles
    // todo ALL COLORS HAVE been replaced from Int to Color
    var fillColorOrig: Color = Color.TRANSPARENT.toColor()
    var strokeColorOrig: Color = Color.TRANSPARENT.toColor()
    var strokeWidthOrig: Float = 0f
    var pathValueOrigin: PathValue = PathValue()


    override fun toString(): String
    {
        return pathValue.toString()
    }
}
