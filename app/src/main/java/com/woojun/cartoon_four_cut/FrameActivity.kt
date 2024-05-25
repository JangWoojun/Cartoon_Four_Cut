package com.woojun.cartoon_four_cut

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.woojun.cartoon_four_cut.adapter.FrameViewPagerAdapter
import com.woojun.cartoon_four_cut.data.DownloadItem
import com.woojun.cartoon_four_cut.data.FrameItem
import com.woojun.cartoon_four_cut.data.FrameResponse
import com.woojun.cartoon_four_cut.database.BitmapData.getImage1
import com.woojun.cartoon_four_cut.database.BitmapData.getImage2
import com.woojun.cartoon_four_cut.database.BitmapData.getImage3
import com.woojun.cartoon_four_cut.database.BitmapData.getImage4
import com.woojun.cartoon_four_cut.database.Preferences.loadId
import com.woojun.cartoon_four_cut.databinding.ActivityFrameBinding
import com.woojun.cartoon_four_cut.network.RetrofitAPI
import com.woojun.cartoon_four_cut.network.RetrofitClient
import com.woojun.cartoon_four_cut.util.Utils.dpToPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class FrameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrameBinding
    private var frameIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val isAi = intent.getBooleanExtra("isAi", false)

        val isBitmapNotNull = (getImage1() != null && getImage2() != null && getImage3() != null && getImage4() != null)

        CoroutineScope(Dispatchers.Main).launch {
            val list = generateFrameItemList(name!!, isAi)
            if (list != null && isBitmapNotNull) {
                val adapter = FrameViewPagerAdapter(list)
                binding.viewPager.apply {
                    this.adapter = adapter
                    offscreenPageLimit = 3

                    val pageMargin = this@FrameActivity.dpToPx(8f)
                    val offset = this@FrameActivity.dpToPx(16f)
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
                            frameIndex = position
                        }
                    })
                }

                binding.selectButton.setOnClickListener {
                    val intent = Intent(this@FrameActivity, DownloadActivity::class.java)
                    if (isAi) {
                        generateAiImages(name) { uploadResponse ->
                            intent.putExtra("item", DownloadItem(uploadResponse, list[frameIndex].frameResponse, true))
                            startActivity(intent)
                            finishAffinity()
                        }
                    } else {
                        intent.putExtra("item", DownloadItem(listOf(""), list[frameIndex].frameResponse, false))
                        startActivity(intent)
                        finishAffinity()
                    }

                }

            }


        }
    }

    private fun generateAiImages(type: String, callback: (List<String>) -> Unit) {
        val images = listOf(getImage1()!!, getImage2()!!, getImage3()!!, getImage4()!!)
        val imageParts = createPartsFromBitmaps(images, System.currentTimeMillis().toString())

        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<List<String>> = retrofitAPI.postUpload(loadId(this@FrameActivity)!!, type, imageParts)

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(
                call: Call<List<String>>,
                response: Response<List<String>>
            ) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                } else {
                    Toast.makeText(this@FrameActivity, "A.I 필터는 사용은\n하루 최대 10번만 가능합니다", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Toast.makeText(this@FrameActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createPartsFromBitmaps(files: List<Bitmap>, name: String): List<MultipartBody.Part> {
        val parts = mutableListOf<MultipartBody.Part>()

        for ((index, bitmap) in files.withIndex()) {
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            val imageData = bos.toByteArray()

            val requestFile = RequestBody.create(MediaType.parse("image/*"), imageData)

            val part = MultipartBody.Part.createFormData("images", "$name-$index.jpg", requestFile)
            parts.add(part)
        }

        return parts
    }

    private suspend fun generateFrameItemList(name: String, isAi: Boolean): List<FrameItem>? {
        return withContext(Dispatchers.Main) {
            getFrame()?.map {
                FrameItem(
                    name,
                    it,
                    mutableListOf(
                        getImage1()!!,
                        getImage2()!!,
                        getImage3()!!,
                        getImage4()!!,
                    ),
                    isAi
                )
            }
        }
    }

    private suspend fun getFrame(): List<FrameResponse>? {
        return withContext(Dispatchers.IO) {
            val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
            val response = retrofitAPI.getFrame()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
    }
}