package com.aminography.primedatepicker

import com.aminography.primecalendar.PrimeCalendar

/**
 * @author aminography
 */
interface OnDayPickedListener {

    fun onDayPicked(pickType: PickType, singleDay: PrimeCalendar?, startDay: PrimeCalendar?, endDay: PrimeCalendar?)

}