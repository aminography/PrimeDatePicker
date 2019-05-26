package com.aminography.primedatepicker.calendarview.viewholder

import com.aminography.primeadapter.PrimeViewHolder
import com.aminography.primeadapter.callback.PrimeDelegate
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder
import kotlinx.android.synthetic.main.list_item_month_view.view.*

class MonthViewHolder(
        delegate: PrimeDelegate
) : PrimeViewHolder<MonthDataHolder>(delegate, R.layout.list_item_month_view) {

    override fun bindDataToView(dataHolder: MonthDataHolder) {
        with(itemView) {
            dataHolder.apply {
                monthView.setDate(year, month)
            }
        }
    }
}