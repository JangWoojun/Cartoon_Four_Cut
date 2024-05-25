package com.woojun.cartoon_four_cut.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FrameResponse(
    val background: String,
    val bottom: String,
    val top: String,
    val name: String
): Parcelable