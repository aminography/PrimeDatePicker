package com.aminography.primedatepicker.fragment.header.multiple.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.fragment.base.BaseAdapter
import com.aminography.primedatepicker.fragment.header.multiple.dataholder.PickedDayDataHolder
import kotlinx.android.synthetic.main.list_item_picked_day.view.*

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
            }
        }
    }

    override fun <DH> bindDataToView(dataHolder: DH) {
        if (dataHolder is PickedDayDataHolder) {
            with(itemView) {
                twoLineTextView.firstLabelText = dataHolder.day
                twoLineTextView.secondLabelText = String.format("%s '%s", dataHolder.month, dataHolder.year)
            }
        }
    }

}