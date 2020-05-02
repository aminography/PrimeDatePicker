package com.aminography.primedatepicker.picker.theme.abs

import android.view.animation.Interpolator
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.common.LabelFormatter

/**
 * @author aminography
 */
interface CalendarViewTheme : GeneralTheme {

    val elementBackgroundColor: Int

    val showTwoWeeksInLandscape: Boolean

    val elementPaddingLeft: Int

    val elementPaddingRight: Int

    val elementPaddingTop: Int

    val elementPaddingBottom: Int

    // ------- Month Label

    val monthLabelTextSize: Int

    val monthLabelTextColor: Int

    val monthLabelTopPadding: Int

    val monthLabelBottomPadding: Int

    val monthLabelFormatter: LabelFormatter

    // ------- Week Label

    val weekLabelTextSize: Int

    val weekLabelTextColor: Int

    val weekLabelTopPadding: Int

    val weekLabelBottomPadding: Int

    val weekLabelFormatter: LabelFormatter

    // ------- Day Label

    val dayLabelTextSize: Int

    val dayLabelTextColor: Int

    val dayLabelVerticalPadding: Int

    val todayLabelTextColor: Int

    val pickedDayLabelTextColor: Int

    val pickedDayCircleColor: Int

    val disabledDayLabelTextColor: Int

    // ------- Divider

    val dividerColor: Int

    val dividerThickness: Int

    val dividerInsetLeft: Int

    val dividerInsetRight: Int

    val dividerInsetTop: Int

    val dividerInsetBottom: Int

    // ------- Animation

    val animateSelection: Boolean

    val animationDuration: Int

    val animationInterpolator: Interpolator

    // ------- Transition

    val loadFactor: Int

    val maxTransitionLength: Int

    val transitionSpeedFactor: Float

    val flingOrientation: PrimeCalendarView.FlingOrientation

    // ------- Developer Options

    val developerOptionsShowGuideLines: Boolean
}