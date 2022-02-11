package com.oddinstitute.svgparser

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.oddinstitute.svgparser.svg_to_artwork.SvgToArtwork
import com.oddinstitute.svgparser.svg_to_artwork.parse
import com.oddinstitute.svgparser.temp_draw.DrawView
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import android.widget.ImageView
import com.pixplicity.sharp.Sharp
import java.io.InputStream


class MainActivity : AppCompatActivity()
{

    var curIndex = 1400
    lateinit var files: ArrayList<String>

    lateinit var drawView: DrawView
    lateinit var titleTextView: TextView

    lateinit var svgImageView: ImageView

    var timerHandler: Handler = object : Handler(Looper.getMainLooper())
    {
        override fun handleMessage(msg: Message)
        {

            curIndex %= files.count()

            val file = files[curIndex]
            try
            {
                // MINE
                val parser = SvgToArtwork()
                val istream = assets.open("svgs/$file")
                Log.d(MainActivity::class.simpleName, "i: $curIndex : $file")
                val artwork = parser.parse(istream)
                artwork.title = file
                drawView.redraw(artwork)
                istream.close()
                titleTextView.text = artwork.title



                // OTHER
//                Handler(Looper.getMainLooper()).postDelayed({
                    val istream2 = assets.open("svgs/$file")
                    Sharp.loadInputStream(istream2).into(svgImageView)
                    istream2.close()

//                                                            }, 500)






                curIndex++

            } catch (e: IOException)
            {
                e.printStackTrace()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO
        // polylines and paths are closed unless their fill value is "none"


        files = getSvgsFromAssets("svgs", this)

        drawView = DrawView(this)
        findViewById<FrameLayout>(R.id.canvasId).addView(drawView)
        titleTextView = findViewById<TextView>(R.id.artworkTitleTextView)

        svgImageView = findViewById(R.id.svgImageViewId)


        // single file trial
        try
        {
            // MY SVG
            val parser = SvgToArtwork()
            val istream = assets.open("mysvg.svg")

            val artwork = parser.parse(istream)

            drawView.redraw(artwork)
            titleTextView.text = artwork.title
            istream.close()


            // READER SVG
            Handler(Looper.getMainLooper()).postDelayed({
                                                            val istream2 = assets.open("mysvg.svg")
                                                            Sharp.loadInputStream(istream2)
                                                                    .into(svgImageView)
                                                            istream2.close()

                                                        }, 500)





        } catch (e: IOException)
        {
            e.printStackTrace()
        }






        findViewById<Button>(R.id.newArtworkButton).setOnClickListener {

//            var playTimer: Timer? = null

            Timer().scheduleAtFixedRate(object : TimerTask()
                                        {
                                            override fun run()
                                            {
                                                timerHandler.obtainMessage(1)
                                                        .sendToTarget()
                                            }
                                        },
                                        0,
                                        250)

        }
    }

}


// do NOT delete this
fun MainActivity.getSvgsFromAssets(path: String,
                                   context: Context): ArrayList<String>
{
    val listOfSVGs = ArrayList<String>()

    context.assets.list(path)
            ?.forEach { file ->
                listOfSVGs.add("$file")
            }

    return listOfSVGs
}