package com.aminography.primedatepicker.picker.header.multiple.viewholder

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Typeface
import androidx.core.widget.ImageViewCompat
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.picker.base.adapter.BaseAdapter
import com.aminography.primedatepicker.picker.base.adapter.ItemViewInflater
import com.aminography.primedatepicker.picker.header.multiple.dataholder.PickedDayDataHolder
import kotlinx.android.synthetic.main.list_item_picked_day.view.*

/**
 * @author aminography
 */
@SuppressLint("InflateParams")
class PickedDayViewHolder(
    inflater: ItemViewInflater,
    typeface: Typeface?,
    backgroundColor: Int,
    firstLabelTextSize: Int,
    firstLabelTextColor: Int,
    secondLabelTextSize: Int,
    secondLabelTextColor: Int,
    gapBetweenLines: Int
) : BaseAdapter.BaseViewHolder(inflater(R.layout.list_item_picked_day)) {

    init {
        with(itemView) {
            typeface?.let { twoLineTextView.typeface = it }
            ImageViewCompat.setImageTintList(
                backgroundImageView,
                ColorStateList.valueOf(backgroundColor)
            )
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
        if (dataHolder is PickedDayDataHolder) {
            with(itemView) {
                twoLineTextView.also {
                    it.firstLabelText = dataHolder.firstLabel
                    it.secondLabelText = dataHolder.secondLabel
                }
            }
        }
    }

}