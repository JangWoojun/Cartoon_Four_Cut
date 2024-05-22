package com.woojun.cartoon_four_cut

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.woojun.cartoon_four_cut.database.Preferences.loadId
import com.woojun.cartoon_four_cut.database.Preferences.saveId
import com.woojun.cartoon_four_cut.data.AuthResponse
import com.woojun.cartoon_four_cut.databinding.ActivitySplashBinding
import com.woojun.cartoon_four_cut.network.RetrofitAPI
import com.woojun.cartoon_four_cut.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
            val authId = loadId(this@SplashActivity)
            if (authId == null) {
                val isSuccessful = getAuthId { saveId(this@SplashActivity, it) }
                if (!isSuccessful) {
                    Toast.makeText(this@SplashActivity, "초기 세팅을 실패하였습니다", Toast.LENGTH_SHORT).show()
                    return@launch
                }
            }
            animateLogos()
            startMainActivity()
        }

    }

    private fun startMainActivity() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finishAffinity()
    }

    private fun getAuthId(callback: (String) -> Unit): Boolean {
        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<AuthResponse> = retrofitAPI.getAuthId()

        var isSuccessful = false

        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()!!.id)
                    isSuccessful = true
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
            }
        })

        return isSuccessful
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
            delay(200)
        }
    }
}