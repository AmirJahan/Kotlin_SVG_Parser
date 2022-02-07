package com.oddinstitute.svgparser.polygon

import com.oddinstitute.svgparser.svg_elements.SvgStyle


fun Polygon.applySvgStyleT(style: SvgStyle)
{
    // these three are in the shape node, because they are animatable
    style.fill?.let { this.shapeNode.fillColor = it }
    style.stroke?.let { this.shapeNode.strokeColor = it }
    style.strokeWidth?.let { this.shapeNode.strokeWidth = it }

    // these are on polygon level, because they are not animateable
    style.fillRule?.let { svgFillRule ->
        svgFillRule.toType()?.let {
            this.fillType = it
        }
    }

    style.strokeLineCap?.let { svgLinecap ->
        svgLinecap.toType()?.let {
            this.strokeLineCap = it
        }
    }

    style.clipRule?.let { svgClipRule ->
        svgClipRule.toType()?.let {
            this.clipRule = it
        }
    }
}