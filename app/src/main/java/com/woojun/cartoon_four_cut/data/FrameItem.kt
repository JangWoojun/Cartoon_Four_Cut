package com.woojun.cartoon_four_cut.data

import android.graphics.Bitmap

data class FrameItem (
    val name: String,
    val frameResponse: FrameResponse,
    val images: MutableList<Bitmap>,
    val isAi: Boolean = false
)