package com.woojun.cartoon_four_cut

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.woojun.cartoon_four_cut.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val fadeIn = ObjectAnimator.ofFloat(binding.logo1, "alpha", 0f, 1f)
            fadeIn.duration = 500
            fadeIn.start()
        }, 500)

        Handler(Looper.getMainLooper()).postDelayed({
            val fadeIn = ObjectAnimator.ofFloat(binding.logo2, "alpha", 0f, 1f)
            fadeIn.duration = 500
            fadeIn.start()
        }, 1000)

        Handler(Looper.getMainLooper()).postDelayed({
            val fadeIn = ObjectAnimator.ofFloat(binding.logo3, "alpha", 0f, 1f)
            fadeIn.duration = 500
            fadeIn.start()
        }, 1500)

        Handler(Looper.getMainLooper()).postDelayed({
            val fadeIn = ObjectAnimator.ofFloat(binding.logo4, "alpha", 0f, 1f)
            fadeIn.duration = 500
            fadeIn.start()
        }, 2000)
    }
}