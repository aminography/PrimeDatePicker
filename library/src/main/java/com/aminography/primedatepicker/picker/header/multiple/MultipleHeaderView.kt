package com.aminography.primedatepicker.picker.header.multiple

import android.graphics.Typeface
import android.view.View
import android.view.ViewStub
import androidx.recyclerview.widget.LinearLayoutManager
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.Direction
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.picker.header.BaseLazyView
import com.aminography.primedatepicker.picker.header.multiple.adapter.PickedDaysListAdapter
import com.aminography.primedatepicker.picker.header.multiple.dataholder.PickedDayDataHolder
import com.aminography.primedatepicker.picker.header.multiple.dataholder.PickedDayEmptyDataHolder
import kotlinx.android.synthetic.main.multiple_days_header.view.*

/**
 * @author aminography
 */
class MultipleHeaderView(
    viewStub: ViewStub,
    private val direction: Direction
) : BaseLazyView(R.layout.multiple_days_header, viewStub) {

    private val multipleDaysAdapter: PickedDaysListAdapter by lazy {
        PickedDaysListAdapter().also {
            it.setOnListItemClickListener(object : OnListItemClickListener {
                override fun <DH> onItemClicked(dataHolder: DH) {
                    if (dataHolder is PickedDayDataHolder) {
                        onPickedDayClickListener?.invoke(dataHolder.calendar)
                    }
                }
            })

            rootView.recyclerView.layoutManager = LinearLayoutManager(rootView.recyclerView.context, LinearLayoutManager.HORIZONTAL, direction == Direction.RTL)
            rootView.recyclerView.adapter = it
            rootView.recyclerView.isNestedScrollingEnabled = false
            rootView.recyclerView.speedFactor = 2.5f
        }
    }

    var typeface: Typeface? = null
        set(value) {
            field = value
            multipleDaysAdapter.typeface = typeface
        }

    var pickedDays: List<PrimeCalendar>? = null
        set(value) {
            field = value
            value?.map {
                PickedDayDataHolder(it.shortDateString, it)
            }?.also {
                val count = multipleDaysAdapter.itemCount

                if (it.isEmpty()) {
                    rootView.emptyStateTextView.visibility = View.VISIBLE
                    arrayListOf(PickedDayEmptyDataHolder())
                } else {
                    rootView.emptyStateTextView.visibility = View.GONE
                    it
                }.let { list ->
                    multipleDaysAdapter.submitList(list)
                }

                if (it.size > 2 && count < it.size) {
                    rootView.recyclerView.smoothScrollTo(it.size - 1)
                }
            }
        }

    var onPickedDayClickListener: ((PrimeCalendar) -> Unit)? = null

}