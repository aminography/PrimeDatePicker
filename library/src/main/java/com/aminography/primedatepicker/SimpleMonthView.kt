package com.aminography.primedatepicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.util.AttributeSet
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.tools.CurrentCalendarType
import com.aminography.primedatepicker.tools.PersianUtils
import com.aminography.primedatepicker.tools.TypefaceHelper
import java.util.*

class SimpleMonthView @JvmOverloads constructor(
        context: Context,
        attr: AttributeSet? = null,
        controller: DatePickerController? = null,
        @ColorInt mainColor: Int? = null
) : BaseMonthView(context, attr, controller, mainColor) {

    private var typefaceBold = Typeface.create(TypefaceHelper[context, controller?.typeface], Typeface.BOLD)
    private var typefaceNormal = Typeface.create(TypefaceHelper[context, controller?.typeface], Typeface.NORMAL)

    override fun drawMonthDay(
            canvas: Canvas,
            year: Int,
            month: Int,
            day: Int,
            x: Int,
            y: Int,
            startX: Int,
            stopX: Int,
            startY: Int,
            stopY: Int
    ) {
        if (mSelectedDay == day) {
            canvas.drawCircle(
                    x.toFloat(),
                    (y - context.resources.getDimensionPixelSize(R.dimen.mdtp_day_number_size) / 3).toFloat(),
                    context.resources.getDimensionPixelSize(R.dimen.mdtp_day_number_select_circle_radius).toFloat(),
                    selectedCirclePaint!!
            )
        }

        if (isHighlighted(year, month, day)) {
            monthNumPaint!!.typeface = typefaceBold
        } else {
            monthNumPaint!!.typeface = typefaceNormal
        }

        if (isOutOfRange(year, month, day)) {
            monthNumPaint!!.color = mDisabledDayTextColor
        } else if (mSelectedDay == day) {
            monthNumPaint!!.color = mSelectedDayTextColor
        } else if (mHasToday && mToday == day) {
            monthNumPaint!!.color = mTodayNumberColor
        } else {
            monthNumPaint!!.color = if (isHighlighted(year, month, day)) mHighlightedDayTextColor else mDayTextColor
        }

        monthNumPaint!!.typeface = typefaceNormal
        monthNumPaint!!.textSize = context.resources.getDimensionPixelSize(R.dimen.mdtp_day_number_size).toFloat()

        val date = when (CurrentCalendarType.type) {
            CalendarType.CIVIL -> String.format(Locale.getDefault(), "%d", day)
            CalendarType.PERSIAN -> PersianUtils.convertLatinDigitsToPersian(String.format(Locale.getDefault(), "%d", day))
            CalendarType.HIJRI -> PersianUtils.convertLatinDigitsToPersian(String.format(Locale.getDefault(), "%d", day))
        }
        canvas.drawText(date, x.toFloat(), y.toFloat(), monthNumPaint!!)
    }

}
