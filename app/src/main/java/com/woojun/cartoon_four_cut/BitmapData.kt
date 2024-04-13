package com.woojun.cartoon_four_cut

import android.graphics.Bitmap

object BitmapData {
    private var image1: Bitmap? = null
    private var image2: Bitmap? = null
    private var image3: Bitmap? = null
    private var image4: Bitmap? = null

    fun getImage1(): Bitmap? {
        return image1?.copy(Bitmap.Config.RGBA_F16, true)
    }

    fun setImage1(bitmap: Bitmap) {
        this.image1 = bitmap
    }

    fun getImage2(): Bitmap? {
        return image2?.copy(Bitmap.Config.RGBA_F16, true)
    }

    fun setImage2(bitmap: Bitmap) {
        this.image2 = bitmap
    }

    fun getImage3(): Bitmap? {
        return image3?.copy(Bitmap.Config.RGBA_F16, true)
    }

    fun setImage3(bitmap: Bitmap) {
        this.image3 = bitmap
    }

    fun getImage4(): Bitmap? {
        return image4?.copy(Bitmap.Config.RGBA_F16, true)
    }

    fun setImage4(bitmap: Bitmap) {
        this.image4 = bitmap
    }
}
