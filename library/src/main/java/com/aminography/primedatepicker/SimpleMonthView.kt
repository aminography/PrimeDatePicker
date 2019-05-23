package com.aminography.primedatepicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.util.AttributeSet
import com.aminography.primecalendar.common.CalendarType
import java.util.*

class SimpleMonthView : MonthView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attr: AttributeSet?, controller: DatePickerController, @ColorInt mainColor: Int? = null) : super(context, attr, controller, mainColor) {
        this.controller = controller
        typefaceBold = Typeface.create(TypefaceHelper[context, controller.typeface], Typeface.BOLD)
        typefaceNormal = Typeface.create(TypefaceHelper[context, controller.typeface], Typeface.NORMAL)
    }

    private var typefaceBold: Typeface? = null
    private var typefaceNormal: Typeface? = null
    private var controller: DatePickerController? = null

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
                    mSelectedCirclePaint!!
            )
        }

        if (isHighlighted(year, month, day)) {
            mMonthNumPaint!!.typeface = typefaceBold
        } else {
            mMonthNumPaint!!.typeface = typefaceNormal
        }

        if (isOutOfRange(year, month, day)) {
            mMonthNumPaint!!.color = mDisabledDayTextColor
        } else if (mSelectedDay == day) {
            mMonthNumPaint!!.color = mSelectedDayTextColor
        } else if (mHasToday && mToday == day) {
            mMonthNumPaint!!.color = mTodayNumberColor
        } else {
            mMonthNumPaint!!.color = if (isHighlighted(year, month, day)) mHighlightedDayTextColor else mDayTextColor
        }

        mMonthNumPaint!!.typeface = typefaceNormal
        mMonthNumPaint!!.textSize = context.resources.getDimensionPixelSize(R.dimen.mdtp_day_number_size).toFloat()

        val date = when (CurrentCalendarType.type) {
            CalendarType.CIVIL -> String.format(Locale.getDefault(), "%d", day)
            CalendarType.PERSIAN -> PersianUtils.convertLatinDigitsToPersian(String.format(Locale.getDefault(), "%d", day))
            CalendarType.HIJRI -> PersianUtils.convertLatinDigitsToPersian(String.format(Locale.getDefault(), "%d", day))
        }
        canvas.drawText(date, x.toFloat(), y.toFloat(), mMonthNumPaint!!)
    }

}
