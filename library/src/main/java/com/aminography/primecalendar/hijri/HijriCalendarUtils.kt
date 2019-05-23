package com.aminography.primecalendar.hijri

import com.aminography.primecalendar.common.DateHolder
import org.threeten.bp.LocalDate
import org.threeten.bp.chrono.HijrahDate
import org.threeten.bp.temporal.ChronoField

/**
 * @author aminography
 */
internal object HijriCalendarUtils {

    fun gregorianToHijri(gregorian: DateHolder): DateHolder {
        val gregorianDate = LocalDate.of(gregorian.year, gregorian.month + 1, gregorian.day)
        val hijriDate = HijrahDate.from(gregorianDate)
        return DateHolder(hijriDate.get(ChronoField.YEAR), hijriDate.get(ChronoField.MONTH_OF_YEAR) - 1, hijriDate.get(ChronoField.DAY_OF_MONTH))
    }

    fun hijriToGregorian(hijri: DateHolder): DateHolder {
        val hijriDate = HijrahDate.of(hijri.year, hijri.month + 1, hijri.day)
        val gregorianDate = LocalDate.from(hijriDate)
        return DateHolder(gregorianDate.get(ChronoField.YEAR), gregorianDate.get(ChronoField.MONTH_OF_YEAR) - 1, gregorianDate.get(ChronoField.DAY_OF_MONTH))
    }

    private val normalMonthLength = intArrayOf(30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29)
    private val leapYearMonthLength = intArrayOf(30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 30)

    val monthNames = arrayOf(
            "\u0645\u062D\u0631\u0645",
            "\u0635\u0641\u0631",
            "\u0631\u0628\u064A\u0639 \u0627\u0644\u0623\u0648\u0644",
            "\u0631\u0628\u064A\u0639 \u0627\u0644\u062B\u0627\u0646\u064A",
            "\u062C\u0645\u0627\u062F\u0649 \u0627\u0644\u0623\u0648\u0644\u0649",
            "\u062C\u0645\u0627\u062F\u0649 \u0627\u0644\u0622\u062E\u0631\u0629",
            "\u0631\u062C\u0628",
            "\u0634\u0639\u0628\u0627\u0646",
            "\u0631\u0645\u0636\u0627\u0646",
            "\u0634\u0648\u0627\u0644",
            "\u0630\u0648 \u0627\u0644\u0642\u0639\u062F\u0629",
            "\u0630\u0648 \u0627\u0644\u062D\u062C\u0629"
    )

    val hijriWeekDays = arrayOf(
            "\u0627\u0644\u0633\u0628\u062a",
            "\u0627\u0644\u0623\u062d\u062f",
            "\u0627\u0644\u0625\u062b\u0646\u064a\u0646",
            "\u0627\u0644\u062b\u0644\u0627\u062b\u0627\u0621",
            "\u0627\u0644\u0623\u0631\u0628\u0639\u0627\u0621",
            "\u0627\u0644\u062e\u0645\u064a\u0633",
            "\u0627\u0644\u062c\u0645\u0639\u0629"
    )

    fun getMonthLength(year: Int, month: Int): Int {
        return if (isHijriLeapYear(year)) {
            leapYearMonthLength[month]
        } else {
            normalMonthLength[month]
        }
    }

    fun isHijriLeapYear(year: Int): Boolean = (14 + 11 * if (year > 0) year else -year) % 30 < 11

}