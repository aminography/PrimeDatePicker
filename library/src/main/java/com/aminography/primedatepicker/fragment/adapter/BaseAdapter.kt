package com.aminography.primedatepicker.fragment.adapter

import android.view.View
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aminography.primedatepicker.fragment.OnListItemClickListener

/**
 * A class contains base functionalities of a [RecyclerView.Adapter] to make adapter classes cleaner.
 * This implementation is based on [AsyncListDiffer] which makes data changes presentation more efficient.
 *
 * @author aminography
 */
abstract class BaseAdapter<DH, VH : BaseAdapter.BaseViewHolder> : RecyclerView.Adapter<VH>() {

    protected abstract val diffUtilCallback: DiffUtil.ItemCallback<DH>

    private val differ by lazy {
        AsyncListDiffer(this, diffUtilCallback)
    }

    private var onItemClickListener: OnListItemClickListener? = null

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        if (position < differ.currentList.size) {
            differ.currentList[position].apply {
                viewHolder.bindDataToView(this)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun getItem(position: Int): DH = differ.currentList[position]

    fun submitList(list: List<DH>) {
        differ.submitList(list)
    }

    fun setOnListItemClickListener(listener: OnListItemClickListener) {
        onItemClickListener = listener
    }

    protected fun setupClickListener(viewHolder: VH) {
        viewHolder.itemView.setOnClickListener {
            onItemClickListener?.apply {
                val position = viewHolder.adapterPosition
                if (position in 0 until differ.currentList.size) {
                    onItemClicked(differ.currentList[position])
                }
            }
        }
    }

    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun <DH> bindDataToView(dataHolder: DH)
    }

}