package com.woojun.cartoon_four_cut

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.woojun.cartoon_four_cut.BitmapData.getImage1
import com.woojun.cartoon_four_cut.BitmapData.getImage2
import com.woojun.cartoon_four_cut.BitmapData.getImage3
import com.woojun.cartoon_four_cut.BitmapData.getImage4
import com.woojun.cartoon_four_cut.databinding.ActivityFilterBinding


class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (getImage1() != null && getImage2() != null && getImage3() != null && getImage4() != null) {
            binding.viewPager.adapter = ViewPagerAdapter()
            binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {

                }
            })
        }
    }
}