package com.aminography.primedatepicker.common

import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.monthview.PrimeMonthView

/**
 * @author aminography
 */
fun interface OnDayPickedListener {
    /**
     * Called when one day is picked by the user.
     *
     * @param pickType     Specifies type of the picked day based on [pickType] which is set to the [PrimeMonthView] or [PrimeCalendarView].
     * @param singleDay    Specifies the picked day if the [pickType] would be [PickType.SINGLE].
     * @param startDay     Specifies the start day of picked range if the [pickType] would be [PickType.RANGE_START].
     * @param endDay       Specifies the end day of picked range if the [pickType] would be [PickType.RANGE_END].
     * @param multipleDays Specifies the list of picked days if the [pickType] would be [PickType.MULTIPLE].
     */
    fun onDayPicked(
        pickType: PickType,
        singleDay: PrimeCalendar?,
        startDay: PrimeCalendar?,
        endDay: PrimeCalendar?,
        multipleDays: List<PrimeCalendar>
    )
}