package com.aminography.primedatepicker.calendarview

import android.content.Context
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
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.calendarview.adapter.MonthListAdapter
import com.aminography.primedatepicker.calendarview.callback.IMonthViewHolderCallback
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder
import com.aminography.primedatepicker.tools.DateUtils
import com.aminography.primedatepicker.tools.monthOffset


/**
 * @author aminography
 */
@Suppress("PrivatePropertyName")
class PrimeCalendarView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0,
        @Suppress("UNUSED_PARAMETER") @StyleRes defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IMonthViewHolderCallback {

    private var adapter: MonthListAdapter
    private var recyclerView = TouchControllableRecyclerView(context)
    private var layoutManager = LinearLayoutManager(context)
    private var dataList: MutableList<PrimeDataHolder>? = null
    private var isInTransition = false
    private var isInLoading = false

    var onDayClickListener: OnDayClickListener? = null

    private var definedHeight: Int = 0
    private var detectedItemHeight: Float = 0f

    private var heightMultiplier: Float = DEFAULT_HEIGHT_MULTIPLIER
    private var loadFactor: Int = DEFAULT_LOAD_FACTOR
    private var maxTransitionLength: Int = DEFAULT_MAX_TRANSITION_LENGTH
    private var transitionSpeedFactor: Float = TouchControllableRecyclerView.DEFAULT_TRANSITION_SPEED_FACTOR

    private var dividerColor: Int = 0
    private var dividerThickness: Int = 0
    private var dividerInsetLeft: Int = 0
    private var dividerInsetRight: Int = 0

    private var gotoYear: Int = 0
    private var gotoMonth: Int = 0

    internal var internalPickedSingleDayCalendar: BaseCalendar? = null
    internal var internalPickedStartRangeCalendar: BaseCalendar? = null
    internal var internalPickedEndRangeCalendar: BaseCalendar? = null

    override var pickedSingleDayCalendar: BaseCalendar?
        get() = internalPickedSingleDayCalendar
        set(value) {
            internalPickedSingleDayCalendar = value
            @Suppress("UNNECESSARY_SAFE_CALL")
            adapter?.notifyDataSetChanged()
        }

    override var pickedStartRangeCalendar: BaseCalendar?
        get() = internalPickedStartRangeCalendar
        set(value) {
            internalPickedStartRangeCalendar = value
            @Suppress("UNNECESSARY_SAFE_CALL")
            adapter?.notifyDataSetChanged()
        }

    override var pickedEndRangeCalendar: BaseCalendar?
        get() = internalPickedEndRangeCalendar
        set(value) {
            internalPickedEndRangeCalendar = value
            @Suppress("UNNECESSARY_SAFE_CALL")
            adapter?.notifyDataSetChanged()
        }

    internal var internalMinDateCalendar: BaseCalendar? = null
    internal var internalMaxDateCalendar: BaseCalendar? = null

    override var minDateCalendar: BaseCalendar?
        get() = internalMinDateCalendar
        set(value) {
            internalMinDateCalendar = value
            val minOffset = value?.monthOffset() ?: Int.MIN_VALUE

            findFirstVisibleItem()?.also { current ->
                if (current.offset < minOffset) {
                    minDateCalendar?.apply {
                        goto(year, month, false)
                    }
                } else {
                    goto(current.year, current.month, false)
                }
            }
        }

    override var maxDateCalendar: BaseCalendar?
        get() = internalMaxDateCalendar
        set(value) {
            internalMaxDateCalendar = value
            val maxOffset = value?.monthOffset() ?: Int.MAX_VALUE

            findLastVisibleItem()?.also { current ->
                if (current.offset > maxOffset) {
                    maxDateCalendar?.apply {
                        goto(year, month, false)
                    }
                } else {
                    goto(current.year, current.month, false)
                }
            }
        }

    internal var internalPickType: PickType = PickType.NOTHING
        set(value) {
            field = value
            when (value) {
                PickType.SINGLE -> {
                    internalPickedStartRangeCalendar = null
                    internalPickedEndRangeCalendar = null
                }
                PickType.START_RANGE -> internalPickedSingleDayCalendar = null
                PickType.END_RANGE -> internalPickedSingleDayCalendar = null
                PickType.NOTHING -> {
                    internalPickedSingleDayCalendar = null
                    internalPickedStartRangeCalendar = null
                    internalPickedEndRangeCalendar = null
                }
            }
        }

    override var pickType: PickType
        get() = internalPickType
        set(value) {
            internalPickType = value
            @Suppress("UNNECESSARY_SAFE_CALL")
            adapter?.notifyDataSetChanged()
        }

    @Suppress("RedundantSetter")
    internal var internalCalendarType = CalendarType.CIVIL
        set(value) {
            field = value
//            internalPickedSingleDayCalendar = null
//            internalPickedStartRangeCalendar = null
//            internalPickedEndRangeCalendar = null
//            internalMinDateCalendar = null
//            internalMaxDateCalendar = null
//            internalPickType = PickType.NOTHING
        }

    var calendarType: CalendarType
        get() = internalCalendarType
        set(value) {
            internalCalendarType = value

            val calendar = CalendarFactory.newInstance(value)
            currentItemCalendar?.apply {
                dayOfMonth = 1
                val target = when (value) {
                    CalendarType.CIVIL -> toCivil()
                    CalendarType.PERSIAN -> toPersian()
                    CalendarType.HIJRI -> toHijri()
                }
                calendar.setDate(target.year, target.month, target.dayOfMonth)
            }
            goto(calendar, false)
        }

    private val currentItemCalendar: BaseCalendar?
        get() {
            return findFirstVisibleItem()?.run {
                val calendar = CalendarFactory.newInstance(calendarType)
                calendar.setDate(year, month, 1)
                return calendar
            }
        }

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
            internalCalendarType = CalendarType.values()[getInt(R.styleable.PrimeCalendarView_calendarType, CalendarType.CIVIL.ordinal)]

            heightMultiplier = getFloat(R.styleable.PrimeCalendarView_heightMultiplier, DEFAULT_HEIGHT_MULTIPLIER)
            loadFactor = getInteger(R.styleable.PrimeCalendarView_loadFactor, DEFAULT_LOAD_FACTOR)
            maxTransitionLength = getInteger(R.styleable.PrimeCalendarView_maxTransitionLength, DEFAULT_MAX_TRANSITION_LENGTH)
            transitionSpeedFactor = getFloat(R.styleable.PrimeCalendarView_transitionSpeedFactor, TouchControllableRecyclerView.DEFAULT_TRANSITION_SPEED_FACTOR)

            dividerColor = getColor(R.styleable.PrimeCalendarView_dividerColor, ContextCompat.getColor(context, R.color.defaultDividerColor))
            dividerThickness = getDimensionPixelSize(R.styleable.PrimeCalendarView_dividerThickness, resources.getDimensionPixelSize(R.dimen.defaultdividerThickness))
            dividerInsetLeft = getDimensionPixelSize(R.styleable.PrimeCalendarView_dividerInsetLeft, resources.getDimensionPixelSize(R.dimen.defaultDividerInsetLeft))
            dividerInsetRight = getDimensionPixelSize(R.styleable.PrimeCalendarView_dividerInsetRight, resources.getDimensionPixelSize(R.dimen.defaultDividerInsetRight))

            recycle()
        }

        addView(recyclerView)
        recyclerView.speedFactor = transitionSpeedFactor
        recyclerView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        recyclerView.addOnScrollListener(OnScrollListener())

        adapter = PrimeAdapter.with(recyclerView)
                .setLayoutManager(layoutManager)
                .setSnapHelper(StartSnapHelper())
                .setHasFixedSize(true)
                .setDivider(color = dividerColor, thickness = dividerThickness, insetLeft = dividerInsetLeft, insetRight = dividerInsetRight)
                .set()
                .build(MonthListAdapter::class.java)

        adapter.iMonthViewHolderCallback = this

        if (isInEditMode) {
            val calendar = CalendarFactory.newInstance(internalCalendarType)
            goto(calendar.year, calendar.month, false)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        when (definedHeight) {
            ViewGroup.LayoutParams.MATCH_PARENT -> {
            }
            ViewGroup.LayoutParams.WRAP_CONTENT -> {
                if (detectedItemHeight > 0f) {
                    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), detectedItemHeight.toInt())
                }
            }
            in 0 until Int.MAX_VALUE -> {
                if (definedHeight > detectedItemHeight) {
                    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), definedHeight)
                } else {
                    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), detectedItemHeight.toInt())
                }
            }
        }
        recyclerView.layoutParams.height = measuredHeight
    }

    fun goto(calendar: BaseCalendar, animate: Boolean = false): Boolean {
        return goto(calendar.year, calendar.month, animate)
    }

    fun goto(year: Int, month: Int, animate: Boolean = false): Boolean {
        if (DateUtils.isOutOfRange(year, month, internalMinDateCalendar, internalMaxDateCalendar)) {
            return false
        }
        dataList = CalendarViewUtils.createPivotList(internalCalendarType, year, month, internalMinDateCalendar, internalMaxDateCalendar, loadFactor)
        if (animate) {
            findFirstVisibleItem()?.let { current ->
                val transitionList = CalendarViewUtils.createTransitionList(internalCalendarType, current.year, current.month, year, month, maxTransitionLength)
                val isForward = DateUtils.isBefore(current.year, current.month, year, month)
                transitionList?.apply {
                    var isLastTransitionItemRemoved = false
                    if (isForward) {
                        internalMaxDateCalendar?.let { max ->
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

    override fun onDayClick(day: BaseCalendar) {
        when (internalPickType) {
            PickType.SINGLE -> {
                internalPickedSingleDayCalendar = day
            }
            PickType.START_RANGE -> {
                if (DateUtils.isAfter(day.year, day.month, day.dayOfMonth, internalPickedEndRangeCalendar)) {
                    internalPickedEndRangeCalendar = null
                }
                internalPickedStartRangeCalendar = day
            }
            PickType.END_RANGE -> {
                if (!DateUtils.isBefore(day.year, day.month, day.dayOfMonth, internalPickedStartRangeCalendar)) {
                    internalPickedEndRangeCalendar = day
                }
            }
            PickType.NOTHING -> {
            }
        }

        // to update MonthViews:
        findLastVisibleItem()?.apply {
            goto(year, month, false)
        }

        onDayClickListener?.onDayClick(this, day)
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
                if (dy > 0) { // scroll down
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isInLoading && (visibleItemCount + firstVisibleItemPosition >= totalItemCount)) {
                        isInLoading = true
                        val dataHolder = adapter.getItem(totalItemCount - 1) as MonthDataHolder
                        val offset = dataHolder.offset
                        val maxOffset = internalMaxDateCalendar?.monthOffset() ?: Int.MAX_VALUE

                        if (offset < maxOffset) {
                            val moreData = CalendarViewUtils.extendMoreList(internalCalendarType, dataHolder.year, dataHolder.month, internalMinDateCalendar, internalMaxDateCalendar, loadFactor, true)
                            dataList?.apply {
                                addAll(size, moreData)
                                adapter.replaceDataList(this)
                            }
                        }
                        isInLoading = false
                    }
                } else if (dy < 0) {
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isInLoading && firstVisibleItemPosition == 0) {
                        isInLoading = true
                        val dataHolder = adapter.getItem(0) as MonthDataHolder
                        val offset = dataHolder.offset
                        val minOffset = internalMinDateCalendar?.monthOffset() ?: Int.MIN_VALUE

                        if (offset > minOffset) {
                            val moreData = CalendarViewUtils.extendMoreList(internalCalendarType, dataHolder.year, dataHolder.month, internalMinDateCalendar, internalMaxDateCalendar, loadFactor, false)
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

    interface OnDayClickListener {
        fun onDayClick(calendarView: PrimeCalendarView, day: BaseCalendar)
    }

    // Save/Restore States -------------------------------------------------------------------------

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)

        savedState.calendarType = internalCalendarType.ordinal
        currentItemCalendar?.apply {
            savedState.currentYear = year
            savedState.currentMonth = month
        }

        savedState.minDateCalendar = DateUtils.storeCalendar(internalMinDateCalendar)
        savedState.maxDateCalendar = DateUtils.storeCalendar(internalMaxDateCalendar)

        savedState.pickType = internalPickType.name
        savedState.pickedSingleDayCalendar = DateUtils.storeCalendar(internalPickedSingleDayCalendar)
        savedState.pickedStartRangeCalendar = DateUtils.storeCalendar(internalPickedStartRangeCalendar)
        savedState.pickedEndRangeCalendar = DateUtils.storeCalendar(internalPickedEndRangeCalendar)
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)

        internalCalendarType = CalendarType.values()[savedState.calendarType]
        val currentYear = savedState.currentYear
        val currentMonth = savedState.currentMonth

        internalMinDateCalendar = DateUtils.restoreCalendar(savedState.minDateCalendar)
        internalMaxDateCalendar = DateUtils.restoreCalendar(savedState.maxDateCalendar)

        internalPickType = savedState.pickType?.let {
            PickType.valueOf(it)
        } ?: PickType.NOTHING
        internalPickedSingleDayCalendar = DateUtils.restoreCalendar(savedState.pickedSingleDayCalendar)
        internalPickedStartRangeCalendar = DateUtils.restoreCalendar(savedState.pickedStartRangeCalendar)
        internalPickedEndRangeCalendar = DateUtils.restoreCalendar(savedState.pickedEndRangeCalendar)

        goto(currentYear, currentMonth, false)
    }

    private class SavedState : BaseSavedState {

        internal var calendarType: Int = 0
        internal var currentYear: Int = 0
        internal var currentMonth: Int = 0

        internal var minDateCalendar: String? = null
        internal var maxDateCalendar: String? = null

        internal var pickType: String? = null
        internal var pickedSingleDayCalendar: String? = null
        internal var pickedStartRangeCalendar: String? = null
        internal var pickedEndRangeCalendar: String? = null

        internal constructor(superState: Parcelable?) : super(superState)

        private constructor(input: Parcel) : super(input) {
            calendarType = input.readInt()
            currentYear = input.readInt()
            currentMonth = input.readInt()

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
            out.writeInt(currentYear)
            out.writeInt(currentMonth)

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

    companion object {
        private const val DEFAULT_HEIGHT_MULTIPLIER = 1f
        private const val DEFAULT_LOAD_FACTOR = 24
        private const val DEFAULT_MAX_TRANSITION_LENGTH = 2
    }

}