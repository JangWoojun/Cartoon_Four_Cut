package com.woojun.cartoon_four_cut

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.woojun.cartoon_four_cut.databinding.ActivityPhotoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoBinding
    private var selectFrame: Int = 1
    private val imageList: MutableList<Pair<Int, Uri>> = mutableListOf()

    private var pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri.let {
            if (uri != null) {
                when (selectFrame) {
                    1 -> {
                        Glide.with(this@PhotoActivity)
                            .load(it)
                            .centerCrop()
                            .into(binding.image1)
                        imageList.add(Pair(selectFrame, uri))
                    }
                    2 -> {
                        Glide.with(this@PhotoActivity)
                            .load(it)
                            .centerCrop()
                            .into(binding.image2)
                        imageList.add(Pair(selectFrame, uri))
                    }
                    3 -> {
                        Glide.with(this@PhotoActivity)
                            .load(it)
                            .centerCrop()
                            .into(binding.image3)
                        imageList.add(Pair(selectFrame, uri))
                    }
                    4 -> {
                        Glide.with(this@PhotoActivity)
                            .load(it)
                            .centerCrop()
                            .into(binding.image4)
                        imageList.add(Pair(selectFrame, uri))
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        overridePendingTransition(R.anim.anim_slide_in_from_right_fade_in, R.anim.anim_fade_out)
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
            if (imageList.size == 4) {
                CoroutineScope(Dispatchers.IO).launch {
                    imageList.forEach {
                        val bitmap = getBitmap(it.second)
                        when (it.first) {
                            1 -> BitmapData.setImage1(bitmap)
                            2 -> BitmapData.setImage2(bitmap)
                            3 -> BitmapData.setImage3(bitmap)
                            4 -> BitmapData.setImage4(bitmap)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        overridePendingTransition(R.anim.anim_slide_in_from_right_fade_in, R.anim.anim_fade_out)
                        startActivity(Intent(this@PhotoActivity, FilterActivity::class.java))
                    }
                }
            } else {
                Toast.makeText(this@PhotoActivity, "모든 사진을 선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }


        binding.backButton.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.anim_slide_in_from_left_fade_in, R.anim.anim_fade_out)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_in_from_left_fade_in, R.anim.anim_fade_out)
    }

    private fun getBitmap(imageUri: Uri): Bitmap {
        return ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                contentResolver,
                imageUri
            )
        )
    }
}