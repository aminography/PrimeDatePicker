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
import com.aminography.primedatepicker.calendarview.adapter.MonthListAdapter
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder
import com.aminography.primedatepicker.tools.Utils
import java.util.*


/**
 * @author aminography
 */
@Suppress("PrivatePropertyName")
class CalendarView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0,
        @StyleRes defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val DEFAULT_LOAD_FACTOR = 12
    private val DEFAULT_TRANSITION_FACTOR = 2

    private var adapter: MonthListAdapter
    private var recyclerView = TouchControllableRecyclerView(context)
    private var layoutManager = LinearLayoutManager(context)
    private var centeredData: ArrayList<PrimeDataHolder>? = null
    private var isInTransition = false
    private var isInLoading = false

    private var gotoYear: Int = 0
    private var gotoMonth: Int = 0

    private var minDateCalendar: BaseCalendar? = null
    private var maxDateCalendar: BaseCalendar? = null

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

        minDateCalendar = Utils.newCalendar()
        minDateCalendar?.year = 2018
        minDateCalendar?.month = 1

        maxDateCalendar = Utils.newCalendar()
        maxDateCalendar?.year = 2020
        maxDateCalendar?.month = 7

        val calendar = Utils.newCalendar()
        goto(calendar.year, calendar.month, false)
    }

    fun goto(year: Int, month: Int, animate: Boolean = false) {
        if (CalendarViewUtils.isOutOfRange(year, month, minDateCalendar, maxDateCalendar)) {
            return
        }
        centeredData = CalendarViewUtils.centeredData(year, month, minDateCalendar, maxDateCalendar, DEFAULT_LOAD_FACTOR)
        if (animate) {
            var position = layoutManager.findFirstCompletelyVisibleItemPosition()
            if (position == RecyclerView.NO_POSITION) {
                position = layoutManager.findFirstVisibleItemPosition()
            }
            val dataHolder = adapter.getItem(position) as MonthDataHolder

            val transitionData = CalendarViewUtils.transitionData(dataHolder.year, dataHolder.month, year, month, DEFAULT_TRANSITION_FACTOR)
            val isForward = CalendarViewUtils.isForward(dataHolder.year, dataHolder.month, year, month)
            transitionData?.apply {
                var isLastTransitionItemRemoved = false
                if (isForward) {
                    maxDateCalendar?.let { max ->
                        val maxOffset = max.year * 12 + max.month
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
            centeredData?.apply {
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
            val firstOffset = dataHolder.year * 12 + dataHolder.month
            val targetOffset = year * 12 + month
            return targetOffset - firstOffset
        }
        return null
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
                            centeredData?.apply {
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
                        val offset = dataHolder.year * 12 + dataHolder.month
                        val maxOffset = maxDateCalendar?.let { max ->
                            max.year * 12 + max.month
                        } ?: Int.MAX_VALUE

                        if (offset < maxOffset) {
                            val moreData = CalendarViewUtils.moreData(dataHolder.year, dataHolder.month, minDateCalendar, maxDateCalendar, DEFAULT_LOAD_FACTOR, true)
                            centeredData?.apply {
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
                        val offset = dataHolder.year * 12 + dataHolder.month
                        val minOffset = minDateCalendar?.let { min ->
                            min.year * 12 + min.month
                        } ?: Int.MIN_VALUE

                        if (offset > minOffset) {
                            val moreData = CalendarViewUtils.moreData(dataHolder.year, dataHolder.month, minDateCalendar, maxDateCalendar, DEFAULT_LOAD_FACTOR, false)
                            centeredData?.apply {
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