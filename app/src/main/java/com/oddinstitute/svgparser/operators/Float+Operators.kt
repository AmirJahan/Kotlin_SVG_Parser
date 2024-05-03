package com.oddinstitute.svgparser.operators

import android.graphics.Color
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import java.math.RoundingMode

fun Float.roundTwoDecimals(): Float {
    return this.toBigDecimal().setScale(2, RoundingMode.UP).toFloat()
}

fun Color.roundTwoDecimals(): Color {
    val r = this.red().roundTwoDecimals()
    val g = this.green().roundTwoDecimals()
    val b = this.blue().roundTwoDecimals()
    val a = this.alpha().roundTwoDecimals()

    return Color.valueOf(r, g, b, a)
}
