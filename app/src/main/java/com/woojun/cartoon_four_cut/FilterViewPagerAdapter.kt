package com.woojun.cartoon_four_cut

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.woojun.cartoon_four_cut.Utils.dpToPx
import com.woojun.cartoon_four_cut.databinding.PhotoFrameLayoutBinding

class FilterViewPagerAdapter(private val filterItemList: MutableList<FilterItem>) : RecyclerView.Adapter<FilterViewPagerAdapter.ViewPagerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        val binding = PhotoFrameLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ViewPagerHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        holder.bind(filterItemList[position])
    }

    override fun getItemCount(): Int {
        return filterItemList.size
    }


    class ViewPagerHolder(private val binding: PhotoFrameLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(filterItem: FilterItem) {
            binding.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                marginStart = binding.root.context.dpToPx(24f).toInt()
                marginEnd = binding.root.context.dpToPx(24f).toInt()
            }

            binding.filterText.text = filterItem.name

            Glide.with(binding.root.context)
                .load(filterItem.images[0])
                .centerCrop()
                .into(binding.image1)

            Glide.with(binding.root.context)
                .load(filterItem.images[1])
                .centerCrop()
                .into(binding.image2)

            Glide.with(binding.root.context)
                .load(filterItem.images[2])
                .centerCrop()
                .into(binding.image3)

            Glide.with(binding.root.context)
                .load(filterItem.images[3])
                .centerCrop()
                .into(binding.image4)
        }
    }

}
