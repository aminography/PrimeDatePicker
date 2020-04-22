package com.aminography.primedatepicker.picker.header.multiple.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.picker.base.BaseAdapter
import kotlinx.android.synthetic.main.list_item_picked_day_empty.view.*

/**
 * @author aminography
 */
@SuppressLint("InflateParams")
class PickedDayEmptyViewHolder(
    context: Context,
    typeface: Typeface?,
    firstLabelTextSize: Int,
    firstLabelTextColor: Int,
    secondLabelTextSize: Int,
    secondLabelTextColor: Int,
    gapBetweenLines: Int
) : BaseAdapter.BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_picked_day_empty, null)) {

    init {
        with(itemView) {
            typeface?.let { twoLineTextView.typeface = it }
            twoLineTextView.also {
                it.firstLabelTextSize = firstLabelTextSize
                it.firstLabelTextColor = firstLabelTextColor
                it.secondLabelTextSize = secondLabelTextSize
                it.secondLabelTextColor = secondLabelTextColor
                it.gapBetweenLines = gapBetweenLines
            }
        }
    }

    override fun <DH> bindDataToView(dataHolder: DH) {
    }

}