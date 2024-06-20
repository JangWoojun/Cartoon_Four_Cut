package com.woojun.cartoon_four_cut.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

object Utils {
    fun Context.dpToPx(dp: Float): Float {
        return dp * this.resources.displayMetrics.density
    }

    fun loadImageFromFilePath(filePath: String): Bitmap? {
        val file = File(filePath)

        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }
}
