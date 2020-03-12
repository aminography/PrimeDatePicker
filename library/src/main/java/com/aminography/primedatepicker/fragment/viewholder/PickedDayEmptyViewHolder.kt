package com.aminography.primedatepicker.fragment.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.fragment.adapter.BaseAdapter
import kotlinx.android.synthetic.main.list_item_picked_day_empty.view.*

/**
 * @author aminography
 */
@SuppressLint("InflateParams")
class PickedDayEmptyViewHolder(
        context: Context,
        typeface: Typeface?
) : BaseAdapter.BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_picked_day_empty, null)) {

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
    }

}