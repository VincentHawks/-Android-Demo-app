package com.swtecnn.demomode

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.VideoView

class MainActivity : AppCompatActivity() {

    private lateinit var video: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        val serviceIntent = Intent(this, IdleModeReceiverHostService::class.java)
        startForegroundService(serviceIntent)

        video = findViewById(R.id.video)
        video.setMediaController(null)
        video.setVideoURI(Uri.parse(
            "android.resource://com.swtecnn.demomode/${R.raw.background}"
        ))
        video.keepScreenOn = true
        video.requestFocus()
        video.setOnCompletionListener { video.start() }
        video.setOnPreparedListener { mp -> mp.isLooping = true }
        video.start()

    }

    fun screenTouched(v: View) {
        this.finish()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        this.finish()
        super.onConfigurationChanged(newConfig)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        this.finish()
    }
}