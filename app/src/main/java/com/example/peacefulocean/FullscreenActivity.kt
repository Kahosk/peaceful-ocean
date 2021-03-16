package com.example.peacefulocean

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.peacefulocean.views.DrawingWatterView

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var fullscreenContent: FrameLayout
    private lateinit var drawingView: DrawingWatterView
    private var brush: Float = 20F

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = findViewById(R.id.fullscreen_container)
        drawingView = findViewById(R.id.drawing)

    }

    override fun onResume() {
        super.onResume()
        drawingView.invalidate()
    }

    override fun onPause() {
        super.onPause()
        onRestart()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        hide()
    }


    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        fullscreenContent.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    override fun onClick(p0: View?) {
    }
}