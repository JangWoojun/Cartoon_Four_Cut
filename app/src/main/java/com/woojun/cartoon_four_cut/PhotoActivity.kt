package com.woojun.cartoon_four_cut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.woojun.cartoon_four_cut.databinding.ActivityPhotoBinding

class PhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoBinding
    private var selectFrame: Int = 1

    private var pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()) { uri ->
        uri.let {
            when (selectFrame) {
                1 -> {
                    Glide.with(this@PhotoActivity)
                        .load(it)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image1)
                }
                2 -> {
                    Glide.with(this@PhotoActivity)
                        .load(it)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image2)
                }
                3 -> {
                    Glide.with(this@PhotoActivity)
                        .load(it)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image3)
                }
                4 -> {
                    Glide.with(this@PhotoActivity)
                        .load(it)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image4)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageFrame1.setOnClickListener {
            selectFrame = 1
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.imageFrame2.setOnClickListener {
            selectFrame = 2
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.imageFrame3.setOnClickListener {
            selectFrame = 3
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.imageFrame4.setOnClickListener {
            selectFrame = 4
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.selectButton.setOnClickListener {

        }


        binding.backButton.setOnClickListener {
            finish()
        }
    }
}