package com.aminography.primedatepicker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.Paint.Style
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.StyleRes
import android.support.annotation.StyleableRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.tools.CurrentCalendarType
import com.aminography.primedatepicker.tools.PersianUtils
import com.aminography.primedatepicker.tools.Utils
import org.jetbrains.anko.dip
import java.util.*

/**
 * A calendar-like view displaying a specified month and the appropriate selectable day numbers
 * within the specified month.
 */
class MonthView @JvmOverloads constructor(
        context: Context,
        @StyleableRes attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0,
        @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val defaultRowHeight = dip(36)
    private var minRowHeight: Int = 0

    @ColorInt
    private var monthLabelTextColor: Int = 0
    @ColorInt
    private var weekLabelTextColor: Int = 0
    @ColorInt
    private var dayLabelTextColor: Int = 0
    @ColorInt
    private var todayLabelTextColor: Int = 0
    @ColorInt
    private var selectedDayLabelTextColor: Int = 0
    @ColorInt
    private var selectedDayCircleColor: Int = 0
    @ColorInt
    private var disabledDayLabelTextColor: Int = 0

    private var monthLabelTextSize: Int = 0
    private var weekLabelTextSize: Int = 0
    private var dayLabelTextSize: Int = 0
    private var sidePadding: Int = 0
    private var monthLabelTopPadding: Int = 0
    private var monthLabelBottomPadding: Int = 0
    private var weekLabelTopPadding: Int = 0
    private var weekLabelBottomPadding: Int = 0

    private val dayOfWeekLabelCalendar = CalendarFactory.newInstance(CurrentCalendarType.type)
    private val firstDayOfMonthCalendar = CalendarFactory.newInstance(CurrentCalendarType.type)
    private var datePickerController: DatePickerController? = null
    private var onDayClickListener: OnDayClickListener? = null

    private var monthLabelPaint: Paint? = null
    private var weekLabelPaint: Paint? = null
    private var dayLabelPaint: Paint? = null
    private var selectedDayCirclePaint: Paint? = null

    private var monthHeaderHeight = 0
    private var weekHeaderHeight = 0

    private var month = 0
    private var year = 0

    private var viewWidth = 0
    // The height this view should draw at in pixels, set by height param
    private var rowHeight = defaultRowHeight
    //        set(value) {
//            field = if (value < minRowHeight) minRowHeight else value
//        }
    // If this view contains the today
    private var hasToday = false
    // Which day is selected [0-6] or -1 if no day is selected
    private var selectedDay = -1
    // Which day is today [0-6] or -1 if no day is today
    private var todayDayOfMonth = DEFAULT_SELECTED_DAY
    // Which day of the week to start on [0-6]
    private var weekStart = DEFAULT_WEEK_START
    // The number of days + a spot for week number if it is displayed
    private var daysInMonth = 7

    private var firstDayOfMonthDayOfWeek = 0
    private var numRows = DEFAULT_NUM_ROWS

    init {
        context.obtainStyledAttributes(attrs, R.styleable.MonthView, defStyleAttr, defStyleRes).apply {
            monthLabelTextColor = getColor(R.styleable.MonthView_monthLabelTextColor, ContextCompat.getColor(context, R.color.defaultMonthLabelTextColor))
            weekLabelTextColor = getColor(R.styleable.MonthView_weekLabelTextColor, ContextCompat.getColor(context, R.color.defaultWeekLabelTextColor))
            dayLabelTextColor = getColor(R.styleable.MonthView_dayLabelTextColor, ContextCompat.getColor(context, R.color.defaultDayLabelTextColor))
            todayLabelTextColor = getColor(R.styleable.MonthView_todayLabelTextColor, ContextCompat.getColor(context, R.color.defaultTodayLabelTextColor))
            selectedDayLabelTextColor = getColor(R.styleable.MonthView_selectedDayLabelTextColor, ContextCompat.getColor(context, R.color.defaultSelectedDayLabelTextColor))
            selectedDayCircleColor = getColor(R.styleable.MonthView_selectedDayCircleColor, ContextCompat.getColor(context, R.color.defaultSelectedDayCircleColor))
            disabledDayLabelTextColor = getColor(R.styleable.MonthView_disabledDayLabelTextColor, ContextCompat.getColor(context, R.color.defaultDisabledDayLabelTextColor))

            monthLabelTextSize = getDimensionPixelSize(R.styleable.MonthView_monthLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultMonthLabelTextSize))
            weekLabelTextSize = getDimensionPixelSize(R.styleable.MonthView_weekLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultWeekLabelTextSize))
            dayLabelTextSize = getDimensionPixelSize(R.styleable.MonthView_dayLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultDayLabelTextSize))

            sidePadding = getDimensionPixelSize(R.styleable.MonthView_sidePadding, resources.getDimensionPixelSize(R.dimen.defaultSidePadding))
            monthLabelTopPadding = getDimensionPixelSize(R.styleable.MonthView_monthLabelTopPadding, resources.getDimensionPixelSize(R.dimen.defaultMonthLabelTopPadding))
            monthLabelBottomPadding = getDimensionPixelSize(R.styleable.MonthView_monthLabelBottomPadding, resources.getDimensionPixelSize(R.dimen.defaultMonthLabelBottomPadding))
            weekLabelTopPadding = getDimensionPixelSize(R.styleable.MonthView_weekLabelTopPadding, resources.getDimensionPixelSize(R.dimen.defaultWeekLabelTopPadding))
            weekLabelBottomPadding = getDimensionPixelSize(R.styleable.MonthView_weekLabelBottomPadding, resources.getDimensionPixelSize(R.dimen.defaultWeekLabelBottomPadding))
            recycle()
        }

        monthHeaderHeight = monthLabelTextSize + monthLabelTopPadding + monthLabelBottomPadding
        weekHeaderHeight = weekLabelTextSize + weekLabelTopPadding + weekLabelBottomPadding

        minRowHeight = dayLabelTextSize

//        post {
//            val viewHeight = measuredHeight
//            rowHeight = (viewHeight - (monthHeaderHeight + weekHeaderHeight)) / MAX_NUM_ROWS
//            requestLayout()
//        }
        rowHeight = (2.75 * dayLabelTextSize).toInt()

        monthLabelPaint = Paint().apply {
            textSize = monthLabelTextSize.toFloat()
            color = monthLabelTextColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
//            typeface = Typeface.create(TypefaceHelper[context, controller!!.typeface], Typeface.BOLD)
        }

        weekLabelPaint = Paint().apply {
            textSize = weekLabelTextSize.toFloat()
            color = weekLabelTextColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
//            typeface = TypefaceHelper[context, controller!!.typeface]
        }

        dayLabelPaint = Paint().apply {
            textSize = dayLabelTextSize.toFloat()
            color = dayLabelTextColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = false
//            typeface = TypefaceHelper[context, controller!!.typeface]
        }

        selectedDayCirclePaint = Paint().apply {
            color = selectedDayCircleColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
//            typeface = TypefaceHelper[context, controller!!.typeface]
        }
    }

    fun setDatePickerController(controller: DatePickerController) {
        datePickerController = controller
    }

    fun setOnDayClickListener(listener: OnDayClickListener) {
        onDayClickListener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                val day = getDayFromLocation(event.x, event.y)
                if (day >= 0) {
                    onDayClick(day)
                }
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        drawMonthTitle(canvas)
        drawWeekLabels(canvas)
        drawDays(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
                View.MeasureSpec.getSize(widthMeasureSpec),
                rowHeight * numRows + monthHeaderHeight + weekHeaderHeight
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        viewWidth = w
    }

    fun setMonthParams(params: HashMap<String, Int>) {
        if (!params.containsKey(VIEW_PARAMS_MONTH) && !params.containsKey(VIEW_PARAMS_YEAR)) {
            throw Exception("You must specify month and year for this view")
        }
        tag = params
        // We keep the current value for any params not present
        if (params.containsKey(VIEW_PARAMS_HEIGHT)) {
            rowHeight = params[VIEW_PARAMS_HEIGHT]!!
            if (rowHeight < minRowHeight) {
                rowHeight = minRowHeight
            }
        }
        if (params.containsKey(VIEW_PARAMS_SELECTED_DAY)) {
            selectedDay = params[VIEW_PARAMS_SELECTED_DAY]!!
        }

        // Allocate space for caching the day numbers and focus values
        month = params[VIEW_PARAMS_MONTH]!!
        year = params[VIEW_PARAMS_YEAR]!!

        val todayCalendar = CalendarFactory.newInstance(CurrentCalendarType.type)
        hasToday = false
        todayDayOfMonth = -1

        firstDayOfMonthCalendar.setDate(year, month, 1)
        firstDayOfMonthDayOfWeek = firstDayOfMonthCalendar.get(Calendar.DAY_OF_WEEK)

        weekStart = when (CurrentCalendarType.type) {
            CalendarType.CIVIL -> Calendar.SUNDAY
            CalendarType.PERSIAN -> Calendar.SATURDAY
            CalendarType.HIJRI -> Calendar.SATURDAY
        }

        daysInMonth = Utils.getDaysInMonth(month, year)
        for (i in 0 until daysInMonth) {
            val day = i + 1
            if (sameDay(day, todayCalendar)) {
                hasToday = true
                todayDayOfMonth = day
            }
        }
        numRows = calculateNumRows()
    }

    fun reuse() {
        numRows = DEFAULT_NUM_ROWS
        requestLayout()
    }

    private fun calculateNumRows(): Int {
        val offset = findFirstDayOfMonthOffsetInWeek()
        val dividend = (offset + daysInMonth) / 7
        val remainder = (offset + daysInMonth) % 7
        return dividend + if (remainder > 0) 1 else 0
    }

    private fun sameDay(day: Int, today: BaseCalendar): Boolean {
        return year == today.year && month == today.month && day == today.dayOfMonth
    }

    private fun drawMonthTitle(canvas: Canvas) {
        val x = viewWidth / 2
        val y = (monthHeaderHeight - monthLabelTopPadding - monthLabelBottomPadding) / 2 + monthLabelTopPadding

        var monthAndYearString = "${firstDayOfMonthCalendar.monthName} ${firstDayOfMonthCalendar.year}"
        monthAndYearString = when (CurrentCalendarType.type) {
            CalendarType.CIVIL -> monthAndYearString
            CalendarType.PERSIAN -> PersianUtils.convertLatinDigitsToPersian(monthAndYearString)
            CalendarType.HIJRI -> PersianUtils.convertLatinDigitsToPersian(monthAndYearString)
        }

        monthLabelPaint?.apply {
            canvas.drawText(
                    monthAndYearString,
                    x.toFloat(),
                    y.toFloat() - ((descent() + ascent()) / 2),
                    this
            )
        }

        if (SHOW_GUIDES) {
            Paint().apply {
                isAntiAlias = true
                color = Color.RED
                style = Style.FILL
                alpha = 50
                canvas.drawRect(
                        sidePadding.toFloat(),
                        0f,
                        (viewWidth - sidePadding).toFloat(),
                        monthHeaderHeight.toFloat(),
                        this
                )
            }
            Paint().apply {
                isAntiAlias = true
                color = Color.RED
                style = Style.FILL
                canvas.drawCircle(
                        x.toFloat(),
                        (monthHeaderHeight / 2).toFloat(),
                        dip(1).toFloat(),
                        this
                )
            }
        }
    }

    private fun drawWeekLabels(canvas: Canvas) {
        val y = monthHeaderHeight + (weekHeaderHeight - weekLabelTopPadding - weekLabelBottomPadding) / 2 + weekLabelTopPadding
        val dayWidth = (viewWidth - sidePadding * 2) / 7
        val dayWidthHalf = dayWidth / 2

        for (i in 0 until 7) {
            val dayOfWeek = (i + weekStart) % 7

            // RTLize for Persian and Hijri Calendars
            val x = when (CurrentCalendarType.type) {
                CalendarType.CIVIL -> (2 * i + 1) * dayWidthHalf + sidePadding
                CalendarType.PERSIAN -> (2 * (6 - i) + 1) * dayWidthHalf + sidePadding
                CalendarType.HIJRI -> (2 * (6 - i) + 1) * dayWidthHalf + sidePadding
            }

            dayOfWeekLabelCalendar.set(Calendar.DAY_OF_WEEK, dayOfWeek)
            val localWeekDisplayName = dayOfWeekLabelCalendar.weekDayName

            val weekString = when (CurrentCalendarType.type) {
                CalendarType.CIVIL -> localWeekDisplayName.substring(0, 2)
                CalendarType.PERSIAN -> localWeekDisplayName.substring(0, 1)
                CalendarType.HIJRI -> localWeekDisplayName.substring(2, 4)
            }

            weekLabelPaint?.apply {
                canvas.drawText(
                        weekString,
                        x.toFloat(),
                        y.toFloat() - ((descent() + ascent()) / 2),
                        this
                )
            }

            if (SHOW_GUIDES) {
                Paint().apply {
                    isAntiAlias = true
                    color = Color.GRAY
                    style = Style.STROKE
                    canvas.drawRect(
                            (x - dayWidthHalf).toFloat(),
                            monthHeaderHeight.toFloat(),
                            (x + dayWidthHalf).toFloat(),
                            (monthHeaderHeight + weekHeaderHeight).toFloat(),
                            this
                    )
                }
                Paint().apply {
                    isAntiAlias = true
                    color = Color.RED
                    style = Style.FILL
                    canvas.drawCircle(
                            x.toFloat(),
                            (monthHeaderHeight + weekHeaderHeight / 2).toFloat(),
                            dip(1).toFloat(),
                            this
                    )
                }
            }
        }

        if (SHOW_GUIDES) {
            Paint().apply {
                isAntiAlias = true
                color = Color.GREEN
                style = Style.FILL
                alpha = 50
                canvas.drawRect(
                        sidePadding.toFloat(),
                        monthHeaderHeight.toFloat(),
                        viewWidth.toFloat() - sidePadding.toFloat(),
                        (monthHeaderHeight + weekHeaderHeight).toFloat(),
                        this
                )
            }
        }
    }

    private fun drawDays(canvas: Canvas) {
        var topY = monthHeaderHeight + weekHeaderHeight
        val dayWidth = (viewWidth - sidePadding * 2) / 7
        val dayWidthHalf = dayWidth / 2

        var offset = findFirstDayOfMonthOffsetInWeek()
        for (dayNumber in 1..daysInMonth) {

            // RTLize for Persian and Hijri Calendars
            val x = when (CurrentCalendarType.type) {
                CalendarType.CIVIL -> ((2 * offset + 1) * dayWidthHalf + sidePadding)
                CalendarType.PERSIAN -> ((2 * (6 - offset) + 1) * dayWidthHalf + sidePadding)
                CalendarType.HIJRI -> ((2 * (6 - offset) + 1) * dayWidthHalf + sidePadding)
            }

            val y = topY + rowHeight / 2

            drawMonthDay(canvas, year, month, dayNumber, x, y, dayWidth, rowHeight)

            if (SHOW_GUIDES) {
                Paint().apply {
                    isAntiAlias = true
                    color = Color.GRAY
                    style = Style.STROKE
                    canvas.drawRect(
                            (x - dayWidthHalf).toFloat(),
                            topY.toFloat(),
                            (x + dayWidthHalf).toFloat(),
                            (topY + rowHeight).toFloat(),
                            this
                    )
                }
                Paint().apply {
                    isAntiAlias = true
                    color = Color.RED
                    style = Style.FILL
                    canvas.drawCircle(
                            x.toFloat(),
                            y.toFloat(),
                            dip(1).toFloat(),
                            this
                    )
                }
            }

            offset++
            if (offset == 7) {
                offset = 0
                topY += rowHeight
            }
        }
    }

    /**
     * This method should draw the month day.  Implemented by sub-classes to allow customization.
     *
     * @param canvas The canvas to draw on
     * @param year   The year of this month day
     * @param month  The month of this month day
     * @param day    The day number of this month day
     * @param x      The default x position to draw the day number
     * @param y      The default y position to draw the day number
     * @param width  The width of the day number rect
     * @param height The height of the day number rect
     */
    private fun drawMonthDay(canvas: Canvas, year: Int, month: Int, day: Int, x: Int, y: Int, width: Int, height: Int) {
        if (selectedDay == day) {
            canvas.drawCircle(
                    x.toFloat(),
                    y.toFloat(),
                    Math.min(width, height).toFloat() / 2,
                    selectedDayCirclePaint!!
            )
        }

        dayLabelPaint?.apply {
            color = if (isOutOfRange(year, month, day)) {
                disabledDayLabelTextColor
            } else if (selectedDay == day) {
                selectedDayLabelTextColor
            } else if (hasToday && todayDayOfMonth == day) {
                todayLabelTextColor
            } else {
                dayLabelTextColor
            }

//            typeface = typefaceNormal
            textSize = dayLabelTextSize.toFloat()
        }

        val date = when (CurrentCalendarType.type) {
            CalendarType.CIVIL -> String.format(Locale.getDefault(), "%d", day)
            CalendarType.PERSIAN -> PersianUtils.convertLatinDigitsToPersian(String.format(Locale.getDefault(), "%d", day))
            CalendarType.HIJRI -> PersianUtils.convertLatinDigitsToPersian(String.format(Locale.getDefault(), "%d", day))
        }

        dayLabelPaint?.apply {
            canvas.drawText(
                    date,
                    x.toFloat(),
                    y.toFloat() - ((descent() + ascent()) / 2),
                    this
            )
        }

    }

    private fun findFirstDayOfMonthOffsetInWeek(): Int {
        val firstDay = if (firstDayOfMonthDayOfWeek < weekStart)
            firstDayOfMonthDayOfWeek + 7
        else firstDayOfMonthDayOfWeek
        return (firstDay - weekStart) % 7
    }


    /**
     * Calculates the day that the given x position is in, accounting for week
     * number. Returns the day or -1 if the position wasn't in a day.
     *
     * @param x The x position of the touch event
     * @return The day number, or -1 if the position wasn't in a day
     */
    private fun getDayFromLocation(x: Float, y: Float): Int {
        val day = getInternalDayFromLocation(x, y)
        return if (day < 1 || day > daysInMonth) {
            -1
        } else day
    }

    /**
     * Calculates the day that the given x position is in, accounting for week
     * number.
     *
     * @param inputX The x position of the touch event
     * @param inputY The y position of the touch event
     * @return The day number
     */
    private fun getInternalDayFromLocation(inputX: Float, inputY: Float): Int {
        val y = inputY - dip(12)
        val dayStart = sidePadding
        if (inputX < dayStart || inputX > viewWidth - sidePadding) {
            return -1
        }
        val row = ((y - (monthHeaderHeight + weekHeaderHeight)) / rowHeight).toInt()
        var column = ((inputX - dayStart) * 7 / (viewWidth - dayStart - sidePadding)).toInt()

        // RTLize for Persian and Hijri Calendars
        column = when (CurrentCalendarType.type) {
            CalendarType.CIVIL -> column
            CalendarType.PERSIAN -> 6 - column
            CalendarType.HIJRI -> 6 - column
        }

        var day = column - findFirstDayOfMonthOffsetInWeek() + 1
        day += row * 7
        return day
    }

    /**
     * Called when the user clicks on a day. Handles callbacks to the
     * [OnDayClickListener] if one is set.
     *
     *
     * If the day is out of the range set by minDate and/or maxDate, this is a no-op.
     *
     * @param day The day that was clicked
     */
    private fun onDayClick(day: Int) {
        // If the min / max date are set, only process the click if it's a valid selection.
        if (isOutOfRange(year, month, day)) {
            return
        }

        onDayClickListener?.apply {
            val calendar = Utils.newCalendar()
            calendar.setDate(year, month, day)
            onDayClick(this@MonthView, calendar)
        }
    }

    /**
     * @return true if the specified year/month/day are within the selectable days or the range set by minDate and maxDate.
     * If one or either have not been set, they are considered as Integer.MIN_VALUE and
     * Integer.MAX_VALUE.
     */
    private fun isOutOfRange(year: Int, month: Int, day: Int): Boolean {
        if (datePickerController?.selectableDays != null) {
            return !isSelectable(year, month, day)
        }

        return if (isBeforeMin(year, month, day)) {
            true
        } else {
            isAfterMax(year, month, day)
        }
    }

    private fun isSelectable(year: Int, month: Int, day: Int): Boolean {
        datePickerController?.selectableDays?.apply {
            for (c in this) {
                if (year < c.year) {
                    break
                }
                if (year > c.year) {
                    continue
                }
                if (month < c.month) {
                    break
                }
                if (month > c.month) {
                    continue
                }
                if (day < c.dayOfMonth) {
                    break
                }
                if (day > c.dayOfMonth) {
                    continue
                }
                return true
            }
        }
        return false
    }

    private fun isBeforeMin(year: Int, month: Int, dayOfMonth: Int): Boolean {
        val func = "isBeforeMin > "

        if (datePickerController == null) {
            Log.d(TAG, "$func #> false + controller == null")
            return false
        }
        val minDate = datePickerController?.minDate
        val day = func + "input: " + year + "-" + month + "-" + dayOfMonth + " *** minDate: " + minDate?.year + "-" + minDate?.month + "-" + minDate?.dayOfMonth + " "

        if (minDate == null) {
            Log.d(TAG, "$day #> false + minDate == null")
            return false
        }

        if (year < minDate.year) {
            Log.d(TAG, "$day #> true + year < minDate.getyear()")
            return true
        } else if (year > minDate.year) {
            Log.d(TAG, "$day #> false + year > minDate.getyear()")
            return false
        }

        if (month < minDate.month) {
            Log.d(TAG, "$day #> true + month < minDate.getmonth()")
            return true
        } else if (month > minDate.month) {
            Log.d(TAG, "$day #> false + month > minDate.getmonth()")
            return false
        }

        Log.d(TAG, "$day #> ... + day < minDate.getdayOfMonth()")
        return day < "${minDate.dayOfMonth}"
    }

    private fun isAfterMax(year: Int, month: Int, dayOfMonth: Int): Boolean {
        val func = "isAfterMax > "

        if (datePickerController == null) {
            Log.d(TAG, "$func #> false + controller == null")
            return false
        }
        val maxDate = datePickerController?.maxDate
        val day = func + "input: " + year + "-" + month + "-" + dayOfMonth + " *** maxDate: " + maxDate?.year + "-" + maxDate?.month + "-" + maxDate?.dayOfMonth + " "

        if (maxDate == null) {
            Log.d(TAG, "$day #> false + maxDate == null")
            return false
        }

        if (year > maxDate.year) {
            Log.d(TAG, "$day #> true + year > maxDate.getyear()")
            return true
        } else if (year < maxDate.year) {
            Log.d(TAG, "$day #> false + year < maxDate.getyear()")
            return false
        }

        if (month > maxDate.month) {
            Log.d(TAG, "$day #> true + month > maxDate.getmonth()")
            return true
        } else if (month < maxDate.month) {
            Log.d(TAG, "$day #> false + month < maxDate.getmonth()")
            return false
        }
        Log.d(TAG, "$day #> ... +  day > maxDate.getdayOfMonth()")
        return day < "${maxDate.dayOfMonth}"
    }

    /**
     * Handles callbacks when the user clicks on a time object.
     */
    interface OnDayClickListener {
        fun onDayClick(view: MonthView, day: BaseCalendar)
    }

    companion object {
        /**
         * This sets the height of this week in pixels
         */
        const val VIEW_PARAMS_HEIGHT = "height"
        /**
         * This specifies the position (or weeks since the epoch) of this week.
         */
        const val VIEW_PARAMS_MONTH = "month"
        /**
         * This specifies the position (or weeks since the epoch) of this week.
         */
        const val VIEW_PARAMS_YEAR = "year"
        /**
         * This sets one of the days in this view as selected [Calendar.SUNDAY]
         * through [Calendar.SATURDAY].
         */
        const val VIEW_PARAMS_SELECTED_DAY = "selected_day"

        private val TAG = MonthView::class.java.canonicalName
        private const val SHOW_GUIDES = true

        private const val DEFAULT_SELECTED_DAY = -1
        private const val DEFAULT_WEEK_START = Calendar.SATURDAY
        private const val DEFAULT_NUM_ROWS = 6
        private const val MAX_NUM_ROWS = 6

    }

}
