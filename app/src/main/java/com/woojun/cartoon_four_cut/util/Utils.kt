package com.woojun.cartoon_four_cut.util

import android.content.Context

object Utils {
    fun Context.dpToPx(dp: Float): Float {
        return dp * this.resources.displayMetrics.density
    }
}
