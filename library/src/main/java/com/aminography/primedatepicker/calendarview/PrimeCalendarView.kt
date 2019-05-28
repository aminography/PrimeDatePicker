package com.aminography.primedatepicker.calendarview

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.FrameLayout
import com.aminography.primeadapter.PrimeAdapter
import com.aminography.primeadapter.PrimeDataHolder
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primedatepicker.DateUtils
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.calendarview.adapter.MonthListAdapter
import com.aminography.primedatepicker.calendarview.callback.IMonthViewHolderCallback
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder
import com.aminography.primedatepicker.monthOffset


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

    private var adapter: MonthListAdapter
    private var recyclerView = TouchControllableRecyclerView(context)
    private var layoutManager = LinearLayoutManager(context)
    private var dataList: MutableList<PrimeDataHolder>? = null
    private var isInTransition = false
    private var isInLoading = false

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

    init {
        addView(recyclerView)
        recyclerView.addOnScrollListener(OnScrollListener())

        adapter = PrimeAdapter.with(recyclerView)
                .setLayoutManager(layoutManager)
                .setSnapHelper(StartSnapHelper())
                .setHasFixedSize(true)
                .setDivider()
                .set()
                .build(MonthListAdapter::class.java)

        adapter.iMonthViewHolderCallback = this

        val calendar = DateUtils.newCalendar()
        goto(calendar.year, calendar.month, false)
    }

    fun goto(year: Int, month: Int, animate: Boolean = false) {
        if (DateUtils.isOutOfRange(year, month, minDateCalendar, maxDateCalendar)) {
            return
        }
        dataList = CalendarViewUtils.createPivotList(year, month, minDateCalendar, maxDateCalendar, DEFAULT_LOAD_FACTOR)
        if (animate) {
            val dataHolder = findFirstVisibleItem()

            val transitionData = CalendarViewUtils.createTransitionList(dataHolder.year, dataHolder.month, year, month, DEFAULT_TRANSITION_FACTOR)
            val isForward = DateUtils.isBefore(dataHolder.year, dataHolder.month, year, month)
            transitionData?.apply {
                var isLastTransitionItemRemoved = false
                if (isForward) {
                    maxDateCalendar?.let { max ->
                        val maxOffset = max.monthOffset()
                        val targetOffset = year * 12 + month
                        if (maxOffset == targetOffset) {
                            transitionData.removeAt(transitionData.size - 1)
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
                findPosition(year, month, this)?.apply {
                    recyclerView.fastScrollTo(this)
                }
            }
        }
    }

    private fun findPosition(year: Int, month: Int, data: List<PrimeDataHolder>?): Int? {
        data?.apply {
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
                                findPosition(gotoYear, gotoMonth, this)?.apply {
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
                            val moreData = CalendarViewUtils.extendMoreList(dataHolder.year, dataHolder.month, minDateCalendar, maxDateCalendar, DEFAULT_LOAD_FACTOR, true)
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
                            val moreData = CalendarViewUtils.extendMoreList(dataHolder.year, dataHolder.month, minDateCalendar, maxDateCalendar, DEFAULT_LOAD_FACTOR, false)
                            dataList?.apply {
                                addAll(0, moreData)
                                adapter.replaceDataList(this)
                                findPosition(dataHolder.year, dataHolder.month, this)?.apply {
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