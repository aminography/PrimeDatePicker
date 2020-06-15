package com.aminography.primedatepicker.picker.theme.base

import android.content.res.ColorStateList
import androidx.core.widget.ImageViewCompat
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.picker.action.ActionBarView
import com.aminography.primedatepicker.picker.base.BaseLazyView
import com.aminography.primedatepicker.picker.component.ColoredNumberPicker
import com.aminography.primedatepicker.picker.go.GotoView
import com.aminography.primedatepicker.picker.selection.SelectionBarView
import com.aminography.primedatepicker.picker.selection.multiple.MultipleDaysSelectionBarView
import com.aminography.primedatepicker.picker.selection.range.RangeDaysSelectionBarView
import com.aminography.primedatepicker.picker.selection.single.SingleDaySelectionBarView
import com.aminography.primedatepicker.picker.theme.abs.ActionBarTheme
import com.aminography.primedatepicker.picker.theme.abs.CalendarViewTheme
import com.aminography.primedatepicker.picker.theme.abs.GotoViewTheme
import com.aminography.primedatepicker.picker.theme.abs.SelectionBarTheme
import kotlinx.android.synthetic.main.action_bar_container.view.*
import kotlinx.android.synthetic.main.goto_container.view.*
import kotlinx.android.synthetic.main.selection_bar_range_days_container.view.*
import kotlinx.android.synthetic.main.selection_bar_single_day_container.view.*

/**
 * @author aminography
 */

internal fun ActionBarView.applyTheme(theme: ActionBarTheme) {
    with(rootView) {
        todayTwoLineTextView.topLabelTextColor = theme.actionBarTodayTextColor
        negativeTwoLineTextView.topLabelTextColor = theme.actionBarNegativeTextColor
        positiveTwoLineTextView.topLabelTextColor = theme.actionBarPositiveTextColor

        todayTwoLineTextView.topLabelTextSize = theme.actionBarTextSize
        negativeTwoLineTextView.topLabelTextSize = theme.actionBarTextSize
        positiveTwoLineTextView.topLabelTextSize = theme.actionBarTextSize
    }
}

internal fun SelectionBarView.applyTheme(theme: SelectionBarTheme) {
    when (this) {
        is SingleDaySelectionBarView -> {
            labelFormatter = theme.selectionBarSingleDayLabelFormatter

            with((this as BaseLazyView).rootView) {
                pickedTextView.topLabelTextSize = theme.selectionBarSingleDayItemTopLabelTextSize
                pickedTextView.topLabelTextColor = theme.selectionBarSingleDayItemTopLabelTextColor
                pickedTextView.bottomLabelTextSize = theme.selectionBarSingleDayItemBottomLabelTextSize
                pickedTextView.bottomLabelTextColor = theme.selectionBarSingleDayItemBottomLabelTextColor
                pickedTextView.gapBetweenLines = theme.selectionBarSingleDayItemGapBetweenLines
            }
        }
        is RangeDaysSelectionBarView -> {
            labelFormatter = theme.selectionBarRangeDaysLabelFormatter
            itemBackgroundColor = theme.selectionBarRangeDaysItemBackgroundColor

            with((this as BaseLazyView).rootView) {
                rangeStartTextView.topLabelTextSize = theme.selectionBarRangeDaysItemTopLabelTextSize
                rangeStartTextView.topLabelTextColor = theme.selectionBarRangeDaysItemTopLabelTextColor
                rangeStartTextView.bottomLabelTextSize = theme.selectionBarRangeDaysItemBottomLabelTextSize
                rangeStartTextView.bottomLabelTextColor = theme.selectionBarRangeDaysItemBottomLabelTextColor
                rangeStartTextView.gapBetweenLines = theme.selectionBarRangeDaysItemGapBetweenLines

                rangeEndTextView.topLabelTextSize = theme.selectionBarRangeDaysItemTopLabelTextSize
                rangeEndTextView.topLabelTextColor = theme.selectionBarRangeDaysItemTopLabelTextColor
                rangeEndTextView.bottomLabelTextSize = theme.selectionBarRangeDaysItemBottomLabelTextSize
                rangeEndTextView.bottomLabelTextColor = theme.selectionBarRangeDaysItemBottomLabelTextColor
                rangeEndTextView.gapBetweenLines = theme.selectionBarRangeDaysItemGapBetweenLines
            }
        }
        is MultipleDaysSelectionBarView -> {
            topLabelFormatter = theme.selectionBarMultipleDaysItemTopLabelFormatter
            bottomLabelFormatter = theme.selectionBarMultipleDaysItemBottomLabelFormatter

            itemBackgroundColor = theme.selectionBarMultipleDaysItemBackgroundColor
            topLabelTextSize = theme.selectionBarMultipleDaysItemTopLabelTextSize
            topLabelTextColor = theme.selectionBarMultipleDaysItemTopLabelTextColor
            bottomLabelTextSize = theme.selectionBarMultipleDaysItemBottomLabelTextSize
            bottomLabelTextColor = theme.selectionBarMultipleDaysItemBottomLabelTextColor
            gapBetweenLines = theme.selectionBarMultipleDaysItemGapBetweenLines
        }
    }
}

internal fun GotoView.applyTheme(theme: GotoViewTheme) {
    with(rootView) {
        ImageViewCompat.setImageTintList(goIconImageView, ColorStateList.valueOf(theme.gotoViewBackgroundColor))
        ImageViewCompat.setImageTintList(closeIconImageView, ColorStateList.valueOf(theme.gotoViewBackgroundColor))

        ColoredNumberPicker.labelTextSize = theme.gotoViewTextSize
        ColoredNumberPicker.labelTextColor = theme.gotoViewTextColor
        ColoredNumberPicker.dividerColor = theme.gotoViewDividerColor
    }
}

internal fun PrimeCalendarView.applyTheme(theme: CalendarViewTheme) {
    // ------- Color Customizations
    setBackgroundColor(theme.calendarViewBackgroundColor)
    monthLabelTextColor = theme.calendarViewMonthLabelTextColor
    weekLabelTextColors = theme.calendarViewWeekLabelTextColors
    dayLabelTextColor = theme.calendarViewDayLabelTextColor
    todayLabelTextColor = theme.calendarViewTodayLabelTextColor
    pickedDayLabelTextColor = theme.calendarViewPickedDayLabelTextColor
    pickedDayInRangeLabelTextColor = theme.calendarViewPickedDayInRangeLabelTextColor
    pickedDayBackgroundColor = theme.calendarViewPickedDayBackgroundColor
    pickedDayInRangeBackgroundColor = theme.calendarViewPickedDayInRangeBackgroundColor
    disabledDayLabelTextColor = theme.calendarViewDisabledDayLabelTextColor
    adjacentMonthDayLabelTextColor = theme.calendarViewAdjacentMonthDayLabelTextColor
    dividerColor = theme.calendarViewDividerColor

    // ------- Size & Behaviour Customizations
    monthLabelTextSize = theme.calendarViewMonthLabelTextSize
    weekLabelTextSize = theme.calendarViewWeekLabelTextSize
    dayLabelTextSize = theme.calendarViewDayLabelTextSize
    monthLabelTopPadding = theme.calendarViewMonthLabelTopPadding
    monthLabelBottomPadding = theme.calendarViewMonthLabelBottomPadding
    weekLabelTopPadding = theme.calendarViewWeekLabelTopPadding
    weekLabelBottomPadding = theme.calendarViewWeekLabelBottomPadding
    dayLabelVerticalPadding = theme.calendarViewDayLabelVerticalPadding
    showTwoWeeksInLandscape = theme.calendarViewShowTwoWeeksInLandscape
    showAdjacentMonthDays = theme.calendarViewShowAdjacentMonthDays
    animateSelection = theme.calendarViewAnimateSelection
    animationDuration = theme.calendarViewAnimationDuration
    animationInterpolator = theme.calendarViewAnimationInterpolator
    dividerThickness = theme.calendarViewDividerThickness
    dividerInsetLeft = theme.calendarViewDividerInsetLeft
    dividerInsetRight = theme.calendarViewDividerInsetRight
    dividerInsetTop = theme.calendarViewDividerInsetTop
    dividerInsetBottom = theme.calendarViewDividerInsetBottom

    elementPaddingLeft = theme.calendarViewPaddingLeft
    elementPaddingRight = theme.calendarViewPaddingRight
    elementPaddingTop = theme.calendarViewPaddingTop
    elementPaddingBottom = theme.calendarViewPaddingBottom

    pickedDayBackgroundShapeType = theme.pickedDayBackgroundShapeType
    pickedDayRoundSquareCornerRadius = theme.pickedDayRoundSquareCornerRadius

    loadFactor = theme.calendarViewLoadFactor
    maxTransitionLength = theme.calendarViewMaxTransitionLength
    transitionSpeedFactor = theme.calendarViewTransitionSpeedFactor
    flingOrientation = theme.calendarViewFlingOrientation
    monthLabelFormatter = theme.calendarViewMonthLabelFormatter
    weekLabelFormatter = theme.calendarViewWeekLabelFormatter
    developerOptionsShowGuideLines = theme.calendarViewDeveloperOptionsShowGuideLines
}
