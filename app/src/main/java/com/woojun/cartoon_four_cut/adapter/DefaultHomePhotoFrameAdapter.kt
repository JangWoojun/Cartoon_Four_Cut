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
import com.woojun.cartoon_four_cut.databinding.HomePhotoFrameItemBinding
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DefaultHomePhotoFrameAdapter(private val imageList: List<List<Int>>): RecyclerView.Adapter<DefaultHomePhotoFrameAdapter.HomePhotoFrameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePhotoFrameViewHolder {
        val binding = HomePhotoFrameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomePhotoFrameViewHolder(binding).also {
            binding.deleteButton.setOnClickListener {
                Toast.makeText(binding.root.context, "예시 이미지는 삭제 할 수 없습니다", Toast.LENGTH_SHORT).show()
            }
            binding.downloadButton.setOnClickListener {
                Toast.makeText(binding.root.context, "예시 이미지는 다운로드 할 수 없습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: HomePhotoFrameViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    class HomePhotoFrameViewHolder(private val binding: HomePhotoFrameItemBinding):
        ViewHolder(binding.root) {
        fun bind(imageList: List<Int>) {
            if (binding.root.context != null) {
                Glide.with(binding.root.context)
                    .load(imageList[0])
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.image1)

                Glide.with(binding.root.context)
                    .load(imageList[1])
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.image2)

                Glide.with(binding.root.context)
                    .load(imageList[2])
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.image3)

                Glide.with(binding.root.context)
                    .load(imageList[3])
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.image4)

                val currentDate = LocalDate.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")
                val dateString = currentDate.format(formatter)

                binding.dateText.text = dateString
            }
        }
    }

}