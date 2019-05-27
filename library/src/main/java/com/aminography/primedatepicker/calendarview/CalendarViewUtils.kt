package com.aminography.primedatepicker.calendarview

import com.aminography.primeadapter.PrimeDataHolder
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder


/**
 * @author aminography
 */
internal object CalendarViewUtils {

    fun moreData(year: Int, month: Int, minDateCalendar: BaseCalendar?, maxDateCalendar: BaseCalendar?, loadFactor: Int, isForward: Boolean): MutableList<PrimeDataHolder> {
        return if (isForward) {
            val offset = year * 12 + month + 1
            val maxOffset = maxDateCalendar?.let { max ->
                max.year * 12 + max.month
            } ?: Int.MAX_VALUE

            val max = if (maxOffset < (offset + loadFactor - 1)) maxOffset else (offset + loadFactor - 1)
            createDataList(offset, max)
        } else {
            val offset = year * 12 + month - 1
            val minOffset = minDateCalendar?.let { min ->
                min.year * 12 + min.month
            } ?: Int.MIN_VALUE

            val min = if (minOffset > (offset - loadFactor + 1)) minOffset else (offset - loadFactor + 1)
            createDataList(min, offset)
        }
    }

    fun centeredData(year: Int, month: Int, minDateCalendar: BaseCalendar?, maxDateCalendar: BaseCalendar?, loadFactor: Int): MutableList<PrimeDataHolder> {
        val centerOffset = year * 12 + month

        val minOffset = minDateCalendar?.let { min ->
            min.year * 12 + min.month
        } ?: Int.MIN_VALUE

        val maxOffset = maxDateCalendar?.let { max ->
            max.year * 12 + max.month
        } ?: Int.MAX_VALUE

        val min = if (minOffset > (centerOffset - loadFactor)) minOffset else (centerOffset - loadFactor)
        val max = if (maxOffset < (centerOffset + loadFactor)) maxOffset else (centerOffset + loadFactor)
        return createDataList(min, max)
    }

    fun transitionData(currentYear: Int, currentMonth: Int, targetYear: Int, targetMonth: Int, transitionFactor: Int): MutableList<PrimeDataHolder>? {
        val current = currentYear * 12 + currentMonth
        val target = targetYear * 12 + targetMonth
        return if (current == target) {
            null
        } else {
            if (current < target) {
                if (target - current - 1 <= transitionFactor) {
                    createDataList(current - 1, target + 1)
                } else {
                    arrayListOf<PrimeDataHolder>().apply {
                        addAll(createDataList(current - 1, Math.ceil(current + transitionFactor / 2.0).toInt()))
                        addAll(createDataList(Math.floor(target - transitionFactor / 2.0).toInt(), target + 1))
                    }
                }
            } else {
                if (current - target - 1 <= transitionFactor) {
                    createDataList(target - 1, current + 1)
                } else {
                    arrayListOf<PrimeDataHolder>().apply {
                        addAll(createDataList(target - 1, Math.ceil(target + transitionFactor / 2.0).toInt()))
                        addAll(createDataList(Math.floor(current - transitionFactor / 2.0).toInt(), current + 1))
                    }
                }
            }
        }
    }

    fun isOutOfRange(year: Int, month: Int, minDateCalendar: BaseCalendar?, maxDateCalendar: BaseCalendar?) =
            isBeforeMin(year, month, minDateCalendar) || isAfterMax(year, month, maxDateCalendar)

    private fun isBeforeMin(year: Int, month: Int, minDateCalendar: BaseCalendar?): Boolean =
            minDateCalendar?.let { min ->
                year < min.year || (year == min.year && month < min.month)
            } ?: false

    private fun isAfterMax(year: Int, month: Int, maxDateCalendar: BaseCalendar?): Boolean =
            maxDateCalendar?.let { max ->
                year > max.year || (year == max.year && month > max.month)
            } ?: false

    fun isForward(currentYear: Int, currentMonth: Int, targetYear: Int, targetMonth: Int) =
            (targetYear * 12 + targetMonth) > (currentYear * 12 + currentMonth)

    private fun createDataList(lower: Int, upper: Int): MutableList<PrimeDataHolder> {
        return arrayListOf<PrimeDataHolder>().apply {
            for (offset in lower..upper) {
                add(MonthDataHolder(offset / 12, offset % 12))
            }
        }
    }

}