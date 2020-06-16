package com.aminography.primedatepicker.picker.theme

import android.util.SparseIntArray
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.picker.theme.base.NormalThemeFactory
import java.util.*

/**
 * @author aminography
 */
open class LightThemeFactory : NormalThemeFactory() {

    // --------------------------------------- Calendar View ---------------------------------------

    override val calendarViewBackgroundColor: Int
        get() = getColor(R.color.lightElementBackgroundColor)

    // ------- Month Label

    override val calendarViewMonthLabelTextColor: Int
        get() = getColor(R.color.lightMonthLabelTextColor)

    // ------- Week Label

    override val calendarViewWeekLabelTextColors: SparseIntArray
        get() = SparseIntArray(7).apply {
            val defaultColor = getColor(R.color.lightWeekLabelTextColor)
            put(Calendar.SUNDAY, defaultColor)
            put(Calendar.MONDAY, defaultColor)
            put(Calendar.TUESDAY, defaultColor)
            put(Calendar.WEDNESDAY, defaultColor)
            put(Calendar.THURSDAY, defaultColor)
            put(Calendar.FRIDAY, defaultColor)
            put(Calendar.SATURDAY, defaultColor)
        }

    // ------- Day Label

    override val calendarViewDayLabelTextColor: Int
        get() = getColor(R.color.lightDayLabelTextColor)

    override val calendarViewTodayLabelTextColor: Int
        get() = getColor(R.color.lightTodayLabelTextColor)

    override val calendarViewPickedDayLabelTextColor: Int
        get() = getColor(R.color.lightPickedDayLabelTextColor)

    override val calendarViewPickedDayInRangeLabelTextColor: Int
        get() = getColor(R.color.lightPickedDayInRangeLabelTextColor)

    override val calendarViewPickedDayBackgroundColor: Int
        get() = getColor(R.color.lightPickedDayBackgroundColor)

    override val calendarViewPickedDayInRangeBackgroundColor: Int
        get() = getColor(R.color.lightPickedDayInRangeBackgroundColor)

    override val calendarViewDisabledDayLabelTextColor: Int
        get() = getColor(R.color.lightDisabledDayLabelTextColor)

    override val calendarViewAdjacentMonthDayLabelTextColor: Int
        get() = getColor(R.color.lightAdjacentMonthDayLabelTextColor)

    // ------- Divider

    override val calendarViewDividerColor: Int
        get() = getColor(R.color.lightDividerColor)

    // ------------------------------------ Picker Bottom Sheet ------------------------------------

    override val dialogBackgroundColor: Int
        get() = getColor(R.color.lightButtonBarBackgroundColor)

    // ------- Action Bar

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

    override val selectionBarSingleDayItemTopLabelTextColor: Int
        get() = getColor(R.color.lightSelectionBarSingleDayItemTopLabelTextColor)

    override val selectionBarSingleDayItemBottomLabelTextColor: Int
        get() = getColor(R.color.lightSelectionBarSingleDayItemBottomLabelTextColor)

    // ------- Selection Bar - Range Days

    override val selectionBarRangeDaysItemBackgroundColor: Int
        get() = getColor(R.color.lightSelectionBarRangeDaysItemBackgroundColor)

    override val selectionBarRangeDaysItemTopLabelTextColor: Int
        get() = getColor(R.color.lightSelectionBarRangeDaysItemTopLabelTextColor)

    override val selectionBarRangeDaysItemBottomLabelTextColor: Int
        get() = getColor(R.color.lightSelectionBarRangeDaysItemBottomLabelTextColor)

    // ------- Selection Bar - Multiple Days

    override val selectionBarMultipleDaysItemBackgroundColor: Int
        get() = getColor(R.color.lightSelectionBarMultipleDaysItemBackgroundColor)

    override val selectionBarMultipleDaysItemTopLabelTextColor: Int
        get() = getColor(R.color.lightSelectionBarMultipleDaysItemTopLabelTextColor)

    override val selectionBarMultipleDaysItemBottomLabelTextColor: Int
        get() = getColor(R.color.lightSelectionBarMultipleDaysItemBottomLabelTextColor)

    // ------- Goto View

    override val gotoViewBackgroundColor: Int
        get() = getColor(R.color.lightGotoBackgroundColor)

    override val gotoViewTextColor: Int
        get() = getColor(R.color.lightGotoTextColor)

    override val gotoViewDividerColor: Int
        get() = getColor(R.color.lightGotoDividerColor)

}