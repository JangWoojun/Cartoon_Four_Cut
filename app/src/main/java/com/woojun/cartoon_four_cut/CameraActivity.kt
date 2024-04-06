package com.woojun.cartoon_four_cut

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Flash
import com.woojun.cartoon_four_cut.Utils.dpToPx
import com.woojun.cartoon_four_cut.databinding.ActivityCameraBinding


class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding

    private var flashToggle = false
    private var imageCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraView.setLifecycleOwner(this)
        binding.cameraView.addCameraListener(object : CameraListener() {

            override fun onPictureTaken(result: PictureResult) {
                result.toBitmap { bitmap ->
                    if (bitmap != null) {
                        imageCount++
                        binding.countText.text = "${imageCount}/4"
                        binding.cameraView.post {
                            val image = cropBitmap(this@CameraActivity, bitmap)

                            when(imageCount) {
                                1 -> BitmapData.setImage1(image)
                                2 -> BitmapData.setImage2(image)
                                3 -> BitmapData.setImage3(image)
                                4 -> {
                                    BitmapData.setImage4(image)
                                    startActivity(Intent(this@CameraActivity, FilterActivity::class.java))
                                    finish()
                                }
                            }
                        }
                    }
                }
                binding.captureButton.isEnabled = true
            }
            override fun onPictureShutter() {
            }
        })

        binding.switchButton.setOnClickListener {
            binding.cameraView.toggleFacing()
        }

        binding.captureButton.setOnClickListener {
            it.isEnabled = false
            binding.pulseCountDown.start {
                binding.cameraView.takePictureSnapshot()
            }
        }

        binding.flashButton.setOnClickListener {
            flashToggle = !flashToggle
            if (flashToggle) {
                binding.cameraView.flash = Flash.ON
            } else {
                binding.cameraView.flash = Flash.OFF
            }
        }

    }

    fun cropBitmap(context: Context, bitmap: Bitmap): Bitmap {
        val bitmapWidth = bitmap.width
        val startY = context.dpToPx(146.5f).toInt()

        return Bitmap.createBitmap(bitmap, 0, startY, bitmapWidth, bitmapWidth)
    }

}