package com.aminography.primedatepicker.picker.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * A class contains base functionalities of a [RecyclerView.Adapter] to make adapter classes cleaner.
 * This implementation is based on [AsyncListDiffer] which makes data changes presentation more efficient.
 *
 * @author aminography
 */
internal abstract class BaseAdapter<DH, VH : BaseAdapter.BaseViewHolder> : RecyclerView.Adapter<VH>() {

    protected abstract val diffUtilCallback: DiffUtil.ItemCallback<DH>

    private val differ by lazy {
        AsyncListDiffer(this, diffUtilCallback)
    }

    private var onItemClickListener: OnListItemClickListener? = null

    private lateinit var inflater: ItemViewInflater

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        if (position in 0 until itemCount) {
            differ.currentList[position].apply {
                viewHolder.bindDataToView(this)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        setupInflater(parent)
        return createViewHolder(inflater, viewType).also { setupClickListener(it) }
    }

    protected abstract fun createViewHolder(
        inflater: ItemViewInflater,
        viewType: Int
    ): VH


    fun itemAt(position: Int): DH = differ.currentList[position]

    fun submitList(list: List<DH>) {
        differ.submitList(list)
    }

    fun setOnListItemClickListener(listener: OnListItemClickListener) {
        onItemClickListener = listener
    }

    private fun setupInflater(parent: ViewGroup) {
        if (!::inflater.isInitialized) {
            inflater = { layoutResId ->
                LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
            }
        }
    }

    private fun setupClickListener(viewHolder: VH) {
        onItemClickListener?.apply {
            viewHolder.itemView.setOnClickListener {
                val position = viewHolder.adapterPosition
                if (position in 0 until itemCount) {
                    onItemClicked(differ.currentList[position])
                }
            }
        }
    }

    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun <DH> bindDataToView(dataHolder: DH)
    }

}