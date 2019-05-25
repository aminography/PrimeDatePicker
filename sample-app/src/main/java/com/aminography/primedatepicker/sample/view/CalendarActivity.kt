package com.aminography.primedatepicker.sample.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.aminography.primeadapter.PrimeAdapter
import com.aminography.primeadapter.PrimeDataHolder
import com.aminography.primedatepicker.sample.R
import com.aminography.primedatepicker.sample.view.adapter.MonthListAdapter
import com.aminography.primedatepicker.sample.view.dataholder.MonthDataHolder
import kotlinx.android.synthetic.main.activity_calendar.*

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val adapter = PrimeAdapter.with(recyclerView)
                .setLayoutManager(LinearLayoutManager(this))
                .setSnapHelper(MyLinearSnapHelper())
                .setHasFixedSize(true)
                .setDivider()
                .set()
                .build(MonthListAdapter::class.java)

        val list = arrayListOf<PrimeDataHolder>()
        for (i in 0 until 12) {
            list.add(MonthDataHolder(2019, i))
        }
        for (i in 0 until 12) {
            list.add(MonthDataHolder(2020, i))
        }
        adapter.replaceDataList(list)
    }
}
