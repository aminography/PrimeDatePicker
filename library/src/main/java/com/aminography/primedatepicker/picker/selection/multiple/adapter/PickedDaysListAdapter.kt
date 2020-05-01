package com.aminography.primedatepicker.picker.selection.multiple.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import androidx.recyclerview.widget.DiffUtil
import com.aminography.primedatepicker.picker.base.adapter.BaseAdapter
import com.aminography.primedatepicker.picker.base.adapter.ItemViewInflater
import com.aminography.primedatepicker.picker.selection.multiple.dataholder.BasePickedDayDataHolder
import com.aminography.primedatepicker.picker.selection.multiple.dataholder.PickedDayDataHolder
import com.aminography.primedatepicker.picker.selection.multiple.dataholder.PickedDayEmptyDataHolder
import com.aminography.primedatepicker.picker.selection.multiple.viewholder.PickedDayEmptyViewHolder
import com.aminography.primedatepicker.picker.selection.multiple.viewholder.PickedDayViewHolder

/**
 * A concrete instance of adapter for list of the venue recommendations
 *
 * @author aminography
 */
internal class PickedDaysListAdapter : BaseAdapter<BasePickedDayDataHolder, BaseAdapter.BaseViewHolder>() {

    var typeface: Typeface? = null
    var backgroundColor: Int = 0
    var topLabelTextSize: Int = 0
    var topLabelTextColor: Int = 0
    var bottomLabelTextSize: Int = 0
    var bottomLabelTextColor: Int = 0
    var gapBetweenLines: Int = 0

    override fun createViewHolder(inflater: ItemViewInflater, viewType: Int): BaseViewHolder {
        return when (viewType) {
            0 -> PickedDayEmptyViewHolder(
                inflater,
                typeface,
                topLabelTextSize,
                topLabelTextColor,
                bottomLabelTextSize,
                bottomLabelTextColor,
                gapBetweenLines
            )
            1 -> PickedDayViewHolder(
                inflater,
                typeface,
                backgroundColor,
                topLabelTextSize,
                topLabelTextColor,
                bottomLabelTextSize,
                bottomLabelTextColor,
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
