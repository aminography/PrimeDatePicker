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
abstract class ThemeFactory : Serializable {

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

    // ------- Action Bar

    abstract val actionBarBackgroundColor: Int

    abstract val actionBarTextSize: Int

    abstract val actionBarTodayTextColor: Int

    abstract val actionBarNegativeTextColor: Int

    abstract val actionBarPositiveTextColor: Int

    // ------- Selection Bar - General

    abstract val selectionBarBackgroundColor: Int

    // ------- Selection Bar - Single Day

    abstract val selectionBarSingleDayItemTopLabelTextSize: Int

    abstract val selectionBarSingleDayItemTopLabelTextColor: Int

    abstract val selectionBarSingleDayItemBottomLabelTextSize: Int

    abstract val selectionBarSingleDayItemBottomLabelTextColor: Int

    abstract val selectionBarSingleDayItemGapBetweenLines: Int

    abstract val selectionBarSingleDayLabelFormatter: LabelFormatter

    // ------- Selection Bar - Range Days

    abstract val selectionBarRangeDaysItemBackgroundColor: Int

    abstract val selectionBarRangeDaysItemTopLabelTextSize: Int

    abstract val selectionBarRangeDaysItemTopLabelTextColor: Int

    abstract val selectionBarRangeDaysItemBottomLabelTextSize: Int

    abstract val selectionBarRangeDaysItemBottomLabelTextColor: Int

    abstract val selectionBarRangeDaysItemGapBetweenLines: Int

    abstract val selectionBarRangeDaysLabelFormatter: LabelFormatter

    // ------- Selection Bar - Multiple Days

    abstract val selectionBarMultipleDaysItemBackgroundColor: Int

    abstract val selectionBarMultipleDaysItemTopLabelTextSize: Int

    abstract val selectionBarMultipleDaysItemTopLabelTextColor: Int

    abstract val selectionBarMultipleDaysItemBottomLabelTextSize: Int

    abstract val selectionBarMultipleDaysItemBottomLabelTextColor: Int

    abstract val selectionBarMultipleDaysItemGapBetweenLines: Int

    abstract val selectionBarMultipleDaysItemTopLabelFormatter: LabelFormatter

    abstract val selectionBarMultipleDaysItemBottomLabelFormatter: LabelFormatter

    // ------- Goto View

    abstract val gotoViewBackgroundColor: Int

    abstract val gotoViewTextColor: Int

    abstract val gotoViewTextSize: Int

    abstract val gotoViewDividerColor: Int

}
