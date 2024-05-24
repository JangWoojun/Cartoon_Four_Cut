package com.woojun.cartoon_four_cut.data

import android.graphics.Bitmap

data class FilterItem (
    val name: String,
    val images: MutableList<Bitmap>,
    val isAi: Boolean = false
)