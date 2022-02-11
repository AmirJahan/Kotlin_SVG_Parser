package com.oddinstitute.svgparser.operators

// this cleaning can only be used for Style, d of Path and polylines
fun String.cleanTags(): String
{
    var newString = this.trimIndent()
            .trimStart()
            .trimEnd()
            .trim()
            .replace("\n", " ")
            .replace("\\s+".toRegex(), " ") // remove multiple spaces
            .replace(", ", ",")

// If the remaining still includes Z, it's closed. Otherwise open



    // the minus situation is only happening in the path
    // so, we test if a minus has two numbers by its sides, we replace it
    var tempMinusString = ""
    for (i in newString.indices)
    {


        if (newString[i] == '-')
        {
            if (i > 0 && newString[i-1].isDigit())
            {
                if (newString[i+1].isDigit() || newString[i+1] == '.') // 27.9335937-.515625
                tempMinusString += " -" // in this situation add the "space minus"
            }
            else
                tempMinusString += newString[i] // otherwise add the character
        }
        else
            tempMinusString += newString[i] // otherwise add the character
    }
    newString = tempMinusString // this is now the working string



// i don't think any of these are needed any longer
//    newString = newString.replace(",,", ",")


//    val delimiters: Array<Char> =
//        arrayOf('M', 'm', 'L', 'l', 'C', 'c', 'S', 's', 'Q', 'q', 'T', 't', 'H', 'h', 'V', 'v', 'a', 'A')
//
//    for (char in delimiters)
//        newString = newString.replace("$char,", "$char")
//
//
//    newString = newString.replace(" ,-", " -") // for poly lines
//            .replace("l,-r", "l-r") // for styles
//            .replace("p,-r", "p-r") // for styles clip rule
//            .replace("e,-l", "e-l") // for styles stroke - linecap
//            .replace("e,-w", "e-w") // for styles stroke - width

    return newString
}