package com.woojun.cartoon_four_cut.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.woojun.cartoon_four_cut.R

object Dialog {
    fun createLoadingDialog(context: Context): Pair<Dialog, (String) -> Unit> {
        val dialog = Dialog(context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.loading_dialog)

        val textSwitcher = dialog.findViewById<TextSwitcher>(R.id.text_switcher)
        textSwitcher.setFactory {
            val textView = TextView(context)
            textView.gravity = Gravity.CENTER
            textView.setTextColor(Color.WHITE)
            textView.textSize = 16f
            textView.typeface = ResourcesCompat.getFont(context, R.font.s_bold)
            textView
        }

        val inAnim = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
        val outAnim = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right)

        textSwitcher.inAnimation = inAnim
        textSwitcher.outAnimation = outAnim

        val setText: (String) -> Unit = { newText ->
            textSwitcher.setText(newText)
        }

        return Pair(dialog, setText)
    }
}