package com.woojun.cartoon_four_cut.network

import com.woojun.cartoon_four_cut.BuildConfig
import com.woojun.cartoon_four_cut.data.AuthResponse
import com.woojun.cartoon_four_cut.data.FrameResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part


interface RetrofitAPI {
    @GET("${BuildConfig.baseUrl}auth")
    suspend fun getAuthId(): Response<AuthResponse>

    @GET("${BuildConfig.baseUrl}frame")
    suspend fun getFrame(): Response<List<FrameResponse>>

    @GET("${BuildConfig.baseUrl}filter")
    fun getFilter(): Call<List<String>>

    @Multipart
    @PUT("${BuildConfig.baseUrl}image")
    fun putImage(
        @Header("auth") auth: String,
        @Header("type") type: String,
        @Part body: List<MultipartBody.Part>,
    ): Call<List<String>>

    @POST("${BuildConfig.baseUrl}image")
    fun postImage(
        @Body body: List<String>,
    ): Call<List<String>>

}