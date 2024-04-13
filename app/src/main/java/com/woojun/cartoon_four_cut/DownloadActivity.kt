package com.woojun.cartoon_four_cut

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.woojun.cartoon_four_cut.databinding.ActivityDownloadBinding
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class DownloadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDownloadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.downloadButton.setOnClickListener {
            val bitmap: Bitmap = viewToImage(binding.mainFrame)
            saveImageOnAboveAndroidQ(bitmap)
        }

        binding.selectButton.setOnClickListener {
            startActivity(Intent(this@DownloadActivity, MainActivity::class.java))
            finishAffinity()
        }
    }

    private fun saveImageOnAboveAndroidQ(bitmap: Bitmap) {
        val fileName = System.currentTimeMillis().toString() + ".png"

        val contentValues = ContentValues()
        contentValues.apply {
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/ImageSave")
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            if(uri != null) {
                contentResolver.openFileDescriptor(uri, "w", null).use {
                    if(it != null) {
                        val fos = FileOutputStream(it.fileDescriptor)
                        bitmap.compress(CompressFormat.PNG, 100, fos)
                        fos.close()

                        contentValues.clear()
                        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                        contentResolver.update(uri, contentValues, null, null)
                        Toast.makeText(this@DownloadActivity, "이미지 저장이 완료되었습니다", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DownloadActivity, "이미지 저장을 실패하였습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@DownloadActivity, "이미지 저장을 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
        } catch(e: FileNotFoundException) {
            Toast.makeText(this@DownloadActivity, "이미지 저장을 실패하였습니다", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(this@DownloadActivity, "이미지 저장을 실패하였습니다", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this@DownloadActivity, "이미지 저장을 실패하였습니다", Toast.LENGTH_SHORT).show()
        }
    }


    private fun viewToImage(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnedBitmap
    }

}