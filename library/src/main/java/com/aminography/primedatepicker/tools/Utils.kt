package com.aminography.primedatepicker.tools

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import com.aminography.primecalendar.civil.CivilCalendarUtils
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primecalendar.hijri.HijriCalendarUtils
import com.aminography.primecalendar.persian.PersianCalendarUtils

/**
 * Utility helper functions for time and date pickers.
 */
object Utils {

    private val isJellybeanOrLater: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN

    /**
     * Try to speak the specified text, for accessibility. Only available on JB or later.
     *
     * @param text Text to announce.
     */
    @SuppressLint("NewApi")
    fun tryAccessibilityAnnounce(view: View?, text: CharSequence?) {
        if (isJellybeanOrLater && view != null && text != null) {
            view.announceForAccessibility(text)
        }
    }

    fun getDaysInMonth(month: Int, year: Int): Int {
        return when (CurrentCalendarType.type) {
            CalendarType.CIVIL -> CivilCalendarUtils.getMonthLength(year, month)
            CalendarType.PERSIAN -> PersianCalendarUtils.getMonthLength(year, month)
            CalendarType.HIJRI -> HijriCalendarUtils.getMonthLength(year, month)
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
