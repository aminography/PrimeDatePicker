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
@Suppress("ConstantConditionIf")
class PrimeMonthView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0,
        @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr) {

    @Suppress("PrivatePropertyName")
    private val SHOW_GUIDE_LINES = false
    private val dp = dip(1)
    private fun dp(value: Float) = dp.times(value).toInt()

    private var monthLabelTextColor: Int = 0
    private var weekLabelTextColor: Int = 0
    private var dayLabelTextColor: Int = 0
    private var todayLabelTextColor: Int = 0
    private var selectedDayLabelTextColor: Int = 0
    private var selectedDayCircleColor: Int = 0
    private var disabledDayLabelTextColor: Int = 0

    private var monthLabelTextSize: Int = 0
    private var weekLabelTextSize: Int = 0
    private var dayLabelTextSize: Int = 0
    private var monthLabelTopPadding: Int = 0
    private var monthLabelBottomPadding: Int = 0
    private var weekLabelTopPadding: Int = 0
    private var weekLabelBottomPadding: Int = 0

    private var monthLabelPaint: Paint? = null
    private var weekLabelPaint: Paint? = null
    private var dayLabelPaint: Paint? = null
    private var selectedDayBackgroundPaint: Paint? = null

    var fontTypeface: Typeface? = null
        set(value) {
            field = value
            initPaints()
            invalidate()
        }

    private var viewWidth = 0
    private var monthHeaderHeight = 0
    private var weekHeaderHeight = 0

    private val defaultCellHeight = dp(36f).toFloat()
    private var minCellHeight: Float = 0f
    private var cellHeight: Float = defaultCellHeight
    private var cellWidth: Float = cellHeight

    private var calendarType = CalendarType.CIVIL
    private var month = 0
    private var year = 0

    var pickType = PickType.NOTHING
        set(value) {
            field = value
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
//            invalidate()
        }

    var pickedSingleDayCalendar: BaseCalendar? = null
    var pickedStartRangeCalendar: BaseCalendar? = null
    var pickedEndRangeCalendar: BaseCalendar? = null

    private var hasToday = false
    private var todayDayOfMonth = -1

    private var weekStartDay = Calendar.SUNDAY
    private var daysInMonth = 0

    private var firstDayOfMonthDayOfWeek = 0

    private var rowCount = 6
    private var columnCount = 7

    private var dayOfWeekLabelCalendar: BaseCalendar? = null
    private var firstDayOfMonthCalendar: BaseCalendar? = null

    var onHeightDetectListener: OnHeightDetectListener? = null
    var onDayClickListener: OnDayClickListener? = null

    private var isInternalChange = false

    var minDateCalendar: BaseCalendar? = null
        set(value) {
            field = value
            pickedSingleDayCalendar?.let { single ->
                if (DateUtils.isBefore(single, value)) {
                    pickedSingleDayCalendar = value
                }
            }
            pickedStartRangeCalendar?.let { start ->
                if (DateUtils.isBefore(start, value)) {
                    pickedStartRangeCalendar = value
                }
            }
            pickedEndRangeCalendar?.let { end ->
                if (DateUtils.isBefore(end, value)) {
                    pickedStartRangeCalendar = null
                    pickedEndRangeCalendar = null
                }
            }
            if (!isInternalChange) invalidate()
        }

    var maxDateCalendar: BaseCalendar? = null
        set(value) {
            field = value
            pickedSingleDayCalendar?.let { single ->
                if (DateUtils.isAfter(single, value)) {
                    pickedSingleDayCalendar = value
                }
            }
            pickedStartRangeCalendar?.let { start ->
                if (DateUtils.isAfter(start, value)) {
                    pickedStartRangeCalendar = null
                    pickedEndRangeCalendar = null
                }
            }
            pickedEndRangeCalendar?.let { end ->
                if (DateUtils.isAfter(end, value)) {
                    pickedEndRangeCalendar = value
                }
            }
            if (!isInternalChange) invalidate()
        }

    fun setMinMaxDateCalendar(minDateCalendar: BaseCalendar?, maxDateCalendar: BaseCalendar?, invalidate: Boolean = true) {
        isInternalChange = true
        this.minDateCalendar = minDateCalendar
        this.maxDateCalendar = maxDateCalendar
        isInternalChange = false
        if (invalidate) invalidate()
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.PrimeMonthView, defStyleAttr, defStyleRes).apply {
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
            recycle()
        }

        monthHeaderHeight = monthLabelTextSize + monthLabelTopPadding + monthLabelBottomPadding
        weekHeaderHeight = weekLabelTextSize + weekLabelTopPadding + weekLabelBottomPadding

        if(context.isDisplayLandscape()){
            rowCount = 3
            columnCount = 14
        }

        minCellHeight = dayLabelTextSize.toFloat()
        cellHeight = 2.75f * dayLabelTextSize
        cellWidth = (viewWidth - (paddingLeft + paddingRight)) / columnCount.toFloat()

        initPaints()

        if (isInEditMode) {
            val calendar = CalendarFactory.newInstance(calendarType)
            setDate(calendar)
        }
    }

    private fun initPaints() {
        monthLabelPaint = Paint().apply {
            textSize = monthLabelTextSize.toFloat()
            color = monthLabelTextColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
            fontTypeface?.apply {
                typeface = this
            }
        }

        weekLabelPaint = Paint().apply {
            textSize = weekLabelTextSize.toFloat()
            color = weekLabelTextColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
            fontTypeface?.apply {
                typeface = this
            }
        }

        dayLabelPaint = Paint().apply {
            textSize = dayLabelTextSize.toFloat()
            color = dayLabelTextColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = false
            fontTypeface?.apply {
                typeface = this
            }
        }

        selectedDayBackgroundPaint = Paint().apply {
            color = selectedDayCircleColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = paddingTop + monthHeaderHeight + weekHeaderHeight + cellHeight * rowCount + paddingBottom
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height.toInt())
        val maxHeight = paddingTop + monthHeaderHeight + weekHeaderHeight + cellHeight * rowCount + paddingBottom
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
        this.calendarType = calendarType
        this.year = year
        this.month = month

        dayOfWeekLabelCalendar = CalendarFactory.newInstance(calendarType)
        firstDayOfMonthCalendar = CalendarFactory.newInstance(calendarType)

        firstDayOfMonthCalendar?.setDate(year, month, 1)
        firstDayOfMonthDayOfWeek = firstDayOfMonthCalendar!!.get(Calendar.DAY_OF_WEEK)

        daysInMonth = DateUtils.getDaysInMonth(calendarType, month, year)
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
        monthAndYearString = when (calendarType) {
            CalendarType.CIVIL -> monthAndYearString
            CalendarType.PERSIAN, CalendarType.HIJRI -> PersianUtils.convertLatinDigitsToPersian(monthAndYearString)
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
                add(when (calendarType) {
                    CalendarType.CIVIL -> (2 * i + 1) * (cellWidth / 2) + paddingLeft
                    CalendarType.PERSIAN, CalendarType.HIJRI -> (2 * (columnCount - 1 - i) + 1) * (cellWidth / 2) + paddingLeft
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
                add(when (calendarType) {
                    CalendarType.CIVIL -> ((2 * i + 1) * (cellWidth / 2) + paddingLeft)
                    CalendarType.PERSIAN, CalendarType.HIJRI -> ((2 * (columnCount - 1 - i) + 1) * (cellWidth / 2) + paddingLeft)
                })
            }
        }

        for (dayOfMonth in 1..daysInMonth) {
            val y = topY + cellHeight / 2
            val x = xPositionList[offset]

            val pickedDayState = MonthViewUtils.findDayState(year, month, dayOfMonth, pickType, pickedSingleDayCalendar, pickedStartRangeCalendar, pickedEndRangeCalendar)
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
                // RTLize for Persian and Hijri Calendars
                when (calendarType) {
                    CalendarType.CIVIL -> if (isStart) {
                        canvas.drawRect(x, y - radius, (x + cellWidth / 2), y + radius, this)
                    } else {
                        canvas.drawRect(x - cellWidth / 2, y - radius, x, y + radius, this)
                    }
                    CalendarType.PERSIAN, CalendarType.HIJRI -> if (isStart) {
                        canvas.drawRect(x - cellWidth / 2, y - radius, x, y + radius, this)
                    } else {
                        canvas.drawRect(x, y - radius, (x + cellWidth / 2), y + radius, this)
                    }
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

        val date = when (calendarType) {
            CalendarType.CIVIL -> String.format(Locale.getDefault(), "%d", dayOfMonth)
            CalendarType.PERSIAN, CalendarType.HIJRI -> PersianUtils.convertLatinDigitsToPersian(String.format(Locale.getDefault(), "%d", dayOfMonth))
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
            when (pickType) {
                PickType.SINGLE -> {
                    pickedSingleDayCalendar = calendar
                    invalidate()
                }
                PickType.START_RANGE -> {
                    if (DateUtils.isAfter(year, month, dayOfMonth, pickedEndRangeCalendar)) {
                        pickedEndRangeCalendar = null
                    }
                    pickedStartRangeCalendar = calendar
                    invalidate()
                }
                PickType.END_RANGE -> {
                    if (!DateUtils.isBefore(year, month, dayOfMonth, pickedStartRangeCalendar)) {
                        pickedEndRangeCalendar = calendar
                        invalidate()
                    }
                }
                PickType.NOTHING -> {
                }
            }
        }

        onDayClickListener?.apply {
            onDayClick(this@PrimeMonthView, calendar)
        }
    }

    private fun findDayByCoordinates(inputX: Float, inputY: Float): Int? {
        if (inputX < paddingLeft || inputX > viewWidth - paddingRight) return null

        val y = inputY - dp(12f)
        val row = ((y - (monthHeaderHeight + weekHeaderHeight)) / cellHeight).toInt()
        var column = ((inputX - paddingLeft) * columnCount / (viewWidth - (paddingLeft + paddingRight))).toInt()

        // RTLize for Persian and Hijri Calendars
        column = when (calendarType) {
            CalendarType.CIVIL -> column
            CalendarType.PERSIAN, CalendarType.HIJRI -> columnCount - 1 - column
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

    interface OnDayClickListener {
        fun onDayClick(view: PrimeMonthView, day: BaseCalendar)
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
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        isInternalChange = true

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

        isInternalChange = false
        setDate(calendarType, year, month)
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
        }

        companion object {

            @Suppress("unused")
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(input: Parcel): SavedState {
                    return SavedState(input)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

}
