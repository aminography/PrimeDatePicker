package com.aminography.primedatepicker.picker.selection.multiple.viewholder

import android.annotation.SuppressLint
import android.graphics.Typeface
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.picker.base.adapter.BaseAdapter
import com.aminography.primedatepicker.picker.base.adapter.ItemViewInflater
import kotlinx.android.synthetic.main.list_item_picked_day_empty.view.*

/**
 * @author aminography
 */
@SuppressLint("InflateParams")
internal class PickedDayEmptyViewHolder(
    inflater: ItemViewInflater,
    typeface: Typeface?,
    topLabelTextSize: Int,
    topLabelTextColor: Int,
    bottomLabelTextSize: Int,
    bottomLabelTextColor: Int,
    gapBetweenLines: Int
) : BaseAdapter.BaseViewHolder(inflater(R.layout.list_item_picked_day_empty)) {

    init {
        with(itemView) {
            typeface?.let { twoLineTextView.typeface = it }
            twoLineTextView.also {
                it.topLabelTextSize = topLabelTextSize
                it.topLabelTextColor = topLabelTextColor
                it.bottomLabelTextSize = bottomLabelTextSize
                it.bottomLabelTextColor = bottomLabelTextColor
                it.gapBetweenLines = gapBetweenLines
            }
        }
    }

    override fun <DH> bindDataToView(dataHolder: DH) {
    }

}