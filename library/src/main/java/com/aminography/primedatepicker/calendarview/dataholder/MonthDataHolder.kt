package com.aminography.primedatepicker.calendarview.dataholder

import androidx.recyclerview.widget.RecyclerView
import com.aminography.primecalendar.common.CalendarType

/**
 * @author aminography
 */
data class MonthDataHolder(
    val calendarType: CalendarType,
    val year: Int,
    val month: Int,
    var listPosition: Int = RecyclerView.NO_POSITION,
    var hasDivider: Boolean = true
) {

    val offset: Int
        get() = year * 12 + month

}