package com.aminography.primedatepicker.calendarview.adapter

import com.aminography.primeadapter.PrimeAdapter
import com.aminography.primeadapter.PrimeViewHolder
import com.aminography.primedatepicker.calendarview.callback.IMonthViewHolderCallback
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder
import com.aminography.primedatepicker.calendarview.viewholder.MonthViewHolder

/**
 * @author aminography
 */
class MonthListAdapter : PrimeAdapter() {

    var iMonthViewHolderCallback: IMonthViewHolderCallback? = null

    override fun makeViewHolder(dataHolderClass: Class<*>?): PrimeViewHolder<*>? {
        return when (dataHolderClass) {
            MonthDataHolder::class.java -> MonthViewHolder(this, iMonthViewHolderCallback)
            else -> null
        }
    }
}