package com.aminography.primedatepicker.picker.theme.base

import android.content.Context
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.aminography.primedatepicker.picker.theme.abs.*
import java.io.Serializable

/**
 * @author aminography
 */
abstract class ThemeFactory : GeneralTheme,
    CalendarViewTheme,
    ActionBarTheme,
    SelectionBarTheme,
    GotoViewTheme,
    Serializable {

    @Transient
    internal lateinit var context: Context

    protected fun getColor(@ColorRes colorResId: Int): Int =
        ContextCompat.getColor(context, colorResId)

    protected fun getDimension(@DimenRes dimenResId: Int): Int =
        context.resources.getDimensionPixelSize(dimenResId)

    protected fun getBoolean(@BoolRes boolResId: Int): Boolean =
        context.resources.getBoolean(boolResId)

    protected fun getInteger(@IntegerRes intResId: Int): Int =
        context.resources.getInteger(intResId)

    protected fun getFloat(@StringRes floatResId: Int): Float =
        context.resources.getString(floatResId).toFloat()

    protected fun getString(@StringRes stringResId: Int): String =
        context.resources.getString(stringResId)

    protected fun getInterpolator(@InterpolatorRes interpolatorResId: Int): Interpolator =
        AnimationUtils.loadInterpolator(context, interpolatorResId)

}
