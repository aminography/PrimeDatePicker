package com.aminography.primedatepicker.fragment.header.multiple.dataholder

import com.aminography.primecalendar.PrimeCalendar

/**
 * @author aminography
 */
data class PickedDayDataHolder(
    override val id: String,
    val calendar: PrimeCalendar
) : BasePickedDayDataHolder(id)