package com.aminography.primedatepicker.fragment.header.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.aminography.primedatepicker.fragment.header.dataholder.BasePickedDayDataHolder
import com.aminography.primedatepicker.fragment.header.dataholder.PickedDayDataHolder
import com.aminography.primedatepicker.fragment.header.dataholder.PickedDayEmptyDataHolder
import com.aminography.primedatepicker.fragment.header.viewholder.PickedDayEmptyViewHolder
import com.aminography.primedatepicker.fragment.header.viewholder.PickedDayViewHolder

/**
 * A concrete instance of adapter for list of the venue recommendations
 *
 * @author aminography
 */
class PickedDaysListAdapter : BaseAdapter<BasePickedDayDataHolder, BaseAdapter.BaseViewHolder>() {

    var typeface: Typeface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            0 -> PickedDayEmptyViewHolder(parent.context, typeface).also { setupClickListener(it) }
            1 -> PickedDayViewHolder(parent.context, typeface).also { setupClickListener(it) }
            else -> null!!
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
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
