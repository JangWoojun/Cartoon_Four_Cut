package com.woojun.cartoon_four_cut.network

import com.woojun.cartoon_four_cut.BuildConfig
import com.woojun.cartoon_four_cut.data.AuthResponse
import com.woojun.cartoon_four_cut.data.FrameResponse
import com.woojun.cartoon_four_cut.data.UploadRequest
import com.woojun.cartoon_four_cut.data.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface RetrofitAPI {
    @GET("${BuildConfig.baseUrl}auth")
    fun getAuthId(): Call<AuthResponse>

    @GET("${BuildConfig.baseUrl}frame")
    fun getFrame(): Call<FrameResponse>

    @Multipart
    @POST("${BuildConfig.baseUrl}upload")
    fun postUpload(
        @Header("Header") header: UploadRequest,
        @Part body: MultipartBody.Part,
    ): Call<UploadResponse>

    @GET("${BuildConfig.baseUrl}filter")
    fun getFilter(): Call<List<String>>

}