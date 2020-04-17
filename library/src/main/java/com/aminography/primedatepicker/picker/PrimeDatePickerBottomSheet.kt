package com.aminography.primedatepicker.picker

import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.*
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.picker.action.ActionView
import com.aminography.primedatepicker.picker.base.BaseBottomSheetDialogFragment
import com.aminography.primedatepicker.picker.callback.BaseDayPickCallback
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.picker.callback.RangeDaysPickCallback
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.aminography.primedatepicker.picker.go.GotoView
import com.aminography.primedatepicker.picker.header.BaseLazyView
import com.aminography.primedatepicker.picker.header.multiple.MultipleHeaderView
import com.aminography.primedatepicker.picker.header.range.RangeHeaderView
import com.aminography.primedatepicker.picker.header.single.SingleHeaderView
import com.aminography.primedatepicker.tools.DateUtils
import com.aminography.primedatepicker.tools.LanguageUtils
import com.aminography.primedatepicker.tools.forceLocaleStrings
import kotlinx.android.synthetic.main.fragment_date_picker_bottom_sheet.view.*
import java.util.*

@Suppress("unused")
class PrimeDatePickerBottomSheet : BaseBottomSheetDialogFragment(
    R.layout.fragment_date_picker_bottom_sheet
), OnDayPickedListener, OnMonthLabelClickListener {

    private var onCancelListener: DialogInterface.OnCancelListener? = null
    private var onDismissListener: DialogInterface.OnDismissListener? = null
    private var onDayPickCallback: BaseDayPickCallback? = null

    private var calendarType: CalendarType = CalendarType.CIVIL

    private var internalPickType: PickType = PickType.NOTHING
    private var initialDateCalendar: PrimeCalendar? = null

    val pickType: PickType
        get() = internalPickType

    private lateinit var headerView: BaseLazyView
    private var gotoView: BaseLazyView? = null
    private var direction: Direction = Direction.LTR
    private lateinit var locale: Locale
    private var typeface: Typeface? = null

    override fun onInitViews(rootView: View) {
        initialDateCalendar = DateUtils.restoreCalendar(
            arguments?.getString("initialDateCalendar")
        )?.also {
            calendarType = it.calendarType
        }

        arguments?.getString("pickType")?.let { internalPickType = PickType.valueOf(it) }

        locale = initialDateCalendar!!.locale
        direction = LanguageUtils.direction(calendarType, initialDateCalendar!!.locale.language)
        typeface = arguments?.getString("typefacePath")?.let {
            Typeface.createFromAsset(activityContext.assets, it)
        }

        with(rootView) {
            typeface?.let { calendarView.typeface = it }

            calendarView.doNotInvalidate {
                if (calendarView.pickType == PickType.NOTHING) {
                    calendarView.calendarType = calendarType

                    calendarView.minDateCalendar = DateUtils.restoreCalendar(arguments?.getString("minDateCalendar"))
                    calendarView.maxDateCalendar = DateUtils.restoreCalendar(arguments?.getString("maxDateCalendar"))

                    arguments?.getInt("weekStartDay")?.let { calendarView.weekStartDay = it }

                    calendarView.pickType = internalPickType
                    calendarView.animateSelection = arguments?.getBoolean("animateSelection")
                        ?: true

                    calendarView.pickedSingleDayCalendar = DateUtils.restoreCalendar(arguments?.getString("pickedSingleDayCalendar"))
                    calendarView.pickedRangeStartCalendar = DateUtils.restoreCalendar(arguments?.getString("pickedRangeStartCalendar"))
                    calendarView.pickedRangeEndCalendar = DateUtils.restoreCalendar(arguments?.getString("pickedRangeEndCalendar"))
                    arguments?.getStringArrayList("pickedMultipleDaysList")?.run {
                        calendarView.pickedMultipleDaysList = map { DateUtils.restoreCalendar(it)!! }
                    }
                }
            }

            initHeaderView()
            initActionView()

            calendarView.onDayPickedListener = this@PrimeDatePickerBottomSheet
            calendarView.onMonthLabelClickListener = this@PrimeDatePickerBottomSheet
            calendarView.goto(initialDateCalendar!!)
        }
    }

    override fun onResume() {
        super.onResume()
        // To be sure of calendar view state restoration is done.
        with(rootView) {
            fab.isExpanded = false
            if (calendarView.pickType != PickType.NOTHING) {
                internalPickType = calendarView.pickType
                when (internalPickType) {
                    PickType.RANGE_START, PickType.RANGE_END -> {
                        (headerView as RangeHeaderView).run {
                            pickType = internalPickType
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun handleOnPositiveButtonClick(calendarView: PrimeCalendarView) {
        when (calendarView.pickType) {
            PickType.SINGLE -> {
                if (calendarView.pickedSingleDayCalendar == null) {
                    toast(requireContext().forceLocaleStrings(locale, R.string.no_day_is_selected)[0])
                } else {
                    (onDayPickCallback as? SingleDayPickCallback)?.onSingleDayPicked(
                        calendarView.pickedSingleDayCalendar!!
                    )
                    dismiss()
                }
            }
            PickType.RANGE_START, PickType.RANGE_END -> {
                if (calendarView.pickedRangeStartCalendar == null || calendarView.pickedRangeEndCalendar == null) {
                    toast(requireContext().forceLocaleStrings(locale, R.string.no_range_is_selected)[0])
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
                    toast(requireContext().forceLocaleStrings(locale, R.string.no_day_is_selected)[0])
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

    private fun initActionView() {
        with(rootView) {
            ActionView(actionViewStub, direction).also {
                it.locale = locale
                it.typeface = typeface
                it.onTodayButtonClick = { calendarView.goto(CalendarFactory.newInstance(calendarType, calendarView.locale), true) }
                it.onPositiveButtonClick = { handleOnPositiveButtonClick(calendarView) }
                it.onNegativeButtonClick = { dismiss() }
            }
        }
    }

    private fun initHeaderView() {
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
            headerView = SingleHeaderView(headerViewStub).also {
                it.locale = locale
                it.typeface = typeface
                it.pickedDay = calendarView.pickedSingleDayCalendar
                it.onPickedDayClickListener = {
                    calendarView.pickedSingleDayCalendar?.apply {
                        calendarView.focusOnDay(this)
                    }
                }
            }
        }
    }

    private fun initHeaderRange(typeface: Typeface?) {
        with(rootView) {
            headerView = RangeHeaderView(headerViewStub, direction).also {
                it.locale = locale
                it.typeface = typeface
                it.pickType = calendarView.pickType
                it.pickedRangeStartDay = calendarView.pickedRangeStartCalendar
                it.pickedRangeEndDay = calendarView.pickedRangeEndCalendar
                it.onRangeStartClickListener = {
                    calendarView.pickType = PickType.RANGE_START
                    calendarView.pickedRangeStartCalendar?.apply {
                        calendarView.goto(this, true)
                    }
                }
                it.onRangeEndClickListener = {
                    calendarView.pickType = PickType.RANGE_END
                    calendarView.pickedRangeEndCalendar?.apply {
                        calendarView.goto(this, true)
                    }
                }
            }
        }
    }

    private fun initHeaderMultiple(typeface: Typeface?) {
        with(rootView) {
            headerView = MultipleHeaderView(headerViewStub, direction).also {
                it.locale = locale
                it.typeface = typeface
                it.onPickedDayClickListener = { day ->
                    calendarView.focusOnDay(day)
                }
                it.pickedDays = calendarView?.pickedMultipleDaysList
            }
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
            PickType.SINGLE -> {
                (headerView as SingleHeaderView).pickedDay = singleDay
            }
            PickType.RANGE_START, PickType.RANGE_END -> {
                (headerView as RangeHeaderView).run {
                    pickedRangeStartDay = startDay
                    pickedRangeEndDay = endDay
                }
            }
            PickType.MULTIPLE -> {
                (headerView as MultipleHeaderView).pickedDays = multipleDays
            }
            PickType.NOTHING -> {
            }
        }
    }

    override fun onMonthLabelClicked(calendar: PrimeCalendar, touchedX: Int, touchedY: Int) {
        with(rootView) {
            fun expandGoto(isExpanded: Boolean, touchedX: Int, touchedY: Int) {
                if (isExpanded) {
                    (fab.layoutParams as CoordinatorLayout.LayoutParams).apply {
                        leftMargin = touchedX
                        topMargin = touchedY
                    }
                }
                fab.post { fab.isExpanded = isExpanded }
            }

            if (gotoView == null) {
                gotoView = GotoView(gotoViewStub, direction).also {
                    it.typeface = typeface
                    it.minDateCalendar = calendarView.minDateCalendar
                    it.maxDateCalendar = calendarView.maxDateCalendar
                }
            }
            (gotoView as? GotoView)?.also {
                it.calendar = calendar
                it.onCloseClickListener = { expandGoto(false, touchedX, touchedY) }
                it.onGoClickListener = { year, month ->
                    expandGoto(false, touchedX, touchedY)
                    postDelayed({
                        initialDateCalendar?.clone()?.let { calendar ->
                            calendar.year = year
                            calendar.month = month
                            calendarView.goto(calendar, true)
                        }
                    }, 250)
                }
            }
            expandGoto(true, touchedX, touchedY)
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

    fun setOnCancelListener(listener: DialogInterface.OnCancelListener?): PrimeDatePickerBottomSheet {
        onCancelListener = listener
        return this
    }

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener?): PrimeDatePickerBottomSheet {
        onDismissListener = listener
        return this
    }

    fun setDayPickCallback(callback: BaseDayPickCallback?): PrimeDatePickerBottomSheet {
        onDayPickCallback = callback
        return this
    }

    private fun toast(text: String) =
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()

    // Builder Constructions -----------------------------------------------------------------------

    abstract class BaseRequestBuilder<T : BaseDayPickCallback> internal constructor(
        pickType: PickType,
        initialDateCalendar: PrimeCalendar,
        private val callback: T?
    ) {

        protected val bundle = Bundle()

        init {
            bundle.putString("initialDateCalendar", DateUtils.storeCalendar(initialDateCalendar))
            bundle.putString("pickType", pickType.name)
        }

        fun weekStartDay(weekStartDay: Int): BaseRequestBuilder<T> {
            bundle.putInt("weekStartDay", weekStartDay)
            return this
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

        fun build(): PrimeDatePickerBottomSheet {
            return PrimeDatePickerBottomSheet().also {
                it.arguments = bundle
                it.setDayPickCallback(callback)
            }
        }
    }

    class SingleDayRequestBuilder internal constructor(
        initialDateCalendar: PrimeCalendar,
        callback: SingleDayPickCallback?
    ) : BaseRequestBuilder<SingleDayPickCallback>(PickType.SINGLE, initialDateCalendar, callback) {

        fun initiallyPickedSingleDay(singleDay: PrimeCalendar): SingleDayRequestBuilder {
            bundle.putString("pickedSingleDayCalendar", DateUtils.storeCalendar(singleDay))
            return this
        }
    }

    class RangeDaysRequestBuilder internal constructor(
        initialDateCalendar: PrimeCalendar,
        callback: RangeDaysPickCallback?
    ) : BaseRequestBuilder<RangeDaysPickCallback>(PickType.RANGE_START, initialDateCalendar, callback) {

        fun initiallyPickedRangeDays(startDay: PrimeCalendar, endDay: PrimeCalendar): RangeDaysRequestBuilder {
            bundle.putString("pickedRangeStartCalendar", DateUtils.storeCalendar(startDay))
            bundle.putString("pickedRangeEndCalendar", DateUtils.storeCalendar(endDay))
            return this
        }
    }

    class MultipleDaysRequestBuilder internal constructor(
        initialDateCalendar: PrimeCalendar,
        callback: MultipleDaysPickCallback?
    ) : BaseRequestBuilder<MultipleDaysPickCallback>(PickType.MULTIPLE, initialDateCalendar, callback) {

        fun initiallyPickedMultipleDays(multipleDays: List<PrimeCalendar>): MultipleDaysRequestBuilder {
            bundle.putStringArrayList("pickedMultipleDaysList", multipleDays.map {
                DateUtils.storeCalendar(it)!!
            } as ArrayList<String>)
            return this
        }

//        fun maxPickedDays(maxPickedDays: Int): MultipleDaysRequestBuilder {
//            bundle.putInt("maxPickedDays", maxPickedDays)
//            return this
//        }
    }

    class RequestBuilder(
        private val initialDateCalendar: PrimeCalendar
    ) {

        @JvmOverloads
        fun pickSingleDay(callback: SingleDayPickCallback? = null): SingleDayRequestBuilder {
            return SingleDayRequestBuilder(initialDateCalendar, callback)
        }

        @JvmOverloads
        fun pickRangeDays(callback: RangeDaysPickCallback? = null): RangeDaysRequestBuilder {
            return RangeDaysRequestBuilder(initialDateCalendar, callback)
        }

        @JvmOverloads
        fun pickMultipleDays(callback: MultipleDaysPickCallback? = null): MultipleDaysRequestBuilder {
            return MultipleDaysRequestBuilder(initialDateCalendar, callback)
        }
    }

    companion object {

        @JvmStatic
        fun from(initialDate: PrimeCalendar): RequestBuilder = RequestBuilder(initialDate)
    }

}
