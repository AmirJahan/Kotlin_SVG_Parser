package com.oddinstitute.svgparser.svg_elements

enum class SvgObjectType(val text: String)
{
    POLYLINE("polyline"),
    POLYGON("polygon"),
    CIRCLE("circle"),
    PATH("path"),
    RECT("rect"),
    G("g"),
}