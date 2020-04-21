package com.aminography.primedatepicker.picker.theme

import android.content.Context
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.monthview.MonthLabelFormatter
import com.aminography.primedatepicker.monthview.WeekLabelFormatter
import java.io.Serializable

/**
 * @author aminography
 */
abstract class BaseThemeFactory : Serializable {

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

    // ---------------------------------------------------------------------------------------------

    abstract val backgroundColor: Int

    abstract val monthLabelTextColor: Int

    abstract val weekLabelTextColor: Int

    abstract val dayLabelTextColor: Int

    abstract val todayLabelTextColor: Int

    abstract val pickedDayLabelTextColor: Int

    abstract val pickedDayCircleColor: Int

    abstract val disabledDayLabelTextColor: Int

    abstract val monthLabelTextSize: Int

    abstract val weekLabelTextSize: Int

    abstract val dayLabelTextSize: Int

    abstract val monthLabelTopPadding: Int

    abstract val monthLabelBottomPadding: Int

    abstract val weekLabelTopPadding: Int

    abstract val weekLabelBottomPadding: Int

    abstract val dayLabelVerticalPadding: Int

    abstract val showTwoWeeksInLandscape: Boolean

    abstract val animateSelection: Boolean

    abstract val animationDuration: Int

    abstract val animationInterpolator: Interpolator

    abstract val dividerColor: Int

    abstract val dividerThickness: Int

    abstract val dividerInsetLeft: Int

    abstract val dividerInsetRight: Int

    abstract val dividerInsetTop: Int

    abstract val dividerInsetBottom: Int

    abstract val loadFactor: Int

    abstract val maxTransitionLength: Int

    abstract val transitionSpeedFactor: Float

    abstract val flingOrientation: PrimeCalendarView.FlingOrientation

    abstract val typefacePath: String?

    // ---------------------------------------------------------------------------------------------

    abstract val gotoBackgroundColor: Int

    abstract val gotoTextColor: Int

    abstract val gotoDividerColor: Int

    abstract val gotoTextSize: Int

    abstract val monthLabelFormatter: MonthLabelFormatter

    abstract val weekLabelFormatter: WeekLabelFormatter

    abstract val buttonBarBackgroundColor: Int

    abstract val buttonBarTextSize: Int

    abstract val buttonBarTodayTextColor: Int

    abstract val buttonBarNegativeTextColor: Int

    abstract val buttonBarPositiveTextColor: Int

    abstract val selectionBarBackgroundColor: Int
//
//    abstract val selectionBarPrimaryTextColor: Int
//
//    abstract val selectionBarSecondaryTextColor: Int
//
//    abstract val selectionBarPrimaryTextSize: Int
//
//    abstract val selectionBarSecondaryTextSize: Int
//
//    abstract fun multipleDaysItemFirstLabelFormatter(calendar: PrimeCalendar): String
//
//    abstract fun multipleDaysItemSecondLabelFormatter(calendar: PrimeCalendar): String
//
//    abstract val multipleDaysItemFirstLabelTextColor: Int
//
//    abstract val multipleDaysItemSecondLabelTextColor: Int
//
//    abstract val multipleDaysItemFirstLabelTextSize: Int
//
//    abstract val multipleDaysItemSecondLabelTextSize: Int

    abstract val developerOptionsShowGuideLines: Boolean

}
