package com.aminography.primedatepicker.tools

import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.civil.CivilCalendarUtils
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primecalendar.hijri.HijriCalendarUtils
import com.aminography.primecalendar.japanese.JapaneseCalendarUtils
import com.aminography.primecalendar.persian.PersianCalendarUtils
import java.util.*


/**
 * @author aminography
 */
@Suppress("MemberVisibilityCanBePrivate")
object DateUtils {

    fun isSame(year: Int, month: Int, dayOfMonth: Int, calendar: PrimeCalendar) =
            year == calendar.year && month == calendar.month && dayOfMonth == calendar.dayOfMonth

    fun isSame(first: PrimeCalendar, second: PrimeCalendar) =
            first.year == second.year && first.month == second.month && first.dayOfMonth == second.dayOfMonth

    fun isBetweenExclusive(year: Int, month: Int, dayOfMonth: Int, start: PrimeCalendar, end: PrimeCalendar): Boolean {
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

    fun isBefore(year: Int, month: Int, dayOfMonth: Int, target: PrimeCalendar?): Boolean =
            target?.let {
                year < it.year ||
                        (year == it.year && month < it.month) ||
                        (year == it.year && month == it.month && dayOfMonth < it.dayOfMonth)
            } ?: false

    fun isBefore(calendar: PrimeCalendar, target: PrimeCalendar?): Boolean =
            target?.let {
                calendar.year < it.year ||
                        (calendar.year == it.year && calendar.month < it.month) ||
                        (calendar.year == it.year && calendar.month == it.month && calendar.dayOfMonth < it.dayOfMonth)
            } ?: false

    fun isAfter(year: Int, month: Int, dayOfMonth: Int, target: PrimeCalendar?): Boolean =
            target?.let {
                year > it.year ||
                        (year == it.year && month > it.month) ||
                        (year == it.year && month == it.month && dayOfMonth > it.dayOfMonth)
            } ?: false

    fun isAfter(calendar: PrimeCalendar, target: PrimeCalendar?): Boolean =
            target?.let {
                calendar.year > it.year ||
                        (calendar.year == it.year && calendar.month > it.month) ||
                        (calendar.year == it.year && calendar.month == it.month && calendar.dayOfMonth > it.dayOfMonth)
            } ?: false

    fun isOutOfRange(year: Int, month: Int, dayOfMonth: Int, minDateCalendar: PrimeCalendar?, maxDateCalendar: PrimeCalendar?): Boolean =
            isBefore(year, month, dayOfMonth, minDateCalendar) || isAfter(year, month, dayOfMonth, maxDateCalendar)

    //----------------------------------------------------------------------------------------------

    fun isBefore(year: Int, month: Int, target: PrimeCalendar?): Boolean =
            target?.let {
                year < it.year || (year == it.year && month < it.month)
            } ?: false

    fun isBefore(year: Int, month: Int, targetYear: Int, targetMonth: Int) =
            (year * 12 + month) < (targetYear * 12 + targetMonth)

    fun isAfter(year: Int, month: Int, target: PrimeCalendar?): Boolean =
            target?.let {
                year > it.year || (year == it.year && month > it.month)
            } ?: false

    fun isAfter(year: Int, month: Int, targetYear: Int, targetMonth: Int) =
            (year * 12 + month) > (targetYear * 12 + targetMonth)

    fun isOutOfRange(year: Int, month: Int, minDateCalendar: PrimeCalendar?, maxDateCalendar: PrimeCalendar?) =
            isBefore(year, month, minDateCalendar) || isAfter(year, month, maxDateCalendar)

    //----------------------------------------------------------------------------------------------

    fun getDaysInMonth(calendarType: CalendarType, year: Int, month: Int): Int {
        return when (calendarType) {
            CalendarType.CIVIL -> CivilCalendarUtils.monthLength(year, month)
            CalendarType.PERSIAN -> PersianCalendarUtils.monthLength(year, month)
            CalendarType.HIJRI -> HijriCalendarUtils.monthLength(year, month)
            CalendarType.JAPANESE -> JapaneseCalendarUtils.monthLength(year, month)
        }
    }

    fun storeCalendar(calendar: PrimeCalendar?): String? =
            calendar?.run {
                "${calendarType.name}-${locale.language}-${year}-${month}-${dayOfMonth}"
            }

    fun restoreCalendar(input: String?): PrimeCalendar? =
            input?.run {
                split("-").let {
                    CalendarFactory.newInstance(CalendarType.valueOf(it[0]), Locale(it[1])).apply {
                        set(it[2].toInt(), it[3].toInt(), it[4].toInt())
                    }
                }
            }

    fun dateString(calendar: PrimeCalendar?): String? =
            calendar?.run {
                "${year}-${month}-${dayOfMonth}"
            }

    fun dateString(year: Int, month: Int, dayOfMonth: Int) = "${year}-${month}-${dayOfMonth}"

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