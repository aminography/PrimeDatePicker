package com.aminography.primedatepicker.sample.view.dataholder

import com.aminography.primeadapter.PrimeDataHolder
import com.aminography.primeadapter.annotation.DataHolder

@DataHolder
data class MonthDataHolder(
        val year: Int,
        val month: Int
) : PrimeDataHolder()