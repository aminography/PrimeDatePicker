package com.aminography.primedatepicker.picker.theme.abs

import android.util.SparseIntArray
import android.view.animation.Interpolator
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.common.LabelFormatter

/**
 * @author aminography
 */
interface CalendarViewTheme : GeneralTheme {

    val calendarViewBackgroundColor: Int

    val calendarViewShowTwoWeeksInLandscape: Boolean

    val calendarViewShowAdjacentMonthDays: Boolean

    val calendarViewPaddingLeft: Int

    val calendarViewPaddingRight: Int

    val calendarViewPaddingTop: Int

    val calendarViewPaddingBottom: Int

    // ------- Month Label

    val calendarViewMonthLabelTextSize: Int

    val calendarViewMonthLabelTextColor: Int

    val calendarViewMonthLabelTopPadding: Int

    val calendarViewMonthLabelBottomPadding: Int

    val calendarViewMonthLabelFormatter: LabelFormatter

    // ------- Week Label

    val calendarViewWeekLabelTextSize: Int

    val calendarViewWeekLabelTextColors: SparseIntArray

    val calendarViewWeekLabelTopPadding: Int

    val calendarViewWeekLabelBottomPadding: Int

    val calendarViewWeekLabelFormatter: LabelFormatter

    // ------- Day Label

    val calendarViewDayLabelTextSize: Int

    val calendarViewDayLabelTextColor: Int

    val calendarViewDayLabelVerticalPadding: Int

    val calendarViewTodayLabelTextColor: Int

    val calendarViewPickedDayLabelTextColor: Int

    val calendarViewPickedDayInRangeLabelTextColor: Int

    val calendarViewPickedDayBackgroundColor: Int

    val calendarViewPickedDayInRangeBackgroundColor: Int

    val pickedDayBackgroundShapeType: BackgroundShapeType

    val pickedDayRoundSquareCornerRadius: Int

    val calendarViewDisabledDayLabelTextColor: Int

    val calendarViewAdjacentMonthDayLabelTextColor: Int

    // ------- Divider

    val calendarViewDividerColor: Int

    val calendarViewDividerThickness: Int

    val calendarViewDividerInsetLeft: Int

    val calendarViewDividerInsetRight: Int

    val calendarViewDividerInsetTop: Int

    val calendarViewDividerInsetBottom: Int

    // ------- Animation

    val calendarViewAnimateSelection: Boolean

    val calendarViewAnimationDuration: Int

    val calendarViewAnimationInterpolator: Interpolator

    // ------- Transition

    val calendarViewLoadFactor: Int

    val calendarViewMaxTransitionLength: Int

    val calendarViewTransitionSpeedFactor: Float

    val calendarViewFlingOrientation: PrimeCalendarView.FlingOrientation

    // ------- Developer Options

    val calendarViewDeveloperOptionsShowGuideLines: Boolean
}