package com.aminography.primedatepicker.calendarview.viewholder

import com.aminography.primedatepicker.calendarview.callback.IMonthViewHolderCallback
import com.aminography.primedatepicker.monthview.PrimeMonthView

/**
 * @author aminography
 */

internal fun PrimeMonthView.configFrom(callback: IMonthViewHolderCallback?) {
    callback?.let {
        pickType = it.pickType
        typeface = it.typeface
        minDateCalendar = it.minDateCalendar
        maxDateCalendar = it.maxDateCalendar
        pickedSingleDayCalendar = it.pickedSingleDayCalendar
        pickedRangeStartCalendar = it.pickedRangeStartCalendar
        pickedRangeEndCalendar = it.pickedRangeEndCalendar
        pickedMultipleDaysMap = it.pickedMultipleDaysMap
        disabledDaysSet = it.disabledDaysSet
        firstDayOfWeek = it.firstDayOfWeek
        locale = it.locale
        weekLabelTextColors = it.weekLabelTextColors

        // ------- Common Attributes

        monthLabelTextColor = it.monthLabelTextColor
        weekLabelTextColor = it.weekLabelTextColor
        dayLabelTextColor = it.dayLabelTextColor
        todayLabelTextColor = it.todayLabelTextColor
        pickedDayLabelTextColor = it.pickedDayLabelTextColor
        pickedDayInRangeLabelTextColor = it.pickedDayInRangeLabelTextColor
        pickedDayBackgroundColor = it.pickedDayBackgroundColor
        pickedDayInRangeBackgroundColor = it.pickedDayInRangeBackgroundColor
        disabledDayLabelTextColor = it.disabledDayLabelTextColor
        adjacentMonthDayLabelTextColor = it.adjacentMonthDayLabelTextColor
        monthLabelTextSize = it.monthLabelTextSize
        weekLabelTextSize = it.weekLabelTextSize
        dayLabelTextSize = it.dayLabelTextSize
        monthLabelTopPadding = it.monthLabelTopPadding
        monthLabelBottomPadding = it.monthLabelBottomPadding
        weekLabelTopPadding = it.weekLabelTopPadding
        weekLabelBottomPadding = it.weekLabelBottomPadding
        dayLabelVerticalPadding = it.dayLabelVerticalPadding

        setPadding(
            it.elementPaddingLeft,
            it.elementPaddingTop,
            it.elementPaddingRight,
            it.elementPaddingBottom
        )

        pickedDayBackgroundShapeType = it.pickedDayBackgroundShapeType
        pickedDayRoundSquareCornerRadius = it.pickedDayRoundSquareCornerRadius

        showTwoWeeksInLandscape = it.showTwoWeeksInLandscape
        showAdjacentMonthDays = it.showAdjacentMonthDays
        animateSelection = it.animateSelection
        animationDuration = it.animationDuration
        animationInterpolator = it.animationInterpolator
        monthLabelFormatter = it.monthLabelFormatter
        weekLabelFormatter = it.weekLabelFormatter
        developerOptionsShowGuideLines = it.developerOptionsShowGuideLines
    }
}