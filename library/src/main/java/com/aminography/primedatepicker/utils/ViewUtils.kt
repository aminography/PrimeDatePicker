package com.aminography.primedatepicker.utils

import android.app.Activity
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.graphics.ColorUtils
import kotlinx.android.synthetic.main.fragment_date_picker_bottom_sheet.view.*

/**
 * @author aminography
 */

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.setBackgroundDrawableCompat(drawable: Drawable) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        circularRevealFrameLayout.background = drawable
    } else {
        @Suppress("DEPRECATION")
        circularRevealFrameLayout.setBackgroundDrawable(drawable)
    }
}

fun generateTopGradientDrawable(color: Int): Drawable {
    return GradientDrawable(
        GradientDrawable.Orientation.TOP_BOTTOM,
        intArrayOf(
            ColorUtils.setAlphaComponent(color, 0),
            color, color, color, color, color, color, color, color,
            color, color, color, color, color, color, color, color,
            color, color, color, color, color, color, color, color,
            color, color, color, color, color, color, color, color
        )
    )
}

fun Activity.enableHardwareAcceleration() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
    )
}
