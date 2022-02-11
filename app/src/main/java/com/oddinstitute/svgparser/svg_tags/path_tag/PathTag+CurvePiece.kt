package com.oddinstitute.svgparser.svg_tags.path_tag

import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.Segment
import com.oddinstitute.svgparser.operators.toFloat
import com.oddinstitute.svgparser.polygon.plus


//Multiple sets of coordinates may be specified to draw a polybézier.
//At the end of the command, the new current point becomes the final (x,y)
//coordinate pair used in the polybézier.


fun PathTag.curvePiece(piece: String, curPoint: PointF): ArrayList<Segment>
{
    val segments = arrayListOf<Segment>()


    val str = piece
            .trimStart().trimEnd().trim()
            .replace("c", "")
            .replace("C", "")
            .replace(" ", ",")
            .replace(",,", ",") // it's possible to get two commas

    var intCurPoint = PointF() // this is either the current point or the knot of the last segments


    // curves have multiples of 6 pieces, SO
    val initialPoints : List<String> = str.split(",")



    val points = cleanPointsOfDecimals(initialPoints)


    for (i in 0 until points.count() step 6)
    {
        val curve = Segment(PathType.Curve)


        // if we are relative, we find the actual value based on the cur point
        // this is simply a shortcut, when relative, we use curPoint, when not, we don't
        // if not, we just add zero
        if (piece[0] == 'c') // all relative
        {
            intCurPoint = curPoint

            // after the first piece, we should take the previous location
            if (segments.count() > 0)
                intCurPoint = segments.last().knot
        }

        curve.cp1 = PointF(points[i].toFloat() + intCurPoint.x,
                           points[i+1].toFloat() + intCurPoint.y)

        curve.cp2 = PointF(points[i+2].toFloat() + intCurPoint.x,
                           points[i+3].toFloat() + intCurPoint.y)

        curve.knot = PointF(points[i+4].toFloat() + intCurPoint.x,
                            points[i+5].toFloat() + intCurPoint.y)

        segments.add(curve)
    }

    return segments
}



fun PathTag.cleanPointsOfDecimals (initialPoints: List<String>): ArrayList<String>
{
    val points: ArrayList<String> = arrayListOf()

    // when we split, it is possible that we get two values clustered into just one.
    // situation: M 1.2.3 is valid and would be parsed like M 1.2 0.3
    // so, we check to see if any of the values has more than two dots


    for (point in initialPoints)
    {
        // -50.8.100.11.16
        //-50
        // 8
        //100
        //11
        // 16

            // another 27.9335937-.515625

        if (point.filter { it == '.' }.count() == 2)
        {
            val internalTwoDecimalPoints = point.split(".")

            for (i in 0 until internalTwoDecimalPoints.count())
            {
                if (i == 0)
                    points.add("${internalTwoDecimalPoints[i]}.${internalTwoDecimalPoints[i+1]}")
                else if (i == 1)
                    continue
                else
                    points.add("0.${internalTwoDecimalPoints[i]}")
            }
        }
        else
            points.add(point)
    }

    return points
}