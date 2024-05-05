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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.woojun.cartoon_four_cut.BitmapData.getImage1
import com.woojun.cartoon_four_cut.BitmapData.getImage2
import com.woojun.cartoon_four_cut.BitmapData.getImage3
import com.woojun.cartoon_four_cut.BitmapData.getImage4
import com.woojun.cartoon_four_cut.databinding.ActivityDownloadBinding
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class DownloadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDownloadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadBinding.inflate(layoutInflater)
        overridePendingTransition(R.anim.anim_slide_in_from_right_fade_in, R.anim.anim_fade_out)
        setContentView(binding.root)

        val layout = intent.getIntExtra("layout", 0)

        val inflater = layoutInflater
        val layoutItem = inflater.inflate(layout, null, false)

        binding.includedLayout.addView(layoutItem)

        val textView = layoutItem.findViewById<TextView>(R.id.filter_text)
        textView.visibility = View.GONE

        val image1 = layoutItem.findViewById<ImageView>(R.id.image1)
        image1.setImageBitmap(getImage1())

        val image2 = layoutItem.findViewById<ImageView>(R.id.image2)
        image2.setImageBitmap(getImage2())

        val image3 = layoutItem.findViewById<ImageView>(R.id.image3)
        image3.setImageBitmap(getImage3())

        val image4 = layoutItem.findViewById<ImageView>(R.id.image4)
        image4.setImageBitmap(getImage4())

        binding.downloadButton.setOnClickListener {
            val mainFrame = layoutItem.findViewById<CardView>(R.id.main_frame)
            val bitmap = viewToImage(mainFrame)
            saveImageOnAboveAndroidQ(bitmap)
        }

        binding.selectButton.setOnClickListener {
            startActivity(Intent(this@DownloadActivity, MainActivity::class.java))
            finishAffinity()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_in_from_left_fade_in, R.anim.anim_fade_out)
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