package com.aminography.primedatepicker.calendarview

import android.content.Context
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.aminography.primeadapter.PrimeAdapter
import com.aminography.primeadapter.PrimeDataHolder
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.Direction
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.calendarview.adapter.MonthListAdapter
import com.aminography.primedatepicker.calendarview.callback.IMonthViewHolderCallback
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder
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

    private var adapter: MonthListAdapter
    private var recyclerView = TouchControllableRecyclerView(context)
    private lateinit var layoutManager: LinearLayoutManager
    private var dataList: MutableList<PrimeDataHolder>? = null
    private var isInTransition = false
    private var isInLoading = false

    private var definedHeight: Int = 0
    private var detectedItemHeight: Float = 0f

    private var direction = Direction.LTR

    private var gotoYear: Int = 0
    private var gotoMonth: Int = 0

    // Listeners -----------------------------------------------------------------------------------

    var onDayPickedListener: OnDayPickedListener? = null

    // Control Variables ---------------------------------------------------------------------------

    var heightMultiplier: Float = 0f
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

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

    override var pickedSingleDayCalendar: BaseCalendar? = null
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
            notifyDayPicked(true)
        }

    override var pickedStartRangeCalendar: BaseCalendar? = null
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
            notifyDayPicked(true)
        }

    override var pickedEndRangeCalendar: BaseCalendar? = null
        set(value) {
            field = value
            if (invalidate) adapter?.notifyDataSetChanged()
            notifyDayPicked(true)
        }

    override var minDateCalendar: BaseCalendar? = null
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

    override var maxDateCalendar: BaseCalendar? = null
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
            if (invalidate) adapter?.notifyDataSetChanged()
            notifyDayPicked(true)
        }

    var calendarType = CalendarType.CIVIL
        set(value) {
            field = value
            direction = when (value) {
                CalendarType.CIVIL -> Direction.LTR
                CalendarType.PERSIAN, CalendarType.HIJRI -> Direction.RTL
            }

            layoutManager = createLayoutManager()
            recyclerView.layoutManager = layoutManager
            applyDividers()

            if (invalidate) goto(CalendarFactory.newInstance(value), false)
        }


    var flingOrientation = FlingOrientation.VERTICAL
        set(value) {
            field = value
            val calendar = CalendarFactory.newInstance(calendarType)
            currentItemCalendar()?.let { current ->
                calendar.year = current.year
                calendar.month = current.month
            }
            layoutManager = createLayoutManager()
            recyclerView.layoutManager = layoutManager
            applyDividers()
//            applyFadingEdges()

            if (invalidate) goto(calendar, false)
        }

    // ---------------------------------------------------------------------------------------------

    private fun currentItemCalendar(): BaseCalendar? = findFirstVisibleItem()?.run {
        val calendar = CalendarFactory.newInstance(calendarType)
        calendar.setDate(year, month, 1)
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
            layoutHeight.equals(ViewGroup.LayoutParams.MATCH_PARENT.toString()) ->
                definedHeight = ViewGroup.LayoutParams.MATCH_PARENT
            layoutHeight.equals(ViewGroup.LayoutParams.WRAP_CONTENT.toString()) ->
                definedHeight = ViewGroup.LayoutParams.WRAP_CONTENT
            else -> context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.layout_height)).apply {
                definedHeight = getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT)
                recycle()
            }
        }

        context.obtainStyledAttributes(attrs, R.styleable.PrimeCalendarView, defStyleAttr, defStyleRes).apply {
            doNotInvalidate {
                calendarType = CalendarType.values()[getInt(R.styleable.PrimeCalendarView_calendarType, DEFAULT_CALENDAR_TYPE.ordinal)]
                flingOrientation = FlingOrientation.values()[getInt(R.styleable.PrimeCalendarView_flingOrientation, DEFAULT_FLING_ORIENTATION.ordinal)]

                heightMultiplier = getFloat(R.styleable.PrimeCalendarView_heightMultiplier, resources.getString(R.string.defaultHeightMultiplier).toFloat())
                loadFactor = getInteger(R.styleable.PrimeCalendarView_loadFactor, resources.getInteger(R.integer.defaultLoadFactor))
                maxTransitionLength = getInteger(R.styleable.PrimeCalendarView_maxTransitionLength, resources.getInteger(R.integer.defaultMaxTransitionLength))
                transitionSpeedFactor = getFloat(R.styleable.PrimeCalendarView_transitionSpeedFactor, resources.getString(R.string.defaultTransitionSpeedFactor).toFloat())

                dividerColor = getColor(R.styleable.PrimeCalendarView_dividerColor, ContextCompat.getColor(context, R.color.defaultDividerColor))
                dividerThickness = getDimensionPixelSize(R.styleable.PrimeCalendarView_dividerThickness, resources.getDimensionPixelSize(R.dimen.defaultDividerThickness))
                dividerInsetLeft = getDimensionPixelSize(R.styleable.PrimeCalendarView_dividerInsetLeft, resources.getDimensionPixelSize(R.dimen.defaultDividerInsetLeft))
                dividerInsetRight = getDimensionPixelSize(R.styleable.PrimeCalendarView_dividerInsetRight, resources.getDimensionPixelSize(R.dimen.defaultDividerInsetRight))
                dividerInsetTop = getDimensionPixelSize(R.styleable.PrimeCalendarView_dividerInsetTop, resources.getDimensionPixelSize(R.dimen.defaultDividerInsetTop))
                dividerInsetBottom = getDimensionPixelSize(R.styleable.PrimeCalendarView_dividerInsetBottom, resources.getDimensionPixelSize(R.dimen.defaultDividerInsetBottom))
            }
            recycle()
        }

        addView(recyclerView)
        recyclerView.speedFactor = transitionSpeedFactor
        recyclerView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        recyclerView.addOnScrollListener(OnScrollListener())

        adapter = PrimeAdapter.with(recyclerView)
                .setSnapHelper(StartSnapHelper())
                .setHasFixedSize(true)
                .set()
                .build(MonthListAdapter::class.java)

        applyDividers()
//        applyFadingEdges()

        adapter.iMonthViewHolderCallback = this

        if (isInEditMode) {
            goto(CalendarFactory.newInstance(calendarType), false)
        }
    }

//    private fun applyFadingEdges() {
//        when (internalFlingOrientation) {
//            FlingOrientation.VERTICAL -> {
//                isVerticalFadingEdgeEnabled = true
//                isHorizontalFadingEdgeEnabled = false
//                setFadingEdgeLength(resources.getDimensionPixelSize(R.dimen.defaultFadingEdgeLength))
//            }
//            FlingOrientation.HORIZONTAL -> {
//                isVerticalFadingEdgeEnabled = false
//                isHorizontalFadingEdgeEnabled = true
//                setFadingEdgeLength(resources.getDimensionPixelSize(R.dimen.defaultFadingEdgeLength))
//            }
//        }
//    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        when (definedHeight) {
            ViewGroup.LayoutParams.MATCH_PARENT -> {
            }
            ViewGroup.LayoutParams.WRAP_CONTENT -> {
                if (detectedItemHeight > 0f) {
                    val height = (detectedItemHeight * heightMultiplier).toInt()
                    setMeasuredDimension(width, height)
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
        recyclerView.setSize(width, measuredHeight)
    }

    fun gotoNextMonth(animate: Boolean = true): Boolean {
        CalendarFactory.newInstance(calendarType).apply {
            add(Calendar.MONTH, 1)
            return goto(year, month, animate)
        }
    }

    fun gotoPreviousMonth(animate: Boolean = true): Boolean {
        CalendarFactory.newInstance(calendarType).apply {
            add(Calendar.MONTH, -1)
            return goto(year, month, animate)
        }
    }

    fun gotoNextYear(animate: Boolean = true): Boolean {
        CalendarFactory.newInstance(calendarType).apply {
            add(Calendar.YEAR, 1)
            return goto(year, month, animate)
        }
    }

    fun gotoPreviousYear(animate: Boolean = true): Boolean {
        CalendarFactory.newInstance(calendarType).apply {
            add(Calendar.YEAR, -1)
            return goto(year, month, animate)
        }
    }

    fun goto(calendar: BaseCalendar, animate: Boolean = false): Boolean {
        return goto(calendar.year, calendar.month, animate)
    }

    fun goto(year: Int, month: Int, animate: Boolean = false): Boolean {
        if (DateUtils.isOutOfRange(year, month, minDateCalendar, maxDateCalendar)) {
            return false
        }
        dataList = CalendarViewUtils.createPivotList(calendarType, year, month, minDateCalendar, maxDateCalendar, loadFactor)
        if (animate) {
            findFirstVisibleItem()?.let { current ->
                val transitionList = CalendarViewUtils.createTransitionList(calendarType, current.year, current.month, year, month, maxTransitionLength)
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

    private fun findPositionInList(year: Int, month: Int, list: List<PrimeDataHolder>?): Int? {
        list?.apply {
            val dataHolder = get(0) as MonthDataHolder
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
            return adapter.getItem(position) as MonthDataHolder
        }
        return null
    }

    private fun findLastVisibleItem(): MonthDataHolder? {
        var position = layoutManager.findLastCompletelyVisibleItemPosition()
        if (position == RecyclerView.NO_POSITION) {
            position = layoutManager.findLastVisibleItemPosition()
        }
        if (position != RecyclerView.NO_POSITION) {
            return adapter.getItem(position) as MonthDataHolder
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

    override fun onDayPicked(pickType: PickType, singleDay: BaseCalendar?, startDay: BaseCalendar?, endDay: BaseCalendar?) {
        var change = false
        invalidateAfter {
            when (pickType) {
                PickType.SINGLE -> {
                    pickedSingleDayCalendar = singleDay
                    change = true
                }
                PickType.START_RANGE -> {
                    startDay?.apply {
                        if (DateUtils.isAfter(startDay, pickedEndRangeCalendar)) {
                            pickedEndRangeCalendar = null
                        }
                    }
                    pickedStartRangeCalendar = startDay
                    change = true
                }
                PickType.END_RANGE -> {
                    if (endDay != null) {
                        if (pickedStartRangeCalendar != null && !DateUtils.isBefore(endDay, pickedStartRangeCalendar)) {
                            pickedEndRangeCalendar = endDay
                            change = true
                        }
                    } else {
                        pickedEndRangeCalendar = endDay
                        change = true
                    }
                }
                PickType.NOTHING -> {
                }
            }
        }

        // to update MonthViews:
        findLastVisibleItem()?.apply {
            goto(year, month, false)
        }
        notifyDayPicked(change)
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
            if (!isInTransition) {
                val factor = when (flingOrientation) {
                    FlingOrientation.VERTICAL -> dy
                    FlingOrientation.HORIZONTAL -> when (direction) {
                        Direction.LTR -> dx
                        Direction.RTL -> -dx
                    }
                }
                if (factor > 0) { // scroll down / left
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isInLoading && (visibleItemCount + firstVisibleItemPosition >= totalItemCount)) {
                        isInLoading = true
                        val dataHolder = adapter.getItem(totalItemCount - 1) as MonthDataHolder
                        val offset = dataHolder.offset
                        val maxOffset = maxDateCalendar?.monthOffset() ?: Int.MAX_VALUE

                        if (offset < maxOffset) {
                            val moreData = CalendarViewUtils.extendMoreList(calendarType, dataHolder.year, dataHolder.month, minDateCalendar, maxDateCalendar, loadFactor, true)
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
                        val dataHolder = adapter.getItem(0) as MonthDataHolder
                        val offset = dataHolder.offset
                        val minOffset = minDateCalendar?.monthOffset() ?: Int.MIN_VALUE

                        if (offset > minOffset) {
                            val moreData = CalendarViewUtils.extendMoreList(calendarType, dataHolder.year, dataHolder.month, minDateCalendar, maxDateCalendar, loadFactor, false)
                            dataList?.apply {
                                addAll(0, moreData)
                                adapter.replaceDataList(this)
                                findPositionInList(dataHolder.year, dataHolder.month, this)?.apply {
                                    recyclerView.fastScrollTo(this)
                                }
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
        currentItemCalendar()?.apply {
            savedState.currentYear = year
            savedState.currentMonth = month
        }

        savedState.flingOrientation = flingOrientation.ordinal

        savedState.minDateCalendar = DateUtils.storeCalendar(minDateCalendar)
        savedState.maxDateCalendar = DateUtils.storeCalendar(maxDateCalendar)

        savedState.pickType = pickType.name
        savedState.pickedSingleDayCalendar = DateUtils.storeCalendar(pickedSingleDayCalendar)
        savedState.pickedStartRangeCalendar = DateUtils.storeCalendar(pickedStartRangeCalendar)
        savedState.pickedEndRangeCalendar = DateUtils.storeCalendar(pickedEndRangeCalendar)

        savedState.heightMultiplier = heightMultiplier
        savedState.loadFactor = loadFactor
        savedState.maxTransitionLength = maxTransitionLength
        savedState.transitionSpeedFactor = transitionSpeedFactor
        savedState.dividerColor = dividerColor
        savedState.dividerThickness = dividerThickness
        savedState.dividerInsetLeft = dividerInsetLeft
        savedState.dividerInsetRight = dividerInsetRight
        savedState.dividerInsetTop = dividerInsetTop
        savedState.dividerInsetBottom = dividerInsetBottom
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)

        val currentYear = savedState.currentYear
        val currentMonth = savedState.currentMonth

        doNotInvalidate {
            calendarType = CalendarType.values()[savedState.calendarType]

            flingOrientation = FlingOrientation.values()[savedState.flingOrientation]

            minDateCalendar = DateUtils.restoreCalendar(savedState.minDateCalendar)
            maxDateCalendar = DateUtils.restoreCalendar(savedState.maxDateCalendar)

            pickType = savedState.pickType?.let {
                PickType.valueOf(it)
            } ?: PickType.NOTHING
            pickedSingleDayCalendar = DateUtils.restoreCalendar(savedState.pickedSingleDayCalendar)
            pickedStartRangeCalendar = DateUtils.restoreCalendar(savedState.pickedStartRangeCalendar)
            pickedEndRangeCalendar = DateUtils.restoreCalendar(savedState.pickedEndRangeCalendar)

            heightMultiplier = savedState.heightMultiplier
            loadFactor = savedState.loadFactor
            maxTransitionLength = savedState.maxTransitionLength
            transitionSpeedFactor = savedState.transitionSpeedFactor
            dividerColor = savedState.dividerColor
            dividerThickness = savedState.dividerThickness
            dividerInsetLeft = savedState.dividerInsetLeft
            dividerInsetRight = savedState.dividerInsetRight
            dividerInsetTop = savedState.dividerInsetTop
            dividerInsetBottom = savedState.dividerInsetBottom
        }

        applyDividers()
        goto(currentYear, currentMonth, false)
        notifyDayPicked(true)
    }

    private class SavedState : BaseSavedState {

        internal var calendarType: Int = 0
        internal var currentYear: Int = 0
        internal var currentMonth: Int = 0

        internal var flingOrientation: Int = 0

        internal var minDateCalendar: String? = null
        internal var maxDateCalendar: String? = null

        internal var pickType: String? = null
        internal var pickedSingleDayCalendar: String? = null
        internal var pickedStartRangeCalendar: String? = null
        internal var pickedEndRangeCalendar: String? = null

        internal var heightMultiplier: Float = 0f
        internal var loadFactor: Int = 0
        internal var maxTransitionLength: Int = 0
        internal var transitionSpeedFactor: Float = 0f
        internal var dividerColor: Int = 0
        internal var dividerThickness: Int = 0
        internal var dividerInsetLeft: Int = 0
        internal var dividerInsetRight: Int = 0
        internal var dividerInsetTop: Int = 0
        internal var dividerInsetBottom: Int = 0

        internal constructor(superState: Parcelable?) : super(superState)

        private constructor(input: Parcel) : super(input) {
            calendarType = input.readInt()
            currentYear = input.readInt()
            currentMonth = input.readInt()

            flingOrientation = input.readInt()

            minDateCalendar = input.readString()
            maxDateCalendar = input.readString()

            pickType = input.readString()
            pickedSingleDayCalendar = input.readString()
            pickedStartRangeCalendar = input.readString()
            pickedEndRangeCalendar = input.readString()

            heightMultiplier = input.readFloat()
            loadFactor = input.readInt()
            maxTransitionLength = input.readInt()
            transitionSpeedFactor = input.readFloat()
            dividerColor = input.readInt()
            dividerThickness = input.readInt()
            dividerInsetLeft = input.readInt()
            dividerInsetRight = input.readInt()
            dividerInsetTop = input.readInt()
            dividerInsetBottom = input.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(calendarType)
            out.writeInt(currentYear)
            out.writeInt(currentMonth)

            out.writeInt(flingOrientation)

            out.writeString(minDateCalendar)
            out.writeString(maxDateCalendar)

            out.writeString(pickType)
            out.writeString(pickedSingleDayCalendar)
            out.writeString(pickedStartRangeCalendar)
            out.writeString(pickedEndRangeCalendar)

            out.writeFloat(heightMultiplier)
            out.writeInt(loadFactor)
            out.writeInt(maxTransitionLength)
            out.writeFloat(transitionSpeedFactor)
            out.writeInt(dividerColor)
            out.writeInt(dividerThickness)
            out.writeInt(dividerInsetLeft)
            out.writeInt(dividerInsetRight)
            out.writeInt(dividerInsetTop)
            out.writeInt(dividerInsetBottom)
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