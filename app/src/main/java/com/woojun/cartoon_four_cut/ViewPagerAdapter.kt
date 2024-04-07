package com.woojun.cartoon_four_cut

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.woojun.cartoon_four_cut.custom_view.FilterTypes
import com.woojun.cartoon_four_cut.databinding.PhotoFrameLayoutBinding

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerAdapter>() {
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
            when (filterType) {
                FilterTypes.None -> {
                    binding.image1.setImageBitmap(image1)
                    binding.image2.setImageBitmap(image2)
                    binding.image3.setImageBitmap(image3)
                    binding.image4.setImageBitmap(image4)
                }
            }
        }
    }

}
