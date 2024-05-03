package com.oddinstitute.svgparser.temp_draw

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.core.graphics.toColor
import com.oddinstitute.svgparser.Artwork
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.polygon.Polygon

class DrawView(context: Context) : View(context) {
    var artwork: Artwork? = null

    var paint: Paint = Paint()

    fun redraw(art: Artwork) {
        this.artwork = art

        this.artwork?.let {
            for (poly in it.polygons)
                poly.makePath()
        }

        // FIXME -> Is this needed. Maybe it causes a double rendering
        this.invalidate()
    }

    override fun onDraw(canvas: Canvas) {
//        tempPath.moveTo(150f,0f)
//        tempPath.lineTo(121f, 90f)
//        tempPath.lineTo(198f, 35f)
//        tempPath.lineTo(102f, 35f)
//        tempPath.lineTo(179f, 90f)
//        tempPath.close()
//        tempPath.fillType = Path.FillType.EVEN_ODD
//
//
//
//        paint.style = Paint.Style.FILL
//        paint.color = Color.RED
//        paint.clearShadowLayer()
//
//        canvas.drawPath(tempPath, paint)

        artwork?.let {
            drawArtwork(canvas, it)
        }
    }
}

fun DrawView.drawArtwork(canvas: Canvas, artwork: Artwork) {
    // drawing all paths
    for (polygon in artwork.polygons) {
        // fill
        if (polygon.shapeNode.fillColor != Color.TRANSPARENT.toColor()) {
            styleFillPaint(polygon)
            canvas.drawPath(polygon.mainPath,
                            paint)
        }

        if (polygon.shapeNode.strokeWidth > 0f) {
            styleStrokePaint(polygon)
            canvas.drawPath(polygon.mainPath,
                            paint)
        }
    }
}

fun DrawView.styleFillPaint(polygon: Polygon) {
    paint.style = Paint.Style.FILL

    polygon.shapeNode.filColorApplied.let {
        paint.color = it.toArgb()
    }

    // paint.color = Color.RED
    paint.strokeCap = polygon.strokeLineCap

    paint.strokeJoin = polygon.strokeLineJoin

    paint.pathEffect = null
    paint.clearShadowLayer()
}

// todo important
fun DrawView.styleStrokePaint(polygon: Polygon) {
    polygon.shapeNode.strokeWidth.let { width ->
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = width
        polygon.shapeNode.strokeColorApplied.let {
            paint.color = it.toArgb()
        }

        paint.strokeCap = polygon.strokeLineCap
        paint.pathEffect = null
        paint.clearShadowLayer()

        polygon.dashArray?.let {
            paint.pathEffect = DashPathEffect(floatArrayOf(it, it), 0f)
        }

        paint.strokeJoin = polygon.strokeLineJoin
    }
}

// TODO
// THIS IS TEMPORARY.
// IT'S DIFFERENT THAN THE ACTUAL THING IN MOUSH
// TODO IMPORTANT -> this.mainPath.fillType should go to moush
fun Polygon.makePath() {
    // before thishappens, there is always
    // transform
    // and apply boum

// Multiple cycles are printing at the beginning
    this.mainPath = Path()

    // todo
    this.mainPath.fillType = this.fillType

    for (piece in this.shapeNode.pathValue.segments) {
        when (piece.type) {
            PathType.Move -> {
                mainPath.moveToPoint(piece.knot)
            }
            PathType.Line, PathType.Horizontal, PathType.Vertical -> {
                mainPath.lineToPoint(piece.knot)
            }
            PathType.Curve, PathType.SmoothCurve -> {
                piece.cp1?.let { cp1Drawn ->
                    piece.cp2?.let { cp2Drawn ->
                        mainPath.cubicToCpCpPoint(cp1Drawn,
                                                  cp2Drawn,
                                                  piece.knot)
                    }
                }
            }
            PathType.Quad, PathType.SmoothQuad -> {
                piece.cp1?.let { cp1Drawn ->
                    mainPath.quadToCpPoint(cp1Drawn,
                                           piece.knot)
                }
            }
        }
    }

    if (this.closed)
        mainPath.close()
}
