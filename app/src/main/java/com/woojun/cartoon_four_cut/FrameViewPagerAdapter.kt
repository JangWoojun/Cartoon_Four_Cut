package com.woojun.cartoon_four_cut

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.woojun.cartoon_four_cut.Utils.dpToPx
import com.woojun.cartoon_four_cut.databinding.PhotoFrameLayoutBinding

class FrameViewPagerAdapter(private val frameItemList: MutableList<FrameItem>) : RecyclerView.Adapter<FrameViewPagerAdapter.ViewPagerHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        val binding = PhotoFrameLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewPagerHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        holder.bind(frameItemList[position])
    }

    override fun getItemCount(): Int {
        return frameItemList.size
    }

    class ViewPagerHolder(private val binding: PhotoFrameLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(frameItem: FrameItem) {
            binding.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                marginStart = binding.root.context.dpToPx(24f).toInt()
                marginEnd = binding.root.context.dpToPx(24f).toInt()
            }

            Glide.with(binding.root.context)
                .load(frameItem.images[0])
                .centerCrop()
                .into(binding.image1)

            Glide.with(binding.root.context)
                .load(frameItem.images[1])
                .centerCrop()
                .into(binding.image2)

            Glide.with(binding.root.context)
                .load(frameItem.images[2])
                .centerCrop()
                .into(binding.image3)

            Glide.with(binding.root.context)
                .load(frameItem.images[3])
                .centerCrop()
                .into(binding.image4)
        }
    }

}
