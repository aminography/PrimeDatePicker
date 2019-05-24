package com.aminography.primedatepicker

import android.content.Context

class SimpleMonthListAdapter(
        context: Context,
        controller: DatePickerController
) : BaseMonthListAdapter(context, controller) {

    override fun createMonthView(context: Context): MonthView {
        val monthView = MonthView(context)
        monthView.setDatePickerController(controller)
        return monthView
    }

}
