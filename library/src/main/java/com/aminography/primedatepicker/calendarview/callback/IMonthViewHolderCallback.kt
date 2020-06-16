package com.aminography.primedatepicker.calendarview.callback

import android.graphics.Typeface
import android.util.SparseIntArray
import android.view.animation.Interpolator
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.common.LabelFormatter
import com.aminography.primedatepicker.common.OnDayPickedListener
import com.aminography.primedatepicker.common.PickType
import java.util.*

/**
 * @author aminography
 */
interface IMonthViewHolderCallback : OnDayPickedListener {

    val minDateCalendar: PrimeCalendar?

    val maxDateCalendar: PrimeCalendar?

    val pickType: PickType

    val firstDayOfWeek: Int

    val locale: Locale

    val pickedRangeStartCalendar: PrimeCalendar?

    val pickedRangeEndCalendar: PrimeCalendar?

    val pickedSingleDayCalendar: PrimeCalendar?

    val pickedMultipleDaysMap: LinkedHashMap<String, PrimeCalendar>?

    val disabledDaysSet: MutableSet<String>?

    val weekLabelTextColors: SparseIntArray?

    val typeface: Typeface?

    fun onHeightDetect(height: Float)

    fun onMonthLabelClicked(calendar: PrimeCalendar, touchedX: Int, touchedY: Int)

    // Common Attributes ---------------------------------------------------------------------------

    val monthLabelTextColor: Int
    val weekLabelTextColor: Int
    val dayLabelTextColor: Int
    val todayLabelTextColor: Int
    val pickedDayLabelTextColor: Int
    val pickedDayInRangeLabelTextColor: Int
    val pickedDayBackgroundColor: Int
    val pickedDayInRangeBackgroundColor: Int
    val disabledDayLabelTextColor: Int
    val adjacentMonthDayLabelTextColor: Int
    val monthLabelTextSize: Int
    val weekLabelTextSize: Int
    val dayLabelTextSize: Int
    val monthLabelTopPadding: Int
    val monthLabelBottomPadding: Int
    val weekLabelTopPadding: Int
    val weekLabelBottomPadding: Int
    val dayLabelVerticalPadding: Int

    val elementPaddingLeft: Int
    val elementPaddingRight: Int
    val elementPaddingTop: Int
    val elementPaddingBottom: Int

    val pickedDayBackgroundShapeType: BackgroundShapeType
    val pickedDayRoundSquareCornerRadius: Int

    val showTwoWeeksInLandscape: Boolean
    val showAdjacentMonthDays: Boolean
    val animateSelection: Boolean
    val animationDuration: Int
    val animationInterpolator: Interpolator
    val monthLabelFormatter: LabelFormatter
    val weekLabelFormatter: LabelFormatter
    val toFocusDay: PrimeCalendar?
    val developerOptionsShowGuideLines: Boolean
}