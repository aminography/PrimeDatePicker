package com.aminography.primedatepicker.fragment

import android.graphics.Typeface
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil

/**
 * A concrete instance of adapter for list of the venue recommendations
 *
 * @author aminography
 */
class PickedDaysListAdapter : BaseAdapter<PickedDayDataHolder, PickedDayViewHolder>() {

    init {
        diffUtilCallback = object : DiffUtil.ItemCallback<PickedDayDataHolder>() {
            override fun areItemsTheSame(
                new: PickedDayDataHolder,
                old: PickedDayDataHolder
            ): Boolean {
                return new.id == old.id
            }

            override fun areContentsTheSame(
                new: PickedDayDataHolder,
                old: PickedDayDataHolder
            ): Boolean {
                return new == old
            }
        }
    }

    var typeface: Typeface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickedDayViewHolder {
        return PickedDayViewHolder(parent.context, typeface).also { setupClickListener(it) }
    }

}
