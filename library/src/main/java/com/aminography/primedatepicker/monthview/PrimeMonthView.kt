package com.aminography.primedatepicker.monthview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.Paint.Style
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.Direction
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.tools.DateUtils
import com.aminography.primedatepicker.tools.PersianUtils
import com.aminography.primedatepicker.tools.isDisplayLandscape
import org.jetbrains.anko.dip
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
) : View(context, attrs, defStyleAttr) {

    // Interior Variables --------------------------------------------------------------------------

    private val dp = dip(1)
    private fun dp(value: Float) = dp.times(value).toInt()

    private var monthLabelPaint: Paint? = null
    private var weekLabelPaint: Paint? = null
    private var dayLabelPaint: Paint? = null
    private var selectedDayBackgroundPaint: Paint? = null

    private var viewWidth = 0
    private var monthHeaderHeight = 0
    private var weekHeaderHeight = 0

    private val defaultCellHeight = dp(36f).toFloat()
    private var minCellHeight: Float = 0f
    private var cellHeight: Float = defaultCellHeight
    private var cellWidth: Float = cellHeight

    private var direction = Direction.LTR

    private var month = 0
    private var year = 0

    private var hasToday = false
    private var todayDayOfMonth = -1

    private var weekStartDay = Calendar.SUNDAY
    private var daysInMonth = 0

    private var firstDayOfMonthDayOfWeek = 0

    private var maxRowCount = 6
    private var rowCount = 6
    private var columnCount = 7

    private var dayOfWeekLabelCalendar: BaseCalendar? = null
    private var firstDayOfMonthCalendar: BaseCalendar? = null

    // Listeners -----------------------------------------------------------------------------------

    var onHeightDetectListener: OnHeightDetectListener? = null
    var onDayPickedListener: OnDayPickedListener? = null

    // Control Variables ---------------------------------------------------------------------------

    var monthLabelTextColor: Int = 0
        set(value) {
            field = value
            monthLabelPaint?.color = value
            if (invalidate) invalidate()
        }

    var weekLabelTextColor: Int = 0
        set(value) {
            field = value
            weekLabelPaint?.color = value
            if (invalidate) invalidate()
        }

    var dayLabelTextColor: Int = 0
        set(value) {
            field = value
            if (invalidate) invalidate()
        }

    var todayLabelTextColor: Int = 0
        set(value) {
            field = value
            if (invalidate) invalidate()
        }

    var selectedDayLabelTextColor: Int = 0
        set(value) {
            field = value
            if (invalidate) invalidate()
        }

    var selectedDayCircleColor: Int = 0
        set(value) {
            field = value
            if (invalidate) invalidate()
        }

    var disabledDayLabelTextColor: Int = 0
        set(value) {
            field = value
            if (invalidate) invalidate()
        }

    var monthLabelTextSize: Int = 0
        set(value) {
            field = value
            monthLabelPaint?.textSize = value.toFloat()
            calculateSizes()
            if (invalidate) {
                requestLayout()
                invalidate()
            }
        }

    var weekLabelTextSize: Int = 0
        set(value) {
            field = value
            weekLabelPaint?.textSize = value.toFloat()
            calculateSizes()
            if (invalidate) {
                requestLayout()
                invalidate()
            }
        }

    var dayLabelTextSize: Int = 0
        set(value) {
            field = value
            dayLabelPaint?.textSize = value.toFloat()
            calculateSizes()
            if (invalidate) {
                requestLayout()
                invalidate()
            }
        }

    var monthLabelTopPadding: Int = 0
        set(value) {
            field = value
            calculateSizes()
            if (invalidate) {
                requestLayout()
                invalidate()
            }
        }

    var monthLabelBottomPadding: Int = 0
        set(value) {
            field = value
            calculateSizes()
            if (invalidate) {
                requestLayout()
                invalidate()
            }
        }

    var weekLabelTopPadding: Int = 0
        set(value) {
            field = value
            calculateSizes()
            if (invalidate) {
                requestLayout()
                invalidate()
            }
        }

    var weekLabelBottomPadding: Int = 0
        set(value) {
            field = value
            calculateSizes()
            if (invalidate) {
                requestLayout()
                invalidate()
            }
        }

    var dayLabelVerticalPadding: Int = 0
        set(value) {
            field = value
            calculateSizes()
            if (invalidate) {
                requestLayout()
                invalidate()
            }
        }

    var twoWeeksInLandscape: Boolean = false
        set(value) {
            field = value
            calculateSizes()
            if (invalidate) invalidate()
        }

    // Programmatically Control Variables ----------------------------------------------------------

    var typeface: Typeface? = null
        set(value) {
            field = value
            applyTypeface()
            if (invalidate) invalidate()
        }

    var pickedSingleDayCalendar: BaseCalendar? = null
        set(value) {
            field = value
            if (invalidate) invalidate()
            notifyDayPicked(true)
        }

    var pickedStartRangeCalendar: BaseCalendar? = null
        set(value) {
            field = value
            if (invalidate) invalidate()
            notifyDayPicked(true)
        }

    var pickedEndRangeCalendar: BaseCalendar? = null
        set(value) {
            field = value
            if (invalidate) invalidate()
            notifyDayPicked(true)
        }

    var pickType: PickType = PickType.NOTHING
        set(value) {
            field = value
            notInvalidate {
                when (value) {
                    PickType.SINGLE -> {
                        pickedStartRangeCalendar = null
                        pickedEndRangeCalendar = null
                    }
                    PickType.START_RANGE -> pickedSingleDayCalendar = null
                    PickType.END_RANGE -> pickedSingleDayCalendar = null
                    PickType.NOTHING -> {
                        pickedSingleDayCalendar = null
                        pickedStartRangeCalendar = null
                        pickedEndRangeCalendar = null
                    }
                }
            }
            if (invalidate) invalidate()
            notifyDayPicked(true)
        }

    var minDateCalendar: BaseCalendar? = null
        set(value) {
            field = value
            var change = false
            notInvalidate {
                value?.also { min ->
                    pickedSingleDayCalendar?.let { single ->
                        if (DateUtils.isBefore(single, min)) {
                            pickedSingleDayCalendar = min
                            change = true
                        }
                    }
                    pickedStartRangeCalendar?.let { start ->
                        if (DateUtils.isBefore(start, min)) {
                            pickedStartRangeCalendar = min
                            change = true
                        }
                    }
                    pickedEndRangeCalendar?.let { end ->
                        if (DateUtils.isBefore(end, min)) {
                            pickedStartRangeCalendar = null
                            pickedEndRangeCalendar = null
                            change = true
                        }
                    }
                }
            }
            if (invalidate) invalidate()
            notifyDayPicked(change)
        }

    var maxDateCalendar: BaseCalendar? = null
        set(value) {
            field = value
            var change = false
            notInvalidate {
                value?.also { max ->
                    pickedSingleDayCalendar?.let { single ->
                        if (DateUtils.isAfter(single, max)) {
                            pickedSingleDayCalendar = max
                            change = true
                        }
                    }
                    pickedStartRangeCalendar?.let { start ->
                        if (DateUtils.isAfter(start, max)) {
                            pickedStartRangeCalendar = null
                            pickedEndRangeCalendar = null
                            change = true
                        }
                    }
                    pickedEndRangeCalendar?.let { end ->
                        if (DateUtils.isAfter(end, max)) {
                            pickedEndRangeCalendar = max
                            change = true
                        }
                    }
                }
            }
            if (invalidate) invalidate()
            notifyDayPicked(change)
        }

    var calendarType = CalendarType.CIVIL
        set(value) {
            field = value
            direction = when (value) {
                CalendarType.CIVIL -> Direction.LTR
                CalendarType.PERSIAN, CalendarType.HIJRI -> Direction.RTL
            }
            if (invalidate) setDate(CalendarFactory.newInstance(value))
        }

    // ---------------------------------------------------------------------------------------------

    private var pickedDaysChanged: Boolean = false
    private var invalidate: Boolean = true

    fun together(function: () -> Unit) {
        invalidate = false
        function.invoke()
        invalidate = true
        invalidate()
    }

    fun notInvalidate(function: () -> Unit) {
        val previous = invalidate
        invalidate = false
        function.invoke()
        invalidate = previous
    }

    // ---------------------------------------------------------------------------------------------

    init {
        context.obtainStyledAttributes(attrs, R.styleable.PrimeMonthView, defStyleAttr, defStyleRes).apply {
            notInvalidate {
                monthLabelTextColor = getColor(R.styleable.PrimeMonthView_monthLabelTextColor, ContextCompat.getColor(context, R.color.defaultMonthLabelTextColor))
                weekLabelTextColor = getColor(R.styleable.PrimeMonthView_weekLabelTextColor, ContextCompat.getColor(context, R.color.defaultWeekLabelTextColor))
                dayLabelTextColor = getColor(R.styleable.PrimeMonthView_dayLabelTextColor, ContextCompat.getColor(context, R.color.defaultDayLabelTextColor))
                todayLabelTextColor = getColor(R.styleable.PrimeMonthView_todayLabelTextColor, ContextCompat.getColor(context, R.color.defaultTodayLabelTextColor))
                selectedDayLabelTextColor = getColor(R.styleable.PrimeMonthView_pickedDayLabelTextColor, ContextCompat.getColor(context, R.color.defaultSelectedDayLabelTextColor))
                selectedDayCircleColor = getColor(R.styleable.PrimeMonthView_pickedDayCircleColor, ContextCompat.getColor(context, R.color.defaultSelectedDayCircleColor))
                disabledDayLabelTextColor = getColor(R.styleable.PrimeMonthView_disabledDayLabelTextColor, ContextCompat.getColor(context, R.color.defaultDisabledDayLabelTextColor))

                monthLabelTextSize = getDimensionPixelSize(R.styleable.PrimeMonthView_monthLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultMonthLabelTextSize))
                weekLabelTextSize = getDimensionPixelSize(R.styleable.PrimeMonthView_weekLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultWeekLabelTextSize))
                dayLabelTextSize = getDimensionPixelSize(R.styleable.PrimeMonthView_dayLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultDayLabelTextSize))

                monthLabelTopPadding = getDimensionPixelSize(R.styleable.PrimeMonthView_monthLabelTopPadding, resources.getDimensionPixelSize(R.dimen.defaultMonthLabelTopPadding))
                monthLabelBottomPadding = getDimensionPixelSize(R.styleable.PrimeMonthView_monthLabelBottomPadding, resources.getDimensionPixelSize(R.dimen.defaultMonthLabelBottomPadding))
                weekLabelTopPadding = getDimensionPixelSize(R.styleable.PrimeMonthView_weekLabelTopPadding, resources.getDimensionPixelSize(R.dimen.defaultWeekLabelTopPadding))
                weekLabelBottomPadding = getDimensionPixelSize(R.styleable.PrimeMonthView_weekLabelBottomPadding, resources.getDimensionPixelSize(R.dimen.defaultWeekLabelBottomPadding))
                dayLabelVerticalPadding = getDimensionPixelSize(R.styleable.PrimeMonthView_dayLabelVerticalPadding, resources.getDimensionPixelSize(R.dimen.defaultDayLabelVerticalPadding))

                twoWeeksInLandscape = getBoolean(R.styleable.PrimeMonthView_showTwoWeeksInLandscape, resources.getBoolean(R.bool.defaultShowTwoWeeksInLandscape))
            }
            recycle()
        }

        calculateSizes()
        initPaints()
        applyTypeface()

        if (isInEditMode) {
            val calendar = CalendarFactory.newInstance(calendarType)
            setDate(calendar)
        }
    }

    private fun calculateSizes() {
        if (context.isDisplayLandscape()) {
            if (twoWeeksInLandscape) {
                maxRowCount = 3
                rowCount = 3
                columnCount = 14
            } else {
                maxRowCount = 6
                rowCount = 6
                columnCount = 7
            }
        }

        minCellHeight = dayLabelTextSize.toFloat()
        cellHeight = dayLabelTextSize + 2f * dayLabelVerticalPadding
        cellWidth = (viewWidth - (paddingLeft + paddingRight)) / columnCount.toFloat()

        monthHeaderHeight = monthLabelTextSize + monthLabelTopPadding + monthLabelBottomPadding
        weekHeaderHeight = weekLabelTextSize + weekLabelTopPadding + weekLabelBottomPadding
    }

    private fun initPaints() {
        monthLabelPaint = Paint().apply {
            textSize = monthLabelTextSize.toFloat()
            color = monthLabelTextColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }

        weekLabelPaint = Paint().apply {
            textSize = weekLabelTextSize.toFloat()
            color = weekLabelTextColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }

        dayLabelPaint = Paint().apply {
            textSize = dayLabelTextSize.toFloat()
            color = dayLabelTextColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = false
        }

        selectedDayBackgroundPaint = Paint().apply {
            color = selectedDayCircleColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
    }

    private fun applyTypeface() {
        monthLabelPaint?.typeface = typeface
        weekLabelPaint?.typeface = typeface
        dayLabelPaint?.typeface = typeface
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = paddingTop + monthHeaderHeight + weekHeaderHeight + cellHeight * rowCount + paddingBottom
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height.toInt())
        val maxHeight = paddingTop + monthHeaderHeight + weekHeaderHeight + cellHeight * maxRowCount + paddingBottom
        onHeightDetectListener?.onHeightDetect(maxHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        viewWidth = w
        cellWidth = (viewWidth - (paddingLeft + paddingRight)) / columnCount.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        drawMonthLabel(canvas)
        drawWeekLabels(canvas)
        drawDayLabels(canvas)
    }

    fun setDate(calendar: BaseCalendar) {
        setDate(calendar.calendarType, calendar.year, calendar.month)
    }

    fun setDate(calendarType: CalendarType, year: Int, month: Int) {
        notInvalidate {
            this.calendarType = calendarType
        }

        this.year = year
        this.month = month

        dayOfWeekLabelCalendar = CalendarFactory.newInstance(calendarType)
        firstDayOfMonthCalendar = CalendarFactory.newInstance(calendarType)

        firstDayOfMonthCalendar?.setDate(year, month, 1)
        firstDayOfMonthDayOfWeek = firstDayOfMonthCalendar!!.get(Calendar.DAY_OF_WEEK)

        daysInMonth = DateUtils.getDaysInMonth(calendarType, year, month)
        weekStartDay = when (calendarType) {
            CalendarType.CIVIL -> Calendar.SUNDAY
            CalendarType.PERSIAN -> Calendar.SATURDAY
            CalendarType.HIJRI -> Calendar.SATURDAY
        }

        updateToday()
        updateRowCount()

        requestLayout()
        invalidate()
    }

    private fun updateToday() {
        val todayCalendar = CalendarFactory.newInstance(calendarType)
        hasToday = todayCalendar.year == year && todayCalendar.month == month
        todayDayOfMonth = if (hasToday) todayCalendar.dayOfMonth else -1
    }

    private fun updateRowCount() {
        val offset = weekOffset(firstDayOfMonthDayOfWeek)
        val dividend = (offset + daysInMonth) / columnCount
        val remainder = (offset + daysInMonth) % columnCount
        rowCount = dividend + if (remainder > 0) 1 else 0
    }

    private fun weekOffset(dayOfWeek: Int): Int {
        val day = if (dayOfWeek < weekStartDay) dayOfWeek + 7 else dayOfWeek
        return (day - weekStartDay) % 7
    }

    private fun drawMonthLabel(canvas: Canvas) {
        val x = viewWidth / 2f
        var y = paddingTop + (monthHeaderHeight - monthLabelTopPadding - monthLabelBottomPadding) / 2f + monthLabelTopPadding
        monthLabelPaint?.apply {
            y -= ((descent() + ascent()) / 2)
        }

        var monthAndYearString = "${firstDayOfMonthCalendar?.monthName} ${firstDayOfMonthCalendar?.year}"
        monthAndYearString = when (direction) {
            Direction.LTR -> monthAndYearString
            Direction.RTL -> PersianUtils.convertLatinDigitsToPersian(monthAndYearString)
        }

        monthLabelPaint?.apply {
            canvas.drawText(
                    monthAndYearString,
                    x,
                    y,
                    this
            )
        }

        if (SHOW_GUIDE_LINES) {
            Paint().apply {
                isAntiAlias = true
                color = Color.RED
                style = Style.FILL
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
                style = Style.STROKE
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
                style = Style.FILL
                canvas.drawCircle(
                        x,
                        paddingTop + (monthHeaderHeight / 2).toFloat(),
                        dp(1f).toFloat(),
                        this
                )
            }
        }
    }

    private fun drawWeekLabels(canvas: Canvas) {
        var y = paddingTop + monthHeaderHeight + (weekHeaderHeight - weekLabelTopPadding - weekLabelBottomPadding) / 2f + weekLabelTopPadding
        weekLabelPaint?.apply {
            y -= ((descent() + ascent()) / 2)
        }

        val xPositionList = arrayListOf<Float>().apply {
            for (i in 0 until columnCount) {
                // RTLize for Persian and Hijri Calendars
                add(when (direction) {
                    Direction.LTR -> (2 * i + 1) * (cellWidth / 2) + paddingLeft
                    Direction.RTL -> (2 * (columnCount - 1 - i) + 1) * (cellWidth / 2) + paddingLeft
                })
            }
        }

        for (i in 0 until columnCount) {
            val dayOfWeek = (i + weekStartDay) % columnCount
            val x = xPositionList[i]

            dayOfWeekLabelCalendar?.set(Calendar.DAY_OF_WEEK, dayOfWeek % 7)
            val localWeekDisplayName = dayOfWeekLabelCalendar?.weekDayName

            val weekString = when (calendarType) {
                CalendarType.CIVIL -> localWeekDisplayName?.substring(0, 2)
                CalendarType.PERSIAN -> localWeekDisplayName?.substring(0, 1)
                CalendarType.HIJRI -> localWeekDisplayName?.substring(2, 4)
            } ?: "?"

            weekLabelPaint?.apply {
                canvas.drawText(
                        weekString,
                        x,
                        y,
                        this
                )
            }

            if (SHOW_GUIDE_LINES) {
                Paint().apply {
                    isAntiAlias = true
                    color = Color.GRAY
                    style = Style.STROKE
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
                    style = Style.FILL
                    canvas.drawCircle(
                            x,
                            paddingTop + (monthHeaderHeight + weekHeaderHeight / 2).toFloat(),
                            dp(1f).toFloat(),
                            this
                    )
                }
            }
        }

        if (SHOW_GUIDE_LINES) {
            Paint().apply {
                isAntiAlias = true
                color = Color.GREEN
                style = Style.FILL
                alpha = 50
                canvas.drawRect(
                        paddingLeft.toFloat(),
                        paddingTop + monthHeaderHeight.toFloat(),
                        viewWidth.toFloat() - paddingRight.toFloat(),
                        paddingTop + (monthHeaderHeight + weekHeaderHeight).toFloat(),
                        this
                )
            }
        }
    }

    private fun drawDayLabels(canvas: Canvas) {
        var topY: Float = (paddingTop + monthHeaderHeight + weekHeaderHeight).toFloat()
        var offset = weekOffset(firstDayOfMonthDayOfWeek)
        val radius = Math.min(cellWidth, cellHeight) / 2 - dp(2f)

        val xPositionList = arrayListOf<Float>().apply {
            for (i in 0 until columnCount) {
                // RTLize for Persian and Hijri Calendars
                add(when (direction) {
                    Direction.LTR -> ((2 * i + 1) * (cellWidth / 2) + paddingLeft)
                    Direction.RTL -> ((2 * (columnCount - 1 - i) + 1) * (cellWidth / 2) + paddingLeft)
                })
            }
        }

        for (dayOfMonth in 1..daysInMonth) {
            val y = topY + cellHeight / 2
            val x = xPositionList[offset]

            val pickedDayState = MonthViewUtils.findDayState(
                    year,
                    month,
                    dayOfMonth,
                    pickType,
                    pickedSingleDayCalendar,
                    pickedStartRangeCalendar,
                    pickedEndRangeCalendar
            )

            drawDayBackground(canvas, pickedDayState, x, y, radius)
            drawDayLabel(canvas, dayOfMonth, pickedDayState, x, y)

            if (SHOW_GUIDE_LINES) {
                Paint().apply {
                    isAntiAlias = true
                    color = Color.GRAY
                    style = Style.STROKE
                    canvas.drawRect(
                            x - cellWidth / 2,
                            topY,
                            x + cellWidth / 2,
                            topY + cellHeight,
                            this
                    )
                }
                Paint().apply {
                    isAntiAlias = true
                    color = Color.RED
                    style = Style.FILL
                    canvas.drawCircle(
                            x,
                            y,
                            dp(1f).toFloat(),
                            this
                    )
                }
            }

            offset++
            if (offset == columnCount) {
                offset = 0
                topY += cellHeight
            }
        }
    }

    private fun drawDayBackground(canvas: Canvas, pickedDayState: PickedDayState, x: Float, y: Float, radius: Float) {
        selectedDayBackgroundPaint?.apply {

            fun drawCircle() = canvas.drawCircle(x, y, radius, this)

            fun drawRect() = canvas.drawRect(x - cellWidth / 2, y - radius, x + cellWidth / 2, y + radius, this)

            fun drawHalfRect(isStart: Boolean) {
                when (direction) {
                    Direction.LTR -> if (isStart)
                        canvas.drawRect(x, y - radius, (x + cellWidth / 2), y + radius, this)
                    else canvas.drawRect(x - cellWidth / 2, y - radius, x, y + radius, this)
                    // ---------------------
                    Direction.RTL -> if (isStart)
                        canvas.drawRect(x - cellWidth / 2, y - radius, x, y + radius, this)
                    else canvas.drawRect(x, y - radius, (x + cellWidth / 2), y + radius, this)
                }
            }

            when (pickedDayState) {
                PickedDayState.PICKED_SINGLE, PickedDayState.START_OF_RANGE_SINGLE -> {
                    drawCircle()
                }
                PickedDayState.START_OF_RANGE -> {
                    drawCircle()
                    drawHalfRect(true)
                }
                PickedDayState.IN_RANGE -> {
                    drawRect()
                }
                PickedDayState.END_OF_RANGE -> {
                    drawCircle()
                    drawHalfRect(false)
                }
                PickedDayState.NOTHING -> {
                }
            }
        }
    }

    private fun drawDayLabel(canvas: Canvas, dayOfMonth: Int, pickedDayState: PickedDayState, x: Float, y: Float) {
        dayLabelPaint?.apply {
            color = if (DateUtils.isOutOfRange(year, month, dayOfMonth, minDateCalendar, maxDateCalendar)) {
                disabledDayLabelTextColor
            } else if (pickType != PickType.NOTHING) {
                when (pickedDayState) {
                    PickedDayState.PICKED_SINGLE -> {
                        selectedDayLabelTextColor
                    }
                    PickedDayState.START_OF_RANGE_SINGLE -> {
                        selectedDayLabelTextColor
                    }
                    PickedDayState.START_OF_RANGE -> {
                        selectedDayLabelTextColor
                    }
                    PickedDayState.IN_RANGE -> {
                        selectedDayLabelTextColor
                    }
                    PickedDayState.END_OF_RANGE -> {
                        selectedDayLabelTextColor
                    }
                    PickedDayState.NOTHING -> {
                        if (hasToday && dayOfMonth == todayDayOfMonth) {
                            todayLabelTextColor
                        } else {
                            dayLabelTextColor
                        }
                    }
                }
            } else if (hasToday && dayOfMonth == todayDayOfMonth) {
                todayLabelTextColor
            } else {
                dayLabelTextColor
            }
        }

        val date = when (direction) {
            Direction.LTR -> String.format(Locale.getDefault(), "%d", dayOfMonth)
            Direction.RTL -> PersianUtils.convertLatinDigitsToPersian(String.format(Locale.getDefault(), "%d", dayOfMonth))
        }

        dayLabelPaint?.apply {
            canvas.drawText(
                    date,
                    x,
                    y - (descent() + ascent()) / 2,
                    this
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                findDayByCoordinates(event.x, event.y)?.let { dayOfMonth ->
                    ifInValidRange(dayOfMonth) {
                        val calendar = CalendarFactory.newInstance(calendarType)
                        calendar.setDate(year, month, dayOfMonth)
                        onDayClicked(calendar)
                    }
                }
            }
        }
        return true
    }

    private fun onDayClicked(calendar: BaseCalendar) {
        calendar.apply {
            var change = false
            together {
                when (pickType) {
                    PickType.SINGLE -> {
                        pickedSingleDayCalendar = calendar
                        change = true
                    }
                    PickType.START_RANGE -> {
                        if (DateUtils.isAfter(calendar, pickedEndRangeCalendar)) {
                            pickedEndRangeCalendar = null
                        }
                        pickedStartRangeCalendar = calendar
                        change = true
                    }
                    PickType.END_RANGE -> {
                        if (pickedStartRangeCalendar != null && !DateUtils.isBefore(calendar, pickedStartRangeCalendar)) {
                            pickedEndRangeCalendar = calendar
                            change = true
                        }
                    }
                    PickType.NOTHING -> {
                    }
                }
            }
            notifyDayPicked(change)
        }
    }

    private fun notifyDayPicked(change: Boolean) {
        pickedDaysChanged = pickedDaysChanged or change
        if (invalidate && pickedDaysChanged) {
            onDayPickedListener?.onDayPicked(
                    pickType,
                    pickedSingleDayCalendar,
                    pickedStartRangeCalendar,
                    pickedEndRangeCalendar
            )
            pickedDaysChanged = false
        }
    }

    private fun findDayByCoordinates(inputX: Float, inputY: Float): Int? {
        if (inputX < paddingLeft || inputX > viewWidth - paddingRight) return null

        val y = inputY - dp(12f)
        val row = ((y - (monthHeaderHeight + weekHeaderHeight)) / cellHeight).toInt()
        var column = ((inputX - paddingLeft) * columnCount / (viewWidth - (paddingLeft + paddingRight))).toInt()

        column = when (direction) {
            Direction.LTR -> column
            Direction.RTL -> columnCount - 1 - column
        }

        var day = column - weekOffset(firstDayOfMonthDayOfWeek) + 1
        day += row * columnCount

        return if (day < 1 || day > daysInMonth) {
            null
        } else day
    }

    private fun ifInValidRange(dayOfMonth: Int, function: () -> Unit) {
        if (!DateUtils.isOutOfRange(year, month, dayOfMonth, minDateCalendar, maxDateCalendar))
            function.invoke()
    }

    enum class PickedDayState {
        PICKED_SINGLE,
        START_OF_RANGE_SINGLE,
        START_OF_RANGE,
        IN_RANGE,
        END_OF_RANGE,
        NOTHING
    }

    interface OnHeightDetectListener {
        fun onHeightDetect(height: Float)
    }

    // Save/Restore States -------------------------------------------------------------------------

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)

        savedState.calendarType = calendarType.ordinal
        savedState.year = year
        savedState.month = month

        savedState.minDateCalendar = DateUtils.storeCalendar(minDateCalendar)
        savedState.maxDateCalendar = DateUtils.storeCalendar(maxDateCalendar)

        savedState.pickType = pickType.name
        savedState.pickedSingleDayCalendar = DateUtils.storeCalendar(pickedSingleDayCalendar)
        savedState.pickedStartRangeCalendar = DateUtils.storeCalendar(pickedStartRangeCalendar)
        savedState.pickedEndRangeCalendar = DateUtils.storeCalendar(pickedEndRangeCalendar)

        savedState.monthLabelTextColor = monthLabelTextColor
        savedState.weekLabelTextColor = weekLabelTextColor
        savedState.dayLabelTextColor = dayLabelTextColor
        savedState.todayLabelTextColor = todayLabelTextColor
        savedState.selectedDayLabelTextColor = selectedDayLabelTextColor
        savedState.selectedDayCircleColor = selectedDayCircleColor
        savedState.disabledDayLabelTextColor = disabledDayLabelTextColor
        savedState.monthLabelTextSize = monthLabelTextSize
        savedState.weekLabelTextSize = weekLabelTextSize
        savedState.dayLabelTextSize = dayLabelTextSize
        savedState.monthLabelTopPadding = monthLabelTopPadding
        savedState.monthLabelBottomPadding = monthLabelBottomPadding
        savedState.weekLabelTopPadding = weekLabelTopPadding
        savedState.weekLabelBottomPadding = weekLabelBottomPadding
        savedState.dayLabelVerticalPadding = dayLabelVerticalPadding
        savedState.twoWeeksInLandscape = twoWeeksInLandscape

        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)

        notInvalidate {
            calendarType = CalendarType.values()[savedState.calendarType]
            year = savedState.year
            month = savedState.month

            minDateCalendar = DateUtils.restoreCalendar(savedState.minDateCalendar)
            maxDateCalendar = DateUtils.restoreCalendar(savedState.maxDateCalendar)

            pickType = savedState.pickType?.let {
                PickType.valueOf(it)
            } ?: PickType.NOTHING
            pickedSingleDayCalendar = DateUtils.restoreCalendar(savedState.pickedSingleDayCalendar)
            pickedStartRangeCalendar = DateUtils.restoreCalendar(savedState.pickedStartRangeCalendar)
            pickedEndRangeCalendar = DateUtils.restoreCalendar(savedState.pickedEndRangeCalendar)

            monthLabelTextColor = savedState.monthLabelTextColor
            weekLabelTextColor = savedState.weekLabelTextColor
            dayLabelTextColor = savedState.dayLabelTextColor
            todayLabelTextColor = savedState.todayLabelTextColor
            selectedDayLabelTextColor = savedState.selectedDayLabelTextColor
            selectedDayCircleColor = savedState.selectedDayCircleColor
            disabledDayLabelTextColor = savedState.disabledDayLabelTextColor
            monthLabelTextSize = savedState.monthLabelTextSize
            weekLabelTextSize = savedState.weekLabelTextSize
            dayLabelTextSize = savedState.dayLabelTextSize
            monthLabelTopPadding = savedState.monthLabelTopPadding
            monthLabelBottomPadding = savedState.monthLabelBottomPadding
            weekLabelTopPadding = savedState.weekLabelTopPadding
            weekLabelBottomPadding = savedState.weekLabelBottomPadding
            dayLabelVerticalPadding = savedState.dayLabelVerticalPadding
            twoWeeksInLandscape = savedState.twoWeeksInLandscape
        }

        applyTypeface()
        calculateSizes()
        setDate(calendarType, year, month)
        notifyDayPicked(true)
    }

    private class SavedState : BaseSavedState {

        internal var calendarType: Int = 0
        internal var year: Int = 0
        internal var month: Int = 0

        internal var minDateCalendar: String? = null
        internal var maxDateCalendar: String? = null

        internal var pickType: String? = null
        internal var pickedSingleDayCalendar: String? = null
        internal var pickedStartRangeCalendar: String? = null
        internal var pickedEndRangeCalendar: String? = null

        internal var monthLabelTextColor: Int = 0
        internal var weekLabelTextColor: Int = 0
        internal var dayLabelTextColor: Int = 0
        internal var todayLabelTextColor: Int = 0
        internal var selectedDayLabelTextColor: Int = 0
        internal var selectedDayCircleColor: Int = 0
        internal var disabledDayLabelTextColor: Int = 0
        internal var monthLabelTextSize: Int = 0
        internal var weekLabelTextSize: Int = 0
        internal var dayLabelTextSize: Int = 0
        internal var monthLabelTopPadding: Int = 0
        internal var monthLabelBottomPadding: Int = 0
        internal var weekLabelTopPadding: Int = 0
        internal var weekLabelBottomPadding: Int = 0
        internal var dayLabelVerticalPadding: Int = 0
        internal var twoWeeksInLandscape: Boolean = false

        internal constructor(superState: Parcelable?) : super(superState)

        private constructor(input: Parcel) : super(input) {
            calendarType = input.readInt()
            year = input.readInt()
            month = input.readInt()

            minDateCalendar = input.readString()
            maxDateCalendar = input.readString()

            pickType = input.readString()
            pickedSingleDayCalendar = input.readString()
            pickedStartRangeCalendar = input.readString()
            pickedEndRangeCalendar = input.readString()

            monthLabelTextColor = input.readInt()
            weekLabelTextColor = input.readInt()
            dayLabelTextColor = input.readInt()
            todayLabelTextColor = input.readInt()
            selectedDayLabelTextColor = input.readInt()
            selectedDayCircleColor = input.readInt()
            disabledDayLabelTextColor = input.readInt()
            monthLabelTextSize = input.readInt()
            weekLabelTextSize = input.readInt()
            dayLabelTextSize = input.readInt()
            monthLabelTopPadding = input.readInt()
            monthLabelBottomPadding = input.readInt()
            weekLabelTopPadding = input.readInt()
            weekLabelBottomPadding = input.readInt()
            dayLabelVerticalPadding = input.readInt()
            twoWeeksInLandscape = input.readInt() == 1
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(calendarType)
            out.writeInt(year)
            out.writeInt(month)

            out.writeString(minDateCalendar)
            out.writeString(maxDateCalendar)

            out.writeString(pickType)
            out.writeString(pickedSingleDayCalendar)
            out.writeString(pickedStartRangeCalendar)
            out.writeString(pickedEndRangeCalendar)

            out.writeInt(monthLabelTextColor)
            out.writeInt(weekLabelTextColor)
            out.writeInt(dayLabelTextColor)
            out.writeInt(todayLabelTextColor)
            out.writeInt(selectedDayLabelTextColor)
            out.writeInt(selectedDayCircleColor)
            out.writeInt(disabledDayLabelTextColor)
            out.writeInt(monthLabelTextSize)
            out.writeInt(weekLabelTextSize)
            out.writeInt(dayLabelTextSize)
            out.writeInt(monthLabelTopPadding)
            out.writeInt(monthLabelBottomPadding)
            out.writeInt(weekLabelTopPadding)
            out.writeInt(weekLabelBottomPadding)
            out.writeInt(dayLabelVerticalPadding)
            out.writeInt(if (twoWeeksInLandscape) 1 else 0)
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
        private const val SHOW_GUIDE_LINES = false
    }

}
