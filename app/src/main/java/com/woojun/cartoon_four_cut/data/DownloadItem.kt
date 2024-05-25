package com.woojun.cartoon_four_cut.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DownloadItem (
    val images: List<String>,
    val frameResponse: FrameResponse,
    val isAi: Boolean = false
): Parcelable