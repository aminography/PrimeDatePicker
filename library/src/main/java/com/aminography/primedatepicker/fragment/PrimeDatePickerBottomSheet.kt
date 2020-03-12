package com.aminography.primedatepicker.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.fragment.base.BaseBottomSheetDialogFragment
import com.aminography.primedatepicker.fragment.header.OnListItemClickListener
import com.aminography.primedatepicker.fragment.header.adapter.PickedDaysListAdapter
import com.aminography.primedatepicker.fragment.header.dataholder.PickedDayDataHolder
import com.aminography.primedatepicker.fragment.header.dataholder.PickedDayEmptyDataHolder
import com.aminography.primedatepicker.tools.DateUtils
import com.aminography.primedatepicker.tools.screenSize
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_date_picker_bottom_sheet.view.*
import kotlinx.android.synthetic.main.multiple_days_header.view.*
import kotlinx.android.synthetic.main.range_days_header.view.*
import kotlinx.android.synthetic.main.single_day_header.view.*
import java.util.*

class PrimeDatePickerBottomSheet : BaseBottomSheetDialogFragment(
        R.layout.fragment_date_picker_bottom_sheet
), OnDayPickedListener {

    private val multipleDaysAdapter: PickedDaysListAdapter by lazy {
        PickedDaysListAdapter().also {
            it.setOnListItemClickListener(object : OnListItemClickListener {
                override fun <DH> onItemClicked(dataHolder: DH) {
                    if (dataHolder is PickedDayDataHolder) {
                        with(rootView) {
                            calendarView.focusOnDay(dataHolder.calendar)
                        }
                    }
                }
            })
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val parentView = rootView.parent as View
        parentView.setBackgroundColor(ContextCompat.getColor(activityContext, R.color.transparent))
        val params = parentView.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior
        if (behavior is BottomSheetBehavior<*>) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = activityContext.screenSize().y
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onInitViews(rootView: View) {
        val currentDateCalendar = DateUtils.restoreCalendar(arguments?.getString("currentDateCalendar"))
        val calendarType = currentDateCalendar?.calendarType ?: CalendarType.CIVIL
        val pickType = PickType.valueOf(arguments?.getString("pickType") ?: PickType.NOTHING.name)
        val typefacePath = arguments?.getString("typefacePath")

        with(rootView) {
            calendarView.doNotInvalidate {
                if (calendarView.pickType == PickType.NOTHING) {
                    calendarView.calendarType = calendarType

                    calendarView.minDateCalendar = DateUtils.restoreCalendar(arguments?.getString("minDateCalendar"))
                    calendarView.maxDateCalendar = DateUtils.restoreCalendar(arguments?.getString("maxDateCalendar"))

                    calendarView.pickType = pickType
                    calendarView.pickedSingleDayCalendar = DateUtils.restoreCalendar(arguments?.getString("pickedSingleDayCalendar"))
                    calendarView.pickedRangeStartCalendar = DateUtils.restoreCalendar(arguments?.getString("pickedRangeStartCalendar"))
                    calendarView.pickedRangeEndCalendar = DateUtils.restoreCalendar(arguments?.getString("pickedRangeEndCalendar"))
                    arguments?.getStringArrayList("pickedMultipleDaysList")?.apply {
                        calendarView.pickedMultipleDaysList = map { DateUtils.restoreCalendar(it)!! }
                    }
                    calendarView.animateSelection = arguments?.getBoolean("animateSelection")
                            ?: true
                }
            }

            val typeface = typefacePath?.let {
                Typeface.createFromAsset(activityContext.assets, it).apply {
                    calendarView.typeface = this
                }
            }

            initHeaderLayout(pickType, typeface)

            calendarView.onDayPickedListener = this@PrimeDatePickerBottomSheet
            calendarView.goto(currentDateCalendar!!)

            positiveButton.setOnClickListener {
                when (calendarView.pickType) {
                    PickType.SINGLE -> {
                        if (calendarView.pickedSingleDayCalendar == null) {
                            toast(R.string.no_day_is_selected)
                        } else {
                            onDayPickedListener?.onSingleDayPicked(calendarView.pickedSingleDayCalendar!!)
                            dismiss()
                        }
                    }
                    PickType.RANGE_START, PickType.RANGE_END -> {
                        if (calendarView.pickedRangeStartCalendar == null || calendarView.pickedRangeEndCalendar == null) {
                            toast(R.string.no_range_is_selected)
                        } else {
                            onDayPickedListener?.onRangeDaysPicked(calendarView.pickedRangeStartCalendar!!, calendarView.pickedRangeEndCalendar!!)
                            dismiss()
                        }
                    }
                    PickType.MULTIPLE -> {
                        if (calendarView.pickedMultipleDaysList.isEmpty()) {
                            toast(R.string.no_day_is_selected)
                        } else {
                            onDayPickedListener?.onMultipleDaysPicked(calendarView.pickedMultipleDaysList)
                            dismiss()
                        }
                    }
                    PickType.NOTHING -> {
                    }
                }
            }

            negativeButton.setOnClickListener { dismiss() }

            todayButton.setOnClickListener {
                val calendar = CalendarFactory.newInstance(calendarType, calendarView.locale)
                calendarView.goto(calendar.year, calendar.month, true)
            }
        }
    }

    private fun initHeaderLayout(pickType: PickType, typeface: Typeface?) {
        with(rootView) {
            when (pickType) {
                PickType.SINGLE -> {
                    viewStub.layoutResource = R.layout.single_day_header
                    val view = viewStub.inflate()

                    typeface?.let { pickedTextView.typeface = it }

                    calendarView.pickedSingleDayCalendar?.apply {
                        pickedTextView.text = shortDateString
                    }

                    view.setOnClickListener {
                        calendarView.pickedSingleDayCalendar?.apply {
                            calendarView.goto(this, true)
                        }
                    }
                }
                PickType.RANGE_START, PickType.RANGE_END -> {
                    viewStub.layoutResource = R.layout.range_days_header
                    viewStub.inflate()

                    typeface?.let {
                        rangeStartTextView.typeface = it
                        rangeEndTextView.typeface = it
                    }

                    when (calendarView.pickType) {
                        PickType.RANGE_START -> {
                            rangeStartLinearLayout.isSelected = true
                            rangeEndLinearLayout.isSelected = false
                        }
                        PickType.RANGE_END -> {
                            rangeStartLinearLayout.isSelected = false
                            rangeEndLinearLayout.isSelected = true
                        }
                        else -> {
                        }
                    }

                    calendarView.pickedRangeStartCalendar?.apply {
                        rangeStartTextView.text = shortDateString
                    }
                    calendarView.pickedRangeEndCalendar?.apply {
                        rangeEndTextView.text = shortDateString
                    }

                    rangeStartLinearLayout.setOnClickListener {
                        calendarView.pickType = PickType.RANGE_START
                        rangeStartLinearLayout.isSelected = true
                        rangeEndLinearLayout.isSelected = false

                        calendarView.pickedRangeStartCalendar?.apply {
                            calendarView.goto(this, true)
                        }
                    }
                    rangeEndLinearLayout.setOnClickListener {
                        calendarView.pickType = PickType.RANGE_END
                        rangeStartLinearLayout.isSelected = false
                        rangeEndLinearLayout.isSelected = true

                        calendarView.pickedRangeEndCalendar?.apply {
                            calendarView.goto(this, true)
                        }
                    }
                }
                PickType.MULTIPLE -> {
                    viewStub.layoutResource = R.layout.multiple_days_header
                    viewStub.inflate()

                    recyclerView.layoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.HORIZONTAL, false)
                    recyclerView.adapter = multipleDaysAdapter
                    recyclerView.isNestedScrollingEnabled = false
                    recyclerView.speedFactor = 2.5f

                    multipleDaysAdapter.typeface = typeface
                    handleMultipleDaysHeader(arrayListOf())
                }
                PickType.NOTHING -> {
                }
            }
        }
    }

    override fun onDayPicked(pickType: PickType,
                             singleDay: PrimeCalendar?,
                             startDay: PrimeCalendar?,
                             endDay: PrimeCalendar?,
                             multipleDays: List<PrimeCalendar>?) {
        with(rootView) {
            when (pickType) {
                PickType.SINGLE -> {
                    singleDay?.apply {
                        pickedTextView.text = shortDateString
                    }
                }
                PickType.RANGE_START -> {
                    startDay?.apply {
                        rangeStartTextView.text = shortDateString
                    }
                    rangeEndTextView.text = endDay?.shortDateString ?: ""
                }
                PickType.RANGE_END -> {
                    endDay?.apply {
                        rangeEndTextView.text = shortDateString
                    }
                }
                PickType.MULTIPLE -> {
                    handleMultipleDaysHeader(multipleDays)
                }
                PickType.NOTHING -> {
                }
            }
        }
    }

    private fun handleMultipleDaysHeader(multipleDays: List<PrimeCalendar>?) {
        with(rootView) {
            multipleDays?.map {
                PickedDayDataHolder(it.shortDateString, it)
            }?.also {
                val count = multipleDaysAdapter.itemCount

                if (it.isEmpty()) {
                    emptyStateTextView.visibility = View.VISIBLE
                    arrayListOf(PickedDayEmptyDataHolder())
                } else {
                    emptyStateTextView.visibility = View.GONE
                    it
                }.let { list ->
                    multipleDaysAdapter.submitList(list)
                }

                if (it.size > 2 && count < it.size) {
                    recyclerView.smoothScrollTo(it.size - 1)
                }
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onCancelListener?.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss(dialog)
    }

    fun setOnCancelListener(listener: DialogInterface.OnCancelListener): PrimeDatePickerBottomSheet {
        onCancelListener = listener
        return this
    }

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener): PrimeDatePickerBottomSheet {
        onDismissListener = listener
        return this
    }

    fun setOnDateSetListener(listener: OnDayPickedListener): PrimeDatePickerBottomSheet {
        onDayPickedListener = listener
        return this
    }

    interface OnDayPickedListener {

        fun onSingleDayPicked(singleDay: PrimeCalendar)

        fun onRangeDaysPicked(startDay: PrimeCalendar, endDay: PrimeCalendar)

        fun onMultipleDaysPicked(multipleDays: List<PrimeCalendar>)
    }

    private fun toast(textResource: Int) =
            Toast.makeText(requireActivity(), textResource, Toast.LENGTH_SHORT).show()

    companion object {

        private var onCancelListener: DialogInterface.OnCancelListener? = null
        private var onDismissListener: DialogInterface.OnDismissListener? = null
        private var onDayPickedListener: OnDayPickedListener? = null

        @JvmStatic
        @JvmOverloads
        fun newInstance(
                currentDateCalendar: PrimeCalendar,
                minDateCalendar: PrimeCalendar? = null,
                maxDateCalendar: PrimeCalendar? = null,
                pickType: PickType,
                pickedSingleDayCalendar: PrimeCalendar? = null,
                pickedRangeStartCalendar: PrimeCalendar? = null,
                pickedRangeEndCalendar: PrimeCalendar? = null,
                pickedMultipleDaysList: List<PrimeCalendar>? = null,
                typefacePath: String? = null,
                animateSelection: Boolean = true
        ): PrimeDatePickerBottomSheet {
            val fragment = PrimeDatePickerBottomSheet()
            val bundle = Bundle()
            bundle.putString("currentDateCalendar", DateUtils.storeCalendar(currentDateCalendar))
            bundle.putString("minDateCalendar", DateUtils.storeCalendar(minDateCalendar))
            bundle.putString("maxDateCalendar", DateUtils.storeCalendar(maxDateCalendar))
            bundle.putString("pickType", pickType.name)
            bundle.putString("pickedSingleDayCalendar", DateUtils.storeCalendar(pickedSingleDayCalendar))
            bundle.putString("pickedRangeStartCalendar", DateUtils.storeCalendar(pickedRangeStartCalendar))
            bundle.putString("pickedRangeEndCalendar", DateUtils.storeCalendar(pickedRangeEndCalendar))
            if (pickedMultipleDaysList != null) {
                bundle.putStringArrayList("pickedMultipleDaysList", pickedMultipleDaysList.map { DateUtils.storeCalendar(it)!! } as ArrayList<String>)
            }
            bundle.putString("typefacePath", typefacePath)
            bundle.putBoolean("animateSelection", animateSelection)
            fragment.arguments = bundle
            return fragment
        }
    }

}
