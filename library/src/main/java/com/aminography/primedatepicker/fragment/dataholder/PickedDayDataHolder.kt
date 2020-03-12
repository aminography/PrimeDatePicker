package com.aminography.primedatepicker.fragment.dataholder

import com.aminography.primecalendar.PrimeCalendar

data class PickedDayDataHolder(
        override val id: String,
        val calendar: PrimeCalendar
) : BasePickedDayDataHolder(id)