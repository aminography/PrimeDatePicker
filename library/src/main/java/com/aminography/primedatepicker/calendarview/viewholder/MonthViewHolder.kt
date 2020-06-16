package com.aminography.primedatepicker.calendarview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.calendarview.callback.IMonthViewHolderCallback
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder
import com.aminography.primedatepicker.common.OnDayPickedListener
import com.aminography.primedatepicker.common.OnMonthLabelClickListener
import com.aminography.primedatepicker.common.PickType
import com.aminography.primedatepicker.monthview.PrimeMonthView
import com.aminography.primedatepicker.monthview.SimpleMonthView

/**
 * @author aminography
 */
class MonthViewHolder(
    private val monthView: PrimeMonthView,
    private val callback: IMonthViewHolderCallback?
) : RecyclerView.ViewHolder(monthView),
    OnDayPickedListener,
    OnMonthLabelClickListener,
    SimpleMonthView.OnHeightDetectListener {

    internal fun bindDataToView(dataHolder: MonthDataHolder) {
        monthView.onDayPickedListener = this@MonthViewHolder
        monthView.onMonthLabelClickListener = this@MonthViewHolder
        monthView.onHeightDetectListener = this@MonthViewHolder

        monthView.doNotInvalidate {
            monthView.configFrom(callback)
            monthView.calendarType = dataHolder.calendarType
        }

        dataHolder.run {
            monthView.goto(year, month)
            callback?.toFocusDay?.let {
                if (it.year == year && it.month == month) {
                    monthView.focusOnDay(it)
                }
            }
        }
    }

    override fun onDayPicked(
        pickType: PickType,
        singleDay: PrimeCalendar?,
        startDay: PrimeCalendar?,
        endDay: PrimeCalendar?,
        multipleDays: List<PrimeCalendar>
    ) {
        callback?.onDayPicked(pickType, singleDay, startDay, endDay, multipleDays)
    }

    override fun onHeightDetect(height: Float) {
        callback?.onHeightDetect(height)
    }

    override fun onMonthLabelClicked(calendar: PrimeCalendar, touchedX: Int, touchedY: Int) {
        callback?.onMonthLabelClicked(calendar, touchedX, touchedY)
    }

}