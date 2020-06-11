package com.aminography.primedatepicker.monthview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseIntArray
import android.view.MotionEvent
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.common.LabelFormatter
import com.aminography.primedatepicker.utils.localizeDigits
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

    private val monthLabelPaint: Paint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
    }

    private val weekLabelPaint: Paint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
    }

    private var monthHeaderHeight = 0
    private var weekHeaderHeight = 0

    private lateinit var monthLabel: String
    private lateinit var weekLabels: Array<String>
    private lateinit var internalWeekLabelTextColors: IntArray

    // Control Variables ---------------------------------------------------------------------------

    var monthLabelTextColor: Int = 0
        set(value) {
            field = value
            monthLabelPaint.color = value
            if (invalidate) invalidate()
        }

    var weekLabelTextColor: Int = 0
        set(value) {
            field = value
            weekLabelPaint.color = value
            if (invalidate) invalidate()
        }

    var monthLabelTextSize: Int = 0
        set(value) {
            field = value
            monthLabelPaint.textSize = value.toFloat()
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var weekLabelTextSize: Int = 0
        set(value) {
            field = value
            weekLabelPaint.textSize = value.toFloat()
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
        context.obtainStyledAttributes(attrs, R.styleable.PrimeMonthView, defStyleAttr, defStyleRes).run {
            doNotInvalidate {
                monthLabelTextColor = getColor(R.styleable.PrimeMonthView_monthLabelTextColor, ContextCompat.getColor(context, R.color.blueGray200))
                weekLabelTextColor = getColor(R.styleable.PrimeMonthView_weekLabelTextColor, ContextCompat.getColor(context, R.color.red300))

                monthLabelTextSize = getDimensionPixelSize(R.styleable.PrimeMonthView_monthLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultMonthLabelTextSize))
                weekLabelTextSize = getDimensionPixelSize(R.styleable.PrimeMonthView_weekLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultWeekLabelTextSize))

                monthLabelTopPadding = getDimensionPixelSize(R.styleable.PrimeMonthView_monthLabelTopPadding, resources.getDimensionPixelSize(R.dimen.defaultMonthLabelTopPadding))
                monthLabelBottomPadding = getDimensionPixelSize(R.styleable.PrimeMonthView_monthLabelBottomPadding, resources.getDimensionPixelSize(R.dimen.defaultMonthLabelBottomPadding))
                weekLabelTopPadding = getDimensionPixelSize(R.styleable.PrimeMonthView_weekLabelTopPadding, resources.getDimensionPixelSize(R.dimen.defaultWeekLabelTopPadding))
                weekLabelBottomPadding = getDimensionPixelSize(R.styleable.PrimeMonthView_weekLabelBottomPadding, resources.getDimensionPixelSize(R.dimen.defaultWeekLabelBottomPadding))
            }
            recycle()
        }

        monthLabelPaint.also {
            it.textSize = monthLabelTextSize.toFloat()
            it.color = monthLabelTextColor
            it.typeface = typeface
        }
        weekLabelPaint.also {
            it.textSize = weekLabelTextSize.toFloat()
            it.color = weekLabelTextColor
            it.typeface = typeface
        }
    }

    override fun calculateSizes() {
        super.calculateSizes()
        monthHeaderHeight = monthLabelTextSize + monthLabelTopPadding + monthLabelBottomPadding
        weekHeaderHeight = weekLabelTextSize + weekLabelTopPadding + weekLabelBottomPadding
    }

    override fun applyTypeface() {
        super.applyTypeface()
        monthLabelPaint.typeface = typeface
        weekLabelPaint.typeface = typeface
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!::monthLabel.isInitialized) {
            val calendar = CalendarFactory.newInstance(calendarType, locale)
            goto(calendar)
        }
        drawMonthLabel(canvas)
        drawWeekLabels(canvas, columnXPositions)
    }

    override fun setupGotoExtras() {
        super.setupGotoExtras()
        firstDayOfMonthCalendar?.let {
            monthLabelFormatter(it)
        }?.also {
            monthLabel = it.localizeDigits(locale)
        }

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

    private fun drawMonthLabel(canvas: Canvas) {
        val x = viewWidth / 2f
        var y = paddingTop +
            (monthHeaderHeight - monthLabelTopPadding - monthLabelBottomPadding) / 2f +
            monthLabelTopPadding

        y -= ((monthLabelPaint.descent() + monthLabelPaint.ascent()) / 2)

        canvas.drawText(
            monthLabel,
            x,
            y,
            monthLabelPaint
        )

        if (developerOptionsShowGuideLines) {
            Paint().apply {
                isAntiAlias = true
                color = Color.RED
                style = Paint.Style.FILL
                alpha = 50
                canvas.drawRect(
                    paddingLeft.toFloat(),
                    paddingTop.toFloat(),
                    viewWidth - paddingRight.toFloat(),
                    paddingTop + monthHeaderHeight.toFloat(),
                    this
                )
            }
            Paint().apply {
                isAntiAlias = true
                color = Color.GRAY
                style = Paint.Style.STROKE
                canvas.drawRect(
                    paddingLeft.toFloat(),
                    paddingTop.toFloat(),
                    viewWidth - paddingRight.toFloat(),
                    paddingTop + monthHeaderHeight.toFloat(),
                    this
                )
            }
            Paint().apply {
                isAntiAlias = true
                color = Color.RED
                style = Paint.Style.FILL
                canvas.drawCircle(
                    x,
                    paddingTop + (monthHeaderHeight / 2).toFloat(),
                    1.dp,
                    this
                )
            }
        }
    }

    private fun drawWeekLabels(canvas: Canvas, xPositions: FloatArray) {
        var y = paddingTop +
            monthHeaderHeight +
            (weekHeaderHeight - weekLabelTopPadding - weekLabelBottomPadding) / 2f +
            weekLabelTopPadding

        y -= ((weekLabelPaint.descent() + weekLabelPaint.ascent()) / 2)

        for (i in 0 until columnCount) {
            val dayOfWeek = (i + firstDayOfWeek) % columnCount
            val x = xPositions[i]

            weekLabelPaint.color = internalWeekLabelTextColors[dayOfWeek % 7]
            canvas.drawText(
                weekLabels[dayOfWeek % 7],
                x,
                y,
                weekLabelPaint
            )

            if (developerOptionsShowGuideLines) {
                Paint().apply {
                    isAntiAlias = true
                    color = Color.GRAY
                    style = Paint.Style.STROKE
                    canvas.drawRect(
                        (x - cellWidth / 2),
                        paddingTop + monthHeaderHeight.toFloat(),
                        (x + cellWidth / 2),
                        paddingTop + monthHeaderHeight + weekHeaderHeight.toFloat(),
                        this
                    )
                }
                Paint().apply {
                    isAntiAlias = true
                    color = Color.RED
                    style = Paint.Style.FILL
                    canvas.drawCircle(
                        x,
                        paddingTop + (monthHeaderHeight + weekHeaderHeight / 2).toFloat(),
                        1.dp,
                        this
                    )
                }
            }
        }

        if (developerOptionsShowGuideLines) {
            Paint().apply {
                isAntiAlias = true
                color = Color.GREEN
                style = Paint.Style.FILL
                alpha = 50
                canvas.drawRect(
                    leftGap.toFloat(),
                    paddingTop + monthHeaderHeight.toFloat(),
                    viewWidth.toFloat() - rightGap.toFloat(),
                    paddingTop + (monthHeaderHeight + weekHeaderHeight).toFloat(),
                    this
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (!super.onTouchEvent(event)) {
                    isMonthTouched(event.x, event.y).takeIf { it }?.run {
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

    override fun onSaveInstanceState(): Parcelable? {
        val savedState = SavedState(super.onSaveInstanceState())

        savedState.monthLabelTextColor = monthLabelTextColor
        savedState.weekLabelTextColor = weekLabelTextColor

        savedState.monthLabelTextSize = monthLabelTextSize
        savedState.weekLabelTextSize = weekLabelTextSize

        savedState.monthLabelTopPadding = monthLabelTopPadding
        savedState.monthLabelBottomPadding = monthLabelBottomPadding
        savedState.weekLabelTopPadding = weekLabelTopPadding
        savedState.weekLabelBottomPadding = weekLabelBottomPadding

        return savedState
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

        internal var monthLabelTextColor: Int = 0
        internal var weekLabelTextColor: Int = 0

        internal var monthLabelTextSize: Int = 0
        internal var weekLabelTextSize: Int = 0

        internal var monthLabelTopPadding: Int = 0
        internal var monthLabelBottomPadding: Int = 0
        internal var weekLabelTopPadding: Int = 0
        internal var weekLabelBottomPadding: Int = 0

        internal constructor(superState: Parcelable?) : super(superState)

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