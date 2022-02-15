package com.oddinstitute.svgparser.svg_tags.path_tag

import android.graphics.PointF
import com.oddinstitute.svgparser.PathType
import com.oddinstitute.svgparser.Segment
import com.oddinstitute.svgparser.operators.toFloat


fun PathTag.linePiece(piece: String, curPoint: PointF): ArrayList<Segment>
{
    val segments = arrayListOf<Segment>()

    val str = piece
            .trimStart()
            .trimEnd()
            .trim()
            .replace("L", "")
            .replace("l", "")
            .replace("V", "")
            .replace("v", "")
            .replace("H", "")
            .replace("h", "")
            .replace(" ", ",")
            .replace(",,", ",") // it's possible to get two commas





    val points = str
            .split(",")


    // if we are relative, we find the actual value based on the cur point
    // this is simply a shortcut, when relative, we use curPoint, when not, we don't
    // if not, we just add zero
    var intCurPoint = curPoint * (  (piece[0] == 'l').toFloat() +
                                    (piece[0] == 'h').toFloat() +
                                    (piece[0] == 'v').toFloat()) // this combination is either zero or one



    when (piece[0])
    {
        // line to new x and y
        'L', 'l' -> {
            for (i in 0 until points.count() step 2)
            {
                val line = Segment(PathType.Line)

                if (piece[0] == 'l' && segments.count() > 0)
                    intCurPoint = segments.last().knot

                line.knot = PointF(points[i].toFloat() + intCurPoint.x, points[i+1].toFloat() + intCurPoint.y)
                segments.add(line)
            }
        }


        // line from current point horizontally relative to the new x
        'h', 'H' ->
        {
            for (i in 0 until points.count() step 2)
            {
                val line = Segment(PathType.Line)

                if (piece[0] == 'h' && segments.count() > 0)
                    intCurPoint = segments.last().knot

                line.knot = PointF(points[0].toFloat() + intCurPoint.x, intCurPoint.y)
                segments.add(line)
            }
        }



        // THESE TWO ARE ALSO points[0], because there is only one value after H or V
        // line from current point vertically relative to the new y
        'v', 'V' ->{
            for (i in 0 until points.count() step 2)
            {
                val line = Segment(PathType.Line)

                if (piece[0] == 'v' && segments.count() > 0)
                    intCurPoint = segments.last().knot

                line.knot = PointF(intCurPoint.x, points[0].toFloat() + intCurPoint.y)
                segments.add(line)
            }
        }
    }








    return segments
}