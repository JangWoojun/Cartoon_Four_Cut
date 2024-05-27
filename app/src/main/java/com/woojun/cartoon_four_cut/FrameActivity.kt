package com.woojun.cartoon_four_cut

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.woojun.cartoon_four_cut.util.Dialog
import com.woojun.cartoon_four_cut.util.OnSingleClickListener
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
        overridePendingTransition(R.anim.anim_slide_in_from_right_fade_in, R.anim.anim_fade_out)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val isAi = intent.getBooleanExtra("isAi", false)

        val isBitmapNotNull = (getImage1() != null && getImage2() != null && getImage3() != null && getImage4() != null)

        CoroutineScope(Dispatchers.Main).launch {
            val list = generateFrameItemList(name!!, isAi)
            if (isBitmapNotNull) {
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

                binding.selectButton.setOnClickListener(object : OnSingleClickListener() {
                    override fun onSingleClick(v: View?) {
                        val (loadingDialog, setDialogText) = Dialog.createLoadingDialog(this@FrameActivity)
                        loadingDialog.show()
                        setDialogText("세팅 중")

                        val intent = Intent(this@FrameActivity, DownloadActivity::class.java)
                        if (isAi) {
                            uploadAiImages(name, loadingDialog, setDialogText) { uploadResponse ->
                                intent.putExtra("item", DownloadItem(uploadResponse, list[frameIndex].frameResponse, true))
                                startActivity(intent)
                            }
                        } else {
                            intent.putExtra("item", DownloadItem(listOf(""), list[frameIndex].frameResponse, false))
                            startActivity(intent)
                        }

                    }
                })

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_in_from_left_fade_in, R.anim.anim_fade_out)
    }

    private fun generateAiImages(imageNameList: List<String>, callback: (List<String>) -> Unit, loadingDialog: android.app.Dialog, setDialogText: ((String) -> Unit)) {
        loadingDialog.show()
        setDialogText("이미지 A.I 변환 중")

        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val handler = Handler(Looper.getMainLooper())

        val callApi = object : Runnable {
            override fun run() {
                val call: Call<List<String>> = retrofitAPI.postImage(imageNameList)
                val thisRunnable = this

                call.enqueue(object : Callback<List<String>> {
                    override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                        if (response.isSuccessful && response.body()!!.size == 4) {
                            callback(response.body()!!)
                            loadingDialog.dismiss()
                        } else if (response.isSuccessful) {
                            handler.postDelayed(thisRunnable, 20000)
                        } else {
                            setDialogText("변환 실패")
                            loadingDialog.dismiss()
                            Toast.makeText(this@FrameActivity, "A.I 필터 사용은\n하루 최대 10번만 가능합니다", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<String>>, t: Throwable) {
                        setDialogText("변환 실패")
                        loadingDialog.dismiss()
                        Toast.makeText(this@FrameActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        handler.post(callApi)
    }


    private fun uploadAiImages(type: String, loadingDialog: android.app.Dialog, setDialogText: ((String) -> Unit), callback: (List<String>) -> Unit) {
        setDialogText("이미지 업로드 중")

        val images = listOf(getImage1()!!, getImage2()!!, getImage3()!!, getImage4()!!)
        val imageParts = createPartsFromBitmaps(images, System.currentTimeMillis().toString())

        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<List<String>> = retrofitAPI.putImage(loadId(this@FrameActivity)!!, type, imageParts)

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(
                call: Call<List<String>>,
                response: Response<List<String>>
            ) {
                if (response.isSuccessful) {
                    setDialogText("업로드 완료")
                    generateAiImages(response.body()!!, callback, loadingDialog, setDialogText)
                } else {
                    setDialogText("업로드 실패")
                    loadingDialog.dismiss()
                    Toast.makeText(this@FrameActivity, "A.I 필터는 사용은\n하루 최대 10번만 가능합니다", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                setDialogText("변환 실패")
                loadingDialog.dismiss()
                Toast.makeText(this@FrameActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
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

    private fun createFrameItem(name: String, response: FrameResponse, isAi: Boolean): FrameItem {
        return FrameItem(
            name,
            response,
            mutableListOf(
                getImage1()!!,
                getImage2()!!,
                getImage3()!!,
                getImage4()!!
            ),
            isAi
        )
    }

    private suspend fun generateFrameItemList(name: String, isAi: Boolean): List<FrameItem> {
        return withContext(Dispatchers.Main) {
            val list = mutableListOf(createFrameItem(name, FrameResponse("", "", "", "카툰네컷 기본 프레임 - 화이트"), isAi))
            getFrame()?.forEach {
                list.add(createFrameItem(name, it, isAi))
            }

            list
        }
    }

    private suspend fun getFrame(): List<FrameResponse>? {
        val (loadingDialog, setDialogText) = Dialog.createLoadingDialog(this)
        loadingDialog.show()
        setDialogText("프레임 로딩 중")

        return try {
            withContext(Dispatchers.IO) {
                val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
                val response = retrofitAPI.getFrame()
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        setDialogText("프레임 로딩 완료")
                        loadingDialog.dismiss()
                    }
                    response.body()
                } else {
                    withContext(Dispatchers.Main) {
                        setDialogText("프레임 로딩 실패")
                        loadingDialog.dismiss()
                    }
                    null
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@FrameActivity, "다른 프레임을 불러오지 못했습니다", Toast.LENGTH_SHORT).show()
                setDialogText("네트워크 오류 발생")
                loadingDialog.dismiss()
            }
            null
        }
    }
}