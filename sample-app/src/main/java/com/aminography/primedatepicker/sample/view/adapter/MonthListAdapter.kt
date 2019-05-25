package com.aminography.primedatepicker.sample.view.adapter

import com.aminography.primeadapter.PrimeAdapter
import com.aminography.primeadapter.PrimeViewHolder
import com.aminography.primedatepicker.sample.view.dataholder.MonthDataHolder
import com.aminography.primedatepicker.sample.view.viewholder.MonthViewHolder

class MonthListAdapter : PrimeAdapter() {

    override fun makeViewHolder(dataHolderClass: Class<*>?): PrimeViewHolder<*>? {
        return when (dataHolderClass) {
            MonthDataHolder::class.java -> MonthViewHolder(this)
            else -> null
        }
    }
}