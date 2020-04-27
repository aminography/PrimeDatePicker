package com.aminography.primedatepicker.picker.header.multiple.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import androidx.recyclerview.widget.DiffUtil
import com.aminography.primedatepicker.picker.base.adapter.BaseAdapter
import com.aminography.primedatepicker.picker.base.adapter.ItemViewInflater
import com.aminography.primedatepicker.picker.header.multiple.dataholder.BasePickedDayDataHolder
import com.aminography.primedatepicker.picker.header.multiple.dataholder.PickedDayDataHolder
import com.aminography.primedatepicker.picker.header.multiple.dataholder.PickedDayEmptyDataHolder
import com.aminography.primedatepicker.picker.header.multiple.viewholder.PickedDayEmptyViewHolder
import com.aminography.primedatepicker.picker.header.multiple.viewholder.PickedDayViewHolder

/**
 * A concrete instance of adapter for list of the venue recommendations
 *
 * @author aminography
 */
class PickedDaysListAdapter : BaseAdapter<BasePickedDayDataHolder, BaseAdapter.BaseViewHolder>() {

    var typeface: Typeface? = null
    var firstLabelTextSize: Int = 0
    var firstLabelTextColor: Int = 0
    var secondLabelTextSize: Int = 0
    var secondLabelTextColor: Int = 0
    var gapBetweenLines: Int = 0

    override fun createViewHolder(inflater: ItemViewInflater, viewType: Int): BaseViewHolder {
        return when (viewType) {
            0 -> PickedDayEmptyViewHolder(
                inflater,
                typeface,
                firstLabelTextSize,
                firstLabelTextColor,
                secondLabelTextSize,
                secondLabelTextColor,
                gapBetweenLines
            )
            1 -> PickedDayViewHolder(
                inflater,
                typeface,
                firstLabelTextSize,
                firstLabelTextColor,
                secondLabelTextSize,
                secondLabelTextColor,
                gapBetweenLines
            )
            else -> null!!
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (itemAt(position)) {
            is PickedDayEmptyDataHolder -> 0
            is PickedDayDataHolder -> 1
            else -> -1
        }
    }

    override val diffUtilCallback: DiffUtil.ItemCallback<BasePickedDayDataHolder> =
        object : DiffUtil.ItemCallback<BasePickedDayDataHolder>() {
            override fun areItemsTheSame(
                new: BasePickedDayDataHolder,
                old: BasePickedDayDataHolder
            ): Boolean {
                return new.id == old.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                new: BasePickedDayDataHolder,
                old: BasePickedDayDataHolder
            ): Boolean {
                return new == old
            }
        }

}
