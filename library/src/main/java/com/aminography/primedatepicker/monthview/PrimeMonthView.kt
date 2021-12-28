package com.aminography.primedatepicker.monthview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseIntArray
import android.view.MotionEvent
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.localizeDigits
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.common.LabelFormatter
import com.aminography.primedatepicker.monthview.painters.MonthLabelPainter
import com.aminography.primedatepicker.monthview.painters.WeekDayLabelsPainter
import java.util.*

/**
 * @author aminography
 */
@Suppress("ConstantConditionIf", "MemberVisibilityCanBePrivate", "unused")
class PrimeMonthView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : SimpleMonthView(context, attrs, defStyleAttr, defStyleRes) {

    // Interior Variables --------------------------------------------------------------------------

    private var monthHeaderHeight = 0
    private var weekHeaderHeight = 0

    private lateinit var monthLabel: String
    private lateinit var weekLabels: Array<String>
    private lateinit var internalWeekLabelTextColors: IntArray

    private val monthLabelPainter: MonthLabelPainter by lazy { MonthLabelPainter() }
    private val weekDayLabelsPainter: WeekDayLabelsPainter by lazy { WeekDayLabelsPainter() }

    // Control Variables ---------------------------------------------------------------------------

    var monthLabelTextColor: Int = 0
        set(value) {
            field = value
            monthLabelPainter.monthLabelTextColor = value
            if (invalidate) invalidate()
        }

    var weekLabelTextColor: Int = 0
        set(value) {
            field = value
            weekDayLabelsPainter.weekLabelTextColor = value
            if (invalidate) invalidate()
        }

    var monthLabelTextSize: Int = 0
        set(value) {
            field = value
            monthLabelPainter.monthLabelTextSize = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var weekLabelTextSize: Int = 0
        set(value) {
            field = value
            weekDayLabelsPainter.weekLabelTextSize = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var monthLabelTopPadding: Int = 0
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var monthLabelBottomPadding: Int = 0
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var weekLabelTopPadding: Int = 0
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var weekLabelBottomPadding: Int = 0
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    // Programmatically Control Variables ----------------------------------------------------------

    var weekLabelTextColors: SparseIntArray? = null
        set(value) {
            field = value
            if (invalidate) invalidate()
        }

    var monthLabelFormatter: LabelFormatter = DEFAULT_MONTH_LABEL_FORMATTER
        set(value) {
            field = value
            if (invalidate) goto(year, month)
        }

    var weekLabelFormatter: LabelFormatter = DEFAULT_WEEK_LABEL_FORMATTER
        set(value) {
            field = value
            if (invalidate) goto(year, month)
        }

    // ---------------------------------------------------------------------------------------------

    init {
        context.obtainStyledAttributes(attrs, R.styleable.PrimeMonthView, defStyleAttr, defStyleRes)
            .run {
                doNotInvalidate {
                    monthLabelTextColor = getColor(
                        R.styleable.PrimeMonthView_monthLabelTextColor,
                        ContextCompat.getColor(context, R.color.blueGray200)
                    )
                    weekLabelTextColor = getColor(
                        R.styleable.PrimeMonthView_weekLabelTextColor,
                        ContextCompat.getColor(context, R.color.red300)
                    )

                    monthLabelTextSize = getDimensionPixelSize(
                        R.styleable.PrimeMonthView_monthLabelTextSize,
                        resources.getDimensionPixelSize(R.dimen.defaultMonthLabelTextSize)
                    )
                    weekLabelTextSize = getDimensionPixelSize(
                        R.styleable.PrimeMonthView_weekLabelTextSize,
                        resources.getDimensionPixelSize(R.dimen.defaultWeekLabelTextSize)
                    )

                    monthLabelTopPadding = getDimensionPixelSize(
                        R.styleable.PrimeMonthView_monthLabelTopPadding,
                        resources.getDimensionPixelSize(R.dimen.defaultMonthLabelTopPadding)
                    )
                    monthLabelBottomPadding = getDimensionPixelSize(
                        R.styleable.PrimeMonthView_monthLabelBottomPadding,
                        resources.getDimensionPixelSize(R.dimen.defaultMonthLabelBottomPadding)
                    )
                    weekLabelTopPadding = getDimensionPixelSize(
                        R.styleable.PrimeMonthView_weekLabelTopPadding,
                        resources.getDimensionPixelSize(R.dimen.defaultWeekLabelTopPadding)
                    )
                    weekLabelBottomPadding = getDimensionPixelSize(
                        R.styleable.PrimeMonthView_weekLabelBottomPadding,
                        resources.getDimensionPixelSize(R.dimen.defaultWeekLabelBottomPadding)
                    )
                }
                recycle()
            }

        monthLabelPainter.also {
            it.monthLabelTextSize = monthLabelTextSize
            it.monthLabelTextColor = monthLabelTextColor
            it.typeface = typeface
        }

        weekDayLabelsPainter.also {
            it.weekLabelTextSize = weekLabelTextSize
            it.weekLabelTextColor = weekLabelTextColor
            it.typeface = typeface
            it.findWeekDayLabelTextColor = { dayOfWeek -> internalWeekLabelTextColors[dayOfWeek] }
            it.weekDayLabelFormatter = { dayOfWeek -> weekLabels[dayOfWeek] }
        }
    }

    override fun calculateSizes() {
        super.calculateSizes()
        monthHeaderHeight = monthLabelTextSize + monthLabelTopPadding + monthLabelBottomPadding
        weekHeaderHeight = weekLabelTextSize + weekLabelTopPadding + weekLabelBottomPadding
    }

    override fun applyTypeface() {
        super.applyTypeface()
        monthLabelPainter.typeface = typeface
        weekDayLabelsPainter.typeface = typeface
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        monthLabelPainter.draw(
            canvas,
            absoluteViewWidth.toFloat(),
            monthHeaderHeight.toFloat(),
            viewWidth / 2f,
            paddingTop + monthHeaderHeight / 2f,
            monthLabel,
            developerOptionsShowGuideLines
        )

        weekDayLabelsPainter.draw(
            canvas,
            cellWidth,
            weekHeaderHeight.toFloat(),
            columnXPositions,
            paddingTop + monthHeaderHeight + weekHeaderHeight / 2f,
            columnCount,
            firstDayOfWeek,
            developerOptionsShowGuideLines
        )
    }

    override fun setupGotoExtras() {
        super.setupGotoExtras()

        firstDayOfMonthCalendar
            ?.let { monthLabelFormatter(it) }
            ?.let { monthLabel = it.localizeDigits(locale) }

        CalendarFactory.newInstance(calendarType, locale).let {
            weekLabels = Array(7) { dayOfWeek ->
                it[Calendar.DAY_OF_WEEK] = dayOfWeek
                weekLabelFormatter(it).localizeDigits(locale)
            }
        }

        internalWeekLabelTextColors = IntArray(7) { dayOfWeek ->
            val color = weekLabelTextColors?.get(if (dayOfWeek > 0) dayOfWeek else 7, -1)
            if (color != null && color != -1) color else weekLabelTextColor
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (!super.onTouchEvent(event)) {
                    isMonthTouched(event.x, event.y)
                        .takeIf { it }
                        ?.run {
                            val calendar = CalendarFactory.newInstance(calendarType, locale)
                            calendar.set(year, month, 1)
                            onMonthLabelClickListener?.onMonthLabelClicked(
                                calendar,
                                event.x.toInt(),
                                event.y.toInt()
                            )
                        }
                }
            }
        }
        return true
    }

    private fun isMonthTouched(inputX: Float, inputY: Float): Boolean {
        return (
            inputX < leftGap ||
                inputX > viewWidth - rightGap ||
                inputY < paddingTop ||
                inputY > paddingTop + monthHeaderHeight
            ).not()
    }

    override val topGap: Int
        get() = paddingTop + monthHeaderHeight + weekHeaderHeight

    // Save/Restore States -------------------------------------------------------------------------

    override fun onSaveInstanceState(): Parcelable {
        return SavedState(super.onSaveInstanceState())
            .also {
                it.monthLabelTextColor = monthLabelTextColor
                it.weekLabelTextColor = weekLabelTextColor

                it.monthLabelTextSize = monthLabelTextSize
                it.weekLabelTextSize = weekLabelTextSize

                it.monthLabelTopPadding = monthLabelTopPadding
                it.monthLabelBottomPadding = monthLabelBottomPadding
                it.weekLabelTopPadding = weekLabelTopPadding
                it.weekLabelBottomPadding = weekLabelBottomPadding
            }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState

        doNotInvalidate {
            monthLabelTextColor = savedState.monthLabelTextColor
            weekLabelTextColor = savedState.weekLabelTextColor

            monthLabelTextSize = savedState.monthLabelTextSize
            weekLabelTextSize = savedState.weekLabelTextSize

            monthLabelTopPadding = savedState.monthLabelTopPadding
            monthLabelBottomPadding = savedState.monthLabelBottomPadding
            weekLabelTopPadding = savedState.weekLabelTopPadding
            weekLabelBottomPadding = savedState.weekLabelBottomPadding
        }

        super.onRestoreInstanceState(savedState.superState)
    }

    private class SavedState : BaseSavedState {

        var monthLabelTextColor: Int = 0
        var weekLabelTextColor: Int = 0

        var monthLabelTextSize: Int = 0
        var weekLabelTextSize: Int = 0

        var monthLabelTopPadding: Int = 0
        var monthLabelBottomPadding: Int = 0
        var weekLabelTopPadding: Int = 0
        var weekLabelBottomPadding: Int = 0

        constructor(superState: Parcelable?) : super(superState)

        private constructor(input: Parcel) : super(input) {
            monthLabelTextColor = input.readInt()
            weekLabelTextColor = input.readInt()

            monthLabelTextSize = input.readInt()
            weekLabelTextSize = input.readInt()

            monthLabelTopPadding = input.readInt()
            monthLabelBottomPadding = input.readInt()
            weekLabelTopPadding = input.readInt()
            weekLabelBottomPadding = input.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            out.writeInt(monthLabelTextColor)
            out.writeInt(weekLabelTextColor)

            out.writeInt(monthLabelTextSize)
            out.writeInt(weekLabelTextSize)

            out.writeInt(monthLabelTopPadding)
            out.writeInt(monthLabelBottomPadding)
            out.writeInt(weekLabelTopPadding)
            out.writeInt(weekLabelBottomPadding)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(input: Parcel): SavedState = SavedState(input)
                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }

    companion object {
        val DEFAULT_MONTH_LABEL_FORMATTER: LabelFormatter =
            { primeCalendar -> "${primeCalendar.monthName} ${primeCalendar.year}" }

        val DEFAULT_WEEK_LABEL_FORMATTER: LabelFormatter =
            { primeCalendar -> primeCalendar.weekDayNameShort }
    }
}
