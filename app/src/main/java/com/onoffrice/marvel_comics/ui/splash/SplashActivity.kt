package com.onoffrice.marvel_comics.ui.splash

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.onoffrice.marvel_comics.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setLogoAnimation()
        //setDelayForActivity()
    }

//    private fun setDelayForActivity() {
//        val handle = Handler()
//        handle.postDelayed({
//            startActivitySlideTransition(createTopGamesIntent())
//            finish()
//            }, 4000)
//    }

    private fun setLogoAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_transition)
        animation.repeatCount    = 1
        animation.duration       = 2000
        animation.fillAfter      = true
        animation.repeatMode     = Animation.REVERSE
        splash_logo.startAnimation(animation)
    }
}
