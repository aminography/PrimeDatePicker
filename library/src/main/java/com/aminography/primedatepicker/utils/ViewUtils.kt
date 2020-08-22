package com.aminography.primedatepicker.utils

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import androidx.core.graphics.ColorUtils

/**
 * @author aminography
 */

fun View.visible() = if (visibility != View.VISIBLE) visibility = View.VISIBLE else Unit

fun View.invisible() = if (visibility != View.INVISIBLE) visibility = View.INVISIBLE else Unit

fun View.gone() = if (visibility != View.GONE) visibility = View.GONE else Unit

fun View.setBackgroundDrawableCompat(drawable: Drawable) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        background = drawable
    } else {
        @Suppress("DEPRECATION")
        setBackgroundDrawable(drawable)
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
