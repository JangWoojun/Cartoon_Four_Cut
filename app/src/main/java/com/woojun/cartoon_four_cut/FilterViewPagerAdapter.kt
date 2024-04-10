package com.woojun.cartoon_four_cut

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.woojun.cartoon_four_cut.Utils.dpToPx
import com.woojun.cartoon_four_cut.databinding.PhotoFrameLayoutBinding

class FilterViewPagerAdapter : RecyclerView.Adapter<FilterViewPagerAdapter.ViewPagerAdapter>() {
    private val filterList = FilterTypes.entries.toTypedArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter {
        val binding = PhotoFrameLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ViewPagerAdapter(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter, position: Int) {
        holder.bind(filterList[position])
    }

    override fun getItemCount(): Int {
        return filterList.size
    }


    class ViewPagerAdapter(private val binding: PhotoFrameLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val image1 = BitmapData.getImage1()
        private val image2 = BitmapData.getImage2()
        private val image3 = BitmapData.getImage3()
        private val image4 = BitmapData.getImage4()

        fun bind(filterType: FilterTypes) {
            binding.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                marginStart = binding.root.context.dpToPx(24f).toInt()
                marginEnd = binding.root.context.dpToPx(24f).toInt()
            }

            when (filterType) {
                FilterTypes.None -> {
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
