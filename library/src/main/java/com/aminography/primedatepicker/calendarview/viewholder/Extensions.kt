package com.aminography.primedatepicker.calendarview.viewholder

import com.aminography.primedatepicker.calendarview.callback.IMonthViewHolderCallback
import com.aminography.primedatepicker.monthview.PrimeMonthView

/**
 * @author aminography
 */

internal fun PrimeMonthView.configFrom(callback: IMonthViewHolderCallback?) {
    callback?.let {
        typeface = it.typeface
        minDateCalendar = it.minDateCalendar
        maxDateCalendar = it.maxDateCalendar
        pickedSingleDayCalendar = it.pickedSingleDayCalendar
        pickedRangeStartCalendar = it.pickedRangeStartCalendar
        pickedRangeEndCalendar = it.pickedRangeEndCalendar
        pickedMultipleDaysMap = it.pickedMultipleDaysMap
        pickType = it.pickType
        weekStartDay = it.weekStartDay
        locale = it.locale

        // ------- Common Attributes

        monthLabelTextColor = it.monthLabelTextColor
        weekLabelTextColor = it.weekLabelTextColor
        dayLabelTextColor = it.dayLabelTextColor
        todayLabelTextColor = it.todayLabelTextColor
        pickedDayLabelTextColor = it.pickedDayLabelTextColor
        pickedDayCircleColor = it.pickedDayCircleColor
        disabledDayLabelTextColor = it.disabledDayLabelTextColor
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
        monthLabelFormatter = it.monthLabelFormatter
        weekLabelFormatter = it.weekLabelFormatter
        developerOptionsShowGuideLines = it.developerOptionsShowGuideLines
    }
}