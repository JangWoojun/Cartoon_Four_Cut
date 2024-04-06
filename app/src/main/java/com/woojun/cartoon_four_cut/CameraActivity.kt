package com.woojun.cartoon_four_cut

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Mode
import com.woojun.cartoon_four_cut.Utils.dpToPx
import com.woojun.cartoon_four_cut.databinding.ActivityCameraBinding
import java.io.FileOutputStream


class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding

    private var flashToggle = false
    private var captureCount = 1

    private val imageList = mutableListOf<Pair<String, Bitmap>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraView.setLifecycleOwner(this)
        binding.cameraView.mode = Mode.PICTURE
        binding.cameraView.addCameraListener(object : CameraListener() {

            override fun onPictureTaken(result: PictureResult) {
                result.toBitmap { bitmap ->
                    if (bitmap != null) {
                        captureCount++
                        binding.countText.text = "${captureCount}/4"

                        imageList.add(Pair("${System.currentTimeMillis()}.png", cropBitmap(baseContext, bitmap)))

                        if (imageList.size == 4) {
                            val intent = Intent(this@CameraActivity, FilterActivity::class.java)

                            imageList.forEach {
                                val (filename, bmp) = it

                                try {
                                    val stream: FileOutputStream = openFileOutput(filename, MODE_PRIVATE)
                                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)

                                    stream.close()
                                    bmp.recycle()

                                    intent.putExtra("images", filename)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            startActivity(intent)
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