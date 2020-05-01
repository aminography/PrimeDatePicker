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

    override val selectionBarSingleDayItemTopLabelTextColor: Int
        get() = getColor(R.color.darkSelectionBarSingleDayItemTopLabelTextColor)

    override val selectionBarSingleDayItemBottomLabelTextColor: Int
        get() = getColor(R.color.darkSelectionBarSingleDayItemBottomLabelTextColor)

    // ------- Selection Bar - Range Days

    override val selectionBarRangeDaysItemBackgroundColor: Int
        get() = getColor(R.color.darkSelectionBarRangeDaysItemBackgroundColor)

    override val selectionBarRangeDaysItemTopLabelTextColor: Int
        get() = getColor(R.color.darkSelectionBarRangeDaysItemTopLabelTextColor)

    override val selectionBarRangeDaysItemBottomLabelTextColor: Int
        get() = getColor(R.color.darkSelectionBarRangeDaysItemBottomLabelTextColor)

    // ------- Selection Bar - Multiple Days

    override val selectionBarMultipleDaysItemBackgroundColor: Int
        get() = getColor(R.color.darkSelectionBarMultipleDaysItemBackgroundColor)

    override val selectionBarMultipleDaysItemTopLabelTextColor: Int
        get() = getColor(R.color.darkSelectionBarMultipleDaysItemTopLabelTextColor)

    override val selectionBarMultipleDaysItemBottomLabelTextColor: Int
        get() = getColor(R.color.darkSelectionBarMultipleDaysItemBottomLabelTextColor)

    // ------- Goto View

    override val gotoViewBackgroundColor: Int
        get() = getColor(R.color.darkGotoViewBackgroundColor)

    override val gotoViewTextColor: Int
        get() = getColor(R.color.darkGotoViewTextColor)

    override val gotoViewDividerColor: Int
        get() = getColor(R.color.darkGotoViewDividerColor)

}