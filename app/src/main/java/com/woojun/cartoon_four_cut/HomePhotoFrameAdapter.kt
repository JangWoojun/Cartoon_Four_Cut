package com.woojun.cartoon_four_cut

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.woojun.cartoon_four_cut.databinding.HomePhotoFrameItemBinding

class HomePhotoFrameAdapter(private val photoFrameList: MutableList<HomePhotoFrame>): RecyclerView.Adapter<HomePhotoFrameAdapter.HomePhotoFrameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePhotoFrameViewHolder {
        val binding = HomePhotoFrameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomePhotoFrameViewHolder(binding).also { handler->
            binding.deleteButton.setOnClickListener {
                showDialog(binding.root.context, handler.position)
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

}