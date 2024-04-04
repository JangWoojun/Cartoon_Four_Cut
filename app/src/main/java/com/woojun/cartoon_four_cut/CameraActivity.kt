package com.woojun.cartoon_four_cut

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Mode
import com.woojun.cartoon_four_cut.databinding.ActivityCameraBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var flashToggle = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraView.setLifecycleOwner(this)
        binding.cameraView.mode = Mode.PICTURE
        binding.cameraView.addCameraListener(object : CameraListener() {
            // 사진 촬영 결과 리스너
            override fun onPictureTaken(result: PictureResult) {
                result.toBitmap { bitmap ->
                    if (bitmap != null) {

                        CoroutineScope(Dispatchers.IO).launch {
                            val file = File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                "카툰 네컷${System.currentTimeMillis()}.jpg"
                            )

                            try {
                                withContext(Dispatchers.IO) {
                                    val fileOutputStream = FileOutputStream(file)
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                                    fileOutputStream.close()
                                }

                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }

            override fun onVideoTaken(result: VideoResult) {
                result.file.apply {
                    // 동영상 파일 사용
                }
            }

            override fun onVideoRecordingEnd() {
                super.onVideoRecordingEnd()
            }

            override fun onPictureShutter() {
            }

            override fun onVideoRecordingStart() {
            }
        })

        binding.switchButton.setOnClickListener {
            binding.cameraView.toggleFacing()
        }

        binding.captureButton.setOnClickListener {
            binding.cameraView.takePicture()
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

}