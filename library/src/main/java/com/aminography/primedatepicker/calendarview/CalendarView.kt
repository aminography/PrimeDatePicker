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
    private var recyclerView: RecyclerView = RecyclerView(context)

    init {
        addView(recyclerView)
        adapter = PrimeAdapter.with(recyclerView)
                .setLayoutManager(LinearLayoutManager(context))
                .setSnapHelper(StartSnapHelper())
                .setHasFixedSize(true)
                .setDivider()
                .set()
                .build(MonthListAdapter::class.java)

        val calendar = Utils.newCalendar()
        goto(calendar.year, calendar.month)
    }

    fun goto(year: Int, month: Int) {
        arrayListOf<PrimeDataHolder>().apply {
            for (i in 12 downTo 0) {
                val y = year - (12 - (month - i + 1)) / 12
                val m = (12 + (month - i)) % 12
                add(MonthDataHolder(y, m))
            }
            for (i in 0 until 12) {
                val y = year + (month + i) / 12
                val m = (month + i + 1) % 12
                add(MonthDataHolder(y, m))
            }
            adapter.replaceDataList(this)
            recyclerView.smoothScrollToPosition(12)
        }
    }

}