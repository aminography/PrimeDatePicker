package com.aminography.primedatepicker.picker.theme

import com.aminography.primedatepicker.R

/**
 * @author aminography
 */
open class DarkThemeFactory : BaseNormalThemeFactory() {

    // --------------------------------------- Calendar View ---------------------------------------

    override val elementBackgroundColor: Int
        get() = getColor(R.color.darkElementBackgroundColor)

    // ------- Month Label

    override val monthLabelTextColor: Int
        get() = getColor(R.color.darkMonthLabelTextColor)

    // ------- Week Label

    override val weekLabelTextColor: Int
        get() = getColor(R.color.darkWeekLabelTextColor)

    // ------- Day Label

    override val dayLabelTextColor: Int
        get() = getColor(R.color.darkDayLabelTextColor)

    override val todayLabelTextColor: Int
        get() = getColor(R.color.darkTodayLabelTextColor)

    override val pickedDayLabelTextColor: Int
        get() = getColor(R.color.darkPickedDayLabelTextColor)

    override val pickedDayCircleColor: Int
        get() = getColor(R.color.darkPickedDayCircleColor)

    override val disabledDayLabelTextColor: Int
        get() = getColor(R.color.darkDisabledDayLabelTextColor)

    // ------- Divider

    override val dividerColor: Int
        get() = getColor(R.color.darkDividerColor)

    // ------------------------------------ Picker Bottom Sheet ------------------------------------

    override val buttonBarBackgroundColor: Int
        get() = getColor(R.color.darkButtonBarBackgroundColor)

    override val buttonBarTodayTextColor: Int
        get() = getColor(R.color.darkButtonBarTodayTextColor)

    override val buttonBarNegativeTextColor: Int
        get() = getColor(R.color.darkButtonBarNegativeTextColor)

    override val buttonBarPositiveTextColor: Int
        get() = getColor(R.color.darkButtonBarPositiveTextColor)

    // ------- Selection Bar - General

    override val selectionBarBackgroundColor: Int
        get() = getColor(R.color.darkSelectionBarBackgroundColor)

    // ------- Selection Bar - Single Day

    override val singleDayItemFirstLabelTextColor: Int
        get() = getColor(R.color.darkSingleDayItemFirstLabelTextColor)

    override val singleDayItemSecondLabelTextColor: Int
        get() = getColor(R.color.darkSingleDayItemSecondLabelTextColor)

    // ------- Selection Bar - Range Days

    override val rangeDaysItemBackgroundColor: Int
        get() = getColor(R.color.darkRangeDaysItemBackgroundColor)

    override val rangeDaysItemFirstLabelTextColor: Int
        get() = getColor(R.color.darkRangeDaysItemFirstLabelTextColor)

    override val rangeDaysItemSecondLabelTextColor: Int
        get() = getColor(R.color.darkRangeDaysItemSecondLabelTextColor)

    // ------- Selection Bar - Multiple Days

    override val multipleDaysItemBackgroundColor: Int
        get() = getColor(R.color.darkMultipleDaysItemBackgroundColor)

    override val multipleDaysItemFirstLabelTextColor: Int
        get() = getColor(R.color.darkMultipleDaysItemFirstLabelTextColor)

    override val multipleDaysItemSecondLabelTextColor: Int
        get() = getColor(R.color.darkMultipleDaysItemSecondLabelTextColor)

    // ------- Goto View

    override val gotoBackgroundColor: Int
        get() = getColor(R.color.darkGotoBackgroundColor)

    override val gotoTextColor: Int
        get() = getColor(R.color.darkGotoTextColor)

    override val gotoDividerColor: Int
        get() = getColor(R.color.darkGotoDividerColor)

}