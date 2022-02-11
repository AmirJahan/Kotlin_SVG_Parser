package com.oddinstitute.svgparser.operators

import android.graphics.Color
import kotlin.random.Random

//fun Int.Companion.toBoolean(): Boolean
//{
//    return if (this == 0)
//        false
//    else
//        true
//}

fun Int.toBoolean(): Boolean = this == 1
fun Boolean.toFloat(): Float = if (this) 1.0f else 0.0f
