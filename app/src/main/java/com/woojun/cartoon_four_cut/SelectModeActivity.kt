package com.woojun.cartoon_four_cut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.woojun.cartoon_four_cut.databinding.ActivitySelectModeBinding

class SelectModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.takePhotoButton.setOnClickListener {
            startActivity(Intent(this@SelectModeActivity, CameraActivity::class.java))
        }

        binding.galleryButton.setOnClickListener {
            startActivity(Intent(this@SelectModeActivity, PhotoActivity::class.java))
        }
    }
}