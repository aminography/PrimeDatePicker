package com.aminography.primedatepicker.picker.theme

import com.aminography.primedatepicker.R

/**
 * @author aminography
 */
open class LightThemeFactory : BaseNormalThemeFactory() {

    // --------------------------------------- Calendar View ---------------------------------------

    override val elementBackgroundColor: Int
        get() = getColor(R.color.lightElementBackgroundColor)

    // ------- Month Label

    override val monthLabelTextColor: Int
        get() = getColor(R.color.lightMonthLabelTextColor)

    // ------- Week Label

    override val weekLabelTextColor: Int
        get() = getColor(R.color.lightWeekLabelTextColor)

    // ------- Day Label

    override val dayLabelTextColor: Int
        get() = getColor(R.color.lightDayLabelTextColor)

    override val todayLabelTextColor: Int
        get() = getColor(R.color.lightTodayLabelTextColor)

    override val pickedDayLabelTextColor: Int
        get() = getColor(R.color.lightPickedDayLabelTextColor)

    override val pickedDayCircleColor: Int
        get() = getColor(R.color.lightPickedDayCircleColor)

    override val disabledDayLabelTextColor: Int
        get() = getColor(R.color.lightDisabledDayLabelTextColor)

    // ------- Divider

    override val dividerColor: Int
        get() = getColor(R.color.lightDividerColor)

    // ------------------------------------ Picker Bottom Sheet ------------------------------------

    override val buttonBarBackgroundColor: Int
        get() = getColor(R.color.lightButtonBarBackgroundColor)

    override val buttonBarTodayTextColor: Int
        get() = getColor(R.color.lightButtonBarTodayTextColor)

    override val buttonBarNegativeTextColor: Int
        get() = getColor(R.color.lightButtonBarNegativeTextColor)

    override val buttonBarPositiveTextColor: Int
        get() = getColor(R.color.lightButtonBarPositiveTextColor)

    // ------- Selection Bar - General

    override val selectionBarBackgroundColor: Int
        get() = getColor(R.color.lightSelectionBarBackgroundColor)

    // ------- Selection Bar - Single Day

    override val singleDayItemFirstLabelTextColor: Int
        get() = getColor(R.color.lightSingleDayItemFirstLabelTextColor)

    override val singleDayItemSecondLabelTextColor: Int
        get() = getColor(R.color.lightSingleDayItemSecondLabelTextColor)

    // ------- Selection Bar - Range Days

    override val rangeDaysItemBackgroundColor: Int
        get() = getColor(R.color.lightRangeDaysItemBackgroundColor)

    override val rangeDaysItemFirstLabelTextColor: Int
        get() = getColor(R.color.lightRangeDaysItemFirstLabelTextColor)

    override val rangeDaysItemSecondLabelTextColor: Int
        get() = getColor(R.color.lightRangeDaysItemSecondLabelTextColor)

    // ------- Selection Bar - Multiple Days

    override val multipleDaysItemBackgroundColor: Int
        get() = getColor(R.color.lightMultipleDaysItemBackgroundColor)

    override val multipleDaysItemFirstLabelTextColor: Int
        get() = getColor(R.color.lightMultipleDaysItemFirstLabelTextColor)

    override val multipleDaysItemSecondLabelTextColor: Int
        get() = getColor(R.color.lightMultipleDaysItemSecondLabelTextColor)

    // ------- Goto View

    override val gotoBackgroundColor: Int
        get() = getColor(R.color.lightGotoBackgroundColor)

    override val gotoTextColor: Int
        get() = getColor(R.color.lightGotoTextColor)

    override val gotoDividerColor: Int
        get() = getColor(R.color.lightGotoDividerColor)

}