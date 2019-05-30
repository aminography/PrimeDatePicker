package com.aminography.primedatepicker.tools

import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.DateUtils

object TimeUtils {

    fun formatSimpleDate(calendar: BaseCalendar): String =
            "${calendar.dayOfMonth} ${calendar.monthName} ${calendar.year}"

    fun storeCalendar(calendar: BaseCalendar?): String? =
            calendar?.let {
                "${calendar.year}-${calendar.month}-${calendar.dayOfMonth}"
            }

    fun restoreCalendar(input: String?): BaseCalendar? =
            input?.let {
                DateUtils.newCalendar().apply {
                    it.split("-").let {
                        setDate(it[0].toInt(), it[1].toInt(), it[2].toInt())
                    }
                }
            }

    fun extractDate(date: String?, calendarType: CalendarType): BaseCalendar {
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
                calendar.setDate(year, month, day)
                return calendar
            }
        }
        return CalendarFactory.newInstance(CalendarType.CIVIL)
    }

    private fun extractNumbers(input: String?): String {
        return input?.replace("[^-?0-9]+".toRegex(), "") ?: "0"
    }

}