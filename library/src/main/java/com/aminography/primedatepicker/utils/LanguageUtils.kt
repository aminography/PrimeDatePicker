package com.aminography.primedatepicker.utils

import com.aminography.primecalendar.common.CalendarType
import com.aminography.primecalendar.hijri.HijriCalendar
import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primedatepicker.common.Direction
import java.util.*


/**
 * @author aminography
 */

fun CalendarType.findDirection(locale: Locale): Direction =
    when (locale.language) {
        PersianCalendar.DEFAULT_LOCALE,
        HijriCalendar.DEFAULT_LOCALE -> when (this) {
            CalendarType.CIVIL,
            CalendarType.JAPANESE -> Direction.LTR
            CalendarType.PERSIAN,
            CalendarType.HIJRI -> Direction.RTL
        }
        else -> Direction.LTR
    }

fun Locale.findDirection(calendarType: CalendarType): Direction =
    when (language) {
        PersianCalendar.DEFAULT_LOCALE,
        HijriCalendar.DEFAULT_LOCALE -> when (calendarType) {
            CalendarType.CIVIL,
            CalendarType.JAPANESE -> Direction.LTR
            CalendarType.PERSIAN,
            CalendarType.HIJRI -> Direction.RTL
        }
        else -> Direction.LTR
    }