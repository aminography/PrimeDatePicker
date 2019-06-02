package com.aminography.primedatepicker

import com.aminography.primecalendar.base.BaseCalendar

/**
 * @author aminography
 */
interface OnDayPickedListener {

    fun onDayPicked(pickType: PickType, singleDay: BaseCalendar?, startDay: BaseCalendar?, endDay: BaseCalendar?)

}