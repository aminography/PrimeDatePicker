package com.aminography.primedatepicker.picker.theme

import android.util.TypedValue
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.picker.action.ActionView
import com.aminography.primedatepicker.picker.go.GotoView
import com.aminography.primedatepicker.picker.header.BaseLazyView
import com.aminography.primedatepicker.picker.header.HeaderView
import kotlinx.android.synthetic.main.action_container.view.*

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
    }
}

internal fun GotoView.applyTheme(theme: BaseThemeFactory?) {
    theme?.let {
        with(rootView) {
            setBackgroundColor(it.gotoBackgroundColor)
        }
    }
}

internal fun PrimeCalendarView.applyTheme(theme: BaseThemeFactory?) {
    theme?.let {
        // ------- Color Customizations
        setBackgroundColor(it.backgroundColor)
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
        loadFactor = it.loadFactor
        maxTransitionLength = it.maxTransitionLength
        transitionSpeedFactor = it.transitionSpeedFactor
        flingOrientation = it.flingOrientation
        monthLabelFormatter = it.monthLabelFormatter
        weekLabelFormatter = it.weekLabelFormatter
        developerOptionsShowGuideLines = it.developerOptionsShowGuideLines
    }
}
