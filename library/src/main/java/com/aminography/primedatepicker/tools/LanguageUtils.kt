package com.aminography.primedatepicker.tools

import com.aminography.primecalendar.common.CalendarType
import com.aminography.primecalendar.hijri.HijriCalendar
import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primedatepicker.Direction


/**
 * @author aminography
 */
object LanguageUtils {

    internal fun direction(calendarType: CalendarType, language: String): Direction =
        when (language) {
            PersianCalendar.DEFAULT_LOCALE, HijriCalendar.DEFAULT_LOCALE -> when (calendarType) {
                CalendarType.CIVIL, CalendarType.JAPANESE -> Direction.LTR
                CalendarType.PERSIAN, CalendarType.HIJRI -> Direction.RTL
            }
            else -> Direction.LTR
        }

}