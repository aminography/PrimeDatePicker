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

    override val actionBarBackgroundColor: Int
        get() = getColor(R.color.darkButtonBarBackgroundColor)

    override val actionBarTodayTextColor: Int
        get() = getColor(R.color.darkButtonBarTodayTextColor)

    override val actionBarNegativeTextColor: Int
        get() = getColor(R.color.darkButtonBarNegativeTextColor)

    override val actionBarPositiveTextColor: Int
        get() = getColor(R.color.darkButtonBarPositiveTextColor)

    // ------- Selection Bar - General

    override val selectionBarBackgroundColor: Int
        get() = getColor(R.color.darkSelectionBarBackgroundColor)

    // ------- Selection Bar - Single Day

    override val selectionBarSingleDayItemFirstLabelTextColor: Int
        get() = getColor(R.color.darkSingleDayItemFirstLabelTextColor)

    override val selectionBarSingleDayItemSecondLabelTextColor: Int
        get() = getColor(R.color.darkSingleDayItemSecondLabelTextColor)

    // ------- Selection Bar - Range Days

    override val selectionBarRangeDaysItemBackgroundColor: Int
        get() = getColor(R.color.darkRangeDaysItemBackgroundColor)

    override val selectionBarRangeDaysItemFirstLabelTextColor: Int
        get() = getColor(R.color.darkRangeDaysItemFirstLabelTextColor)

    override val selectionBarRangeDaysItemSecondLabelTextColor: Int
        get() = getColor(R.color.darkRangeDaysItemSecondLabelTextColor)

    // ------- Selection Bar - Multiple Days

    override val selectionBarMultipleDaysItemBackgroundColor: Int
        get() = getColor(R.color.darkMultipleDaysItemBackgroundColor)

    override val selectionBarMultipleDaysItemFirstLabelTextColor: Int
        get() = getColor(R.color.darkMultipleDaysItemFirstLabelTextColor)

    override val selectionBarMultipleDaysItemSecondLabelTextColor: Int
        get() = getColor(R.color.darkMultipleDaysItemSecondLabelTextColor)

    // ------- Goto View

    override val gotoViewBackgroundColor: Int
        get() = getColor(R.color.darkGotoBackgroundColor)

    override val gotoViewTextColor: Int
        get() = getColor(R.color.darkGotoTextColor)

    override val gotoViewDividerColor: Int
        get() = getColor(R.color.darkGotoDividerColor)

}