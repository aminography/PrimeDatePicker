package com.aminography.primedatepicker.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import com.aminography.primedatepicker.R
import kotlinx.android.synthetic.main.list_item_picked_day.view.*
import java.util.*

/**
 * @author aminography
 */
@SuppressLint("InflateParams")
class PickedDayViewHolder(
        context: Context,
        typeface: Typeface?
) : BaseAdapter.BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_picked_day, null)) {

    init {
        with(itemView) {
            typeface?.run {
//                dayTextView.typeface = this
//                monthTextView.typeface = this
            }
        }
    }

    override fun <DH> bindDataToView(dataHolder: DH) {
        if (dataHolder is PickedDayDataHolder) {
            with(itemView) {
                dataHolder.calendar.run {
                    shortDateString.split("/").run {
//                        dayTextView.text = get(2)
//                        monthTextView.text = String.format("%s '%s", getDisplayName(Calendar.MONTH, Calendar.SHORT, locale), get(0).substring(2))
                    }
                }
            }
        }
    }

}