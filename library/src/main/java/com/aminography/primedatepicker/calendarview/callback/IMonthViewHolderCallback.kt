package com.aminography.primedatepicker.calendarview.callback

import android.graphics.Typeface
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType

/**
 * @author aminography
 */
interface IMonthViewHolderCallback: OnDayPickedListener {

    val minDateCalendar: BaseCalendar?

    val maxDateCalendar: BaseCalendar?

    val pickType: PickType

    val pickedRangeStartCalendar: BaseCalendar?

    val pickedRangeEndCalendar: BaseCalendar?

    val pickedSingleDayCalendar: BaseCalendar?

    val typeface: Typeface?

    fun onHeightDetect(height: Float)

}