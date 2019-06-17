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

    // Common Attributes ---------------------------------------------------------------------------

    val monthLabelTextColor: Int
    val weekLabelTextColor: Int
    val dayLabelTextColor: Int
    val todayLabelTextColor: Int
    val pickedDayLabelTextColor: Int
    val pickedDayCircleColor: Int
    val disabledDayLabelTextColor: Int
    val monthLabelTextSize: Int
    val weekLabelTextSize: Int
    val dayLabelTextSize: Int
    val monthLabelTopPadding: Int
    val monthLabelBottomPadding: Int
    val weekLabelTopPadding: Int
    val weekLabelBottomPadding: Int
    val dayLabelVerticalPadding: Int
    val showTwoWeeksInLandscape: Boolean

}