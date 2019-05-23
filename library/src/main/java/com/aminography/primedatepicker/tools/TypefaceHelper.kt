package com.aminography.primedatepicker.tools


import android.content.Context
import android.graphics.Typeface
import android.support.v4.util.SimpleArrayMap
import com.aminography.primecalendar.common.CalendarType

object TypefaceHelper {

    const val FONT_PATH = "fonts/IRANSans(FaNum).ttf"

    private val cache = SimpleArrayMap<String, Typeface>()

    operator fun get(c: Context, name: String?): Typeface? {
        synchronized(cache) {
            val t = if (!cache.containsKey(name)) {
//                val typeface = Typeface.createFromAsset(c.assets, FONT_PATH)
//                cache.put(name, typeface)
//                typeface
                null
            } else {
                cache.get(name)
            }

            return when (CurrentCalendarType.type) {
                CalendarType.CIVIL -> null
                CalendarType.PERSIAN -> t
                CalendarType.HIJRI -> t
            }
        }
    }

}
