package com.woojun.cartoon_four_cut

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.woojun.cartoon_four_cut.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var logoList: List<Pair<ObjectAnimator, Long>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logoList = listOf(
            Pair(createFadeInAnimation(binding.logo1), 300),
            Pair(createFadeInAnimation(binding.logo2), 400),
            Pair(createFadeInAnimation(binding.logo3), 500),
            Pair(createFadeInAnimation(binding.logo4), 600)
        )

        CoroutineScope(Dispatchers.Main).launch {
            animateLogos()
            delay(200)
        }
    }

    private fun createFadeInAnimation(target: ImageView): ObjectAnimator {
        val fadeIn = ObjectAnimator.ofFloat(target, "alpha", 0f, 1f)
        fadeIn.duration = 200
        return fadeIn
    }

    private suspend fun animateLogos() {
        withContext(Dispatchers.Main) {
            logoList.forEach { (animation, delay) ->
                delay(delay)
                animation.start()
            }
        }
    }
}