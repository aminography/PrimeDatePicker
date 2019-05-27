package com.aminography.primedatepicker.calendarview.viewholder

import com.aminography.primeadapter.PrimeViewHolder
import com.aminography.primeadapter.callback.PrimeDelegate
import com.aminography.primecalendar.base.BaseCalendar
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
) : PrimeViewHolder<MonthDataHolder>(delegate, R.layout.list_item_month_view), PrimeMonthView.OnDayClickListener {

    override fun bindDataToView(dataHolder: MonthDataHolder) {
        with(itemView) {
            dataHolder.apply {
                monthView.onDayClickListener = this@MonthViewHolder
                monthView.setDate(year, month)
                callback?.apply {
                    monthView.setMinMaxDateCalendar(minDateCalendar, maxDateCalendar, false)

                    monthView.pickedSingleDay = null
                    monthView.pickedStartRangeDay = null
                    monthView.pickedEndRangeDay = null
                    when (pickType) {
                        PrimeMonthView.PickType.SINGLE -> {
                            pickedSingleDay?.let { day ->
                                if (day.year == year && day.month == month) {
                                    monthView.pickedSingleDay = day.dayOfMonth
                                }
                            }
                        }
                        PrimeMonthView.PickType.START_RANGE, PrimeMonthView.PickType.END_RANGE -> {
                            pickedStartRangeDay?.let { day ->
                                if (day.year == year && day.month == month) {
                                    monthView.pickedStartRangeDay = day.dayOfMonth
                                }
                            }
                            pickedEndRangeDay?.let { day ->
                                if (day.year == year && day.month == month) {
                                    monthView.pickedEndRangeDay = day.dayOfMonth
                                }
                            }
                        }
                    }
                    monthView.pickType = pickType
                }
            }
        }
    }

    override fun onDayClick(view: PrimeMonthView, day: BaseCalendar) {
        callback?.onDayClick(day)
    }

}