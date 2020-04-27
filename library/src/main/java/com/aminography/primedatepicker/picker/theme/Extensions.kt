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


internal fun ActionView.applyTheme(theme: BaseThemeFactory?) {
    theme?.let {
        with(rootView) {
            todayButton.setTextColor(it.buttonBarTodayTextColor)
            negativeButton.setTextColor(it.buttonBarNegativeTextColor)
            positiveButton.setTextColor(it.buttonBarPositiveTextColor)

            todayButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, it.buttonBarTextSize.toFloat())
            negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, it.buttonBarTextSize.toFloat())
            positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, it.buttonBarTextSize.toFloat())
        }
    }
}

internal fun HeaderView.applyTheme(theme: BaseThemeFactory?) {
    theme?.let {
        with((this as BaseLazyView).rootView) {
            setBackgroundColor(it.selectionBarBackgroundColor)
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

                firstLabelTextSize = theme.multipleDaysItemFirstLabelTextSize
                firstLabelTextColor = theme.multipleDaysItemFirstLabelTextColor
                secondLabelTextSize = theme.multipleDaysItemSecondLabelTextSize
                secondLabelTextColor = theme.multipleDaysItemSecondLabelTextColor
                gapBetweenLines = theme.multipleDaysItemGapBetweenLines
            }
        }
    }
}

internal fun GotoView.applyTheme(theme: BaseThemeFactory?) {
    theme?.let {
        with(rootView) {
            setBackgroundColor(it.gotoBackgroundColor)
            ImageViewCompat.setImageTintList(goButtonImageView, ColorStateList.valueOf(it.gotoBackgroundColor))
            ImageViewCompat.setImageTintList(closeButtonImageView, ColorStateList.valueOf(it.gotoBackgroundColor))

            GotoNumberPicker.labelTextSize = it.gotoTextSize
            GotoNumberPicker.labelTextColor = it.gotoTextColor
            GotoNumberPicker.dividerColor = it.gotoDividerColor
        }
    }
}

internal fun PrimeCalendarView.applyTheme(theme: BaseThemeFactory?) {
    theme?.let {
        // ------- Color Customizations
        setBackgroundColor(it.elementBackgroundColor)
        monthLabelTextColor = it.monthLabelTextColor
        weekLabelTextColor = it.weekLabelTextColor
        dayLabelTextColor = it.dayLabelTextColor
        todayLabelTextColor = it.todayLabelTextColor
        pickedDayLabelTextColor = it.pickedDayLabelTextColor
        pickedDayCircleColor = it.pickedDayCircleColor
        disabledDayLabelTextColor = it.disabledDayLabelTextColor
        dividerColor = it.dividerColor

        // ------- Size & Behaviour Customizations
        monthLabelTextSize = it.monthLabelTextSize
        weekLabelTextSize = it.weekLabelTextSize
        dayLabelTextSize = it.dayLabelTextSize
        monthLabelTopPadding = it.monthLabelTopPadding
        monthLabelBottomPadding = it.monthLabelBottomPadding
        weekLabelTopPadding = it.weekLabelTopPadding
        weekLabelBottomPadding = it.weekLabelBottomPadding
        dayLabelVerticalPadding = it.dayLabelVerticalPadding
        showTwoWeeksInLandscape = it.showTwoWeeksInLandscape
        animateSelection = it.animateSelection
        animationDuration = it.animationDuration
        animationInterpolator = it.animationInterpolator
        dividerThickness = it.dividerThickness
        dividerInsetLeft = it.dividerInsetLeft
        dividerInsetRight = it.dividerInsetRight
        dividerInsetTop = it.dividerInsetTop
        dividerInsetBottom = it.dividerInsetBottom

        elementPaddingLeft = it.elementPaddingLeft
        elementPaddingRight = it.elementPaddingRight
        elementPaddingTop = it.elementPaddingTop
        elementPaddingBottom = it.elementPaddingBottom

        loadFactor = it.loadFactor
        maxTransitionLength = it.maxTransitionLength
        transitionSpeedFactor = it.transitionSpeedFactor
        flingOrientation = it.flingOrientation
        monthLabelFormatter = it.monthLabelFormatter
        weekLabelFormatter = it.weekLabelFormatter
        developerOptionsShowGuideLines = it.developerOptionsShowGuideLines
    }
}
