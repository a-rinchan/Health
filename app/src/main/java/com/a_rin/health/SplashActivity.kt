package com.a_rin.health

import android.os.Bundle
import android.os.Handler
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.animation.AlphaAnimation
import kotlinx.android.synthetic.main.splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.splash)

        val handler : Handler = Handler()
        handler.postDelayed(splashHandler(), 3000)
    }

    internal inner class splashHandler : Runnable {
        override fun run() {
            val intent = Intent(application, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            this@SplashActivity.finish()
        }
    }
}
