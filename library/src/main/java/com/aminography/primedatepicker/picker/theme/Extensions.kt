package com.aminography.primedatepicker.picker.theme

import android.content.res.ColorStateList
import android.util.TypedValue
import androidx.core.widget.ImageViewCompat
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.picker.action.ActionView
import com.aminography.primedatepicker.picker.go.GotoNumberPicker
import com.aminography.primedatepicker.picker.go.GotoView
import com.aminography.primedatepicker.picker.header.BaseLazyView
import com.aminography.primedatepicker.picker.header.HeaderView
import com.aminography.primedatepicker.picker.header.multiple.MultipleHeaderView
import com.aminography.primedatepicker.picker.header.range.RangeHeaderView
import com.aminography.primedatepicker.picker.header.single.SingleHeaderView
import kotlinx.android.synthetic.main.action_container.view.*
import kotlinx.android.synthetic.main.goto_container.view.*
import kotlinx.android.synthetic.main.range_days_header.view.*
import kotlinx.android.synthetic.main.single_day_header.view.*

/**
 * @author aminography
 */


internal fun ActionView.applyTheme(theme: BaseThemeFactory) {
    with(rootView) {
        todayButton.setTextColor(theme.buttonBarTodayTextColor)
        negativeButton.setTextColor(theme.buttonBarNegativeTextColor)
        positiveButton.setTextColor(theme.buttonBarPositiveTextColor)

        todayButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, theme.buttonBarTextSize.toFloat())
        negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, theme.buttonBarTextSize.toFloat())
        positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, theme.buttonBarTextSize.toFloat())
    }
}

internal fun HeaderView.applyTheme(theme: BaseThemeFactory) {
    with((this as BaseLazyView).rootView) {
        setBackgroundColor(theme.selectionBarBackgroundColor)
    }
    when (this) {
        is SingleHeaderView -> {
            labelFormatter = theme.singleDayLabelFormatter

            with((this as BaseLazyView).rootView) {
                pickedTextView.firstLabelTextSize = theme.singleDayItemFirstLabelTextSize
                pickedTextView.firstLabelTextColor = theme.singleDayItemFirstLabelTextColor
                pickedTextView.secondLabelTextSize = theme.singleDayItemSecondLabelTextSize
                pickedTextView.secondLabelTextColor = theme.singleDayItemSecondLabelTextColor
                pickedTextView.gapBetweenLines = theme.singleDayItemGapBetweenLines
            }
        }
        is RangeHeaderView -> {
            labelFormatter = theme.rangeDaysLabelFormatter
            itemBackgroundColor = theme.rangeDaysItemBackgroundColor

            with((this as BaseLazyView).rootView) {
                rangeStartTextView.firstLabelTextSize = theme.rangeDaysItemFirstLabelTextSize
                rangeStartTextView.firstLabelTextColor = theme.rangeDaysItemFirstLabelTextColor
                rangeStartTextView.secondLabelTextSize = theme.rangeDaysItemSecondLabelTextSize
                rangeStartTextView.secondLabelTextColor = theme.rangeDaysItemSecondLabelTextColor
                rangeStartTextView.gapBetweenLines = theme.rangeDaysItemGapBetweenLines

                rangeEndTextView.firstLabelTextSize = theme.rangeDaysItemFirstLabelTextSize
                rangeEndTextView.firstLabelTextColor = theme.rangeDaysItemFirstLabelTextColor
                rangeEndTextView.secondLabelTextSize = theme.rangeDaysItemSecondLabelTextSize
                rangeEndTextView.secondLabelTextColor = theme.rangeDaysItemSecondLabelTextColor
                rangeEndTextView.gapBetweenLines = theme.rangeDaysItemGapBetweenLines
            }
        }
        is MultipleHeaderView -> {
            firstLabelFormatter = theme.multipleDaysItemFirstLabelFormatter
            secondLabelFormatter = theme.multipleDaysItemSecondLabelFormatter

            itemBackgroundColor = theme.multipleDaysItemBackgroundColor
            firstLabelTextSize = theme.multipleDaysItemFirstLabelTextSize
            firstLabelTextColor = theme.multipleDaysItemFirstLabelTextColor
            secondLabelTextSize = theme.multipleDaysItemSecondLabelTextSize
            secondLabelTextColor = theme.multipleDaysItemSecondLabelTextColor
            gapBetweenLines = theme.multipleDaysItemGapBetweenLines
        }
    }
}

internal fun GotoView.applyTheme(theme: BaseThemeFactory) {
    with(rootView) {
        setBackgroundColor(theme.gotoBackgroundColor)
        ImageViewCompat.setImageTintList(goButtonImageView, ColorStateList.valueOf(theme.gotoBackgroundColor))
        ImageViewCompat.setImageTintList(closeButtonImageView, ColorStateList.valueOf(theme.gotoBackgroundColor))

        GotoNumberPicker.labelTextSize = theme.gotoTextSize
        GotoNumberPicker.labelTextColor = theme.gotoTextColor
        GotoNumberPicker.dividerColor = theme.gotoDividerColor
    }
}

internal fun PrimeCalendarView.applyTheme(theme: BaseThemeFactory) {
    // ------- Color Customizations
    setBackgroundColor(theme.elementBackgroundColor)
    monthLabelTextColor = theme.monthLabelTextColor
    weekLabelTextColor = theme.weekLabelTextColor
    dayLabelTextColor = theme.dayLabelTextColor
    todayLabelTextColor = theme.todayLabelTextColor
    pickedDayLabelTextColor = theme.pickedDayLabelTextColor
    pickedDayCircleColor = theme.pickedDayCircleColor
    disabledDayLabelTextColor = theme.disabledDayLabelTextColor
    dividerColor = theme.dividerColor

    // ------- Size & Behaviour Customizations
    monthLabelTextSize = theme.monthLabelTextSize
    weekLabelTextSize = theme.weekLabelTextSize
    dayLabelTextSize = theme.dayLabelTextSize
    monthLabelTopPadding = theme.monthLabelTopPadding
    monthLabelBottomPadding = theme.monthLabelBottomPadding
    weekLabelTopPadding = theme.weekLabelTopPadding
    weekLabelBottomPadding = theme.weekLabelBottomPadding
    dayLabelVerticalPadding = theme.dayLabelVerticalPadding
    showTwoWeeksInLandscape = theme.showTwoWeeksInLandscape
    animateSelection = theme.animateSelection
    animationDuration = theme.animationDuration
    animationInterpolator = theme.animationInterpolator
    dividerThickness = theme.dividerThickness
    dividerInsetLeft = theme.dividerInsetLeft
    dividerInsetRight = theme.dividerInsetRight
    dividerInsetTop = theme.dividerInsetTop
    dividerInsetBottom = theme.dividerInsetBottom

    elementPaddingLeft = theme.elementPaddingLeft
    elementPaddingRight = theme.elementPaddingRight
    elementPaddingTop = theme.elementPaddingTop
    elementPaddingBottom = theme.elementPaddingBottom

    loadFactor = theme.loadFactor
    maxTransitionLength = theme.maxTransitionLength
    transitionSpeedFactor = theme.transitionSpeedFactor
    flingOrientation = theme.flingOrientation
    monthLabelFormatter = theme.monthLabelFormatter
    weekLabelFormatter = theme.weekLabelFormatter
    developerOptionsShowGuideLines = theme.developerOptionsShowGuideLines
}
