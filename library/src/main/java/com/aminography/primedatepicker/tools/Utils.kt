package com.aminography.primedatepicker.tools

import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.civil.CivilCalendarUtils
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primecalendar.hijri.HijriCalendar
import com.aminography.primecalendar.hijri.HijriCalendarUtils
import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primecalendar.persian.PersianCalendarUtils

/**
 * Utility helper functions for time and date pickers.
 */
object Utils {

    fun newCalendar(): BaseCalendar {
        return when (CurrentCalendarType.type) {
            CalendarType.CIVIL -> CivilCalendar()
            CalendarType.PERSIAN -> PersianCalendar()
            CalendarType.HIJRI -> HijriCalendar()
        }
    }

    fun getDaysInMonth(month: Int, year: Int): Int {
        return when (CurrentCalendarType.type) {
            CalendarType.CIVIL -> CivilCalendarUtils.monthLength(year, month)
            CalendarType.PERSIAN -> PersianCalendarUtils.monthLength(year, month)
            CalendarType.HIJRI -> HijriCalendarUtils.monthLength(year, month)
        }
    }

//    fun persianDateToCivilStandardString(context: Context, year: Int, monthOfYear: Int, dayOfMonth: Int): String {
//        val civilDate = DateConverter.persianToCivil(context, PersianDate(context, year, monthOfYear, dayOfMonth))
//        return StringBuilder()
//                .append(String.format(Locale.getDefault(), "%04d", civilDate.getYear()))
//                .append("-")
//                .append(String.format(Locale.getDefault(), "%02d", civilDate.getMonth()))
//                .append("-")
//                .append(String.format(Locale.getDefault(), "%02d", civilDate.getDayOfMonth()))
//                .toString()
//    }
//
//    fun persianCalendarToStandardString(calendar: PersianCalendar): String {
//        return StringBuilder()
//                .append(String.format(Locale.getDefault(), "%04d", calendar.get(Calendar.YEAR)))
//                .append("-")
//                .append(String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.MONTH) + 1))
//                .append("-")
//                .append(String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.DAY_OF_MONTH)))
//                .toString()
//    }
//
//
//    fun persianCalendarToCivilStandardString(context: Context, calendar: PersianCalendar): String {
//        val civilDate = DateConverter.persianToCivil(context, PersianDate(context, calendar.year, calendar.month, calendar.dayOfMonth))
//        return StringBuilder()
//                .append(String.format(Locale.getDefault(), "%04d", civilDate.getYear()))
//                .append("-")
//                .append(String.format(Locale.getDefault(), "%02d", civilDate.getMonth()))
//                .append("-")
//                .append(String.format(Locale.getDefault(), "%02d", civilDate.getDayOfMonth()))
//                .toString()
//    }


}
