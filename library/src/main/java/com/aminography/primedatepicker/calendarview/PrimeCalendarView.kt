package com.aminography.primedatepicker.calendarview

import android.content.Context
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.Direction
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.calendarview.adapter.MonthListAdapter
import com.aminography.primedatepicker.calendarview.callback.IMonthViewHolderCallback
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder
import com.aminography.primedatepicker.calendarview.other.StartSnapHelper
import com.aminography.primedatepicker.calendarview.other.TouchControllableRecyclerView
import com.aminography.primedatepicker.monthview.PrimeMonthView.Companion.DEFAULT_INTERPOLATOR
import com.aminography.primedatepicker.tools.DateUtils
import com.aminography.primedatepicker.tools.monthOffset
import java.util.*


/**
 * @author aminography
 */
@Suppress("PrivatePropertyName", "MemberVisibilityCanBePrivate", "UNNECESSARY_SAFE_CALL", "unused")
class PrimeCalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @Suppress("UNUSED_PARAMETER") @StyleRes defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IMonthViewHolderCallback {

    // Interior Variables --------------------------------------------------------------------------
    private var needInvalidation = false

    private var adapter: MonthListAdapter
    private var recyclerView = TouchControllableRecyclerView(context)
    private lateinit var layoutManager: LinearLayoutManager
    private var dataList: MutableList<MonthDataHolder>? = null
    private var isInTransition = false
    private var isInLoading = false

    private var definedHeight: Int = 0
    private var detectedItemHeight: Float = 0f

    private var direction = Direction.LTR

    private var gotoYear: Int = 0
    private var gotoMonth: Int = 0

    // Listeners -----------------------------------------------------------------------------------

    var onDayPickedListener: OnDayPickedListener? = null

    // Common Control Variables --------------------------------------------------------------------

    override var monthLabelTextColor: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var weekLabelTextColor: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var dayLabelTextColor: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var todayLabelTextColor: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var pickedDayLabelTextColor: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var pickedDayCircleColor: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var disabledDayLabelTextColor: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var monthLabelTextSize: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var weekLabelTextSize: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var dayLabelTextSize: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var monthLabelTopPadding: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var monthLabelBottomPadding: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var weekLabelTopPadding: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var weekLabelBottomPadding: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var dayLabelVerticalPadding: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var showTwoWeeksInLandscape: Boolean = false
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var animateSelection: Boolean = false
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var animationDuration: Int = 0
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var animationInterpolator: Interpolator = DEFAULT_INTERPOLATOR
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var toFocusDay: PrimeCalendar? = null

    // Control Variables ---------------------------------------------------------------------------

    var loadFactor: Int = 0

    var maxTransitionLength: Int = 0

    var transitionSpeedFactor: Float = 0f
        set(value) {
            field = value
            recyclerView.speedFactor = value
        }

    var dividerColor: Int = 0
        set(value) {
            field = value
            if (invalidate) applyDividers()
        }

    var dividerThickness: Int = 0
        set(value) {
            field = value
            if (invalidate) applyDividers()
        }

    var dividerInsetLeft: Int = 0
        set(value) {
            field = value
            if (invalidate) applyDividers()
        }

    var dividerInsetRight: Int = 0
        set(value) {
            field = value
            if (invalidate) applyDividers()
        }

    var dividerInsetTop: Int = 0
        set(value) {
            field = value
            if (invalidate) applyDividers()
        }

    var dividerInsetBottom: Int = 0
        set(value) {
            field = value
            if (invalidate) applyDividers()
        }

    // Programmatically Control Variables ----------------------------------------------------------

    override var typeface: Typeface? = null
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    override var pickedSingleDayCalendar: PrimeCalendar? = null
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
            notifyDayPicked(true)
        }

    override var pickedRangeStartCalendar: PrimeCalendar? = null
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
            notifyDayPicked(true)
        }

    override var pickedRangeEndCalendar: PrimeCalendar? = null
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
            notifyDayPicked(true)
        }

    override var pickedMultipleDaysMap: LinkedHashMap<String, PrimeCalendar>? = null
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

    override var minDateCalendar: PrimeCalendar? = null
        set(value) {
            field = value
            var change = false
            doNotInvalidate {
                value.also { min ->
                    pickedSingleDayCalendar?.let { single ->
                        if (DateUtils.isBefore(single, min)) {
                            pickedSingleDayCalendar = min
                            change = true
                        }
                    }
                    pickedRangeStartCalendar?.let { start ->
                        if (DateUtils.isBefore(start, min)) {
                            pickedRangeStartCalendar = min
                            change = true
                        }
                    }
                    pickedRangeEndCalendar?.let { end ->
                        if (DateUtils.isBefore(end, min)) {
                            pickedRangeStartCalendar = null
                            pickedRangeEndCalendar = null
                            change = true
                        }
                    }
                }
            }
            if (invalidate) {
                val minOffset = value?.monthOffset() ?: Int.MIN_VALUE
                findFirstVisibleItem()?.also { current ->
                    if (current.offset < minOffset) {
                        value?.apply {
                            goto(year, month, false)
                        }
                    } else {
                        goto(current.year, current.month, false)
                    }
                }
            }
            notifyDayPicked(change)
        }

    override var maxDateCalendar: PrimeCalendar? = null
        set(value) {
            field = value
            var change = false
            doNotInvalidate {
                value.also { max ->
                    pickedSingleDayCalendar?.let { single ->
                        if (DateUtils.isAfter(single, max)) {
                            pickedSingleDayCalendar = max
                            change = true
                        }
                    }
                    pickedRangeStartCalendar?.let { start ->
                        if (DateUtils.isAfter(start, max)) {
                            pickedRangeStartCalendar = null
                            pickedRangeEndCalendar = null
                            change = true
                        }
                    }
                    pickedRangeEndCalendar?.let { end ->
                        if (DateUtils.isAfter(end, max)) {
                            pickedRangeEndCalendar = max
                            change = true
                        }
                    }
                }
            }
            if (invalidate) {
                val maxOffset = value?.monthOffset() ?: Int.MAX_VALUE
                findLastVisibleItem()?.also { current ->
                    if (current.offset > maxOffset) {
                        value?.apply {
                            goto(year, month, false)
                        }
                    } else {
                        goto(current.year, current.month, false)
                    }
                }
            }
            notifyDayPicked(change)
        }

    override var pickType: PickType = PickType.NOTHING
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
            if (invalidate) adapter?.notifyDataSetChanged()
            notifyDayPicked(true)
        }

    var calendarType = CalendarType.CIVIL
        set(value) {
            val previous = calendarType
            field = value
            direction = when (locale.language) {
                "fa", "ar" -> when (value) {
                    CalendarType.CIVIL, CalendarType.JAPANESE -> Direction.LTR
                    CalendarType.PERSIAN, CalendarType.HIJRI -> Direction.RTL
                }
                else -> Direction.LTR
            }


            if (invalidate) {
                if (previous != value) {
                    layoutManager = createLayoutManager()
                    recyclerView.layoutManager = layoutManager
                    applyDividers()
                }
                goto(CalendarFactory.newInstance(value, locale), false)
            }
        }

    override var locale: Locale = Locale.getDefault()
        set(value) {
            field = value
            direction = when (value.language) {
                "fa", "ar" -> when (calendarType) {
                    CalendarType.CIVIL, CalendarType.JAPANESE -> Direction.LTR
                    CalendarType.PERSIAN, CalendarType.HIJRI -> Direction.RTL
                }
                else -> Direction.LTR
            }
            if (invalidate) adapter?.notifyDataSetChanged()
        }

    var flingOrientation = FlingOrientation.VERTICAL
        set(value) {
            field = value
            val calendar = CalendarFactory.newInstance(calendarType, locale)
            currentItemCalendar()?.let { current ->
                calendar.year = current.year
                calendar.month = current.month
            }
            layoutManager = createLayoutManager()
            recyclerView.layoutManager = layoutManager
            applyDividers()

            if (invalidate) goto(calendar, false)
        }

    // ---------------------------------------------------------------------------------------------

    private fun currentItemCalendar(): PrimeCalendar? = findFirstVisibleItem()?.run {
        val calendar = CalendarFactory.newInstance(calendarType, locale)
        calendar.set(year, month, 1)
        return calendar
    }

    private fun createLayoutManager(): LinearLayoutManager = when (flingOrientation) {
        FlingOrientation.VERTICAL -> LinearLayoutManager(context)
        FlingOrientation.HORIZONTAL -> LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, calendarType != CalendarType.CIVIL)
    }

    private fun applyDividers() {
        when (flingOrientation) {
            FlingOrientation.VERTICAL -> adapter?.setDivider(
                color = dividerColor,
                thickness = dividerThickness,
                insetLeft = dividerInsetLeft,
                insetRight = dividerInsetRight
            )
            FlingOrientation.HORIZONTAL -> adapter?.setDivider(
                color = dividerColor,
                thickness = dividerThickness,
                insetTop = dividerInsetTop,
                insetBottom = dividerInsetBottom
            )
        }
    }

    // ---------------------------------------------------------------------------------------------

    private var pickedDaysChanged: Boolean = false
    private var invalidate: Boolean = true

    fun invalidateAfter(function: () -> Unit) {
        val previous = invalidate
        invalidate = false
        function.invoke()
        invalidate = previous
        adapter?.notifyDataSetChanged()
    }

    fun doNotInvalidate(function: () -> Unit) {
        val previous = invalidate
        invalidate = false
        function.invoke()
        invalidate = previous
    }

    // ---------------------------------------------------------------------------------------------

    init {
        val layoutHeight = attrs?.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height")
        when {
            layoutHeight.equals(LayoutParams.MATCH_PARENT.toString()) -> definedHeight = LayoutParams.MATCH_PARENT
            layoutHeight.equals(LayoutParams.WRAP_CONTENT.toString()) -> definedHeight = LayoutParams.WRAP_CONTENT
            else -> context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.layout_height)).apply {
                definedHeight = getDimensionPixelSize(0, LayoutParams.WRAP_CONTENT)
                recycle()
            }
        }

        context.obtainStyledAttributes(attrs, R.styleable.PrimeCalendarView, defStyleAttr, defStyleRes).apply {
            doNotInvalidate {
                calendarType = CalendarType.values()[getInt(R.styleable.PrimeCalendarView_calendarType, DEFAULT_CALENDAR_TYPE.ordinal)]

                layoutManager = createLayoutManager()
                recyclerView.layoutManager = layoutManager

                flingOrientation = FlingOrientation.values()[getInt(R.styleable.PrimeCalendarView_flingOrientation, DEFAULT_FLING_ORIENTATION.ordinal)]

                loadFactor = getInteger(R.styleable.PrimeCalendarView_loadFactor, resources.getInteger(R.integer.defaultLoadFactor))
                maxTransitionLength = getInteger(R.styleable.PrimeCalendarView_maxTransitionLength, resources.getInteger(R.integer.defaultMaxTransitionLength))
                transitionSpeedFactor = getFloat(R.styleable.PrimeCalendarView_transitionSpeedFactor, resources.getString(R.string.defaultTransitionSpeedFactor).toFloat())

                dividerColor = getColor(R.styleable.PrimeCalendarView_dividerColor, ContextCompat.getColor(context, R.color.defaultDividerColor))
                dividerThickness = getDimensionPixelSize(R.styleable.PrimeCalendarView_dividerThickness, resources.getDimensionPixelSize(R.dimen.defaultDividerThickness))
                dividerInsetLeft = getDimensionPixelSize(R.styleable.PrimeCalendarView_dividerInsetLeft, resources.getDimensionPixelSize(R.dimen.defaultDividerInsetLeft))
                dividerInsetRight = getDimensionPixelSize(R.styleable.PrimeCalendarView_dividerInsetRight, resources.getDimensionPixelSize(R.dimen.defaultDividerInsetRight))
                dividerInsetTop = getDimensionPixelSize(R.styleable.PrimeCalendarView_dividerInsetTop, resources.getDimensionPixelSize(R.dimen.defaultDividerInsetTop))
                dividerInsetBottom = getDimensionPixelSize(R.styleable.PrimeCalendarView_dividerInsetBottom, resources.getDimensionPixelSize(R.dimen.defaultDividerInsetBottom))

                // Common Attributes ---------------------------------------------------------------

                monthLabelTextColor = getColor(R.styleable.PrimeCalendarView_monthLabelTextColor, ContextCompat.getColor(context, R.color.defaultMonthLabelTextColor))
                weekLabelTextColor = getColor(R.styleable.PrimeCalendarView_weekLabelTextColor, ContextCompat.getColor(context, R.color.defaultWeekLabelTextColor))
                dayLabelTextColor = getColor(R.styleable.PrimeCalendarView_dayLabelTextColor, ContextCompat.getColor(context, R.color.defaultDayLabelTextColor))
                todayLabelTextColor = getColor(R.styleable.PrimeCalendarView_todayLabelTextColor, ContextCompat.getColor(context, R.color.defaultTodayLabelTextColor))
                pickedDayLabelTextColor = getColor(R.styleable.PrimeCalendarView_pickedDayLabelTextColor, ContextCompat.getColor(context, R.color.defaultSelectedDayLabelTextColor))
                pickedDayCircleColor = getColor(R.styleable.PrimeCalendarView_pickedDayCircleColor, ContextCompat.getColor(context, R.color.defaultSelectedDayCircleColor))
                disabledDayLabelTextColor = getColor(R.styleable.PrimeCalendarView_disabledDayLabelTextColor, ContextCompat.getColor(context, R.color.defaultDisabledDayLabelTextColor))

                monthLabelTextSize = getDimensionPixelSize(R.styleable.PrimeCalendarView_monthLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultMonthLabelTextSize))
                weekLabelTextSize = getDimensionPixelSize(R.styleable.PrimeCalendarView_weekLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultWeekLabelTextSize))
                dayLabelTextSize = getDimensionPixelSize(R.styleable.PrimeCalendarView_dayLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultDayLabelTextSize))

                monthLabelTopPadding = getDimensionPixelSize(R.styleable.PrimeCalendarView_monthLabelTopPadding, resources.getDimensionPixelSize(R.dimen.defaultMonthLabelTopPadding))
                monthLabelBottomPadding = getDimensionPixelSize(R.styleable.PrimeCalendarView_monthLabelBottomPadding, resources.getDimensionPixelSize(R.dimen.defaultMonthLabelBottomPadding))
                weekLabelTopPadding = getDimensionPixelSize(R.styleable.PrimeCalendarView_weekLabelTopPadding, resources.getDimensionPixelSize(R.dimen.defaultWeekLabelTopPadding))
                weekLabelBottomPadding = getDimensionPixelSize(R.styleable.PrimeCalendarView_weekLabelBottomPadding, resources.getDimensionPixelSize(R.dimen.defaultWeekLabelBottomPadding))
                dayLabelVerticalPadding = getDimensionPixelSize(R.styleable.PrimeCalendarView_dayLabelVerticalPadding, resources.getDimensionPixelSize(R.dimen.defaultDayLabelVerticalPadding))

                showTwoWeeksInLandscape = getBoolean(R.styleable.PrimeCalendarView_showTwoWeeksInLandscape, resources.getBoolean(R.bool.defaultShowTwoWeeksInLandscape))

                animateSelection = getBoolean(R.styleable.PrimeCalendarView_animateSelection, resources.getBoolean(R.bool.defaultAnimateSelection))
                animationDuration = getInteger(R.styleable.PrimeCalendarView_animationDuration, resources.getInteger(R.integer.defaultAnimationDuration))
            }
            recycle()
        }

        applyDividers()

        addView(recyclerView)
        recyclerView.speedFactor = transitionSpeedFactor
        recyclerView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        recyclerView.addOnScrollListener(OnScrollListener())

        recyclerView.setHasFixedSize(true)
        StartSnapHelper().attachToRecyclerView(recyclerView)

        adapter = MonthListAdapter(recyclerView)
        adapter.iMonthViewHolderCallback = this
        recyclerView.adapter = adapter

        applyDividers()

        if (isInEditMode) {
            goto(CalendarFactory.newInstance(calendarType, locale), false)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        when (definedHeight) {
            ViewGroup.LayoutParams.MATCH_PARENT -> {
            }
            ViewGroup.LayoutParams.WRAP_CONTENT -> {
                if (detectedItemHeight > 0f) {
                    setMeasuredDimension(width, detectedItemHeight.toInt())
                }
            }
            in 0 until Int.MAX_VALUE -> {
                if (definedHeight > detectedItemHeight) {
                    setMeasuredDimension(width, definedHeight)
                } else {
                    setMeasuredDimension(width, detectedItemHeight.toInt())
                }
            }
        }
        recyclerView.height = measuredHeight
    }

    fun gotoNextMonth(animate: Boolean = true): Boolean {
        CalendarFactory.newInstance(calendarType, locale).apply {
            add(Calendar.MONTH, 1)
            return goto(year, month, animate)
        }
    }

    fun gotoPreviousMonth(animate: Boolean = true): Boolean {
        CalendarFactory.newInstance(calendarType, locale).apply {
            add(Calendar.MONTH, -1)
            return goto(year, month, animate)
        }
    }

    fun gotoNextYear(animate: Boolean = true): Boolean {
        CalendarFactory.newInstance(calendarType, locale).apply {
            add(Calendar.YEAR, 1)
            return goto(year, month, animate)
        }
    }

    fun gotoPreviousYear(animate: Boolean = true): Boolean {
        CalendarFactory.newInstance(calendarType, locale).apply {
            add(Calendar.YEAR, -1)
            return goto(year, month, animate)
        }
    }

    fun focusOnDay(calendar: PrimeCalendar) {
        toFocusDay = calendar
        findFirstVisibleItem()?.let { current ->
            if (current.year == calendar.year && current.month == calendar.month) {
                adapter?.notifyDataSetChanged()
            } else {
                goto(calendar, true)
            }
        } ?: goto(calendar, true)
    }

    fun goto(calendar: PrimeCalendar, animate: Boolean = false): Boolean {
        doNotInvalidate {
            locale = calendar.locale
            calendarType = calendar.calendarType
        }
        return goto(calendar.year, calendar.month, animate)
    }

    fun goto(year: Int, month: Int, animate: Boolean = false): Boolean {
        if (DateUtils.isOutOfRange(year, month, minDateCalendar, maxDateCalendar)) {
            return false
        }
        dataList = CalendarViewUtils.createPivotList(calendarType, year, month, minDateCalendar, maxDateCalendar, loadFactor)
        if (animate) {
            findFirstVisibleItem()?.let { current ->
                val transitionList = CalendarViewUtils.createTransitionList(
                    calendarType,
                    current.year,
                    current.month,
                    year,
                    month,
                    maxTransitionLength
                )

                val isForward = DateUtils.isBefore(current.year, current.month, year, month)
                transitionList?.apply {
                    var isLastTransitionItemRemoved = false
                    if (isForward) {
                        maxDateCalendar?.let { max ->
                            val maxOffset = max.monthOffset()
                            val targetOffset = year * 12 + month
                            if (maxOffset == targetOffset) {
                                transitionList.removeAt(transitionList.size - 1)
                                isLastTransitionItemRemoved = true
                            }
                        }
                    }
                    adapter.replaceDataList(this)
                    isInTransition = true
                    recyclerView.touchEnabled = false

                    gotoYear = year
                    gotoMonth = month

                    if (isForward) {
                        recyclerView.fastScrollTo(1)
                        recyclerView.smoothScrollTo(if (isLastTransitionItemRemoved) size - 1 else size - 2)
                    } else {
                        recyclerView.fastScrollTo(size - 2)
                        recyclerView.smoothScrollTo(1)
                    }
                }
            }
        } else {
            dataList?.apply {
                adapter.replaceDataList(this)
                findPositionInList(year, month, this)?.apply {
                    recyclerView.fastScrollTo(this)
                }
            }
        }
        return true
    }

    private fun findPositionInList(year: Int, month: Int, list: List<MonthDataHolder>?): Int? {
        list?.apply {
            val dataHolder = get(0)
            val firstOffset = dataHolder.offset
            val targetOffset = year * 12 + month
            return targetOffset - firstOffset
        }
        return null
    }

    private fun findFirstVisibleItem(): MonthDataHolder? {
        var position = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (position == RecyclerView.NO_POSITION) {
            position = layoutManager.findFirstVisibleItemPosition()
        }
        if (position != RecyclerView.NO_POSITION) {
            return adapter.getItem(position)
        }
        return null
    }

    private fun findLastVisibleItem(): MonthDataHolder? {
        var position = layoutManager.findLastCompletelyVisibleItemPosition()
        if (position == RecyclerView.NO_POSITION) {
            position = layoutManager.findLastVisibleItemPosition()
        }
        if (position != RecyclerView.NO_POSITION) {
            return adapter.getItem(position)
        }
        return null
    }

    override fun setFadingEdgeLength(length: Int) {
        recyclerView.setFadingEdgeLength(length)
    }

    override fun setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled: Boolean) {
        recyclerView.isHorizontalFadingEdgeEnabled = horizontalFadingEdgeEnabled
    }

    override fun setVerticalFadingEdgeEnabled(verticalFadingEdgeEnabled: Boolean) {
        recyclerView.isVerticalFadingEdgeEnabled = verticalFadingEdgeEnabled
    }

    override fun onDayPicked(pickType: PickType,
                             singleDay: PrimeCalendar?,
                             startDay: PrimeCalendar?,
                             endDay: PrimeCalendar?,
                             multipleDays: List<PrimeCalendar>?) {
        var change = false
        doNotInvalidate {
            when (pickType) {
                PickType.SINGLE -> {
                    pickedSingleDayCalendar = singleDay
                    change = true
                }
                PickType.RANGE_START -> {
                    startDay?.apply {
                        if (DateUtils.isAfter(startDay, pickedRangeEndCalendar)) {
                            pickedRangeEndCalendar = null
                        }
                    }
                    pickedRangeStartCalendar = startDay
                    change = true
                }
                PickType.RANGE_END -> {
                    if (endDay != null) {
                        if (pickedRangeStartCalendar != null && !DateUtils.isBefore(endDay, pickedRangeStartCalendar)) {
                            pickedRangeEndCalendar = endDay
                            change = true
                        }
                    } else {
                        pickedRangeEndCalendar = endDay
                        change = true
                    }
                }
                PickType.MULTIPLE -> {
                    pickedMultipleDaysList = multipleDays ?: arrayListOf()
                    change = true
                }
                PickType.NOTHING -> {
                }
            }
        }

        if (animateSelection) {
            needInvalidation = true
        } else {
            adapter?.notifyDataSetChanged()
        }

        notifyDayPicked(change)
    }

    private fun notifyDayPicked(change: Boolean) {
        pickedDaysChanged = pickedDaysChanged or change
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

    override fun onHeightDetect(height: Float) {
        if (detectedItemHeight == 0f && height > 0f) {
            detectedItemHeight = height
            requestLayout()
            invalidate()
        }
    }

    private inner class OnScrollListener : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(view: RecyclerView, newState: Int) {
            super.onScrollStateChanged(view, newState)
            when (newState) {
                RecyclerView.SCROLL_STATE_DRAGGING -> {
                }
                RecyclerView.SCROLL_STATE_SETTLING -> {
                }
                RecyclerView.SCROLL_STATE_IDLE -> {
                    if (isInTransition) {
                        postDelayed({
                            dataList?.apply {
                                adapter.replaceDataList(this)
                                findPositionInList(gotoYear, gotoMonth, this)?.apply {
                                    recyclerView.fastScrollTo(this)
                                }
                                isInTransition = false
                                recyclerView.touchEnabled = true
                            }
                        }, 10)
                    }
                }
            }
        }

        override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(view, dx, dy)
            if (needInvalidation) {
                needInvalidation = false
                adapter?.notifyDataSetChanged()
            }

            if (!isInTransition) {
                val factor = when (flingOrientation) {
                    FlingOrientation.VERTICAL -> dy
                    FlingOrientation.HORIZONTAL -> when (direction) {
                        Direction.LTR -> dx
                        Direction.RTL -> -dx
                    }
                }
                if (factor > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (!isInLoading && (visibleItemCount + lastVisibleItemPosition > totalItemCount)) {
                        isInLoading = true
                        val dataHolder = adapter.getItem(totalItemCount - 1)
                        val offset = dataHolder.offset
                        val maxOffset = maxDateCalendar?.monthOffset() ?: Int.MAX_VALUE

                        if (offset < maxOffset) {
                            val moreData = CalendarViewUtils.extendMoreList(
                                calendarType,
                                dataHolder.year,
                                dataHolder.month,
                                minDateCalendar,
                                maxDateCalendar,
                                loadFactor,
                                true
                            )

                            dataList?.apply {
                                addAll(size, moreData)
                                adapter.replaceDataList(this)
                            }
                        }
                        isInLoading = false
                    }
                } else if (factor < 0) {
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isInLoading && firstVisibleItemPosition == 0) {
                        isInLoading = true
                        val dataHolder = adapter.getItem(0)
                        val offset = dataHolder.offset
                        val minOffset = minDateCalendar?.monthOffset() ?: Int.MIN_VALUE

                        if (offset > minOffset) {
                            val moreData = CalendarViewUtils.extendMoreList(
                                calendarType,
                                dataHolder.year,
                                dataHolder.month,
                                minDateCalendar,
                                maxDateCalendar,
                                loadFactor,
                                false
                            )

                            dataList?.apply {
                                addAll(0, moreData)
                                adapter.replaceDataList(this)
                                recyclerView.fastScrollTo(moreData.size + 1)
                            }
                        }
                        isInLoading = false
                    }
                }
            }
        }
    }

    enum class FlingOrientation {
        VERTICAL,
        HORIZONTAL
    }

    // Save/Restore States -------------------------------------------------------------------------

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)

        savedState.calendarType = calendarType.ordinal
        savedState.locale = locale.language

        currentItemCalendar()?.apply {
            savedState.currentYear = year
            savedState.currentMonth = month
        }

        savedState.flingOrientation = flingOrientation.ordinal

        savedState.minDateCalendar = DateUtils.storeCalendar(minDateCalendar)
        savedState.maxDateCalendar = DateUtils.storeCalendar(maxDateCalendar)

        savedState.pickType = pickType.name
        savedState.pickedSingleDayCalendar = DateUtils.storeCalendar(pickedSingleDayCalendar)
        savedState.pickedRangeStartCalendar = DateUtils.storeCalendar(pickedRangeStartCalendar)
        savedState.pickedRangeEndCalendar = DateUtils.storeCalendar(pickedRangeEndCalendar)

        savedState.pickedMultipleDaysList = pickedMultipleDaysMap?.values?.map {
            DateUtils.storeCalendar(it)!!
        } ?: arrayListOf()

        savedState.loadFactor = loadFactor
        savedState.maxTransitionLength = maxTransitionLength
        savedState.transitionSpeedFactor = transitionSpeedFactor
        savedState.dividerColor = dividerColor
        savedState.dividerThickness = dividerThickness
        savedState.dividerInsetLeft = dividerInsetLeft
        savedState.dividerInsetRight = dividerInsetRight
        savedState.dividerInsetTop = dividerInsetTop
        savedState.dividerInsetBottom = dividerInsetBottom

        // Common Attributes -----------------------------------------------------------------------

        savedState.monthLabelTextColor = monthLabelTextColor
        savedState.weekLabelTextColor = weekLabelTextColor
        savedState.dayLabelTextColor = dayLabelTextColor
        savedState.todayLabelTextColor = todayLabelTextColor
        savedState.selectedDayLabelTextColor = pickedDayLabelTextColor
        savedState.selectedDayCircleColor = pickedDayCircleColor
        savedState.disabledDayLabelTextColor = disabledDayLabelTextColor
        savedState.monthLabelTextSize = monthLabelTextSize
        savedState.weekLabelTextSize = weekLabelTextSize
        savedState.dayLabelTextSize = dayLabelTextSize
        savedState.monthLabelTopPadding = monthLabelTopPadding
        savedState.monthLabelBottomPadding = monthLabelBottomPadding
        savedState.weekLabelTopPadding = weekLabelTopPadding
        savedState.weekLabelBottomPadding = weekLabelBottomPadding
        savedState.dayLabelVerticalPadding = dayLabelVerticalPadding
        savedState.twoWeeksInLandscape = showTwoWeeksInLandscape

        savedState.animateSelection = animateSelection
        savedState.animationDuration = animationDuration

        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)

        val currentYear = savedState.currentYear
        val currentMonth = savedState.currentMonth

        doNotInvalidate {
            calendarType = CalendarType.values()[savedState.calendarType]
            locale = Locale(savedState.locale)

            layoutManager = createLayoutManager()
            recyclerView.layoutManager = layoutManager

            flingOrientation = FlingOrientation.values()[savedState.flingOrientation]

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

            loadFactor = savedState.loadFactor
            maxTransitionLength = savedState.maxTransitionLength
            transitionSpeedFactor = savedState.transitionSpeedFactor
            dividerColor = savedState.dividerColor
            dividerThickness = savedState.dividerThickness
            dividerInsetLeft = savedState.dividerInsetLeft
            dividerInsetRight = savedState.dividerInsetRight
            dividerInsetTop = savedState.dividerInsetTop
            dividerInsetBottom = savedState.dividerInsetBottom

            // Common Attributes -------------------------------------------------------------------

            monthLabelTextColor = savedState.monthLabelTextColor
            weekLabelTextColor = savedState.weekLabelTextColor
            dayLabelTextColor = savedState.dayLabelTextColor
            todayLabelTextColor = savedState.todayLabelTextColor
            pickedDayLabelTextColor = savedState.selectedDayLabelTextColor
            pickedDayCircleColor = savedState.selectedDayCircleColor
            disabledDayLabelTextColor = savedState.disabledDayLabelTextColor
            monthLabelTextSize = savedState.monthLabelTextSize
            weekLabelTextSize = savedState.weekLabelTextSize
            dayLabelTextSize = savedState.dayLabelTextSize
            monthLabelTopPadding = savedState.monthLabelTopPadding
            monthLabelBottomPadding = savedState.monthLabelBottomPadding
            weekLabelTopPadding = savedState.weekLabelTopPadding
            weekLabelBottomPadding = savedState.weekLabelBottomPadding
            dayLabelVerticalPadding = savedState.dayLabelVerticalPadding
            showTwoWeeksInLandscape = savedState.twoWeeksInLandscape

            animateSelection = savedState.animateSelection
            animationDuration = savedState.animationDuration
        }

        applyDividers()
        goto(currentYear, currentMonth, false)
        notifyDayPicked(true)
    }

    private class SavedState : BaseSavedState {

        internal var calendarType: Int = 0
        internal var locale: String? = null
        internal var currentYear: Int = 0
        internal var currentMonth: Int = 0

        internal var flingOrientation: Int = 0

        internal var minDateCalendar: String? = null
        internal var maxDateCalendar: String? = null

        internal var pickType: String? = null
        internal var pickedSingleDayCalendar: String? = null
        internal var pickedRangeStartCalendar: String? = null
        internal var pickedRangeEndCalendar: String? = null
        internal var pickedMultipleDaysList: List<String>? = null

        internal var loadFactor: Int = 0
        internal var maxTransitionLength: Int = 0
        internal var transitionSpeedFactor: Float = 0f
        internal var dividerColor: Int = 0
        internal var dividerThickness: Int = 0
        internal var dividerInsetLeft: Int = 0
        internal var dividerInsetRight: Int = 0
        internal var dividerInsetTop: Int = 0
        internal var dividerInsetBottom: Int = 0

        // Common Attributes -----------------------------------------------------------------------

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

        internal var animateSelection: Boolean = false
        internal var animationDuration: Int = 0

        internal constructor(superState: Parcelable?) : super(superState)

        private constructor(input: Parcel) : super(input) {
            calendarType = input.readInt()
            locale = input.readString()
            currentYear = input.readInt()
            currentMonth = input.readInt()

            flingOrientation = input.readInt()

            minDateCalendar = input.readString()
            maxDateCalendar = input.readString()

            pickType = input.readString()
            pickedSingleDayCalendar = input.readString()
            pickedRangeStartCalendar = input.readString()
            pickedRangeEndCalendar = input.readString()
            input.readStringList(pickedMultipleDaysList)

            loadFactor = input.readInt()
            maxTransitionLength = input.readInt()
            transitionSpeedFactor = input.readFloat()
            dividerColor = input.readInt()
            dividerThickness = input.readInt()
            dividerInsetLeft = input.readInt()
            dividerInsetRight = input.readInt()
            dividerInsetTop = input.readInt()
            dividerInsetBottom = input.readInt()

            // Common Attributes -------------------------------------------------------------------

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

            animateSelection = input.readInt() == 1
            animationDuration = input.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(calendarType)
            out.writeString(locale)
            out.writeInt(currentYear)
            out.writeInt(currentMonth)

            out.writeInt(flingOrientation)

            out.writeString(minDateCalendar)
            out.writeString(maxDateCalendar)

            out.writeString(pickType)
            out.writeString(pickedSingleDayCalendar)
            out.writeString(pickedRangeStartCalendar)
            out.writeString(pickedRangeEndCalendar)
            out.writeStringList(pickedMultipleDaysList)

            out.writeInt(loadFactor)
            out.writeInt(maxTransitionLength)
            out.writeFloat(transitionSpeedFactor)
            out.writeInt(dividerColor)
            out.writeInt(dividerThickness)
            out.writeInt(dividerInsetLeft)
            out.writeInt(dividerInsetRight)
            out.writeInt(dividerInsetTop)
            out.writeInt(dividerInsetBottom)

            // Common Attributes -------------------------------------------------------------------

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
        private val DEFAULT_FLING_ORIENTATION = FlingOrientation.VERTICAL
    }

}