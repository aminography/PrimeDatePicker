package com.aminography.primedatepicker.sample.view.viewholder

import com.aminography.primeadapter.PrimeViewHolder
import com.aminography.primeadapter.callback.PrimeDelegate
import com.aminography.primedatepicker.sample.R
import com.aminography.primedatepicker.sample.view.dataholder.MonthDataHolder
import kotlinx.android.synthetic.main.list_item.view.*

class MonthViewHolder(
        delegate: PrimeDelegate
) : PrimeViewHolder<MonthDataHolder>(delegate, R.layout.list_item) {

    override fun bindDataToView(dataHolder: MonthDataHolder) {
        with(itemView) {
            dataHolder.apply {
                monthView.setDate(year, month)
            }
        }
    }
}