package com.aminography.primedatepicker.old

import android.content.Context
import android.util.AttributeSet

/**
 * A BaseMonthListView customized for [SimpleMonthListAdapter]
 */
class SimpleMonthListView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        controller: DatePickerController? = null
) : BaseMonthListView(context, attributeSet, controller) {

    override fun createMonthAdapter(context: Context, controller: DatePickerController): BaseMonthListAdapter {
        return SimpleMonthListAdapter(context, controller)
    }

}
