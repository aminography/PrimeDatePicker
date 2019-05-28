package com.aminography.primedatepicker.calendarview.callback

import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primedatepicker.PickType

/**
 * @author aminography
 */
interface IMonthViewHolderCallback {

    val minDateCalendar: BaseCalendar?

    val maxDateCalendar: BaseCalendar?

    val pickType: PickType

    val pickedStartRangeCalendar: BaseCalendar?

    val pickedEndRangeCalendar: BaseCalendar?

    val pickedSingleDayCalendar: BaseCalendar?

    fun onDayClick(day: BaseCalendar)
}