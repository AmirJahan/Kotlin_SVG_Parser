package com.oddinstitute.svgparser

enum class ObjectType(val text: String) {
    RECTANGLE("rectangle"),
    ELLIPSE("ellipse"), // ellipses and circles
    POLYGON("polygon"), // polygons with similar size on all sides, can be closed or open
    PATH("path"), // with cubic beziers only
    // any polygon that doesn't fit in regular
    // becomes a polygon

    TEXT("text"), //
    IMAGE("image"),  // bitmaps

    G("g")
}
