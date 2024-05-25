package com.woojun.cartoon_four_cut

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.woojun.cartoon_four_cut.adapter.FilterViewPagerAdapter
import com.woojun.cartoon_four_cut.database.BitmapData.getImage1
import com.woojun.cartoon_four_cut.database.BitmapData.getImage2
import com.woojun.cartoon_four_cut.database.BitmapData.getImage3
import com.woojun.cartoon_four_cut.database.BitmapData.getImage4
import com.woojun.cartoon_four_cut.database.BitmapData.setImage1
import com.woojun.cartoon_four_cut.database.BitmapData.setImage2
import com.woojun.cartoon_four_cut.database.BitmapData.setImage3
import com.woojun.cartoon_four_cut.database.BitmapData.setImage4
import com.woojun.cartoon_four_cut.util.Utils.dpToPx
import com.woojun.cartoon_four_cut.data.FilterItem
import com.woojun.cartoon_four_cut.databinding.ActivityFilterBinding
import com.woojun.cartoon_four_cut.network.RetrofitAPI
import com.woojun.cartoon_four_cut.network.RetrofitClient
import com.zomato.photofilters.SampleFilters
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding

    private var filterIndex = 0

    companion object {
        init {
            System.loadLibrary("NativeImageProcessor")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isBitmapNotNull = (getImage1() != null && getImage2() != null && getImage3() != null && getImage4() != null)

        generateFilterItemList { list ->
            val adapter = FilterViewPagerAdapter(list)

            if (isBitmapNotNull) {
                binding.viewPager.apply {
                    this.adapter = adapter
                    offscreenPageLimit = 3

                    val pageMargin = this@FilterActivity.dpToPx(8f)
                    val offset = this@FilterActivity.dpToPx(16f)
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
                            filterIndex = position
                        }
                    })
                }

                binding.selectButton.setOnClickListener {
                    val intent = Intent(this@FilterActivity, FrameActivity::class.java)

                    if (list[filterIndex].isAi) {
                        intent.putExtra("isAi", true)
                    } else {
                        setImage1(list[filterIndex].images[0])
                        setImage2(list[filterIndex].images[1])
                        setImage3(list[filterIndex].images[2])
                        setImage4(list[filterIndex].images[3])
                    }
                    intent.putExtra("name", list[filterIndex].name)

                    overridePendingTransition(R.anim.anim_slide_in_from_right_fade_in, R.anim.anim_fade_out)
                    startActivity(intent)
                }
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_in_from_left_fade_in, R.anim.anim_fade_out)
    }

    private fun generateFilterItemList(callback: (List<FilterItem>) -> Unit) {
        val filterList = mutableListOf(
            null,
            SampleFilters.getBlueMessFilter(),
            SampleFilters.getLimeStutterFilter(),
            SampleFilters.getStarLitFilter(),
            SampleFilters.getNightWhisperFilter(),
        )
        val filterNameList = mutableListOf(
            "필터 X",
            "필터 - BlueMess",
            "필터 - LimeStutter",
            "필터 - StarLit",
            "필터 - NightWhisper",
        )

        getFilter { aiFilterList ->
            callback(List(filterList.size + aiFilterList.size) { i ->
                if (i < filterList.size) {
                    FilterItem(
                        filterNameList[i],
                        mutableListOf(
                            filterList[i]!!.processFilter(getImage1()),
                            filterList[i]!!.processFilter(getImage2()),
                            filterList[i]!!.processFilter(getImage3()),
                            filterList[i]!!.processFilter(getImage4()),
                        )
                    )
                } else {
                    val aiIndex = i - filterList.size
                    FilterItem(
                        aiFilterList[aiIndex],
                        mutableListOf(
                            getImage1()!!,
                            getImage2()!!,
                            getImage3()!!,
                            getImage4()!!,
                        ),
                        true
                    )
                }
            })
        }
    }

    private fun getFilter(callback: (List<String>) -> Unit) {
        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<List<String>> = retrofitAPI.getFilter()

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(
                call: Call<List<String>>,
                response: Response<List<String>>
            ) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Toast.makeText(this@FilterActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }

}