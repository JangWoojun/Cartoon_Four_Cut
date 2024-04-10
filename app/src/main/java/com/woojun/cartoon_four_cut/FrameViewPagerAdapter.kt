package com.woojun.cartoon_four_cut

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.woojun.cartoon_four_cut.databinding.PhotoFrameLayoutBinding

class FrameViewPagerAdapter : RecyclerView.Adapter<FrameViewPagerAdapter.ViewPagerAdapter>() {
    private val frameList = FrameTypes.entries.toTypedArray()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter {
        val binding = PhotoFrameLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ViewPagerAdapter(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter, position: Int) {
        holder.bind(frameList[position])
    }

    override fun getItemCount(): Int {
        return frameList.size
    }


    class ViewPagerAdapter(private val binding: PhotoFrameLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val image1 = BitmapData.getImage1()
        private val image2 = BitmapData.getImage2()
        private val image3 = BitmapData.getImage3()
        private val image4 = BitmapData.getImage4()

        fun bind(frameTypes: FrameTypes) {
            when (frameTypes) {
                FrameTypes.None -> {
                    Glide.with(binding.root.context)
                        .load(image1)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image1)

                    Glide.with(binding.root.context)
                        .load(image2)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image2)

                    Glide.with(binding.root.context)
                        .load(image3)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image3)

                    Glide.with(binding.root.context)
                        .load(image4)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.image4)
                }
            }
        }
    }

}
