package com.aminography.primedatepicker.fragment.header

/**
 * An interface to implement on item click listener for the [androidx.recyclerview.widget.RecyclerView]
 *
 * @author aminography
 */
interface OnListItemClickListener {

    fun <DH> onItemClicked(dataHolder: DH)

}