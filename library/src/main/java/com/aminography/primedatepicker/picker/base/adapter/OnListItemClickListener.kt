package com.aminography.primedatepicker.picker.base.adapter

/**
 * An interface to implement on item click listener for the [androidx.recyclerview.widget.RecyclerView]
 *
 * @author aminography
 */
internal interface OnListItemClickListener {

    fun <DH> onItemClicked(dataHolder: DH)
}