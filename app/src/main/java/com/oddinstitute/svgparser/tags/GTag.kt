package com.oddinstitute.svgparser.tags


// TODO
// this class is currently unused


// this tag is only there so we know what to do with the group tags
class GTag : Tag()
{
    // this is a deep copy of the Group tag
    // this is used for when copying group information into other tags
    fun clone(): GTag
    {
        val gTag = GTag()

        this.id?.let { gTag.id = it }
        this.fill?.let { gTag.fill = it }
        this.stroke?.let { gTag.stroke = it }
        this.strokeWidth?.let { gTag.strokeWidth = it }

        this.fillRule?.let { gTag.fillRule = it }
        this.clipRule?.let { gTag.clipRule = it }
        this.strokeLineCap?.let { gTag.strokeLineCap = it }


        this.transforms?.let {

            // we force non optional here, because we just allocated it
            gTag.transforms = arrayListOf()
            for (trans in it)
            {
                gTag.transforms!!.add(trans.clone())
            }
        }

        this.style?.let { gTag.style = it }
        this.svgClass?.let { gTag.svgClass = it }

        return gTag
    }
}