package com.woojun.cartoon_four_cut.custom_view.pulse_count_down

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.widget.AppCompatTextView
import com.woojun.cartoon_four_cut.R


class PulseCountDown : AppCompatTextView {


    private var customAttributes: CustomAttributes = CustomAttributes()
        set(value) {
            field = value
            currentCnt = value.startValue
        }

    var startValue: Int = customAttributes.startValue
        set(value) {
            if (value > customAttributes.endValue) {
                field = value
                customAttributes.startValue = value
            }
        }

    var endValue: Int = customAttributes.endValue
        set(value) {
            if (value < customAttributes.startValue) {
                field = value
                customAttributes.endValue = value
            }
        }

    private var currentCnt: Int = customAttributes.startValue

    private val scaleAnimation = ScaleAnimation(
        1f, 2f,
        1f, 2f,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f
    ).apply { duration = 750 }

    private val alphaAnimation = AlphaAnimation(1f, 0f).apply { duration = 250; fillAfter = true }
    private var scaleAnimationStarted = false

    private var onCountdownCompleted: OnCountdownCompleted? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )


    private fun init(context: Context, attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PulseCountDown,
            0, 0
        ).apply {
            try {
                val startValue =
                    getInteger(R.styleable.PulseCountDown_pc_startValue, START_VALUE_DEFAULT)
                val endValue = getInteger(R.styleable.PulseCountDown_pc_endValue, END_VALUE_DEFAULT)
                if (startValue < endValue) throw IllegalStateException("Start value $startValue must be greater than $endValue")
                customAttributes = CustomAttributes(startValue, endValue)
            } finally {
                recycle()
            }
        }

    }

    fun start(callback: OnCountdownCompleted) {
        onCountdownCompleted = callback
        currentCnt = customAttributes.startValue
        handleAnimation()
    }


    override fun onAnimationEnd() {
        super.onAnimationEnd()
        handleAnimation()
    }

    private fun handleAnimation() {
        if (scaleAnimationStarted) {
            scaleAnimationStarted = false
            startAnimation(alphaAnimation)
        } else if (currentCnt != customAttributes.endValue) {
            text = currentCnt.toString()
            currentCnt--
            scaleAnimationStarted = true
            startAnimation(scaleAnimation)
        } else onCountdownCompleted?.completed()

    }
}