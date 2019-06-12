package com.aminography.primedatepicker.calendarview.callback

import android.graphics.Typeface
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType

/**
 * @author aminography
 */
interface IMonthViewHolderCallback : OnDayPickedListener {

    val minDateCalendar: PrimeCalendar?

    val maxDateCalendar: PrimeCalendar?

    val pickType: PickType

    val pickedRangeStartCalendar: PrimeCalendar?

    val pickedRangeEndCalendar: PrimeCalendar?

    val pickedSingleDayCalendar: PrimeCalendar?

    val typeface: Typeface?

    fun onHeightDetect(height: Float)

}