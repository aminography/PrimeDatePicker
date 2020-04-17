package com.aminography.primedatepicker

import com.aminography.primecalendar.PrimeCalendar

/**
 * @author aminography
 */
interface OnMonthLabelClickListener {

    fun onMonthLabelClicked(
        calendar: PrimeCalendar,
        touchedX: Int,
        touchedY: Int
    )

}