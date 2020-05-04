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

fun String.localizeDigits(locale: Locale): String =
    when (locale.language) {
        PersianCalendar.DEFAULT_LOCALE,
        HijriCalendar.DEFAULT_LOCALE -> withPersianDigits
        else -> this
    }

fun Int.localizeDigits(locale: Locale): String =
    when (locale.language) {
        PersianCalendar.DEFAULT_LOCALE,
        HijriCalendar.DEFAULT_LOCALE -> withPersianDigits
        else -> "$this"
    }

val String.withPersianDigits: String
    get() = StringBuilder().also {
        for (c in toCharArray()) {
            if (Character.isDigit(c))
                it.append(PERSIAN_DIGITS["$c".toInt()])
            else it.append(c)
        }
    }.toString()

val Int.withPersianDigits: String
    get() = StringBuilder().also {
        for (c in "$this".toCharArray()) {
            if (Character.isDigit(c))
                it.append(PERSIAN_DIGITS["$c".toInt()])
            else it.append(c)
        }
    }.toString()

private val PERSIAN_DIGITS = charArrayOf(
    '0' + 1728,
    '1' + 1728,
    '2' + 1728,
    '3' + 1728,
    '4' + 1728,
    '5' + 1728,
    '6' + 1728,
    '7' + 1728,
    '8' + 1728,
    '9' + 1728
)
