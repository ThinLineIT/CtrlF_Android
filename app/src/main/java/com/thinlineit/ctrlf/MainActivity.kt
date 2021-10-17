package com.thinlineit.ctrlf

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: Do not use deprecated methods.
        val display = windowManager.defaultDisplay
        /*val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)*/

        val size = Point()
        display.getRealSize(size) // or getSize(size)
        val width = size.x

        /*val density = resources.displayMetrics.density*/

        swipeWidth = if (width > 1080) (width.toFloat() / 6)
        else 3 * (width.toFloat() / 10)
        Log.d("swipe", swipeWidth.toString())
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        var swipeWidth by Delegates.notNull<Float>()
    }
}
