package com.aminography.primecalendar.persian

import com.aminography.primecalendar.common.DateHolder

/**
 * @author aminography
 */
internal object PersianCalendarUtils {

    @Suppress("ReplaceWithOperatorAssignment", "JoinDeclarationAndAssignment")
    fun gregorianToPersian(gregorian: DateHolder): DateHolder {

        if (gregorian.month > 11 || gregorian.month < -11) {
            throw IllegalArgumentException()
        }

        var persianYear: Int
        val persianMonth: Int
        val persianDay: Int

        var gregorianDayNo: Int
        var persianDayNo: Int
        val persianNP: Int
        var i: Int

        gregorian.year = gregorian.year - 1600
        gregorian.day = gregorian.day - 1

        gregorianDayNo = 365 * gregorian.year + Math.floor(((gregorian.year + 3) / 4).toDouble()).toInt() - Math.floor(((gregorian.year + 99) / 100).toDouble()).toInt() + Math.floor(((gregorian.year + 399) / 400).toDouble()).toInt()
        i = 0
        while (i < gregorian.month) {
            gregorianDayNo += gregorianMonthLength[i]
            ++i
        }

        if (gregorian.month > 1 && (gregorian.year % 4 == 0 && gregorian.year % 100 != 0 || gregorian.year % 400 == 0)) {
            ++gregorianDayNo
        }

        gregorianDayNo += gregorian.day

        persianDayNo = gregorianDayNo - 79

        persianNP = Math.floor((persianDayNo / 12053).toDouble()).toInt()
        persianDayNo = persianDayNo % 12053

        persianYear = 979 + 33 * persianNP + 4 * (persianDayNo / 1461)
        persianDayNo = persianDayNo % 1461

        if (persianDayNo >= 366) {
            persianYear += Math.floor(((persianDayNo - 1) / 365).toDouble()).toInt()
            persianDayNo = (persianDayNo - 1) % 365
        }

        i = 0
        while (i < 11 && persianDayNo >= normalMonthLength[i]) {
            persianDayNo -= normalMonthLength[i]
            ++i
        }
        persianMonth = i
        persianDay = persianDayNo + 1

        return DateHolder(persianYear, persianMonth, persianDay)
    }

    @Suppress("ReplaceWithOperatorAssignment", "JoinDeclarationAndAssignment")
    fun persianToGregorian(persian: DateHolder): DateHolder {

        if (persian.month > 11 || persian.month < -11) {
            throw IllegalArgumentException()
        }

        var gregorianYear: Int
        val gregorianMonth: Int
        val gregorianDay: Int

        var gregorianDayNo: Int
        var persianDayNo: Int
        var leap: Int

        var i: Int
        persian.year = persian.year - 979
        persian.day = persian.day - 1

        persianDayNo = 365 * persian.year + (persian.year / 33) * 8 + Math.floor(((persian.year % 33 + 3) / 4).toDouble()).toInt()
        i = 0
        while (i < persian.month) {
            persianDayNo += normalMonthLength[i]
            ++i
        }

        persianDayNo += persian.day

        gregorianDayNo = persianDayNo + 79

        gregorianYear = 1600 + 400 * Math.floor((gregorianDayNo / 146097).toDouble()).toInt() /* 146097 = 365*400 + 400/4 - 400/100 + 400/400 */
        gregorianDayNo = gregorianDayNo % 146097

        leap = 1
        if (gregorianDayNo >= 36525)
        /* 36525 = 365*100 + 100/4 */ {
            gregorianDayNo--
            gregorianYear += 100 * Math.floor((gregorianDayNo / 36524).toDouble()).toInt() /* 36524 = 365*100 + 100/4 - 100/100 */
            gregorianDayNo = gregorianDayNo % 36524

            if (gregorianDayNo >= 365) {
                gregorianDayNo++
            } else {
                leap = 0
            }
        }

        gregorianYear += 4 * Math.floor((gregorianDayNo / 1461).toDouble()).toInt() /* 1461 = 365*4 + 4/4 */
        gregorianDayNo = gregorianDayNo % 1461

        if (gregorianDayNo >= 366) {
            leap = 0

            gregorianDayNo--
            gregorianYear += Math.floor((gregorianDayNo / 365).toDouble()).toInt()
            gregorianDayNo = gregorianDayNo % 365
        }

        i = 0
        while (gregorianDayNo >= gregorianMonthLength[i] + if (i == 1 && leap == 1) i else 0) {
            gregorianDayNo -= gregorianMonthLength[i] + if (i == 1 && leap == 1) i else 0
            i++
        }
        gregorianMonth = i
        gregorianDay = gregorianDayNo + 1

        return DateHolder(gregorianYear, gregorianMonth, gregorianDay)
    }

    private fun ceil(double1: Double, double2: Double): Long {
        return (double1 - double2 * Math.floor(double1 / double2)).toLong()
    }

    private val gregorianMonthLength = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    private val normalMonthLength = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)
    private val leapYearMonthLength = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 30)

    val persianMonthNames = arrayOf(
            "\u0641\u0631\u0648\u0631\u062f\u06cc\u0646", // Farvardin
            "\u0627\u0631\u062f\u06cc\u0628\u0647\u0634\u062a", // Ordibehesht
            "\u062e\u0631\u062f\u0627\u062f", // Khordad
            "\u062a\u06cc\u0631", // Tir
            "\u0645\u0631\u062f\u0627\u062f", // Mordad
            "\u0634\u0647\u0631\u06cc\u0648\u0631", // Shahrivar
            "\u0645\u0647\u0631", // Mehr
            "\u0622\u0628\u0627\u0646", // Aban
            "\u0622\u0630\u0631", // Azar
            "\u062f\u06cc", // Dey
            "\u0628\u0647\u0645\u0646", // Bahman
            "\u0627\u0633\u0641\u0646\u062f" // Esfand
    )

    val persianWeekDays = arrayOf(
            "\u0634\u0646\u0628\u0647", // Shanbeh
            "\u06cc\u06a9\u200c\u0634\u0646\u0628\u0647", // Yekshanbeh
            "\u062f\u0648\u0634\u0646\u0628\u0647", // Doshanbeh
            "\u0633\u0647\u200c\u0634\u0646\u0628\u0647", // Sehshanbeh
            "\u0686\u0647\u0627\u0631\u0634\u0646\u0628\u0647", // Chaharshanbeh
            "\u067e\u0646\u062c\u200c\u0634\u0646\u0628\u0647", // Panjshanbeh
            "\u062c\u0645\u0639\u0647" // jom'e
    )

    fun getMonthLength(year: Int, month: Int): Int {
        return if (isPersianLeapYear(year)) {
            leapYearMonthLength[month]
        } else {
            normalMonthLength[month]
        }
    }

    fun isPersianLeapYear(persianYear: Int): Boolean = ceil((38.0 + (ceil((persianYear - 474L).toDouble(), 2820.0) + 474L)) * 682.0, 2816.0) < 682L

}