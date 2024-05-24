package com.woojun.cartoon_four_cut.data

import android.graphics.Bitmap

data class FrameItem (
    val frame: FrameTypes,
    val src: Int,
    val images: MutableList<Bitmap>
)