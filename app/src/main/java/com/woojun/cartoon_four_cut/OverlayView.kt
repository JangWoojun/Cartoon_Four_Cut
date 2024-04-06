package com.woojun.cartoon_four_cut

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.woojun.cartoon_four_cut.Utils.dpToPx

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
        xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR)
    }

    private val rect = RectF()
    private val path = Path()
    private val cornerRadius = 50f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val topMarginInPx = context.dpToPx(146.5f)

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        rect.set(0f, topMarginInPx , width.toFloat(),  topMarginInPx + width)

        path.reset()
        path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CCW)

        canvas.drawPath(path, holePaint)
    }
}
