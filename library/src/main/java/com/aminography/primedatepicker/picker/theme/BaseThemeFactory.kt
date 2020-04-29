package com.aminography.primedatepicker.picker.theme

import android.content.Context
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.aminography.primedatepicker.LabelFormatter
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import java.io.Serializable

/**
 * @author aminography
 */
abstract class BaseThemeFactory : Serializable {

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

    // ------------------------------------------ General ------------------------------------------

    abstract val typefacePath: String?

    // --------------------------------------- Calendar View ---------------------------------------

    abstract val elementBackgroundColor: Int

    abstract val showTwoWeeksInLandscape: Boolean

    abstract val elementPaddingLeft: Int

    abstract val elementPaddingRight: Int

    abstract val elementPaddingTop: Int

    abstract val elementPaddingBottom: Int

    // ------- Month Label

    abstract val monthLabelTextSize: Int

    abstract val monthLabelTextColor: Int

    abstract val monthLabelTopPadding: Int

    abstract val monthLabelBottomPadding: Int

    abstract val monthLabelFormatter: LabelFormatter

    // ------- Week Label

    abstract val weekLabelTextSize: Int

    abstract val weekLabelTextColor: Int

    abstract val weekLabelTopPadding: Int

    abstract val weekLabelBottomPadding: Int

    abstract val weekLabelFormatter: LabelFormatter

    // ------- Day Label

    abstract val dayLabelTextSize: Int

    abstract val dayLabelTextColor: Int

    abstract val dayLabelVerticalPadding: Int

    abstract val todayLabelTextColor: Int

    abstract val pickedDayLabelTextColor: Int

    abstract val pickedDayCircleColor: Int

    abstract val disabledDayLabelTextColor: Int

    // ------- Divider

    abstract val dividerColor: Int

    abstract val dividerThickness: Int

    abstract val dividerInsetLeft: Int

    abstract val dividerInsetRight: Int

    abstract val dividerInsetTop: Int

    abstract val dividerInsetBottom: Int

    // ------- Animation

    abstract val animateSelection: Boolean

    abstract val animationDuration: Int

    abstract val animationInterpolator: Interpolator

    // ------- Transition

    abstract val loadFactor: Int

    abstract val maxTransitionLength: Int

    abstract val transitionSpeedFactor: Float

    abstract val flingOrientation: PrimeCalendarView.FlingOrientation

    // ------- Developer Options

    abstract val developerOptionsShowGuideLines: Boolean

    // ------------------------------------ Picker Bottom Sheet ------------------------------------

    // ------- Button Bar

    abstract val buttonBarBackgroundColor: Int

    abstract val buttonBarTextSize: Int

    abstract val buttonBarTodayTextColor: Int

    abstract val buttonBarNegativeTextColor: Int

    abstract val buttonBarPositiveTextColor: Int

    // ------- Selection Bar - General

    abstract val selectionBarBackgroundColor: Int

    // ------- Selection Bar - Single Day

    abstract val singleDayItemFirstLabelTextSize: Int

    abstract val singleDayItemFirstLabelTextColor: Int

    abstract val singleDayItemSecondLabelTextSize: Int

    abstract val singleDayItemSecondLabelTextColor: Int

    abstract val singleDayItemGapBetweenLines: Int

    abstract val singleDayLabelFormatter: LabelFormatter

    // ------- Selection Bar - Range Days

    abstract val rangeDaysItemBackgroundColor: Int

    abstract val rangeDaysItemFirstLabelTextSize: Int

    abstract val rangeDaysItemFirstLabelTextColor: Int

    abstract val rangeDaysItemSecondLabelTextSize: Int

    abstract val rangeDaysItemSecondLabelTextColor: Int

    abstract val rangeDaysItemGapBetweenLines: Int

    abstract val rangeDaysLabelFormatter: LabelFormatter

    // ------- Selection Bar - Multiple Days

    abstract val multipleDaysItemBackgroundColor: Int

    abstract val multipleDaysItemFirstLabelTextSize: Int

    abstract val multipleDaysItemFirstLabelTextColor: Int

    abstract val multipleDaysItemSecondLabelTextSize: Int

    abstract val multipleDaysItemSecondLabelTextColor: Int

    abstract val multipleDaysItemGapBetweenLines: Int

    abstract val multipleDaysItemFirstLabelFormatter: LabelFormatter

    abstract val multipleDaysItemSecondLabelFormatter: LabelFormatter

    // ------- Goto View

    abstract val gotoBackgroundColor: Int

    abstract val gotoTextColor: Int

    abstract val gotoTextSize: Int

    abstract val gotoDividerColor: Int

}
