package com.aminography.primedatepicker.tools

import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType

/**
 * @author aminography
 */
object TimeUtils {

    fun formatSimpleDate(calendar: PrimeCalendar): String =
            "${calendar.dayOfMonth} ${calendar.monthName} ${calendar.year}"

    fun extractDate(date: String?, calendarType: CalendarType): PrimeCalendar {
        if (date != null && !date.isEmpty()) {
            val split = date.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (split.size == 3) {
                val year: Int
                val month: Int
                val day: Int
                try {
                    year = Integer.parseInt(extractNumbers(PersianUtils.convertDigitsToLatin(split[0].trim())))
                    month = Integer.parseInt(extractNumbers(PersianUtils.convertDigitsToLatin(split[1].trim())))
                    day = Integer.parseInt(extractNumbers(PersianUtils.convertDigitsToLatin(split[2].trim())))
                } catch (e: NumberFormatException) {
                    return CalendarFactory.newInstance(CalendarType.CIVIL)
                }

                val calendar = CalendarFactory.newInstance(calendarType)
                calendar.set(year, month, day)
                return calendar
            }
        }
        return CalendarFactory.newInstance(CalendarType.CIVIL)
    }

    private fun extractNumbers(input: String?): String {
        return input?.replace("[^-?0-9]+".toRegex(), "") ?: "0"
    }

}