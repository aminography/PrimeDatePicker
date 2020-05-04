package com.aminography.primedatepicker.picker.selection.multiple.viewholder

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Typeface
import androidx.core.widget.ImageViewCompat
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.picker.base.adapter.BaseAdapter
import com.aminography.primedatepicker.picker.base.adapter.ItemViewInflater
import com.aminography.primedatepicker.picker.selection.multiple.dataholder.PickedDayDataHolder
import kotlinx.android.synthetic.main.list_item_picked_day.view.*

/**
 * @author aminography
 */
@SuppressLint("InflateParams")
internal class PickedDayViewHolder(
    inflater: ItemViewInflater,
    typeface: Typeface?,
    backgroundColor: Int,
    topLabelTextSize: Int,
    topLabelTextColor: Int,
    bottomLabelTextSize: Int,
    bottomLabelTextColor: Int,
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
                it.topLabelTextSize = topLabelTextSize
                it.topLabelTextColor = topLabelTextColor
                it.bottomLabelTextSize = bottomLabelTextSize
                it.bottomLabelTextColor = bottomLabelTextColor
                it.gapBetweenLines = gapBetweenLines
            }
        }
    }

    override fun <DH> bindDataToView(dataHolder: DH) {
        if (dataHolder is PickedDayDataHolder) {
            with(itemView) {
                twoLineTextView.also {
                    it.topLabelText = dataHolder.topLabel
                    it.bottomLabelText = dataHolder.bottomLabel
                }
            }
        }
    }

}