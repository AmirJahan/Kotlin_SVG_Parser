package com.oddinstitute.svgparser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.oddinstitute.svgparser.svg_to_artwork.SvgToArtwork
import com.oddinstitute.svgparser.svg_to_artwork.parse
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // this is the only section here
        try {
            val parser = SvgToArtwork()

            // fetches a s file
            val istream = assets.open("mysvg.svg")

            // when this pars happens, the SVG is finished reading
            parser.parse(istream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}


