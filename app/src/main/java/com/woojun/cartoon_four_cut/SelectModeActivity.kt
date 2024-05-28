package com.woojun.cartoon_four_cut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.woojun.cartoon_four_cut.databinding.ActivitySelectModeBinding
import com.woojun.cartoon_four_cut.util.OnSingleClickListener

class SelectModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectModeBinding.inflate(layoutInflater)
        overridePendingTransition(R.anim.vertical_enter, R.anim.anim_fade_out)
        setContentView(binding.root)

        binding.takePhotoButton.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                startActivity(Intent(this@SelectModeActivity, CameraActivity::class.java))
            }
        })

        binding.galleryButton.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                startActivity(Intent(this@SelectModeActivity, PhotoActivity::class.java))
            }
        })

        binding.backButton.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                overridePendingTransition(R.anim.anim_fade_out, R.anim.vertical_exit)
                finish()
            }
        })

    }
    override fun onBackPressed() {
        overridePendingTransition(R.anim.anim_fade_out, R.anim.vertical_exit)
        super.onBackPressed()
    }
}