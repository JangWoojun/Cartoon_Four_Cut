package com.woojun.cartoon_four_cut.adapter

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.woojun.cartoon_four_cut.data.HomePhotoFrame
import com.woojun.cartoon_four_cut.R
import com.woojun.cartoon_four_cut.database.AppDatabase
import com.woojun.cartoon_four_cut.databinding.HomePhotoFrameItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class HomePhotoFrameAdapter(private val photoFrameList: MutableList<HomePhotoFrame>): RecyclerView.Adapter<HomePhotoFrameAdapter.HomePhotoFrameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePhotoFrameViewHolder {
        val binding = HomePhotoFrameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomePhotoFrameViewHolder(binding).also { handler->
            binding.deleteButton.setOnClickListener {
                showDialog(binding.root.context, handler.position)
            }
            binding.downloadButton.setOnClickListener {
                val bitmap = viewToImage(binding.mainFrame)
                saveImageOnAboveAndroidQ(binding.root.context, bitmap)
            }
        }
    }

    override fun getItemCount(): Int {
        return photoFrameList.size
    }

    override fun onBindViewHolder(holder: HomePhotoFrameViewHolder, position: Int) {
        holder.bind(photoFrameList[position])
    }

    class HomePhotoFrameViewHolder(private val binding: HomePhotoFrameItemBinding):
        ViewHolder(binding.root) {
        fun bind(photoFrame: HomePhotoFrame) {
            if (binding.root.context != null) {
                if (photoFrame.downloadItem.isAi) {
                    Glide.with(binding.root.context)
                        .load(photoFrame.downloadItem.images[0])
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image1)

                    Glide.with(binding.root.context)
                        .load(photoFrame.downloadItem.images[1])
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image2)

                    Glide.with(binding.root.context)
                        .load(photoFrame.downloadItem.images[2])
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image3)

                    Glide.with(binding.root.context)
                        .load(photoFrame.downloadItem.images[3])
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image4)
                } else {
                    val images = getBitmapImages(photoFrame)
                    Glide.with(binding.root.context)
                        .load(images[0])
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image1)

                    Glide.with(binding.root.context)
                        .load(images[1])
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image2)

                    Glide.with(binding.root.context)
                        .load(images[2])
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image3)

                    Glide.with(binding.root.context)
                        .load(images[3])
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image4)
                }

                binding.dateText.text = photoFrame.date

                val frameResponse = photoFrame.downloadItem.frameResponse
                Glide.with(binding.root.context)
                    .load(frameResponse.top)
                    .into(binding.topImage)
                Glide.with(binding.root.context)
                    .load(frameResponse.bottom)
                    .into(binding.bottomImage)
                Glide.with(binding.root.context)
                    .load(frameResponse.background)
                    .into(binding.backgroundImage)
            }
        }
        private fun getBitmapImages(photoFrame: HomePhotoFrame): List<Bitmap> {
            val entity = photoFrame.downloadItem.images

            val bitmapList = mutableListOf<Bitmap>()
            val imageStrings = listOf(entity[0], entity[1], entity[2], entity[3])

            for (imageString in imageStrings) {
                val decodedString: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
                val decodedBitmap: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                bitmapList.add(decodedBitmap)
            }
            return bitmapList
        }

    }

    private fun removeItem(context: Context, index: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val homePhotoFrameDao = AppDatabase.getDatabase(context)?.homePhotoFrameItemDao()
            val list = homePhotoFrameDao!!.getHomePhotoFrameList()
            homePhotoFrameDao.deleteHomePhotoFrame(list[index])

            photoFrameList.removeAt(index)
            withContext(Dispatchers.Main) {
                notifyItemRemoved(index)
            }
        }
    }


    private fun showDialog(context: Context, index: Int) {
        val dialog = Dialog(context).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_layout)
        }

        val cancelButton = dialog.findViewById<CardView>(R.id.cancel_button)
        val deleteButton = dialog.findViewById<CardView>(R.id.delete_button)

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        deleteButton.setOnClickListener {
            removeItem(context, index)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun viewToImage(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnedBitmap
    }

    private fun saveImageOnAboveAndroidQ(context: Context, bitmap: Bitmap) {
        val fileName = System.currentTimeMillis().toString() + ".png"

        val contentValues = ContentValues()
        contentValues.apply {
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/ImageSave")
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            if(uri != null) {
                context.contentResolver.openFileDescriptor(uri, "w", null).use {
                    if(it != null) {
                        val fos = FileOutputStream(it.fileDescriptor)
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                        fos.close()

                        contentValues.clear()
                        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                        context.contentResolver.update(uri, contentValues, null, null)
                        Toast.makeText(context, "이미지 저장이 완료되었습니다", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "이미지 저장을 실패하였습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "이미지 저장을 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
        } catch(e: FileNotFoundException) {
            Toast.makeText(context, "이미지 저장을 실패하였습니다", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(context, "이미지 저장을 실패하였습니다", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "이미지 저장을 실패하였습니다", Toast.LENGTH_SHORT).show()
        }
    }

}