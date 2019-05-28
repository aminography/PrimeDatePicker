package com.aminography.primedatepicker

import com.aminography.primecalendar.base.BaseCalendar


/**
 * @author aminography
 */
@Suppress("MemberVisibilityCanBePrivate")
internal object DateUtils {

    fun isSame(year: Int, month: Int, dayOfMonth: Int, calendar: BaseCalendar) =
            year == calendar.year && month == calendar.month && dayOfMonth == calendar.dayOfMonth

    fun isSame(first: BaseCalendar, second: BaseCalendar) =
            first.year == second.year && first.month == second.month && first.dayOfMonth == second.dayOfMonth

    fun isBetweenExclusive(year: Int, month: Int, dayOfMonth: Int, start: BaseCalendar, end: BaseCalendar): Boolean {
        val offset = year * 12 + month
        val startOffset = start.monthOffset()
        val endOffset = end.monthOffset()

        return if (offset == startOffset && (startOffset == endOffset)) {
            dayOfMonth > start.dayOfMonth && dayOfMonth < end.dayOfMonth
        } else when (offset) {
            startOffset -> dayOfMonth > start.dayOfMonth
            endOffset -> dayOfMonth < end.dayOfMonth
            else -> offset in (startOffset + 1) until endOffset
        }
    }

    fun isBefore(year: Int, month: Int, dayOfMonth: Int, target: BaseCalendar?): Boolean =
            target?.let {
                year < it.year ||
                        (year == it.year && month < it.month) ||
                        (year == it.year && month == it.month && dayOfMonth < it.dayOfMonth)
            } ?: false

    fun isBefore(calendar: BaseCalendar, target: BaseCalendar?): Boolean =
            target?.let {
                calendar.year < it.year ||
                        (calendar.year == it.year && calendar.month < it.month) ||
                        (calendar.year == it.year && calendar.month == it.month && calendar.dayOfMonth < it.dayOfMonth)
            } ?: false

    fun isAfter(year: Int, month: Int, dayOfMonth: Int, target: BaseCalendar?): Boolean =
            target?.let {
                year > it.year ||
                        (year == it.year && month > it.month) ||
                        (year == it.year && month == it.month && dayOfMonth > it.dayOfMonth)
            } ?: false

    fun isAfter(calendar: BaseCalendar, target: BaseCalendar?): Boolean =
            target?.let {
                calendar.year > it.year ||
                        (calendar.year == it.year && calendar.month > it.month) ||
                        (calendar.year == it.year && calendar.month == it.month && calendar.dayOfMonth > it.dayOfMonth)
            } ?: false

    fun isOutOfRange(year: Int, month: Int, dayOfMonth: Int, minDateCalendar: BaseCalendar?, maxDateCalendar: BaseCalendar?): Boolean =
            isBefore(year, month, dayOfMonth, minDateCalendar) || isAfter(year, month, dayOfMonth, maxDateCalendar)

    //----------------------------------------------------------------------------------------------

    fun isBefore(year: Int, month: Int, target: BaseCalendar?): Boolean =
            target?.let {
                year < it.year || (year == it.year && month < it.month)
            } ?: false

    fun isBefore(year: Int, month: Int, targetYear: Int, targetMonth: Int) =
            (year * 12 + month) < (targetYear * 12 + targetMonth)

    fun isAfter(year: Int, month: Int, target: BaseCalendar?): Boolean =
            target?.let {
                year > it.year || (year == it.year && month > it.month)
            } ?: false

    fun isAfter(year: Int, month: Int, targetYear: Int, targetMonth: Int) =
            (year * 12 + month) > (targetYear * 12 + targetMonth)

    fun isOutOfRange(year: Int, month: Int, minDateCalendar: BaseCalendar?, maxDateCalendar: BaseCalendar?) =
            isBefore(year, month, minDateCalendar) || isAfter(year, month, maxDateCalendar)

}