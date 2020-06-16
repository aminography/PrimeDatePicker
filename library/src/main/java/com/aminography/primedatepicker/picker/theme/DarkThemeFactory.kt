package com.aminography.primedatepicker.picker.theme

import android.util.SparseIntArray
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.picker.theme.base.NormalThemeFactory
import java.util.*

/**
 * @author aminography
 */
open class DarkThemeFactory : NormalThemeFactory() {

    // --------------------------------------- Calendar View ---------------------------------------

    override val calendarViewBackgroundColor: Int
        get() = getColor(R.color.darkElementBackgroundColor)

    // ------- Month Label

    override val calendarViewMonthLabelTextColor: Int
        get() = getColor(R.color.darkMonthLabelTextColor)

    // ------- Week Label

    override val calendarViewWeekLabelTextColors: SparseIntArray
        get() = SparseIntArray(7).apply {
            val defaultColor = getColor(R.color.darkWeekLabelTextColor)
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
        get() = getColor(R.color.darkDayLabelTextColor)

    override val calendarViewTodayLabelTextColor: Int
        get() = getColor(R.color.darkTodayLabelTextColor)

    override val calendarViewPickedDayLabelTextColor: Int
        get() = getColor(R.color.darkPickedDayLabelTextColor)

    override val calendarViewPickedDayInRangeLabelTextColor: Int
        get() = getColor(R.color.darkPickedDayInRangeLabelTextColor)

    override val calendarViewPickedDayBackgroundColor: Int
        get() = getColor(R.color.darkPickedDayBackgroundColor)

    override val calendarViewPickedDayInRangeBackgroundColor: Int
        get() = getColor(R.color.darkPickedDayInRangeBackgroundColor)

    override val calendarViewDisabledDayLabelTextColor: Int
        get() = getColor(R.color.darkDisabledDayLabelTextColor)

    override val calendarViewAdjacentMonthDayLabelTextColor: Int
        get() = getColor(R.color.darkAdjacentMonthDayLabelTextColor)

    // ------- Divider

    override val calendarViewDividerColor: Int
        get() = getColor(R.color.darkDividerColor)

    // ------------------------------------ Picker Bottom Sheet ------------------------------------

    override val dialogBackgroundColor: Int
        get() = getColor(R.color.darkButtonBarBackgroundColor)

    // ------- Action Bar

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