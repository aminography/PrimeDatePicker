package com.aminography.primedatepicker.calendarview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.calendarview.callback.IMonthViewHolderCallback
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder
import com.aminography.primedatepicker.monthview.PrimeMonthView

/**
 * @author aminography
 */
class MonthViewHolder(
        private val monthView: PrimeMonthView,
        private val callback: IMonthViewHolderCallback?
) : RecyclerView.ViewHolder(monthView),
        OnDayPickedListener,
        PrimeMonthView.OnHeightDetectListener {

    fun bindDataToView(dataHolder: MonthDataHolder) {
        dataHolder.apply {
            monthView.onDayPickedListener = this@MonthViewHolder
            monthView.onHeightDetectListener = this@MonthViewHolder
            callback?.apply {
                monthView.doNotInvalidate {
                    monthView.typeface = typeface
                    monthView.minDateCalendar = minDateCalendar
                    monthView.maxDateCalendar = maxDateCalendar
                    monthView.pickedSingleDayCalendar = pickedSingleDayCalendar
                    monthView.pickedRangeStartCalendar = pickedRangeStartCalendar
                    monthView.pickedRangeEndCalendar = pickedRangeEndCalendar
                    monthView.pickedMultipleDaysMap = pickedMultipleDaysMap
                    monthView.pickType = pickType
                    monthView.locale = locale
                    monthView.calendarType = calendarType

                    // Common Attributes -------------------------------------------------------

                    monthView.monthLabelTextColor = monthLabelTextColor
                    monthView.weekLabelTextColor = weekLabelTextColor
                    monthView.dayLabelTextColor = dayLabelTextColor
                    monthView.todayLabelTextColor = todayLabelTextColor
                    monthView.pickedDayLabelTextColor = pickedDayLabelTextColor
                    monthView.pickedDayCircleColor = pickedDayCircleColor
                    monthView.disabledDayLabelTextColor = disabledDayLabelTextColor
                    monthView.monthLabelTextSize = monthLabelTextSize
                    monthView.weekLabelTextSize = weekLabelTextSize
                    monthView.dayLabelTextSize = dayLabelTextSize
                    monthView.monthLabelTopPadding = monthLabelTopPadding
                    monthView.monthLabelBottomPadding = monthLabelBottomPadding
                    monthView.weekLabelTopPadding = weekLabelTopPadding
                    monthView.weekLabelBottomPadding = weekLabelBottomPadding
                    monthView.dayLabelVerticalPadding = dayLabelVerticalPadding
                    monthView.showTwoWeeksInLandscape = showTwoWeeksInLandscape
                    monthView.animateSelection = animateSelection
                    monthView.animationDuration = animationDuration
                    monthView.animationInterpolator = animationInterpolator
                }
            }
            monthView.goto(year, month)
        }
    }

    override fun onDayPicked(pickType: PickType,
                             singleDay: PrimeCalendar?,
                             startDay: PrimeCalendar?,
                             endDay: PrimeCalendar?,
                             multipleDays: List<PrimeCalendar>?) {
        callback?.onDayPicked(pickType, singleDay, startDay, endDay, multipleDays)
    }

    override fun onHeightDetect(height: Float) {
        callback?.onHeightDetect(height)
    }

}