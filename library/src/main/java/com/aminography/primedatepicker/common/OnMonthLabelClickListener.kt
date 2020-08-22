package com.aminography.primedatepicker.common

import com.aminography.primecalendar.PrimeCalendar

/**
 * @author aminography
 */
fun interface OnMonthLabelClickListener {
    fun onMonthLabelClicked(
        calendar: PrimeCalendar,
        touchedX: Int,
        touchedY: Int
    )
}