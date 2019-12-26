package com.aminography.primedatepicker

import com.aminography.primecalendar.PrimeCalendar

/**
 * @author aminography
 */
interface OnDayPickedListener {

    /**
     * Called when one day is clicked by the user.
     *
     * @param pickType
     *        Specifies type of the picked day based on [PickType] which is set to the [com.aminography.primedatepicker.monthview.PrimeMonthView] or [com.aminography.primedatepicker.calendarview.PrimeCalendarView].
     * @param singleDay
     *        Specifies the picked day if the [pickType] is [PickType.SINGLE].
     * @param startDay
     *        Specifies the start day of picked range if the [pickType] is [PickType.RANGE_START].
     * @param endDay
     *        Specifies the end day of picked range if the [pickType] is [PickType.RANGE_END].
     * @param multipleDays
     *        Specifies the list of picked days if the [pickType] is [PickType.MULTIPLE].
     */
    fun onDayPicked(
            pickType: PickType,
            singleDay: PrimeCalendar?,
            startDay: PrimeCalendar?,
            endDay: PrimeCalendar?,
            multipleDays: List<PrimeCalendar>?
    )

}