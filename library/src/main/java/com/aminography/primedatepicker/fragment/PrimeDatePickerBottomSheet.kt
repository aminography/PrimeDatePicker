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
import com.aminography.primedatepicker.fragment.callback.BaseDayPickCallback
import com.aminography.primedatepicker.fragment.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.fragment.callback.RangeDaysPickCallback
import com.aminography.primedatepicker.fragment.callback.SingleDayPickCallback
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

    private var onCancelListener: DialogInterface.OnCancelListener? = null
    private var onDismissListener: DialogInterface.OnDismissListener? = null
    private var onDayPickCallback: BaseDayPickCallback? = null

    private var internalPickType: PickType = PickType.NOTHING

    val pickType: PickType
        get() = internalPickType

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
        val initialDateCalendar = DateUtils.restoreCalendar(arguments?.getString("initialDateCalendar"))
        val calendarType = initialDateCalendar?.calendarType ?: CalendarType.CIVIL
        internalPickType = PickType.valueOf(arguments?.getString("pickType")
            ?: PickType.NOTHING.name)
        val typefacePath = arguments?.getString("typefacePath")

        with(rootView) {
            calendarView.doNotInvalidate {
                if (calendarView.pickType == PickType.NOTHING) {
                    calendarView.calendarType = calendarType

                    calendarView.minDateCalendar = DateUtils.restoreCalendar(arguments?.getString("minDateCalendar"))
                    calendarView.maxDateCalendar = DateUtils.restoreCalendar(arguments?.getString("maxDateCalendar"))

                    calendarView.pickType = internalPickType
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
            calendarView.goto(initialDateCalendar!!)

            positiveButton.setOnClickListener {
                when (calendarView.pickType) {
                    PickType.SINGLE -> {
                        if (calendarView.pickedSingleDayCalendar == null) {
                            toast(R.string.no_day_is_selected)
                        } else {
                            (onDayPickCallback as? SingleDayPickCallback)?.onSingleDayPicked(
                                calendarView.pickedSingleDayCalendar!!
                            )
                            dismiss()
                        }
                    }
                    PickType.RANGE_START, PickType.RANGE_END -> {
                        if (calendarView.pickedRangeStartCalendar == null || calendarView.pickedRangeEndCalendar == null) {
                            toast(R.string.no_range_is_selected)
                        } else {
                            (onDayPickCallback as? RangeDaysPickCallback)?.onRangeDaysPicked(
                                calendarView.pickedRangeStartCalendar!!,
                                calendarView.pickedRangeEndCalendar!!
                            )
                            dismiss()
                        }
                    }
                    PickType.MULTIPLE -> {
                        if (calendarView.pickedMultipleDaysList.isEmpty()) {
                            toast(R.string.no_day_is_selected)
                        } else {
                            (onDayPickCallback as? MultipleDaysPickCallback)?.onMultipleDaysPicked(
                                calendarView.pickedMultipleDaysList
                            )
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
        when (pickType) {
            PickType.SINGLE -> initHeaderSingle(typeface)
            PickType.RANGE_START, PickType.RANGE_END -> initHeaderRange(typeface)
            PickType.MULTIPLE -> initHeaderMultiple(typeface)
            PickType.NOTHING -> {
            }
        }
    }

    private fun initHeaderSingle(typeface: Typeface?) {
        with(rootView) {
            viewStub.layoutResource = R.layout.single_day_header
            val view = viewStub.inflate()

            typeface?.let { pickedTextView.typeface = it }

            calendarView.pickedSingleDayCalendar?.apply {
                pickedTextView.secondLabelText = shortDateString
            }

            view.setOnClickListener {
                calendarView.pickedSingleDayCalendar?.apply {
                    calendarView.focusOnDay(this)
                }
            }
        }
    }

    private fun initHeaderRange(typeface: Typeface?) {
        with(rootView) {
            viewStub.layoutResource = R.layout.range_days_header
            viewStub.inflate()

            typeface?.let {
                rangeStartTextView.typeface = it
                rangeEndTextView.typeface = it
            }

            when (calendarView.pickType) {
                PickType.RANGE_START -> {
                    rangeStartBackView.isSelected = true
                    rangeEndBackView.isSelected = false
                }
                PickType.RANGE_END -> {
                    rangeStartBackView.isSelected = false
                    rangeEndBackView.isSelected = true
                }
                else -> {
                }
            }

            calendarView.pickedRangeStartCalendar?.apply {
                rangeStartTextView.secondLabelText = shortDateString
            }
            calendarView.pickedRangeEndCalendar?.apply {
                rangeEndTextView.secondLabelText = shortDateString
            }

            rangeStartBackView.setOnClickListener {
                calendarView.pickType = PickType.RANGE_START
                rangeStartBackView.isSelected = true
                rangeEndBackView.isSelected = false

                calendarView.pickedRangeStartCalendar?.apply {
                    calendarView.goto(this, true)
                }
            }
            rangeEndBackView.setOnClickListener {
                calendarView.pickType = PickType.RANGE_END
                rangeStartBackView.isSelected = false
                rangeEndBackView.isSelected = true

                calendarView.pickedRangeEndCalendar?.apply {
                    calendarView.goto(this, true)
                }
            }
        }
    }

    private fun initHeaderMultiple(typeface: Typeface?) {
        with(rootView) {
            viewStub.layoutResource = R.layout.multiple_days_header
            viewStub.inflate()

            recyclerView.layoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = multipleDaysAdapter
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.speedFactor = 2.5f

            multipleDaysAdapter.typeface = typeface
            refreshMultipleDaysHeader(arrayListOf())
        }
    }

    override fun onDayPicked(
        pickType: PickType,
        singleDay: PrimeCalendar?,
        startDay: PrimeCalendar?,
        endDay: PrimeCalendar?,
        multipleDays: List<PrimeCalendar>?
    ) {
        when (pickType) {
            PickType.SINGLE -> refreshSingleDayHeader(singleDay)
            PickType.RANGE_START, PickType.RANGE_END -> refreshRangeDaysHeader(pickType, startDay, endDay)
            PickType.MULTIPLE -> refreshMultipleDaysHeader(multipleDays)
            PickType.NOTHING -> {
            }
        }
    }

    private fun refreshSingleDayHeader(singleDay: PrimeCalendar?) {
        with(rootView) {
            singleDay?.run {
                pickedTextView.secondLabelText = shortDateString
            }
        }
    }

    private fun refreshRangeDaysHeader(pickType: PickType, startDay: PrimeCalendar?, endDay: PrimeCalendar?) {
        with(rootView) {
            when (pickType) {
                PickType.RANGE_START -> {
                    startDay?.run {
                        rangeStartTextView.secondLabelText = shortDateString
                    }
                    rangeEndTextView.secondLabelText = endDay?.shortDateString ?: ""
                }
                PickType.RANGE_END -> {
                    endDay?.run {
                        rangeEndTextView.secondLabelText = shortDateString
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun refreshMultipleDaysHeader(multipleDays: List<PrimeCalendar>?) {
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

    fun setDayPickCallback(callback: BaseDayPickCallback) {
        onDayPickCallback = callback
    }

    private fun toast(textResource: Int) =
        Toast.makeText(requireActivity(), textResource, Toast.LENGTH_SHORT).show()

    //----------------------------------------------------------------------------------------------

    abstract class BaseRequestBuilder<T : BaseDayPickCallback> internal constructor(
        pickType: PickType,
        initialDateCalendar: PrimeCalendar
    ) {

        protected val bundle = Bundle()

        init {
            bundle.putString("initialDateCalendar", DateUtils.storeCalendar(initialDateCalendar))
            bundle.putString("pickType", pickType.name)
        }

        fun minPossibleDate(minDate: PrimeCalendar?): BaseRequestBuilder<T> {
            bundle.putString("minDateCalendar", DateUtils.storeCalendar(minDate))
            return this
        }

        fun maxPossibleDate(maxDate: PrimeCalendar?): BaseRequestBuilder<T> {
            bundle.putString("maxDateCalendar", DateUtils.storeCalendar(maxDate))
            return this
        }

        fun typefacePath(typefacePath: String): BaseRequestBuilder<T> {
            bundle.putString("typefacePath", typefacePath)
            return this
        }

        fun animateSelection(animateSelection: Boolean): BaseRequestBuilder<T> {
            bundle.putBoolean("animateSelection", animateSelection)
            return this
        }

        fun build(callback: T): PrimeDatePickerBottomSheet {
            return PrimeDatePickerBottomSheet().also {
                it.arguments = bundle
                it.setDayPickCallback(callback)
            }
        }
    }

    class SingleDayRequestBuilder internal constructor(
        initialDateCalendar: PrimeCalendar
    ) : BaseRequestBuilder<SingleDayPickCallback>(PickType.SINGLE, initialDateCalendar) {

        fun initiallyPickedSingleDay(singleDay: PrimeCalendar): SingleDayRequestBuilder {
            bundle.putString("pickedSingleDayCalendar", DateUtils.storeCalendar(singleDay))
            return this
        }
    }

    class RangeDaysRequestBuilder internal constructor(
        initialDateCalendar: PrimeCalendar
    ) : BaseRequestBuilder<RangeDaysPickCallback>(PickType.RANGE_START, initialDateCalendar) {

        fun initiallyPickedRangeDays(startDay: PrimeCalendar, endDay: PrimeCalendar): RangeDaysRequestBuilder {
            bundle.putString("pickedRangeStartCalendar", DateUtils.storeCalendar(startDay))
            bundle.putString("pickedRangeEndCalendar", DateUtils.storeCalendar(endDay))
            return this
        }
    }

    class MultipleDaysRequestBuilder internal constructor(
        initialDateCalendar: PrimeCalendar
    ) : BaseRequestBuilder<MultipleDaysPickCallback>(PickType.MULTIPLE, initialDateCalendar) {

        fun initiallyPickedMultipleDays(multipleDays: List<PrimeCalendar>): MultipleDaysRequestBuilder {
            bundle.putStringArrayList("pickedMultipleDaysList", multipleDays.map {
                DateUtils.storeCalendar(it)!!
            } as ArrayList<String>)
            return this
        }
    }

    class RequestBuilder(
        private val initialDateCalendar: PrimeCalendar
    ) {

        fun pickSingleDay(): SingleDayRequestBuilder {
            return SingleDayRequestBuilder(initialDateCalendar)
        }

        fun pickRangeDays(): RangeDaysRequestBuilder {
            return RangeDaysRequestBuilder(initialDateCalendar)
        }

        fun pickMultipleDays(): MultipleDaysRequestBuilder {
            return MultipleDaysRequestBuilder(initialDateCalendar)
        }
    }

    companion object {

        @JvmStatic
        fun with(initialDate: PrimeCalendar): RequestBuilder =
            RequestBuilder(initialDate)
    }

}
