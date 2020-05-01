package com.aminography.primedatepicker.picker.selection.multiple

import android.graphics.Typeface
import android.view.ViewStub
import androidx.recyclerview.widget.LinearLayoutManager
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.common.Direction
import com.aminography.primedatepicker.common.LabelFormatter
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.picker.base.BaseLazyView
import com.aminography.primedatepicker.picker.base.adapter.OnListItemClickListener
import com.aminography.primedatepicker.picker.selection.SelectionBarView
import com.aminography.primedatepicker.picker.selection.multiple.adapter.PickedDaysListAdapter
import com.aminography.primedatepicker.picker.selection.multiple.dataholder.PickedDayDataHolder
import com.aminography.primedatepicker.picker.selection.multiple.dataholder.PickedDayEmptyDataHolder
import com.aminography.primedatepicker.utils.forceLocaleStrings
import com.aminography.primedatepicker.utils.gone
import com.aminography.primedatepicker.utils.visible
import kotlinx.android.synthetic.main.multiple_days_header.view.*
import java.util.*

/**
 * @author aminography
 */
internal class MultipleDaysSelectionBarView(
    viewStub: ViewStub,
    private val direction: Direction
) : BaseLazyView(R.layout.multiple_days_header, viewStub), SelectionBarView {

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

    var locale: Locale? = null
        set(value) {
            field = value
            value?.let {
                val strings = rootView.context.forceLocaleStrings(
                    it,
                    R.string.no_day_is_selected
                )
                rootView.emptyStateTextView.text = strings[0]
            }
        }

    var typeface: Typeface? = null
        set(value) {
            field = value
            multipleDaysAdapter.typeface = value
            rootView.emptyStateTextView.typeface = value
        }

    var itemBackgroundColor: Int = 0
        set(value) {
            field = value
            multipleDaysAdapter.backgroundColor = value
        }

    var topLabelTextSize: Int = 0
        set(value) {
            field = value
            multipleDaysAdapter.topLabelTextSize = value
        }

    var topLabelTextColor: Int = 0
        set(value) {
            field = value
            multipleDaysAdapter.topLabelTextColor = value
        }

    var bottomLabelTextSize: Int = 0
        set(value) {
            field = value
            multipleDaysAdapter.bottomLabelTextSize = value
        }

    var bottomLabelTextColor: Int = 0
        set(value) {
            field = value
            multipleDaysAdapter.bottomLabelTextColor = value
        }

    var gapBetweenLines: Int = 0
        set(value) {
            field = value
            multipleDaysAdapter.gapBetweenLines = value
        }

    var topLabelFormatter: LabelFormatter? = null

    var bottomLabelFormatter: LabelFormatter? = null

    var pickedDays: List<PrimeCalendar>? = null
        set(value) {
            field = value
            value?.map {
                PickedDayDataHolder(
                    it.shortDateString,
                    it,
                    topLabelFormatter,
                    bottomLabelFormatter
                )
            }?.also {
                val count = multipleDaysAdapter.itemCount

                if (it.isEmpty()) {
                    rootView.emptyStateTextView.visible()
                    arrayListOf(PickedDayEmptyDataHolder())
                } else {
                    rootView.emptyStateTextView.gone()
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