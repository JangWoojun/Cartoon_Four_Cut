package com.woojun.cartoon_four_cut.network

import com.woojun.cartoon_four_cut.BuildConfig
import com.woojun.cartoon_four_cut.data.AuthResponse
import com.woojun.cartoon_four_cut.data.FrameResponse
import retrofit2.Call
import retrofit2.http.GET


interface RetrofitAPI {
    @GET("${BuildConfig.baseUrl}auth")
    fun getAuthId(): Call<AuthResponse>

    @GET("${BuildConfig.baseUrl}frame")
    fun getFrame(): Call<FrameResponse>
}