package com.aminography.primedatepicker.picker.header.multiple.dataholder

import com.aminography.primecalendar.PrimeCalendar
import java.util.*

/**
 * @author aminography
 */
data class PickedDayDataHolder(
    override val id: String,
    val calendar: PrimeCalendar
) : BasePickedDayDataHolder(id) {

    val year: String
    val month: String
    val day: String

    init {
        calendar.shortDateString.split("/").let {
            year = it[0].substring(2)
            month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, calendar.locale) ?: ""
            day = it[2]
        }
    }

}