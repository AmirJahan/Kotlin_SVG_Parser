package com.oddinstitute.svgparser.polygon

import android.graphics.PointF
import com.oddinstitute.svgparser.*
import com.oddinstitute.svgparser.svg_elements.SvgMatrixTransform
import com.oddinstitute.svgparser.svg_elements.SvgTransform
import com.oddinstitute.svgparser.svg_elements.SvgTransformType
import kotlin.math.cos
import kotlin.math.sin

fun Polygon.applySvgTransforms(transforms: ArrayList<SvgTransform>) {
    // the transforms here are in order
    // we ordered them when we were combining
    // so, they have to be applied in order

    for (trans in transforms.reversed()) {
        when (trans.type) {
            SvgTransformType.TRANSLATE -> this.svgTransformTranslate(trans)
            SvgTransformType.ROTATE -> this.svgTransformRotate(trans)
            SvgTransformType.SCALE -> this.svgTransformScale(trans)
            SvgTransformType.MATRIX -> trans.matrix?.let { this.svgTransformMatrix(it) }
        }
    }
}

fun Polygon.svgTransformMatrix(matrix: SvgMatrixTransform) {
    for (seg in this.shapeNode.pathValue.segments) {
        seg.knot = seg.knot.matrixTransform(matrix)
        seg.cp1?.let {
            seg.cp1 = it.matrixTransform(matrix)
        }
        seg.cp2?.let {
            seg.cp2 = it.matrixTransform(matrix)
        }
    }
    /*
        newX = a * oldX + c * oldY + e = 3 * 10 - 1 * 10 + 30 = 50
        newY = b * oldX + d * oldY + f = 1 * 10 + 3 * 10 + 40 = 80

    newX = a * oldX + c * oldY + e = 3 * 10 - 1 * 10 + 30 = 50
    newY = b * oldX + d * oldY + f = 1 * 10 + 3 * 10 + 40 = 80
     */
}

fun PointF.matrixTransform(matrix: SvgMatrixTransform): PointF {
    val x = matrix.a * this.x + matrix.c * this.y + matrix.e
    val y = matrix.b * this.x + matrix.d * this.y + matrix.f

    return PointF(x, y)
}

fun Polygon.svgTransformTranslate(trans: SvgTransform) {
    val offset = PointF(trans.x, trans.y)
    for (seg in this.shapeNode.pathValue.segments)
        seg.translate(offset)
}

fun Polygon.svgTransformRotate(trans: SvgTransform) {
    val rotatePivot = PointF(trans.cx, trans.cy)
    val angle = trans.angle

    for (segment in this.shapeNode.pathValue.segments)
        segment.rotate(angle, rotatePivot)
}

fun Polygon.svgTransformScale(trans: SvgTransform) {
    val scaleFactor = PointF(trans.x, trans.y)

    for (segment in this.shapeNode.pathValue.segments)
        segment.scale(scaleFactor, PointF())

    // scale in SVG is against the origin of the scene, not the object
//    segment.scale(scaleFactor, this.findOrigin())
}

fun PointF.translate(offset: PointF) {
    this.x += offset.x
    this.y += offset.y
}

fun PointF.scaleThis(scale: PointF, pivot: PointF) {
    this.x = (this.x - pivot.x) * scale.x + pivot.x
    this.y = (this.y - pivot.y) * scale.y + pivot.y
}

// todo from here down, they all exist
// todo this already exists
fun PointF.scale(scale: PointF, pivot: PointF): PointF {
    val x = (this.x - pivot.x) * scale.x + pivot.x
    val y = (this.y - pivot.y) * scale.y + pivot.y

    return PointF(x, y)
}

fun Segment.rotate(angle: Float, pivot: PointF) {
    this.knot = this.knot.rotate(angle, pivot)
    this.cp1 = this.cp1?.rotate(angle, pivot)
    this.cp2 = this.cp2?.rotate(angle, pivot)
}

operator fun PointF.plus(other: PointF) = PointF(this.x + other.x, this.y + other.y)

fun PointF.scale(scaleFactor: Float, pivot: PointF) {
    this.x = (this.x - pivot.x) * scaleFactor + pivot.x
    this.y = (this.y - pivot.y) * scaleFactor + pivot.y
}

fun PointF.rotate(angle: Float, pivot: PointF): PointF {
    val x = cos(angle.toRadian()) * (this.x - pivot.x) - sin(
        angle.toRadian()) * (this.y - pivot.y) + pivot.x
    val y = sin(angle.toRadian()) * (this.x - pivot.x) + cos(
        angle.toRadian()) * (this.y - pivot.y) + pivot.y

    return PointF(x.toFloat(), y.toFloat())
}

fun Float.toRadian() = (this * (Math.PI / 180))
