package com.woojun.cartoon_four_cut

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.woojun.cartoon_four_cut.Utils.dpToPx
import com.woojun.cartoon_four_cut.databinding.ActivityFilterBinding


class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isBitmapNotNull = (BitmapData.getImage1() != null && BitmapData.getImage2() != null && BitmapData.getImage3() != null && BitmapData.getImage4() != null)

        if (isBitmapNotNull) {
            binding.viewPager.apply {
                adapter = FilterViewPagerAdapter()

                offscreenPageLimit = 1

                val pageMargin = this@FilterActivity.dpToPx(8f)
                val offset = this@FilterActivity.dpToPx(24f)
                setPageTransformer { page, position ->
                    val pageOffset = position * -(2 * offset + pageMargin)
                    if (ViewCompat.getLayoutDirection(binding.viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.translationX = -pageOffset
                    } else {
                        page.translationX = pageOffset
                    }
                }

                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        when (FilterTypes.entries[position]) {
                            FilterTypes.None -> {}
                        }
                    }
                })
            }

            binding.selectButton.setOnClickListener {
                startActivity(Intent(this@FilterActivity, FrameActivity::class.java))
            }
        }
    }
}