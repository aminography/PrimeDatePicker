package com.aminography.primedatepicker.calendarview.adapter

import com.aminography.primeadapter.PrimeAdapter
import com.aminography.primeadapter.PrimeViewHolder
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder
import com.aminography.primedatepicker.calendarview.viewholder.MonthViewHolder

class MonthListAdapter : PrimeAdapter() {

    override fun makeViewHolder(dataHolderClass: Class<*>?): PrimeViewHolder<*>? {
        return when (dataHolderClass) {
            MonthDataHolder::class.java -> MonthViewHolder(this)
            else -> null
        }
    }
}