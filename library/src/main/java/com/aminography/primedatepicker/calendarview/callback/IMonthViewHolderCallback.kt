package com.aminography.primedatepicker.calendarview.callback

import android.graphics.Typeface
import android.view.animation.Interpolator
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.monthview.MonthLabelFormatter
import com.aminography.primedatepicker.monthview.WeekLabelFormatter
import java.util.*

/**
 * @author aminography
 */
interface IMonthViewHolderCallback : OnDayPickedListener {

    val minDateCalendar: PrimeCalendar?

    val maxDateCalendar: PrimeCalendar?

    val pickType: PickType

    val weekStartDay: Int

    val locale: Locale

    val pickedRangeStartCalendar: PrimeCalendar?

    val pickedRangeEndCalendar: PrimeCalendar?

    val pickedSingleDayCalendar: PrimeCalendar?

    val pickedMultipleDaysMap: LinkedHashMap<String, PrimeCalendar>?

    val typeface: Typeface?

    fun onHeightDetect(height: Float)

    fun onMonthLabelClicked(calendar: PrimeCalendar, touchedX: Int, touchedY: Int)

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
    val animateSelection: Boolean
    val animationDuration: Int
    val animationInterpolator: Interpolator
    val monthLabelFormatter: MonthLabelFormatter
    val weekLabelFormatter: WeekLabelFormatter
    val toFocusDay: PrimeCalendar?
    val developerOptionsShowGuideLines: Boolean
}