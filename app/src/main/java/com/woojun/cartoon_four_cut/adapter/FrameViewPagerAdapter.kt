package com.woojun.cartoon_four_cut.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.woojun.cartoon_four_cut.util.Utils.dpToPx
import com.woojun.cartoon_four_cut.data.FrameItem
import com.woojun.cartoon_four_cut.databinding.PhotoFrameLayoutBinding
import jp.wasabeef.glide.transformations.BlurTransformation

class FrameViewPagerAdapter(private val frameItemList: List<FrameItem>) : RecyclerView.Adapter<FrameViewPagerAdapter.ViewPagerHolder>()  {

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

            if (frameItem.isAi) {
                Glide.with(binding.root.context)
                    .load(frameItem.images[0])
                    .transform(MultiTransformation(CenterCrop(), BlurTransformation(15, 3)))
                    .into(binding.image1)

                Glide.with(binding.root.context)
                    .load(frameItem.images[1])
                    .transform(MultiTransformation(CenterCrop(), BlurTransformation(15, 3)))
                    .into(binding.image2)

                Glide.with(binding.root.context)
                    .load(frameItem.images[2])
                    .transform(MultiTransformation(CenterCrop(), BlurTransformation(15, 3)))
                    .into(binding.image3)

                Glide.with(binding.root.context)
                    .load(frameItem.images[3])
                    .transform(MultiTransformation(CenterCrop(), BlurTransformation(15, 3)))
                    .into(binding.image4)
            } else {
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
            binding.filterText.text = frameItem.frameResponse.name

            val frameResponse = frameItem.frameResponse

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

}
