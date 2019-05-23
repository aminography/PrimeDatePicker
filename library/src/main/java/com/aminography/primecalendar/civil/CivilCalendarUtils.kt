package com.aminography.primecalendar.civil

/**
 * @author aminography
 */
internal object CivilCalendarUtils {

    private val normalMonthLength = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    private val leapYearMonthLength = intArrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    fun getMonthLength(year: Int, month: Int): Int {
        return if (isGregorianLeapYear(year)) {
            leapYearMonthLength[month]
        } else {
            normalMonthLength[month]
        }
    }

    fun isGregorianLeapYear(year: Int): Boolean = ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)

}