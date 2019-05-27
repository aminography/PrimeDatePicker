package com.aminography.primedatepicker.calendarview

import com.aminography.primeadapter.PrimeDataHolder
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder


/**
 * @author aminography
 */
internal object CalendarViewUtils {

    fun centeredData(year: Int, month: Int, loadFactor: Int): ArrayList<PrimeDataHolder> {
        val center = year * 12 + month
        return arrayListOf<PrimeDataHolder>().apply {
            for (i in (center - loadFactor)..(center + loadFactor)) {
                add(createDataHolder(i))
            }
        }
    }

    fun transitionData(currentYear: Int, currentMonth: Int, targetYear: Int, targetMonth: Int, transitionFactor: Int): List<PrimeDataHolder>? {
        val current = currentYear * 12 + currentMonth
        val target = targetYear * 12 + targetMonth
        return if (current == target) {
            null
        } else {
            arrayListOf<PrimeDataHolder>().apply {
                if (current < target) {
                    if (target - current - 1 <= transitionFactor) {
                        for (i in (current - 1)..(target + 1)) {
                            add(createDataHolder(i))
                        }
                    } else {
                        for (i in (current - 1)..Math.ceil(current + transitionFactor / 2.0).toInt()) {
                            add(createDataHolder(i))
                        }
                        for (i in Math.floor(target - transitionFactor / 2.0).toInt()..(target + 1)) {
                            add(createDataHolder(i))
                        }
                    }
                } else {
                    if (current - target - 1 <= transitionFactor) {
                        for (i in (target - 1)..(current + 1)) {
                            add(createDataHolder(i))
                        }
                    } else {
                        for (i in (target - 1)..Math.ceil(target + transitionFactor / 2.0).toInt()) {
                            add(createDataHolder(i))
                        }
                        for (i in Math.floor(current - transitionFactor / 2.0).toInt()..(current + 1)) {
                            add(createDataHolder(i))
                        }
                    }
                }
            }
        }
    }

    fun isForward(currentYear: Int, currentMonth: Int, targetYear: Int, targetMonth: Int) =
            (targetYear * 12 + targetMonth) > (currentYear * 12 + currentMonth)

    private fun createDataHolder(offset: Int): MonthDataHolder =
            MonthDataHolder(offset / 12, offset % 12)
}