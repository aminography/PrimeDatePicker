package com.aminography.primedatepicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.Paint.Style
import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.tools.CurrentCalendarType
import com.aminography.primedatepicker.tools.PersianUtils
import com.aminography.primedatepicker.tools.TypefaceHelper
import com.aminography.primedatepicker.tools.Utils
import java.security.InvalidParameterException
import java.util.*

/**
 * A calendar-like view displaying a specified month and the appropriate selectable day numbers
 * within the specified month.
 */
abstract class BaseMonthView @JvmOverloads constructor(
        context: Context,
        attr: AttributeSet? = null,
        private var controller: DatePickerController? = null,
        @ColorInt var mainColor: Int? = null
) : View(context, attr) {

    private val mDayLabelCalendar: BaseCalendar
    private val mStringBuilder: StringBuilder
    private val baseCalendar: BaseCalendar

    // affects the padding on the sides of this view
    private var mEdgePadding = 0
    protected var monthNumPaint: Paint? = null
    private var monthTitlePaint: Paint? = null
    protected var selectedCirclePaint: Paint? = null
    private var monthDayLabelPaint: Paint? = null

    // The Julian day of the first day displayed by this item
    protected var mFirstJulianDay = -1
    // The month of the first day in this week
    protected var mFirstMonth = -1
    // The month of the last day in this week
    protected var mLastMonth = -1

    var month: Int = 0
        protected set
    var year: Int = 0
        protected set

    // Quick reference to the width of this view, matches parent
    private var mWidth: Int = 0
    // The height this view should draw at in pixels, set by height param
    private var mRowHeight = DEFAULT_HEIGHT
    // If this view contains the today
    protected var mHasToday = false
    // Which day is selected [0-6] or -1 if no day is selected
    protected var mSelectedDay = -1
    // Which day is today [0-6] or -1 if no day is today
    protected var mToday = DEFAULT_SELECTED_DAY
    // Which day of the week to start on [0-6]
    private var mWeekStart = DEFAULT_WEEK_START
    // How many days to display
    private var mNumDays = DEFAULT_NUM_DAYS
    // The number of days + a spot for week number if it is displayed
    private var mNumCells = mNumDays

    // The left edge of the selected day
    protected var mSelectedLeft = -1
    // The right edge of the selected day
    protected var mSelectedRight = -1

    private var mNumRows = DEFAULT_NUM_ROWS

    // Optional listener for handling day click actions
    private var mOnDayClickListener: OnDayClickListener? = null
    protected var mDayTextColor: Int = 0
    protected var mSelectedDayTextColor: Int = 0
    private var mSelectedDayColor: Int = 0
    private var mMonthDayTextColor: Int = 0
    protected var mTodayNumberColor: Int = 0
    protected var mHighlightedDayTextColor: Int = 0
    protected var mDisabledDayTextColor: Int = 0
    private var mMonthTitleColor: Int = 0
    private var mDayOfWeekStart = 0

    /**
     * A wrapper to the MonthHeaderSize to allow override it in children
     */
    private val monthHeaderSize: Int
        get() = MONTH_HEADER_SIZE

    private val monthAndYearString: String
        get() {
            mStringBuilder.setLength(0)
            return when (CurrentCalendarType.type) {
                CalendarType.CIVIL -> baseCalendar.monthName + " " + baseCalendar.year
                CalendarType.PERSIAN -> PersianUtils.convertLatinDigitsToPersian(baseCalendar.monthName + " " + baseCalendar.year)
                CalendarType.HIJRI -> PersianUtils.convertLatinDigitsToPersian(baseCalendar.monthName + " " + baseCalendar.year)
            }
        }

    init {
        val res = context.resources

        mEdgePadding = pxFromDp(context, 24f).toInt()
        mDayLabelCalendar = CalendarFactory.newInstance(CurrentCalendarType.type)
        baseCalendar = CalendarFactory.newInstance(CurrentCalendarType.type)

        mDayTextColor = res.getColor(R.color.mdtp_date_picker_text_normal)
        mMonthDayTextColor = mainColor ?: res.getColor(R.color.mdtp_date_picker_month_day)
        mDisabledDayTextColor = res.getColor(R.color.mdtp_date_picker_text_disabled)
        mHighlightedDayTextColor = res.getColor(R.color.mdtp_date_picker_text_highlighted)

        mSelectedDayTextColor = res.getColor(R.color.white)
        mSelectedDayColor = mainColor ?: res.getColor(R.color.mdtp_date_selected_color)
        mTodayNumberColor = res.getColor(R.color.mdtp_date_today_color)
        mMonthTitleColor = res.getColor(R.color.white)

        mStringBuilder = StringBuilder(50)

        MINI_DAY_NUMBER_TEXT_SIZE = res.getDimensionPixelSize(R.dimen.mdtp_day_number_size)
        LARGE_DAY_NUMBER_TEXT_SIZE = res.getDimensionPixelSize(R.dimen.mdtp_day_number_size_large)
        MONTH_LABEL_TEXT_SIZE = res.getDimensionPixelSize(R.dimen.mdtp_month_label_size)
        MONTH_DAY_LABEL_TEXT_SIZE = res.getDimensionPixelSize(R.dimen.mdtp_month_day_label_text_size)
        MONTH_HEADER_SIZE = res.getDimensionPixelOffset(R.dimen.mdtp_month_list_item_header_height)
        DAY_SELECTED_CIRCLE_SIZE = res.getDimensionPixelSize(R.dimen.mdtp_day_number_select_circle_radius)

        mRowHeight = (res.getDimensionPixelOffset(R.dimen.date_picker_view_animator_height) - monthHeaderSize) / MAX_NUM_ROWS

        // Sets up any standard paints that will be used
        initView()
    }

    fun setDatePickerController(controller: DatePickerController) {
        this.controller = controller
    }

    fun setOnDayClickListener(listener: OnDayClickListener) {
        mOnDayClickListener = listener
    }

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

    /**
     * Sets up the text and style properties for painting. Override this if you
     * want to use a different paint.
     */
    private fun initView() {
        monthTitlePaint = Paint()
        monthTitlePaint?.isFakeBoldText = true
        monthTitlePaint?.isAntiAlias = true
        monthTitlePaint?.textSize = MONTH_LABEL_TEXT_SIZE.toFloat()
        val typeface = Typeface.create(TypefaceHelper[context, controller!!.typeface], Typeface.BOLD)
        monthTitlePaint?.typeface = typeface
        monthTitlePaint?.color = mDayTextColor
        monthTitlePaint?.textAlign = Align.CENTER
        monthTitlePaint?.style = Style.FILL

        selectedCirclePaint = Paint()
        selectedCirclePaint?.isFakeBoldText = true
        selectedCirclePaint?.isAntiAlias = true
        selectedCirclePaint?.color = mSelectedDayColor
        selectedCirclePaint?.textAlign = Align.CENTER
        selectedCirclePaint?.style = Style.FILL
        selectedCirclePaint?.typeface = TypefaceHelper[context, controller!!.typeface]
        selectedCirclePaint?.alpha = SELECTED_CIRCLE_ALPHA

        monthDayLabelPaint = Paint()
        monthDayLabelPaint?.isAntiAlias = true
        monthDayLabelPaint?.textSize = MONTH_DAY_LABEL_TEXT_SIZE.toFloat()
        monthDayLabelPaint?.color = mMonthDayTextColor
        monthDayLabelPaint?.typeface = TypefaceHelper[context, controller!!.typeface]
        monthDayLabelPaint?.style = Style.FILL
        monthDayLabelPaint?.textAlign = Align.CENTER
        monthDayLabelPaint?.isFakeBoldText = true

        monthNumPaint = Paint()
        monthNumPaint?.isAntiAlias = true
        monthNumPaint?.textSize = MINI_DAY_NUMBER_TEXT_SIZE.toFloat()
        monthNumPaint?.style = Style.FILL
        monthNumPaint?.textAlign = Align.CENTER
        monthNumPaint?.typeface = TypefaceHelper[context, controller!!.typeface]
        monthNumPaint?.isFakeBoldText = false
    }

    override fun onDraw(canvas: Canvas) {
        drawMonthTitle(canvas)
        drawMonthDayLabels(canvas)
        drawMonthNums(canvas)
    }

    /**
     * Sets all the parameters for displaying this week. The only required
     * parameter is the week number. Other parameters have a default value and
     * will only update if a new value is included, except for focus month,
     * which will always default to no focus month if no value is passed in. See
     * [.VIEW_PARAMS_HEIGHT] for more info on parameters.
     *
     * @param params A map of the new parameters, see
     * [.VIEW_PARAMS_HEIGHT]
     */
    fun setMonthParams(params: HashMap<String, Int>) {
        if (!params.containsKey(VIEW_PARAMS_MONTH) && !params.containsKey(VIEW_PARAMS_YEAR)) {
            throw InvalidParameterException("You must specify month and year for this view")
        }
        tag = params
        // We keep the current value for any params not present
        if (params.containsKey(VIEW_PARAMS_HEIGHT)) {
            mRowHeight = params[VIEW_PARAMS_HEIGHT]!!
            if (mRowHeight < MIN_HEIGHT) {
                mRowHeight = MIN_HEIGHT
            }
        }
        if (params.containsKey(VIEW_PARAMS_SELECTED_DAY)) {
            mSelectedDay = params[VIEW_PARAMS_SELECTED_DAY]!!
        }

        // Allocate space for caching the day numbers and focus values
        month = params[VIEW_PARAMS_MONTH]!!
        year = params[VIEW_PARAMS_YEAR]!!

        // Figure out what day today is
        //final Time today = new Time(Time.getCurrentTimezone());
        //today.setToNow();
        val today = CalendarFactory.newInstance(CurrentCalendarType.type)
        mHasToday = false
        mToday = -1

        baseCalendar.setDate(year, month, 1)
        mDayOfWeekStart = baseCalendar.get(Calendar.DAY_OF_WEEK)

        mWeekStart = if (params.containsKey(VIEW_PARAMS_WEEK_START)) {
            params[VIEW_PARAMS_WEEK_START]!!
        } else {
            Calendar.SATURDAY
        }

        mNumCells = Utils.getDaysInMonth(month, year)
        for (i in 0 until mNumCells) {
            val day = i + 1
            if (sameDay(day, today)) {
                mHasToday = true
                mToday = day
            }
        }
        mNumRows = calculateNumRows()
    }

    fun setSelectedDay(day: Int) {
        mSelectedDay = day
    }

    fun reuse() {
        mNumRows = DEFAULT_NUM_ROWS
        requestLayout()
    }

    private fun calculateNumRows(): Int {
        val offset = findDayOffset()
        val dividend = (offset + mNumCells) / mNumDays
        val remainder = (offset + mNumCells) % mNumDays
        return dividend + if (remainder > 0) 1 else 0
    }

    private fun sameDay(day: Int, today: BaseCalendar): Boolean {
        return year == today.year &&
                month == today.month &&
                day == today.dayOfMonth
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), mRowHeight * mNumRows
                + monthHeaderSize + 5)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w
    }

    private fun drawMonthTitle(canvas: Canvas) {
        val x = mWidth /*+ 2 * mEdgePadding*/ / 2
        val y = (monthHeaderSize - MONTH_DAY_LABEL_TEXT_SIZE) / 2 - mEdgePadding / 3
        canvas.drawText(monthAndYearString, x.toFloat(), y.toFloat(), monthTitlePaint!!)
    }

    private fun drawMonthDayLabels(canvas: Canvas) {
        val y = (monthHeaderSize.toDouble() - (MONTH_DAY_LABEL_TEXT_SIZE / 2).toDouble() - mEdgePadding / 1.5).toInt()
        val dayWidthHalf = (mWidth - mEdgePadding * 2) / (mNumDays * 2)

        for (i in 0 until mNumDays) {
            val calendarDay = (i + mWeekStart) % mNumDays

            // TODO: 7/8/2018 Amin
            val x = when (CurrentCalendarType.type) {
                CalendarType.CIVIL -> (2 * i + 1) * dayWidthHalf + mEdgePadding
                CalendarType.PERSIAN -> (2 * (6 - i) + 1) * dayWidthHalf + mEdgePadding
                CalendarType.HIJRI -> (2 * (6 - i) + 1) * dayWidthHalf + mEdgePadding
            }

            mDayLabelCalendar.set(Calendar.DAY_OF_WEEK, calendarDay)
            val localWeekDisplayName = mDayLabelCalendar.weekDayName // TODO: RTLize

            val weekString = when (CurrentCalendarType.type) {
                CalendarType.CIVIL -> localWeekDisplayName.substring(0, 2)
                CalendarType.PERSIAN -> localWeekDisplayName.substring(0, 1)
                CalendarType.HIJRI -> localWeekDisplayName.substring(2, 4)
            }

            canvas.drawText(weekString, x.toFloat(), y.toFloat(), monthDayLabelPaint!!)
        }
    }

    /**
     * Draws the week and month day numbers for this week. Override this method
     * if you need different placement.
     *
     * @param canvas The canvas to draw on
     */
    private fun drawMonthNums(canvas: Canvas) {
        var y = (mRowHeight + MINI_DAY_NUMBER_TEXT_SIZE) / 2 - DAY_SEPARATOR_WIDTH + monthHeaderSize - mEdgePadding / 2
        val dayWidthHalf = (mWidth - mEdgePadding * 2) / (mNumDays * 2.0f)
        var j = findDayOffset()
        for (dayNumber in 1..mNumCells) {

            // TODO: 7/8/2018 Amin
            val x = when (CurrentCalendarType.type) {
                CalendarType.CIVIL -> ((2 * j + 1) * dayWidthHalf + mEdgePadding).toInt()
                CalendarType.PERSIAN -> ((2 * (6 - j) + 1) * dayWidthHalf + mEdgePadding).toInt()
                CalendarType.HIJRI -> ((2 * (6 - j) + 1) * dayWidthHalf + mEdgePadding).toInt()
            }

            val yRelativeToDay = (mRowHeight + MINI_DAY_NUMBER_TEXT_SIZE) / 2 - DAY_SEPARATOR_WIDTH

            val startX = (x - dayWidthHalf).toInt()
            val stopX = (x + dayWidthHalf).toInt()
            val startY = y - yRelativeToDay
            val stopY = startY + mRowHeight

            drawMonthDay(canvas, year, month, dayNumber, x, y, startX, stopX, startY, stopY)

            j++
            if (j == mNumDays) {
                j = 0
                y += mRowHeight
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
     * @param startX The left boundary of the day number rect
     * @param stopX  The right boundary of the day number rect
     * @param startY The top boundary of the day number rect
     * @param stopY  The bottom boundary of the day number rect
     */
    abstract fun drawMonthDay(canvas: Canvas, year: Int, month: Int, day: Int, x: Int, y: Int, startX: Int, stopX: Int, startY: Int, stopY: Int)

    protected fun findDayOffset(): Int {
        return ((if (mDayOfWeekStart < mWeekStart) mDayOfWeekStart + mNumDays else mDayOfWeekStart) - mWeekStart) % 7
    }


    /**
     * Calculates the day that the given x position is in, accounting for week
     * number. Returns the day or -1 if the position wasn't in a day.
     *
     * @param x The x position of the touch event
     * @return The day number, or -1 if the position wasn't in a day
     */
    fun getDayFromLocation(x: Float, y: Float): Int {
        val day = getInternalDayFromLocation(x, y)
        return if (day < 1 || day > mNumCells) {
            -1
        } else day
    }

    /**
     * Calculates the day that the given x position is in, accounting for week
     * number.
     *
     * @param x The x position of the touch event
     * @return The day number
     */
    private fun getInternalDayFromLocation(x: Float, y: Float): Int {
        var y = y
        y += (mEdgePadding / 2).toFloat()
        val dayStart = mEdgePadding
        if (x < dayStart || x > mWidth - mEdgePadding) {
            return -1
        }
        // Selection is (x - start) / (pixels/day) == (x -s) * day / pixels
        val row = (y - monthHeaderSize).toInt() / mRowHeight
        var column = ((x - dayStart) * mNumDays / (mWidth - dayStart - mEdgePadding)).toInt()

        // TODO: 7/10/2018 Amin
        column = when (CurrentCalendarType.type) {
            CalendarType.CIVIL -> column
            CalendarType.PERSIAN -> 6 - column
            CalendarType.HIJRI -> 6 - column
        }

        var day = column - findDayOffset() + 1
        day += row * mNumDays
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

        mOnDayClickListener?.apply {
            val calendar = Utils.newCalendar()
            calendar.setDate(year, month, day)
            onDayClick(this@BaseMonthView, calendar)
        }
    }

    /**
     * @return true if the specified year/month/day are within the selectable days or the range set by minDate and maxDate.
     * If one or either have not been set, they are considered as Integer.MIN_VALUE and
     * Integer.MAX_VALUE.
     */
    protected fun isOutOfRange(year: Int, month: Int, day: Int): Boolean {
        if (controller!!.selectableDays != null) {
            return !isSelectable(year, month, day)
        }

        return if (isBeforeMin(year, month, day)) {
            true
        } else
            isAfterMax(year, month, day)

    }

    private fun isSelectable(year: Int, month: Int, day: Int): Boolean {
        controller!!.selectableDays?.apply {
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

    private fun isBeforeMin(year: Int, month: Int, day: Int): Boolean {
        val func = "isBeforeMin > "

        if (controller == null) {
            Log.d(TAG, "$func #> false + controller == null")
            return false
        }
        val minDate = controller!!.minDate
        val day = func + "input: " + year + "-" + month + "-" + day + " *** minDate: " + minDate?.year + "-" + minDate?.month + "-" + minDate?.dayOfMonth + " "

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

    private fun isAfterMax(year: Int, month: Int, day: Int): Boolean {
        val func = "isAfterMax > "

        if (controller == null) {
            Log.d(TAG, "$func #> false + controller == null")
            return false
        }
        val maxDate = controller!!.maxDate
        val day = func + "input: " + year + "-" + month + "-" + day + " *** maxDate: " + maxDate?.year + "-" + maxDate?.month + "-" + maxDate?.dayOfMonth + " "

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
     * @param year
     * @param month
     * @param day
     * @return true if the given date should be highlighted
     */
    protected fun isHighlighted(year: Int, month: Int, day: Int): Boolean {
        val highlightedDays = controller!!.highlightedDays ?: return false
        for (c in highlightedDays) {
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
        return false
    }

    /**
     * Handles callbacks when the user clicks on a time object.
     */
    interface OnDayClickListener {
        fun onDayClick(view: BaseMonthView, day: BaseCalendar)
    }

    private fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    companion object {
        /**
         * This sets the height of this week in pixels
         */
        const val VIEW_PARAMS_HEIGHT = "height"

        /**
         * These params can be passed into the view to control how it appears.
         * [.VIEW_PARAMS_WEEK] is the only required field, though the default
         * values are unlikely to fit most layouts correctly.
         */
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
        /**
         * Which day the week should start on. [Calendar.SUNDAY] through
         * [Calendar.SATURDAY].
         */
        const val VIEW_PARAMS_WEEK_START = "week_start"
        /**
         * How many days to display at a time. Days will be displayed starting with
         * [.mWeekStart].
         */
        const val VIEW_PARAMS_NUM_DAYS = "num_days"
        /**
         * Which month is currently in focus, as defined by [Calendar.MONTH]
         * [0-11].
         */
        const val VIEW_PARAMS_FOCUS_MONTH = "focus_month"
        /**
         * If this month should display week numbers. false if 0, true otherwise.
         */
        const val VIEW_PARAMS_SHOW_WK_NUM = "show_wk_num"

        protected const val DEFAULT_SELECTED_DAY = -1
        protected const val DEFAULT_WEEK_START = Calendar.SATURDAY
        protected const val DEFAULT_NUM_DAYS = 7
        protected const val DEFAULT_SHOW_WK_NUM = 0
        protected const val DEFAULT_FOCUS_MONTH = -1
        protected const val DEFAULT_NUM_ROWS = 6
        protected const val MAX_NUM_ROWS = 6
        private val TAG = BaseMonthView::class.java.canonicalName
        private const val SELECTED_CIRCLE_ALPHA = 255
        protected var DEFAULT_HEIGHT = 32
        protected var MIN_HEIGHT = 10
        protected var DAY_SEPARATOR_WIDTH = 1
        protected var MINI_DAY_NUMBER_TEXT_SIZE: Int = 0
        protected var LARGE_DAY_NUMBER_TEXT_SIZE: Int = 0
        protected var MONTH_LABEL_TEXT_SIZE: Int = 0
        protected var MONTH_DAY_LABEL_TEXT_SIZE: Int = 0
        protected var MONTH_HEADER_SIZE: Int = 0
        protected var DAY_SELECTED_CIRCLE_SIZE: Int = 0

        // used for scaling to the device density
        protected var mScale = 0f
    }

}
