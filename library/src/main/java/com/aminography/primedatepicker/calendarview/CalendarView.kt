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
import com.aminography.primedatepicker.calendarview.adapter.MonthListAdapter
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder
import com.aminography.primedatepicker.tools.Utils


/**
 * @author aminography
 */
class CalendarView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0,
        @StyleRes defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var adapter: MonthListAdapter
    private var recyclerView = TouchControllableRecyclerView(context)
    private var layoutManager = LinearLayoutManager(context)
    private var centeredData: ArrayList<PrimeDataHolder>? = null
    private var isInTransition = false

    init {
        addView(recyclerView)
        adapter = PrimeAdapter.with(recyclerView)
                .setLayoutManager(layoutManager)
                .setSnapHelper(StartSnapHelper())
                .setHasFixedSize(true)
                .setDivider()
                .set()
                .build(MonthListAdapter::class.java)

        val calendar = Utils.newCalendar()
        centeredData = centeredData(calendar.year, calendar.month)
        centeredData?.apply {
            adapter.replaceDataList(this)
            recyclerView.scrollToPosition(12)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                                    recyclerView.scrollToPosition(12)
                                    recyclerView.smoothScrollBy(0, -1)
                                    isInTransition = false
                                    recyclerView.touchEnabled = true
                                }
                            }, 500)
                        }
                    }
                }
            }

        })
    }

    fun goto(year: Int, month: Int) {
        centeredData = centeredData(year, month)
        val position = layoutManager.findFirstVisibleItemPosition()
        val dataHolder = adapter.getItem(position) as MonthDataHolder
        val transitionData = transitionData(dataHolder.year, dataHolder.month, year, month)
        transitionData?.apply {
            adapter.replaceDataList(this)
            isInTransition = true
            recyclerView.touchEnabled = false
            recyclerView.smoothScrollToPosition(size)
        }
    }

    private fun transitionData(currentYear: Int, currentMonth: Int, targetYear: Int, targetMonth: Int): ArrayList<PrimeDataHolder>? {
        arrayListOf<PrimeDataHolder>().apply {
            if (targetYear == currentYear && targetMonth == currentMonth) {
                return null
            } else {
                val isForward = targetYear > currentYear || (targetYear == currentYear && targetMonth > currentMonth)
                if (isForward) {
                    add(MonthDataHolder(currentYear, currentMonth))
                    for (i in 0 until 5) {
                        val y = currentYear + (currentMonth + i + 1) / 12
                        val m = (currentMonth + i + 1) % 12
                        add(MonthDataHolder(y, m))
                    }
                    for (i in 5 downTo 1) {
                        val y = targetYear - (12 - (targetMonth - i + 1)) / 12
                        val m = (12 + (targetMonth - i)) % 12
                        add(MonthDataHolder(y, m))
                    }
                    add(MonthDataHolder(targetYear, targetMonth))
                } else {
                    add(MonthDataHolder(currentYear, currentMonth))
                    for (i in 1 until 6) {
                        val y = currentYear - (12 - (currentMonth - i + 1)) / 12
                        val m = (12 + (currentMonth - i)) % 12
                        add(MonthDataHolder(y, m))
                    }
                    for (i in 5 downTo 1) {
                        val y = targetYear + (targetMonth + i) / 12
                        val m = (targetMonth + i) % 12
                        add(MonthDataHolder(y, m))
                    }
                    add(MonthDataHolder(targetYear, targetMonth))
                }
            }
            return this
        }
    }

    private fun centeredData(year: Int, month: Int): ArrayList<PrimeDataHolder> {
        arrayListOf<PrimeDataHolder>().apply {
            for (i in 12 downTo 0) {
                val y = year - (12 - (month - i + 1)) / 12
                val m = (12 + (month - i)) % 12
                add(MonthDataHolder(y, m))
            }
            for (i in 0 until 12) {
                val y = year + (month + i + 1) / 12
                val m = (month + i + 1) % 12
                add(MonthDataHolder(y, m))
            }
            return this
        }
    }

}