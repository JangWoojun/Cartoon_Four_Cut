package com.woojun.cartoon_four_cut

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class OverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x80000000.toInt()
    }

    private val holePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x80000000.toInt()
        xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR) // 구멍 뚫기 효과
    }

    private val path = Path()
    private val cornerRadius = 50f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        val rect = RectF(0f, height / 4f, width.toFloat(), height * 3f / 4)
        path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CCW)
        canvas.drawPath(path, holePaint)
    }
}
