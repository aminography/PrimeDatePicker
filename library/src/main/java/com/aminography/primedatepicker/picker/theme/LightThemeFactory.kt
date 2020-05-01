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

    override val actionBarBackgroundColor: Int
        get() = getColor(R.color.lightButtonBarBackgroundColor)

    override val actionBarTodayTextColor: Int
        get() = getColor(R.color.lightButtonBarTodayTextColor)

    override val actionBarNegativeTextColor: Int
        get() = getColor(R.color.lightButtonBarNegativeTextColor)

    override val actionBarPositiveTextColor: Int
        get() = getColor(R.color.lightButtonBarPositiveTextColor)

    // ------- Selection Bar - General

    override val selectionBarBackgroundColor: Int
        get() = getColor(R.color.lightSelectionBarBackgroundColor)

    // ------- Selection Bar - Single Day

    override val selectionBarSingleDayItemFirstLabelTextColor: Int
        get() = getColor(R.color.lightSingleDayItemFirstLabelTextColor)

    override val selectionBarSingleDayItemSecondLabelTextColor: Int
        get() = getColor(R.color.lightSingleDayItemSecondLabelTextColor)

    // ------- Selection Bar - Range Days

    override val selectionBarRangeDaysItemBackgroundColor: Int
        get() = getColor(R.color.lightRangeDaysItemBackgroundColor)

    override val selectionBarRangeDaysItemFirstLabelTextColor: Int
        get() = getColor(R.color.lightRangeDaysItemFirstLabelTextColor)

    override val selectionBarRangeDaysItemSecondLabelTextColor: Int
        get() = getColor(R.color.lightRangeDaysItemSecondLabelTextColor)

    // ------- Selection Bar - Multiple Days

    override val selectionBarMultipleDaysItemBackgroundColor: Int
        get() = getColor(R.color.lightMultipleDaysItemBackgroundColor)

    override val selectionBarMultipleDaysItemFirstLabelTextColor: Int
        get() = getColor(R.color.lightMultipleDaysItemFirstLabelTextColor)

    override val selectionBarMultipleDaysItemSecondLabelTextColor: Int
        get() = getColor(R.color.lightMultipleDaysItemSecondLabelTextColor)

    // ------- Goto View

    override val gotoViewBackgroundColor: Int
        get() = getColor(R.color.lightGotoBackgroundColor)

    override val gotoViewTextColor: Int
        get() = getColor(R.color.lightGotoTextColor)

    override val gotoViewDividerColor: Int
        get() = getColor(R.color.lightGotoDividerColor)

}