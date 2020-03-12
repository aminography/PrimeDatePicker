package com.aminography.primedatepicker.fragment.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.fragment.adapter.BaseAdapter
import com.aminography.primedatepicker.fragment.dataholder.PickedDayDataHolder
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
                twoLineTextView.typeface = this
                twoLineTextView.firstLabelTextSize = 90
                twoLineTextView.secondLabelTextSize = 40
            }
        }
    }

    override fun <DH> bindDataToView(dataHolder: DH) {
        if (dataHolder is PickedDayDataHolder) {
            with(itemView) {

                dataHolder.calendar.run {
                    shortDateString.split("/").run {
                        twoLineTextView.firstLabelText = get(2)
                        twoLineTextView.secondLabelText = String.format("%s '%s", getDisplayName(Calendar.MONTH, Calendar.SHORT, locale), get(0).substring(2))
                    }
                }
            }
        }
    }

}