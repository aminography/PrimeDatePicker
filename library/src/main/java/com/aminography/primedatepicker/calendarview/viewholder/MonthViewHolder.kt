package com.aminography.primedatepicker.calendarview.viewholder

import com.aminography.primeadapter.PrimeViewHolder
import com.aminography.primeadapter.callback.PrimeDelegate
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.calendarview.callback.IMonthViewHolderCallback
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder
import com.aminography.primedatepicker.monthview.PrimeMonthView
import kotlinx.android.synthetic.main.list_item_month_view.view.*

/**
 * @author aminography
 */
class MonthViewHolder(
        delegate: PrimeDelegate,
        private val callback: IMonthViewHolderCallback?
) : PrimeViewHolder<MonthDataHolder>(delegate, R.layout.list_item_month_view),
        OnDayPickedListener,
        PrimeMonthView.OnHeightDetectListener {

    override fun bindDataToView(dataHolder: MonthDataHolder) {
        with(itemView) {
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
                        monthView.pickType = pickType

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
                    }
                }
                monthView.setDate(calendarType, year, month)
            }
        }
    }

    override fun onDayPicked(pickType: PickType, singleDay: PrimeCalendar?, startDay: PrimeCalendar?, endDay: PrimeCalendar?) {
        callback?.onDayPicked(pickType, singleDay, startDay, endDay)
    }

    override fun onHeightDetect(height: Float) {
        callback?.onHeightDetect(height)
    }

}