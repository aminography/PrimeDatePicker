package com.aminography.primecalendar.common

import com.aminography.primecalendar.hijri.HijriCalendar
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.persian.PersianCalendar

/**
 * @author aminography
 */

internal fun convertCivilToPersian(civilCalendar: CivilCalendar): PersianCalendar {
    val persianCalendar = CalendarFactory.newInstance(PersianCalendar::class.java)
    persianCalendar.timeInMillis = civilCalendar.timeInMillis
    return persianCalendar
}

internal fun convertCivilToHijri(civilCalendar: CivilCalendar): HijriCalendar {
    val hijriCalendar = CalendarFactory.newInstance(HijriCalendar::class.java)
    hijriCalendar.timeInMillis = civilCalendar.timeInMillis
    return hijriCalendar
}

internal fun convertPersianToCivil(persianCalendar: PersianCalendar): CivilCalendar {
    val civilCalendar = CalendarFactory.newInstance(CivilCalendar::class.java)
    civilCalendar.timeInMillis = persianCalendar.timeInMillis
    return civilCalendar
}

internal fun convertPersianToHijri(persianCalendar: PersianCalendar): HijriCalendar {
    val hijriCalendar = CalendarFactory.newInstance(HijriCalendar::class.java)
    hijriCalendar.timeInMillis = persianCalendar.timeInMillis
    return hijriCalendar
}

internal fun convertHijriToCivil(hijriCalendar: HijriCalendar): CivilCalendar {
    val civilCalendar = CalendarFactory.newInstance(CivilCalendar::class.java)
    civilCalendar.timeInMillis = hijriCalendar.timeInMillis
    return civilCalendar
}

internal fun convertHijriToPersian(hijriCalendar: HijriCalendar): PersianCalendar {
    val persianCalendar = CalendarFactory.newInstance(PersianCalendar::class.java)
    persianCalendar.timeInMillis = hijriCalendar.timeInMillis
    return persianCalendar
}
