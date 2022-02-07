package com.oddinstitute.svgparser

import kotlinx.serialization.Serializable


// this class determines the type of a path

@Serializable
enum class PathType(val c: Char)
{
    Move('M'),
    Horizontal ('H'),
    Vertical ('V'),
    Line('L'),

    Curve('C'),
    SmoothCurve('S'), // cp1 is reflection

    Quad('Q'),
    SmoothQuad('T'), // cp 1 is reflection


    Arc('A'), // cp 1 is reflection
}

fun String.pathTypeFromString () : PathType
{
    when (this)
    {
        "Move" -> return PathType.Move
        "Horizontal" -> return PathType.Horizontal
        "Vertical" -> return PathType.Vertical
        "Line" -> return PathType.Line
        "Curve" -> return PathType.Curve
        "SmoothCurve" -> return PathType.SmoothCurve
        "Quade" -> return PathType.Quad
        "SmoothQuad" -> return PathType.SmoothQuad
        "Arc" -> return PathType.Arc
    }

    return PathType.Curve
}