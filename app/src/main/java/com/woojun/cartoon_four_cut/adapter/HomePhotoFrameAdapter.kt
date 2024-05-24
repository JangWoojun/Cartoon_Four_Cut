package com.woojun.cartoon_four_cut.adapter

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
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
import com.woojun.cartoon_four_cut.HomePhotoFrame
import com.woojun.cartoon_four_cut.R
import com.woojun.cartoon_four_cut.databinding.HomePhotoFrameItemBinding
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
                Glide.with(binding.root.context)
                    .load(photoFrame.image1)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.image1)

                Glide.with(binding.root.context)
                    .load(photoFrame.image2)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.image2)
                Glide.with(binding.root.context)

                    .load(photoFrame.image3)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.image3)
                Glide.with(binding.root.context)

                    .load(photoFrame.image4)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.image4)
            }
            binding.dateText.text = photoFrame.date
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
            // TODO HomePhotoFrame 삭제
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