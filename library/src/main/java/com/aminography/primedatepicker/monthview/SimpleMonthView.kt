package com.aminography.primedatepicker.monthview

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.common.*
import com.aminography.primedatepicker.monthview.painters.DayLabelsPainter
import com.aminography.primedatepicker.utils.*
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.math.min


/**
 * @author aminography
 */
@Suppress("ConstantConditionIf", "MemberVisibilityCanBePrivate", "unused")
open class SimpleMonthView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Interior Variables --------------------------------------------------------------------------

    private val dpUnit = context.dp2px(1f)

    protected val Int.dp: Float
        get() = dpUnit.times(this).toFloat()

    protected var viewWidth = 0
        private set
    protected var absoluteViewWidth = 0
        private set

    private val defaultCellHeight = 36.dp
    private var minCellHeight: Float = 0f

    protected var cellHeight: Float = defaultCellHeight
        private set
    protected var cellWidth: Float = cellHeight
        private set

    protected var columnXPositions: FloatArray = floatArrayOf()

    private var direction = Direction.LTR

    protected var month = -1
    protected var year = -1

    private var hasToday = false
    private var todayDayOfMonth = -1

    private var daysInMonth = 0

    private var firstDayOfMonthDayOfWeek = 0

    private var maxRowCount = 6
    private var rowCount = 6
    protected var columnCount = 7
        private set

    var firstDayOfMonthCalendar: PrimeCalendar? = null
        private set

    private var animationProgress = 1.0f
    private val progressProperty = PropertyValuesHolder.ofFloat("PROGRESS", 1.0f, 0.75f, 1f)

    private var pendingAnimateDay: PrimeCalendar? = null

    private val dayLabelsPainter: DayLabelsPainter by lazy { DayLabelsPainter() }

    protected open val topGap: Int
        get() = paddingTop

    protected open val bottomGap: Int
        get() = paddingBottom

    protected open val leftGap: Int
        get() = paddingLeft

    protected open val rightGap: Int
        get() = paddingRight

    // Listeners -----------------------------------------------------------------------------------

    internal var onHeightDetectListener: OnHeightDetectListener? = null
    var onDayPickedListener: OnDayPickedListener? = null
    var onMonthLabelClickListener: OnMonthLabelClickListener? = null

    // Control Variables ---------------------------------------------------------------------------

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

    var pickedDayLabelTextColor: Int = 0
        set(value) {
            field = value
            if (invalidate) invalidate()
        }

    var pickedDayInRangeLabelTextColor: Int = 0
        set(value) {
            field = value
            if (invalidate) invalidate()
        }

    var pickedDayBackgroundColor: Int = 0
        set(value) {
            field = value
            dayLabelsPainter.pickedDayBackgroundColor = value
            if (invalidate) invalidate()
        }

    var pickedDayInRangeBackgroundColor: Int = 0
        set(value) {
            field = value
            dayLabelsPainter.pickedDayInRangeBackgroundColor = value
            if (invalidate) invalidate()
        }

    var disabledDayLabelTextColor: Int = 0
        set(value) {
            field = value
            if (invalidate) invalidate()
        }

    var adjacentMonthDayLabelTextColor: Int = 0
        set(value) {
            field = value
            if (invalidate) invalidate()
        }

    var dayLabelTextSize: Int = 0
        set(value) {
            field = value
            dayLabelsPainter.dayLabelTextSize = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var dayLabelVerticalPadding: Int = 0
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var pickedDayBackgroundShapeType: BackgroundShapeType = BackgroundShapeType.CIRCLE
        set(value) {
            field = value
            dayLabelsPainter.pickedDayBackgroundShapeType = value
            if (invalidate) {
                invalidate()
            }
        }

    var pickedDayRoundSquareCornerRadius: Int = 0
        set(value) {
            field = value
            dayLabelsPainter.pickedDayRoundSquareCornerRadius = value.toFloat()
            if (invalidate) {
                invalidate()
            }
        }

    var showTwoWeeksInLandscape: Boolean = false
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                invalidate()
            }
        }

    var showAdjacentMonthDays: Boolean = false
        set(value) {
            field = value
            dayLabelsPainter.showAdjacentMonthDays = value
            if (invalidate) {
                calculateSizes()
                invalidate()
            }
        }

    var animateSelection: Boolean = false

    var animationDuration: Int = 0
        set(value) {
            field = value
            animator.duration = value.toLong()
        }

    // Programmatically Control Variables ----------------------------------------------------------

    var typeface: Typeface? = null
        set(value) {
            field = value
            applyTypeface()
            if (invalidate) invalidate()
        }

    var pickedSingleDayCalendar: PrimeCalendar? = null
        set(value) {
            field = value
            if (invalidate) invalidate()
            notifyDayPicked(true)
        }

    var pickedRangeStartCalendar: PrimeCalendar? = null
        set(value) {
            field = value
            if (invalidate) invalidate()
            notifyDayPicked(true)
        }

    var pickedRangeEndCalendar: PrimeCalendar? = null
        set(value) {
            field = value
            if (invalidate) invalidate()
            notifyDayPicked(true)
        }

    internal var pickedMultipleDaysMap: LinkedHashMap<String, PrimeCalendar>? = null
        set(value) {
            field = value
            if (invalidate) invalidate()
            notifyDayPicked(true)
        }

    var pickedMultipleDaysList: List<PrimeCalendar>
        get() = pickedMultipleDaysMap?.values?.toList() ?: arrayListOf()
        set(value) {
            linkedMapOf<String, PrimeCalendar>().apply {
                putAll(value.map { Pair(DateUtils.dateString(it) ?: "", it) })
            }.also { pickedMultipleDaysMap = it }
        }

    var pickType: PickType = PickType.NOTHING
        set(value) {
            field = value
            doNotInvalidate {
                when (value) {
                    PickType.SINGLE -> {
                        pickedRangeStartCalendar = null
                        pickedRangeEndCalendar = null
                        pickedMultipleDaysMap = null
                    }
                    PickType.RANGE_START -> {
                        pickedSingleDayCalendar = null
                        pickedMultipleDaysMap = null
                    }
                    PickType.RANGE_END -> {
                        pickedSingleDayCalendar = null
                        pickedMultipleDaysMap = null
                    }
                    PickType.MULTIPLE -> {
                        pickedSingleDayCalendar = null
                        pickedRangeStartCalendar = null
                        pickedRangeEndCalendar = null
                    }
                    PickType.NOTHING -> {
                        pickedSingleDayCalendar = null
                        pickedRangeStartCalendar = null
                        pickedRangeEndCalendar = null
                        pickedMultipleDaysMap = null
                    }
                }
            }
            if (invalidate) invalidate()
            notifyDayPicked(true)
        }

    var minDateCalendar: PrimeCalendar? = null
        set(value) {
            field = value
            var hasChanged = false
            doNotInvalidate {
                value?.also { min ->
                    pickedSingleDayCalendar?.let { single ->
                        if (DateUtils.isBefore(single, min)) {
                            pickedSingleDayCalendar = min
                            hasChanged = true
                        }
                    }
                    pickedRangeStartCalendar?.let { start ->
                        if (DateUtils.isBefore(start, min)) {
                            pickedRangeStartCalendar = min
                            hasChanged = true
                        }
                    }
                    pickedRangeEndCalendar?.let { end ->
                        if (DateUtils.isBefore(end, min)) {
                            pickedRangeStartCalendar = null
                            pickedRangeEndCalendar = null
                            hasChanged = true
                        }
                    }
                }
            }
            if (invalidate) invalidate()
            notifyDayPicked(hasChanged)
        }

    var maxDateCalendar: PrimeCalendar? = null
        set(value) {
            field = value
            var hasChanged = false
            doNotInvalidate {
                value?.also { max ->
                    pickedSingleDayCalendar?.let { single ->
                        if (DateUtils.isAfter(single, max)) {
                            pickedSingleDayCalendar = max
                            hasChanged = true
                        }
                    }
                    pickedRangeStartCalendar?.let { start ->
                        if (DateUtils.isAfter(start, max)) {
                            pickedRangeStartCalendar = null
                            pickedRangeEndCalendar = null
                            hasChanged = true
                        }
                    }
                    pickedRangeEndCalendar?.let { end ->
                        if (DateUtils.isAfter(end, max)) {
                            pickedRangeEndCalendar = max
                            hasChanged = true
                        }
                    }
                }
            }
            if (invalidate) invalidate()
            notifyDayPicked(hasChanged)
        }

    var calendarType = CalendarType.CIVIL
        set(value) {
            field = value
            direction = value.findDirection(locale)
            if (invalidate) goto(CalendarFactory.newInstance(value, locale))
        }

    var locale: Locale = Locale.getDefault()
        set(value) {
            field = value
            direction = value.findDirection(calendarType)
            if (invalidate) goto(CalendarFactory.newInstance(calendarType, value))
        }

    var animationInterpolator: Interpolator = DEFAULT_INTERPOLATOR
        set(value) {
            field = value
            animator.interpolator = animationInterpolator
        }

    var developerOptionsShowGuideLines: Boolean = false
        set(value) {
            field = value
            if (invalidate) invalidate()
        }

    // ---------------------------------------------------------------------------------------------

    internal var disabledDaysSet: MutableSet<String>? = null
        set(value) {
            field = value
            if (invalidate) invalidate()
        }

    var disabledDaysList: List<PrimeCalendar> = arrayListOf()
        set(value) {
            field = value
            mutableSetOf<String>().apply {
                addAll(value.map { DateUtils.dateString(it) ?: "" })
            }.also { disabledDaysSet = it }
        }

    // ---------------------------------------------------------------------------------------------

    private val animator = ValueAnimator().apply {
        setValues(progressProperty)
        duration = animationDuration.toLong()
        interpolator = animationInterpolator
        addUpdateListener {
            animationProgress = it.getAnimatedValue("PROGRESS") as Float
            invalidate()
        }
    }

    internal var firstDayOfWeek = -1
        set(value) {
            field = value
            if (invalidate) invalidate()
        }

    private var pickedDaysChanged: Boolean = false
    protected var invalidate: Boolean = true

    fun doNotInvalidate(block: (SimpleMonthView) -> Unit) {
        val previous = invalidate
        invalidate = false
        block.invoke(this)
        invalidate = previous
    }

    // ---------------------------------------------------------------------------------------------

    init {
        context.obtainStyledAttributes(attrs, R.styleable.SimpleMonthView, defStyleAttr, defStyleRes).run {
            doNotInvalidate {
                calendarType = CalendarType.values()[getInt(R.styleable.SimpleMonthView_calendarType, DEFAULT_CALENDAR_TYPE.ordinal)]

                dayLabelTextColor = getColor(R.styleable.SimpleMonthView_dayLabelTextColor, ContextCompat.getColor(context, R.color.gray900))
                todayLabelTextColor = getColor(R.styleable.SimpleMonthView_todayLabelTextColor, ContextCompat.getColor(context, R.color.green400))
                pickedDayLabelTextColor = getColor(R.styleable.SimpleMonthView_pickedDayLabelTextColor, ContextCompat.getColor(context, R.color.white))
                pickedDayInRangeLabelTextColor = getColor(R.styleable.SimpleMonthView_pickedDayInRangeLabelTextColor, ContextCompat.getColor(context, R.color.white))
                pickedDayBackgroundColor = getColor(R.styleable.SimpleMonthView_pickedDayBackgroundColor, ContextCompat.getColor(context, R.color.red300))
                pickedDayInRangeBackgroundColor = getColor(R.styleable.SimpleMonthView_pickedDayInRangeBackgroundColor, ContextCompat.getColor(context, R.color.red300))
                disabledDayLabelTextColor = getColor(R.styleable.SimpleMonthView_disabledDayLabelTextColor, ContextCompat.getColor(context, R.color.gray400))
                adjacentMonthDayLabelTextColor = getColor(R.styleable.SimpleMonthView_adjacentMonthDayLabelTextColor, ContextCompat.getColor(context, R.color.gray400))

                dayLabelTextSize = getDimensionPixelSize(R.styleable.SimpleMonthView_dayLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultDayLabelTextSize))

                dayLabelVerticalPadding = getDimensionPixelSize(R.styleable.SimpleMonthView_dayLabelVerticalPadding, resources.getDimensionPixelSize(R.dimen.defaultDayLabelVerticalPadding))

                pickedDayBackgroundShapeType = BackgroundShapeType.values()[getInt(R.styleable.SimpleMonthView_pickedDayBackgroundShapeType, DEFAULT_BACKGROUND_SHAPE_TYPE.ordinal)]
                pickedDayRoundSquareCornerRadius = getDimensionPixelSize(R.styleable.SimpleMonthView_pickedDayRoundSquareCornerRadius, resources.getDimensionPixelSize(R.dimen.defaultPickedDayRoundSquareCornerRadius))

                showTwoWeeksInLandscape = getBoolean(R.styleable.SimpleMonthView_showTwoWeeksInLandscape, resources.getBoolean(R.bool.defaultShowTwoWeeksInLandscape))
                showAdjacentMonthDays = getBoolean(R.styleable.SimpleMonthView_showAdjacentMonthDays, resources.getBoolean(R.bool.defaultShowAdjacentMonthDays))

                animateSelection = getBoolean(R.styleable.SimpleMonthView_animateSelection, resources.getBoolean(R.bool.defaultAnimateSelection))
                animationDuration = getInteger(R.styleable.SimpleMonthView_animationDuration, resources.getInteger(R.integer.defaultAnimationDuration))
            }
            recycle()
        }

        dayLabelsPainter.also {
            it.dayLabelTextSize = dayLabelTextSize
            it.pickedDayBackgroundColor = pickedDayBackgroundColor
            it.pickedDayInRangeBackgroundColor = pickedDayInRangeBackgroundColor
            it.typeface = typeface
            it.shouldAnimateDayBackground = { dayOfMonth -> shouldAnimateDayBackground(dayOfMonth) }
            it.findDayState = { dayOfMonth -> findDayState(dayOfMonth) }
            it.findDayLabelTextColor = { dayOfMonth, dayState -> findDayLabelTextColor(dayOfMonth, dayState) }
            it.dayLabelFormatter = { dayOfMonth -> findDayLabelText(dayOfMonth) }
        }

        @Suppress("LeakingThis")
        calculateSizes()

        if (isInEditMode) {
            val calendar = CalendarFactory.newInstance(calendarType, locale)
            goto(calendar)
        }
    }

    protected open fun applyTypeface() {
        dayLabelsPainter.typeface = typeface
    }

    protected open fun calculateSizes() {
        if (context.isDisplayLandscape) {
            if (showTwoWeeksInLandscape) {
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
        cellWidth = absoluteViewWidth / columnCount.toFloat()

        columnXPositions = FloatArray(columnCount) {
            // RTL-ize for RTL Calendars
            when (direction) {
                Direction.LTR -> (2 * it + 1) * (cellWidth / 2) + leftGap
                Direction.RTL -> (2 * (columnCount - 1 - it) + 1) * (cellWidth / 2) + leftGap
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = topGap +
            cellHeight * rowCount +
            bottomGap
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height.toInt())

        val maxHeight = topGap +
            cellHeight * maxRowCount +
            bottomGap
        onHeightDetectListener?.onHeightDetect(maxHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        viewWidth = w
        absoluteViewWidth = viewWidth - leftGap - rightGap
        cellWidth = absoluteViewWidth / columnCount.toFloat()
        calculateSizes()
    }

    override fun onDraw(canvas: Canvas) {
        if (year == -1 && month == -1) {
            val calendar = CalendarFactory.newInstance(calendarType, locale)
            goto(calendar)
        } else {
            val radius = min(cellWidth, cellHeight) / 2 - 2.dp
            val daysInPreviousMonth = if (!showAdjacentMonthDays) 0 else {
                CalendarFactory.newInstance(calendarType, locale).also {
                    it.set(year, month, 1)
                    it.add(Calendar.MONTH, -1)
                }.monthLength
            }
            dayLabelsPainter.draw(
                canvas,
                direction,
                cellWidth,
                cellHeight,
                columnXPositions,
                topGap + cellHeight / 2,
                radius,
                radius * animationProgress,
                daysInMonth,
                daysInPreviousMonth,
                rowCount,
                columnCount,
                adjustDayOfWeekOffset(firstDayOfMonthDayOfWeek),
                developerOptionsShowGuideLines
            )
        }
    }

    fun goTo(calendar: PrimeCalendar) =
        goto(calendar)

    fun goTo(year: Int, month: Int) =
        goto(year, month)

    fun goto(calendar: PrimeCalendar) {
        doNotInvalidate {
            locale = calendar.locale
            calendarType = calendar.calendarType
            firstDayOfWeek = calendar.firstDayOfWeek
        }
        goto(calendar.year, calendar.month)
    }

    fun goto(year: Int, month: Int) {
        this.year = year
        this.month = month

        if (firstDayOfWeek == -1) {
            doNotInvalidate {
                firstDayOfWeek = DateUtils.defaultWeekStartDay(calendarType)
            }
        }

        firstDayOfMonthCalendar = CalendarFactory.newInstance(calendarType, locale).also {
            it.firstDayOfWeek = firstDayOfWeek
            it.set(year, month, 1)
            firstDayOfMonthDayOfWeek = it[Calendar.DAY_OF_WEEK]
        }

        daysInMonth = DateUtils.getDaysInMonth(calendarType, year, month)

        setupGotoExtras()

        pendingAnimateDay = null

        updateToday()
        updateRowCount()

        calculateSizes()
        requestLayout()
        invalidate()
    }

    protected open fun setupGotoExtras() {}

    private fun updateToday() {
        val todayCalendar = CalendarFactory.newInstance(calendarType, locale)
        hasToday = todayCalendar.year == year && todayCalendar.month == month
        todayDayOfMonth = if (hasToday) todayCalendar.dayOfMonth else -1
    }

    private fun updateRowCount() {
        rowCount = if (showAdjacentMonthDays) {
            maxRowCount
        } else {
            val offset = adjustDayOfWeekOffset(firstDayOfMonthDayOfWeek)
            val dividend = (offset + daysInMonth) / columnCount
            val remainder = (offset + daysInMonth) % columnCount
            dividend + if (remainder > 0) 1 else 0
        }
    }

    private fun adjustDayOfWeekOffset(dayOfWeek: Int): Int {
        val day = if (dayOfWeek < firstDayOfWeek) dayOfWeek + 7 else dayOfWeek
        return (day - firstDayOfWeek) % 7
    }

    private fun findDayState(dayOfMonth: Int): DayState {
        return findDayState(
            year,
            month,
            dayOfMonth,
            pickType,
            pickedSingleDayCalendar,
            pickedRangeStartCalendar,
            pickedRangeEndCalendar,
            pickedMultipleDaysMap,
            minDateCalendar,
            maxDateCalendar,
            disabledDaysSet
        )
    }

    private fun shouldAnimateDayBackground(dayOfMonth: Int): Boolean {
        return pendingAnimateDay?.let {
            it.year == year && it.month == month && it.dayOfMonth == dayOfMonth
        } ?: (pickType == PickType.RANGE_START || pickType == PickType.RANGE_END)
    }

    private fun findDayLabelTextColor(dayOfMonth: Int, dayState: DayState): Int {
        return when (dayState) {
            DayState.PICKED_SINGLE -> {
                pickedDayLabelTextColor
            }
            DayState.START_OF_RANGE_SINGLE -> {
                pickedDayLabelTextColor
            }
            DayState.START_OF_RANGE -> {
                pickedDayLabelTextColor
            }
            DayState.IN_RANGE -> {
                pickedDayInRangeLabelTextColor
            }
            DayState.END_OF_RANGE -> {
                pickedDayLabelTextColor
            }
            DayState.NORMAL -> {
                if (hasToday && dayOfMonth == todayDayOfMonth) {
                    todayLabelTextColor
                } else {
                    dayLabelTextColor
                }
            }
            DayState.DISABLED -> {
                disabledDayLabelTextColor
            }
            DayState.OUT_OF_VALID_RANGE -> {
                disabledDayLabelTextColor
            }
            DayState.BESIDE_MONTH -> {
                adjacentMonthDayLabelTextColor
            }
        }
    }

    private fun findDayLabelText(dayOfMonth: Int): String {
        return dayOfMonth.localizeDigits(locale)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                findDayByCoordinates(event.x, event.y)?.let { dayOfMonth ->
                    ifInValidRange(dayOfMonth) {
                        val calendar = CalendarFactory.newInstance(calendarType, locale)
                        calendar.set(year, month, dayOfMonth)
                        onDayClicked(calendar)
                    }
                } ?: return false
            }
        }
        return true
    }

    private fun onDayClicked(calendar: PrimeCalendar) {
        var hasChanged = false
        doNotInvalidate {
            when (pickType) {
                PickType.SINGLE -> {
                    pickedSingleDayCalendar = calendar
                    pendingAnimateDay = calendar
                    hasChanged = true
                }
                PickType.RANGE_START -> {
                    if (DateUtils.isAfter(calendar, pickedRangeEndCalendar)) {
                        pickedRangeEndCalendar = null
                    }
                    pickedRangeStartCalendar = calendar
                    hasChanged = true
                }
                PickType.RANGE_END -> {
                    if (pickedRangeStartCalendar != null && !DateUtils.isBefore(calendar, pickedRangeStartCalendar)) {
                        pickedRangeEndCalendar = calendar
                        hasChanged = true
                    }
                }
                PickType.MULTIPLE -> {
                    if (pickedMultipleDaysMap == null) pickedMultipleDaysMap = LinkedHashMap()
                    val dateString = DateUtils.dateString(year, month, calendar.dayOfMonth)
                    if (pickedMultipleDaysMap?.containsKey(dateString) == true) {
                        pickedMultipleDaysMap?.remove(dateString)
                    } else {
                        pickedMultipleDaysMap?.put(dateString, calendar)
                    }
                    pendingAnimateDay = calendar
                    hasChanged = true
                }
                PickType.NOTHING -> {
                }
            }
        }

        notifyDayPicked(hasChanged)
        checkAnimatedInvalidation()
    }

    private fun checkAnimatedInvalidation() {
        if (animateSelection) {
            animator.start()
        } else {
            invalidate()
        }
    }

    fun focusOnDay(calendar: PrimeCalendar) {
        pendingAnimateDay = calendar
        checkAnimatedInvalidation()
    }

    private fun notifyDayPicked(hasChanged: Boolean) {
        pickedDaysChanged = pickedDaysChanged or hasChanged
        if (invalidate && pickedDaysChanged) {
            onDayPickedListener?.onDayPicked(
                pickType,
                pickedSingleDayCalendar,
                pickedRangeStartCalendar,
                pickedRangeEndCalendar,
                pickedMultipleDaysList
            )
            pickedDaysChanged = false
        }
    }

    private fun findDayByCoordinates(inputX: Float, inputY: Float): Int? {
        if (inputX < leftGap || inputX > viewWidth - rightGap || inputY < topGap) return null

        val row = ((inputY - topGap) / cellHeight).toInt()
        var column = ((inputX - leftGap) * columnCount / absoluteViewWidth).toInt()

        column = when (direction) {
            Direction.LTR -> column
            Direction.RTL -> columnCount - 1 - column
        }

        var day = column - adjustDayOfWeekOffset(firstDayOfMonthDayOfWeek) + 1
        day += row * columnCount

        return if (day < 1 || day > daysInMonth)
            null
        else day
    }

    private fun ifInValidRange(dayOfMonth: Int, function: () -> Unit) {
        when (findDayState(dayOfMonth)) {
            DayState.DISABLED,
            DayState.OUT_OF_VALID_RANGE,
            DayState.BESIDE_MONTH -> {
                // nothing!
            }
            else -> function.invoke()
        }
    }

    interface OnHeightDetectListener {
        fun onHeightDetect(height: Float)
    }

    // Save/Restore States -------------------------------------------------------------------------

    override fun onSaveInstanceState(): Parcelable? {
        val savedState = SavedState(super.onSaveInstanceState())

        savedState.calendarType = calendarType.ordinal
        savedState.firstDayOfWeek = firstDayOfWeek
        savedState.locale = locale.language

        savedState.year = year
        savedState.month = month

        savedState.minDateCalendar = DateUtils.storeCalendar(minDateCalendar)
        savedState.maxDateCalendar = DateUtils.storeCalendar(maxDateCalendar)

        savedState.pickType = pickType.name
        savedState.pickedSingleDayCalendar = DateUtils.storeCalendar(pickedSingleDayCalendar)
        savedState.pickedRangeStartCalendar = DateUtils.storeCalendar(pickedRangeStartCalendar)

        savedState.pickedMultipleDaysList = pickedMultipleDaysMap?.values?.map {
            DateUtils.storeCalendar(it)!!
        } ?: arrayListOf()

        disabledDaysSet?.let { savedState.disabledDaysList = it.toList() }

        savedState.dayLabelTextColor = dayLabelTextColor
        savedState.todayLabelTextColor = todayLabelTextColor
        savedState.pickedDayLabelTextColor = pickedDayLabelTextColor
        savedState.pickedDayInRangeLabelTextColor = pickedDayInRangeLabelTextColor
        savedState.pickedDayBackgroundColor = pickedDayBackgroundColor
        savedState.pickedDayInRangeBackgroundColor = pickedDayInRangeBackgroundColor
        savedState.disabledDayLabelTextColor = disabledDayLabelTextColor
        savedState.adjacentMonthDayLabelTextColor = adjacentMonthDayLabelTextColor
        savedState.dayLabelTextSize = dayLabelTextSize
        savedState.dayLabelVerticalPadding = dayLabelVerticalPadding
        savedState.showTwoWeeksInLandscape = showTwoWeeksInLandscape
        savedState.showAdjacentMonthDays = showAdjacentMonthDays

        savedState.pickedDayBackgroundShapeType = pickedDayBackgroundShapeType.ordinal
        savedState.pickedDayRoundSquareCornerRadius = pickedDayRoundSquareCornerRadius

        savedState.animateSelection = animateSelection
        savedState.animationDuration = animationDuration

        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)

        doNotInvalidate {
            calendarType = CalendarType.values()[savedState.calendarType]
            firstDayOfWeek = savedState.firstDayOfWeek
            savedState.locale?.let { locale = Locale(it) }

            year = savedState.year
            month = savedState.month

            minDateCalendar = DateUtils.restoreCalendar(savedState.minDateCalendar)
            maxDateCalendar = DateUtils.restoreCalendar(savedState.maxDateCalendar)

            pickType = savedState.pickType?.let { PickType.valueOf(it) } ?: PickType.NOTHING
            pickedSingleDayCalendar = DateUtils.restoreCalendar(savedState.pickedSingleDayCalendar)
            pickedRangeStartCalendar = DateUtils.restoreCalendar(savedState.pickedRangeStartCalendar)
            pickedRangeEndCalendar = DateUtils.restoreCalendar(savedState.pickedRangeEndCalendar)

            linkedMapOf<String, PrimeCalendar>().apply {
                savedState.pickedMultipleDaysList?.map {
                    val calendar = DateUtils.restoreCalendar(it)
                    Pair(DateUtils.dateString(calendar)!!, calendar!!)
                }?.also { putAll(it) }
            }.also { pickedMultipleDaysMap = it }

            savedState.disabledDaysList?.toMutableSet()?.let { disabledDaysSet = it }

            dayLabelTextColor = savedState.dayLabelTextColor
            todayLabelTextColor = savedState.todayLabelTextColor
            pickedDayInRangeLabelTextColor = savedState.pickedDayInRangeLabelTextColor
            pickedDayBackgroundColor = savedState.pickedDayBackgroundColor
            pickedDayInRangeBackgroundColor = savedState.pickedDayInRangeBackgroundColor
            disabledDayLabelTextColor = savedState.disabledDayLabelTextColor
            adjacentMonthDayLabelTextColor = savedState.adjacentMonthDayLabelTextColor
            dayLabelTextSize = savedState.dayLabelTextSize
            dayLabelVerticalPadding = savedState.dayLabelVerticalPadding
            showTwoWeeksInLandscape = savedState.showTwoWeeksInLandscape
            showAdjacentMonthDays = savedState.showAdjacentMonthDays

            pickedDayBackgroundShapeType = BackgroundShapeType.values()[savedState.pickedDayBackgroundShapeType]
            pickedDayRoundSquareCornerRadius = savedState.pickedDayRoundSquareCornerRadius

            animateSelection = savedState.animateSelection
            animationDuration = savedState.animationDuration
        }

        applyTypeface()
        calculateSizes()
        goto(year, month)
        notifyDayPicked(true)
    }

    private class SavedState : BaseSavedState {

        internal var calendarType: Int = 0
        internal var firstDayOfWeek: Int = 0
        internal var locale: String? = null
        internal var year: Int = 0
        internal var month: Int = 0

        internal var minDateCalendar: String? = null
        internal var maxDateCalendar: String? = null

        internal var pickType: String? = null
        internal var pickedSingleDayCalendar: String? = null
        internal var pickedRangeStartCalendar: String? = null
        internal var pickedRangeEndCalendar: String? = null
        internal var pickedMultipleDaysList: List<String>? = null

        internal var disabledDaysList: List<String>? = null

        internal var dayLabelTextColor: Int = 0
        internal var todayLabelTextColor: Int = 0
        internal var pickedDayLabelTextColor: Int = 0
        internal var pickedDayInRangeLabelTextColor: Int = 0
        internal var pickedDayBackgroundColor: Int = 0
        internal var pickedDayInRangeBackgroundColor: Int = 0
        internal var disabledDayLabelTextColor: Int = 0
        internal var adjacentMonthDayLabelTextColor: Int = 0
        internal var dayLabelTextSize: Int = 0
        internal var dayLabelVerticalPadding: Int = 0
        internal var showTwoWeeksInLandscape: Boolean = false
        internal var showAdjacentMonthDays: Boolean = false

        internal var pickedDayBackgroundShapeType: Int = 0
        internal var pickedDayRoundSquareCornerRadius: Int = 0

        internal var animateSelection: Boolean = false
        internal var animationDuration: Int = 0

        internal constructor(superState: Parcelable?) : super(superState)

        private constructor(input: Parcel) : super(input) {
            calendarType = input.readInt()
            firstDayOfWeek = input.readInt()
            locale = input.readString()
            year = input.readInt()
            month = input.readInt()

            minDateCalendar = input.readString()
            maxDateCalendar = input.readString()

            pickType = input.readString()
            pickedSingleDayCalendar = input.readString()
            pickedRangeStartCalendar = input.readString()
            pickedRangeEndCalendar = input.readString()
            input.readStringList(pickedMultipleDaysList ?: mutableListOf())

            disabledDaysList?.let { input.readStringList(it) }

            dayLabelTextColor = input.readInt()
            todayLabelTextColor = input.readInt()
            pickedDayLabelTextColor = input.readInt()
            pickedDayInRangeLabelTextColor = input.readInt()
            pickedDayBackgroundColor = input.readInt()
            pickedDayInRangeBackgroundColor = input.readInt()
            disabledDayLabelTextColor = input.readInt()
            adjacentMonthDayLabelTextColor = input.readInt()
            dayLabelTextSize = input.readInt()
            dayLabelVerticalPadding = input.readInt()
            showTwoWeeksInLandscape = input.readInt() == 1
            showAdjacentMonthDays = input.readInt() == 1

            pickedDayBackgroundShapeType = input.readInt()
            pickedDayRoundSquareCornerRadius = input.readInt()

            animateSelection = input.readInt() == 1
            animationDuration = input.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(calendarType)
            out.writeInt(firstDayOfWeek)
            out.writeString(locale)
            out.writeInt(year)
            out.writeInt(month)

            out.writeString(minDateCalendar)
            out.writeString(maxDateCalendar)

            out.writeString(pickType)
            out.writeString(pickedSingleDayCalendar)
            out.writeString(pickedRangeStartCalendar)
            out.writeString(pickedRangeEndCalendar)
            out.writeStringList(pickedMultipleDaysList)

            out.writeStringList(disabledDaysList)

            out.writeInt(dayLabelTextColor)
            out.writeInt(todayLabelTextColor)
            out.writeInt(pickedDayLabelTextColor)
            out.writeInt(pickedDayInRangeLabelTextColor)
            out.writeInt(pickedDayBackgroundColor)
            out.writeInt(pickedDayInRangeBackgroundColor)
            out.writeInt(disabledDayLabelTextColor)
            out.writeInt(adjacentMonthDayLabelTextColor)
            out.writeInt(dayLabelTextSize)
            out.writeInt(dayLabelVerticalPadding)
            out.writeInt(if (showTwoWeeksInLandscape) 1 else 0)
            out.writeInt(if (showAdjacentMonthDays) 1 else 0)

            out.writeInt(pickedDayBackgroundShapeType)
            out.writeInt(pickedDayRoundSquareCornerRadius)

            out.writeInt(if (animateSelection) 1 else 0)
            out.writeInt(animationDuration)
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
        private val DEFAULT_CALENDAR_TYPE = CalendarType.CIVIL
        private val DEFAULT_BACKGROUND_SHAPE_TYPE = BackgroundShapeType.CIRCLE

        val DEFAULT_INTERPOLATOR = OvershootInterpolator()

        val DEFAULT_MONTH_LABEL_FORMATTER: LabelFormatter =
            { primeCalendar -> "${primeCalendar.monthName} ${primeCalendar.year}" }

        val DEFAULT_WEEK_LABEL_FORMATTER: LabelFormatter =
            { primeCalendar -> primeCalendar.weekDayNameShort }
    }

}
