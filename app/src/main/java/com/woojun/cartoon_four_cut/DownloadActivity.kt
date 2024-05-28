package com.woojun.cartoon_four_cut

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.woojun.cartoon_four_cut.data.DownloadItem
import com.woojun.cartoon_four_cut.data.HomePhotoFrame
import com.woojun.cartoon_four_cut.database.AppDatabase
import com.woojun.cartoon_four_cut.database.BitmapData.getImage1
import com.woojun.cartoon_four_cut.database.BitmapData.getImage2
import com.woojun.cartoon_four_cut.database.BitmapData.getImage3
import com.woojun.cartoon_four_cut.database.BitmapData.getImage4
import com.woojun.cartoon_four_cut.databinding.ActivityDownloadBinding
import com.woojun.cartoon_four_cut.util.OnSingleClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DownloadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDownloadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadBinding.inflate(layoutInflater)
        overridePendingTransition(R.anim.anim_slide_in_from_right_fade_in, R.anim.anim_fade_out)
        setContentView(binding.root)

        val downloadItem = intent.getParcelableExtra<DownloadItem>("item")

        CoroutineScope(Dispatchers.IO).launch {
            val homePhotoFrameDao = AppDatabase.getDatabase(this@DownloadActivity)?.homePhotoFrameItemDao()
            if (downloadItem!!.isAi) {
                homePhotoFrameDao?.insertHomePhotoFrame(generateEntity(null, downloadItem))
            } else {
                homePhotoFrameDao?.insertHomePhotoFrame(
                    generateEntity(
                        listOf(getImage1()!!, getImage2()!!, getImage3()!!, getImage4()!!),
                        downloadItem
                    )
                )
            }

            withContext(Dispatchers.Main) {
                if (downloadItem!!.isAi) {
                    Glide.with(this@DownloadActivity)
                        .load(downloadItem.images[0])
                        .centerCrop()
                        .into(binding.image1)
                    Glide.with(this@DownloadActivity)
                        .load(downloadItem.images[1])
                        .centerCrop()
                        .into(binding.image2)
                    Glide.with(this@DownloadActivity)
                        .load(downloadItem.images[2])
                        .centerCrop()
                        .into(binding.image3)
                    Glide.with(this@DownloadActivity)
                        .load(downloadItem.images[3])
                        .centerCrop()
                        .into(binding.image4)
                } else {
                    Glide.with(this@DownloadActivity)
                        .load(getImage1())
                        .centerCrop()
                        .into(binding.image1)
                    Glide.with(this@DownloadActivity)
                        .load(getImage2())
                        .centerCrop()
                        .into(binding.image2)
                    Glide.with(this@DownloadActivity)
                        .load(getImage3())
                        .centerCrop()
                        .into(binding.image3)
                    Glide.with(this@DownloadActivity)
                        .load(getImage4())
                        .centerCrop()
                        .into(binding.image4)
                }

                Glide.with(this@DownloadActivity)
                    .load(downloadItem.frameResponse.background)
                    .into(binding.backgroundImage)

                Glide.with(this@DownloadActivity)
                    .load(downloadItem.frameResponse.top)
                    .into(binding.topImage)

                Glide.with(this@DownloadActivity)
                    .load(downloadItem.frameResponse.bottom)
                    .into(binding.bottomImage)

                binding.downloadButton.setOnClickListener(object : OnSingleClickListener() {
                    override fun onSingleClick(v: View?) {
                        val bitmap = viewToImage(binding.mainFrame)
                        saveImageOnAboveAndroidQ(bitmap)
                    }
                })

                binding.selectButton.setOnClickListener(object : OnSingleClickListener() {
                    override fun onSingleClick(v: View?) {
                        val intent = Intent(this@DownloadActivity, MainActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            putExtra("UPDATE", true)
                        }
                        startActivity(intent)
                        overridePendingTransition(R.anim.anim_fade_out, R.anim.vertical_exit)
                    }
                })

            }
        }

    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun generateEntity(bitmapList: List<Bitmap>?, downloadItem: DownloadItem): HomePhotoFrame {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")
        val dateString = currentDate.format(formatter)

        return if (bitmapList != null) {
            val imageStringList = mutableListOf<String>()
            for (bitmap in bitmapList) {
                val imageString = bitmapToBase64(bitmap)
                imageStringList.add(imageString)
            }
            HomePhotoFrame(
                downloadItem = DownloadItem(imageStringList, downloadItem.frameResponse),
                date = dateString
            )
        } else {
            HomePhotoFrame(downloadItem = downloadItem, date = dateString)
        }

    }



    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@DownloadActivity, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra("UPDATE", true)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.anim_fade_out, R.anim.vertical_exit)
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