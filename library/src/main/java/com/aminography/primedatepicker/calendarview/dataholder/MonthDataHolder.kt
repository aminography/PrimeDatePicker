package com.aminography.primedatepicker.calendarview.dataholder

import com.aminography.primeadapter.PrimeDataHolder
import com.aminography.primeadapter.annotation.DataHolder
import com.aminography.primecalendar.base.BaseCalendar

/**
 * @author aminography
 */
@DataHolder
data class MonthDataHolder(
        val year: Int,
        val month: Int
) : PrimeDataHolder() {

    val offset: Int
        get() = year * 12 + month

}