package com.aminography.primedatepicker.picker.theme

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
        setBackgroundColor(it.backgroundColor)
        dayLabelTextColor = it.dayLabelTextColor
    }
}
