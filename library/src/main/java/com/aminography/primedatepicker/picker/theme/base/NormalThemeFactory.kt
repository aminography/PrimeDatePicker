package com.aminography.primedatepicker.picker.theme.base

import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.common.LabelFormatter

/**
 * @author aminography
 */
abstract class NormalThemeFactory : ThemeFactory() {

    // ------------------------------------------ General ------------------------------------------

    override val typefacePath: String?
        get() = null

    // --------------------------------------- Calendar View ---------------------------------------

    override val calendarViewShowTwoWeeksInLandscape: Boolean
        get() = getBoolean(R.bool.defaultShowTwoWeeksInLandscape)

    override val calendarViewShowAdjacentMonthDays: Boolean
        get() = getBoolean(R.bool.defaultShowAdjacentMonthDays)

    override val calendarViewPaddingLeft: Int
        get() = getDimension(R.dimen.defaultElementPaddingLeft)

    override val calendarViewPaddingRight: Int
        get() = getDimension(R.dimen.defaultElementPaddingRight)

    override val calendarViewPaddingTop: Int
        get() = getDimension(R.dimen.defaultElementPaddingTop)

    override val calendarViewPaddingBottom: Int
        get() = getDimension(R.dimen.defaultElementPaddingBottom)

    // ------- Month Label

    override val calendarViewMonthLabelTextSize: Int
        get() = getDimension(R.dimen.defaultMonthLabelTextSize)

    override val calendarViewMonthLabelTopPadding: Int
        get() = getDimension(R.dimen.defaultMonthLabelTopPadding)

    override val calendarViewMonthLabelBottomPadding: Int
        get() = getDimension(R.dimen.defaultMonthLabelBottomPadding)

    override val calendarViewMonthLabelFormatter: LabelFormatter
        get() = { primeCalendar -> "${primeCalendar.monthName} ${primeCalendar.year}" }

    // ------- Week Label

    override val calendarViewWeekLabelTextSize: Int
        get() = getDimension(R.dimen.defaultWeekLabelTextSize)

    override val calendarViewWeekLabelTopPadding: Int
        get() = getDimension(R.dimen.defaultWeekLabelTopPadding)

    override val calendarViewWeekLabelBottomPadding: Int
        get() = getDimension(R.dimen.defaultWeekLabelBottomPadding)

    override val calendarViewWeekLabelFormatter: LabelFormatter
        get() = { primeCalendar -> primeCalendar.weekDayNameShort }

    // ------- Day Label

    override val calendarViewDayLabelTextSize: Int
        get() = getDimension(R.dimen.defaultDayLabelTextSize)

    override val calendarViewDayLabelVerticalPadding: Int
        get() = getDimension(R.dimen.defaultDayLabelVerticalPadding)

    override val pickedDayBackgroundShapeType: BackgroundShapeType
        get() = BackgroundShapeType.CIRCLE

    override val pickedDayRoundSquareCornerRadius: Int
        get() = getDimension(R.dimen.defaultPickedDayRoundSquareCornerRadius)

    // ------- Divider

    override val calendarViewDividerThickness: Int
        get() = getDimension(R.dimen.defaultDividerThickness)

    override val calendarViewDividerInsetLeft: Int
        get() = getDimension(R.dimen.defaultDividerInsetLeft)

    override val calendarViewDividerInsetRight: Int
        get() = getDimension(R.dimen.defaultDividerInsetRight)

    override val calendarViewDividerInsetTop: Int
        get() = getDimension(R.dimen.defaultDividerInsetTop)

    override val calendarViewDividerInsetBottom: Int
        get() = getDimension(R.dimen.defaultDividerInsetBottom)

    // ------- Animation

    override val calendarViewAnimateSelection: Boolean
        get() = getBoolean(R.bool.defaultAnimateSelection)

    override val calendarViewAnimationDuration: Int
        get() = getInteger(R.integer.defaultAnimationDuration)

    override val calendarViewAnimationInterpolator: Interpolator
        get() = OvershootInterpolator()

    // ------- Transition

    override val calendarViewLoadFactor: Int
        get() = getInteger(R.integer.defaultLoadFactor)

    override val calendarViewMaxTransitionLength: Int
        get() = getInteger(R.integer.defaultMaxTransitionLength)

    override val calendarViewTransitionSpeedFactor: Float
        get() = getFloat(R.string.defaultTransitionSpeedFactor)

    override val calendarViewFlingOrientation: PrimeCalendarView.FlingOrientation
        get() = PrimeCalendarView.FlingOrientation.VERTICAL

    // ------- Developer Options

    override val calendarViewDeveloperOptionsShowGuideLines: Boolean
        get() = false

    // ------------------------------------ Picker Bottom Sheet ------------------------------------

    // ------- Button Bar

    override val actionBarTextSize: Int
        get() = getDimension(R.dimen.defaultButtonTextSize)

    // ------- Selection Bar - Single Day

    override val selectionBarSingleDayItemTopLabelTextSize: Int
        get() = getDimension(R.dimen.text_size_small)

    override val selectionBarSingleDayItemBottomLabelTextSize: Int
        get() = getDimension(R.dimen.text_size_normal)

    override val selectionBarSingleDayItemGapBetweenLines: Int
        get() = getDimension(R.dimen.defaultGapBetweenLines)

    override val selectionBarSingleDayLabelFormatter: LabelFormatter
        get() = { primeCalendar -> primeCalendar.shortDateString }

    // ------- Selection Bar - Range Days

    override val selectionBarRangeDaysItemTopLabelTextSize: Int
        get() = getDimension(R.dimen.text_size_small)

    override val selectionBarRangeDaysItemBottomLabelTextSize: Int
        get() = getDimension(R.dimen.text_size_normal)

    override val selectionBarRangeDaysItemGapBetweenLines: Int
        get() = getDimension(R.dimen.defaultGapBetweenLines)

    override val selectionBarRangeDaysLabelFormatter: LabelFormatter
        get() = { primeCalendar -> primeCalendar.shortDateString }

    // ------- Selection Bar - Multiple Days

    override val selectionBarMultipleDaysItemTopLabelTextSize: Int
        get() = getDimension(R.dimen.text_size_header_multi_large)

    override val selectionBarMultipleDaysItemBottomLabelTextSize: Int
        get() = getDimension(R.dimen.text_size_header_multi_small)

    override val selectionBarMultipleDaysItemGapBetweenLines: Int
        get() = 0

    override val selectionBarMultipleDaysItemTopLabelFormatter: LabelFormatter
        get() = { primeCalendar ->
            String.format("%02d", primeCalendar.dayOfMonth)
        }

    override val selectionBarMultipleDaysItemBottomLabelFormatter: LabelFormatter
        get() = { primeCalendar ->
            String.format(
                "%s '%s",
                primeCalendar.monthNameShort,
                "${primeCalendar.year}".substring(2)
            )
        }

    // ------- Goto View

    override val gotoViewTextSize: Int
        get() = getDimension(R.dimen.defaultGotoTextSize)

}