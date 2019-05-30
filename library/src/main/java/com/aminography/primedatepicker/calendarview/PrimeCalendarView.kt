package com.aminography.primedatepicker.calendarview

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
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

    private val DEFAULT_LOAD_FACTOR = 24
    private val DEFAULT_TRANSITION_FACTOR = 2

    private var isInternalChange = false

    private var adapter: MonthListAdapter
    private var recyclerView = TouchControllableRecyclerView(context)
    private var layoutManager = LinearLayoutManager(context)
    private var dataList: MutableList<PrimeDataHolder>? = null
    private var isInTransition = false
    private var isInLoading = false
    private var definedHeight: Int = 0
    private var detectedItemHeight: Float = 0f

    private var heightMultiplier: Float = 0f

    var calendarType = CalendarType.CIVIL
        set(value) {
            field = value
            if (!isInternalChange) {
                val calendar = CalendarFactory.newInstance(value)
                goto(calendar.year, calendar.month, false)
            }
        }

    private var gotoYear: Int = 0
    private var gotoMonth: Int = 0

    override var minDateCalendar: BaseCalendar? = null
        set(value) {
            field = value
            val minOffset = field?.monthOffset() ?: Int.MIN_VALUE

            val dataHolder = findFirstVisibleItem()
            val offset = dataHolder.offset

            if (offset < minOffset) {
                minDateCalendar?.apply {
                    goto(year, month, false)
                }
            } else {
                goto(dataHolder.year, dataHolder.month, false)
            }
        }

    override var maxDateCalendar: BaseCalendar? = null
        set(value) {
            field = value
            val maxOffset = field?.monthOffset() ?: Int.MAX_VALUE

            val dataHolder = findLastVisibleItem()
            val offset = dataHolder.offset

            if (offset > maxOffset) {
                maxDateCalendar?.apply {
                    goto(year, month, false)
                }
            } else {
                goto(dataHolder.year, dataHolder.month, false)
            }
        }

    override var pickType = PickType.NOTHING
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
            adapter.notifyDataSetChanged()
        }

    override var pickedSingleDayCalendar: BaseCalendar? = null
    override var pickedStartRangeCalendar: BaseCalendar? = null
    override var pickedEndRangeCalendar: BaseCalendar? = null

//    val currentItemCalendar: BaseCalendar
//        get() {
//            val dataHolder = findFirstVisibleItem()
//            val calendar = CalendarFactory.newInstance(calendarType)
//            calendar.setDate(dataHolder.year, dataHolder.month, 1)
//            return calendar
//        }

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
            heightMultiplier = getFloat(R.styleable.PrimeCalendarView_heightMultiplier, 1f)
            isInternalChange = true
            calendarType = CalendarType.values()[getInt(R.styleable.PrimeCalendarView_calendarType, CalendarType.CIVIL.ordinal)]
            isInternalChange = false
            recycle()
        }

        addView(recyclerView)
        recyclerView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        recyclerView.addOnScrollListener(OnScrollListener())

        adapter = PrimeAdapter.with(recyclerView)
                .setLayoutManager(layoutManager)
                .setSnapHelper(StartSnapHelper())
                .setHasFixedSize(true)
                .setDivider()
                .set()
                .build(MonthListAdapter::class.java)

        adapter.iMonthViewHolderCallback = this

        val calendar = CalendarFactory.newInstance(calendarType)
        goto(calendar.year, calendar.month, false)
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

    fun goto(year: Int, month: Int, animate: Boolean = false) {
        if (DateUtils.isOutOfRange(year, month, minDateCalendar, maxDateCalendar)) {
            return
        }
        dataList = CalendarViewUtils.createPivotList(calendarType, year, month, minDateCalendar, maxDateCalendar, DEFAULT_LOAD_FACTOR)
        if (animate) {
            val dataHolder = findFirstVisibleItem()

            val transitionList = CalendarViewUtils.createTransitionList(calendarType, dataHolder.year, dataHolder.month, year, month, DEFAULT_TRANSITION_FACTOR)
            val isForward = DateUtils.isBefore(dataHolder.year, dataHolder.month, year, month)
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
        } else {
            dataList?.apply {
                adapter.replaceDataList(this)
                findPositionInList(year, month, this)?.apply {
                    recyclerView.fastScrollTo(this)
                }
            }
        }
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

    private fun findFirstVisibleItem(): MonthDataHolder {
        var position = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (position == RecyclerView.NO_POSITION) {
            position = layoutManager.findFirstVisibleItemPosition()
        }
        return adapter.getItem(position) as MonthDataHolder
    }

    private fun findLastVisibleItem(): MonthDataHolder {
        var position = layoutManager.findLastCompletelyVisibleItemPosition()
        if (position == RecyclerView.NO_POSITION) {
            position = layoutManager.findLastVisibleItemPosition()
        }
        return adapter.getItem(position) as MonthDataHolder
    }

    override fun onDayClick(day: BaseCalendar) {
        when (pickType) {
            PickType.SINGLE -> {
                pickedSingleDayCalendar = day
            }
            PickType.START_RANGE -> {
                if (DateUtils.isAfter(day.year, day.month, day.dayOfMonth, pickedEndRangeCalendar)) {
                    pickedEndRangeCalendar = null
                }
                pickedStartRangeCalendar = day
            }
            PickType.END_RANGE -> {
                if (!DateUtils.isBefore(day.year, day.month, day.dayOfMonth, pickedStartRangeCalendar)) {
                    pickedEndRangeCalendar = day
                }
            }
            PickType.NOTHING -> {
            }
        }

        val dataHolder = findLastVisibleItem()
        goto(dataHolder.year, dataHolder.month, false)
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
                        val maxOffset = maxDateCalendar?.monthOffset() ?: Int.MAX_VALUE

                        if (offset < maxOffset) {
                            val moreData = CalendarViewUtils.extendMoreList(calendarType, dataHolder.year, dataHolder.month, minDateCalendar, maxDateCalendar, DEFAULT_LOAD_FACTOR, true)
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
                        val minOffset = minDateCalendar?.monthOffset() ?: Int.MIN_VALUE

                        if (offset > minOffset) {
                            val moreData = CalendarViewUtils.extendMoreList(calendarType, dataHolder.year, dataHolder.month, minDateCalendar, maxDateCalendar, DEFAULT_LOAD_FACTOR, false)
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

}