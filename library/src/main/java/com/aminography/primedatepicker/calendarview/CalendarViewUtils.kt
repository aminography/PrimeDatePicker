package com.aminography.primedatepicker.calendarview

import com.aminography.primeadapter.PrimeDataHolder
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder
import com.aminography.primedatepicker.tools.monthOffset


/**
 * @author aminography
 */
@Suppress("MemberVisibilityCanBePrivate")
internal object CalendarViewUtils {

    fun extendMoreList(calendarType: CalendarType, year: Int, month: Int, minDateCalendar: PrimeCalendar?, maxDateCalendar: PrimeCalendar?, loadFactor: Int, isForward: Boolean): MutableList<PrimeDataHolder> {
        return if (isForward) {
            val offset = (year * 12 + month) + 1
            val maxOffset = maxDateCalendar?.monthOffset() ?: Int.MAX_VALUE

            val max = if (maxOffset < (offset + loadFactor - 1)) maxOffset else (offset + loadFactor - 1)
            createList(calendarType, offset, max)
        } else {
            val offset = (year * 12 + month) - 1
            val minOffset = minDateCalendar?.monthOffset() ?: Int.MIN_VALUE

            val min = if (minOffset > (offset - loadFactor + 1)) minOffset else (offset - loadFactor + 1)
            createList(calendarType, min, offset)
        }
    }

    fun createPivotList(calendarType: CalendarType, year: Int, month: Int, minDateCalendar: PrimeCalendar?, maxDateCalendar: PrimeCalendar?, loadFactor: Int): MutableList<PrimeDataHolder> {
        val centerOffset = year * 12 + month

        val minOffset = minDateCalendar?.monthOffset() ?: Int.MIN_VALUE
        val maxOffset = maxDateCalendar?.monthOffset() ?: Int.MAX_VALUE

        val min = if (minOffset > (centerOffset - loadFactor)) minOffset else (centerOffset - loadFactor)
        val max = if (maxOffset < (centerOffset + loadFactor)) maxOffset else (centerOffset + loadFactor)
        return createList(calendarType, min, max)
    }

    fun createTransitionList(calendarType: CalendarType, currentYear: Int, currentMonth: Int, targetYear: Int, targetMonth: Int, maxTransitionLength: Int): MutableList<PrimeDataHolder>? {
        val current = currentYear * 12 + currentMonth
        val target = targetYear * 12 + targetMonth
        return if (current == target) {
            null
        } else {
            if (current < target) {
                if (target - current - 1 <= maxTransitionLength) {
                    createList(calendarType, current - 1, target + 1)
                } else {
                    arrayListOf<PrimeDataHolder>().apply {
                        addAll(createList(calendarType, current - 1, Math.ceil(current + maxTransitionLength / 2.0).toInt()))
                        addAll(createList(calendarType, Math.floor(target - maxTransitionLength / 2.0).toInt(), target + 1))
                    }
                }
            } else {
                if (current - target - 1 <= maxTransitionLength) {
                    createList(calendarType, target - 1, current + 1)
                } else {
                    arrayListOf<PrimeDataHolder>().apply {
                        addAll(createList(calendarType, target - 1, Math.ceil(target + maxTransitionLength / 2.0).toInt()))
                        addAll(createList(calendarType, Math.floor(current - maxTransitionLength / 2.0).toInt(), current + 1))
                    }
                }
            }
        }
    }

    private fun createList(calendarType: CalendarType, lowerOffset: Int, upperOffset: Int): MutableList<PrimeDataHolder> {
        return arrayListOf<PrimeDataHolder>().apply {
            for (offset in lowerOffset..upperOffset) {
                add(MonthDataHolder(calendarType, offset / 12, offset % 12))
            }
        }
    }

}