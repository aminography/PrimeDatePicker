package com.aminography.primedatepicker.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.tools.DateUtils
import com.aminography.primedatepicker.tools.PersianUtils
import com.aminography.primedatepicker.tools.screenSize
import kotlinx.android.synthetic.main.fragment_date_picker_bottom_sheet.view.*

class DatePickerBottomSheetDialogFragment : BaseBottomSheetDialogFragment(R.layout.fragment_date_picker_bottom_sheet), PrimeCalendarView.OnDayClickListener {

    private var currentDateCalendar: BaseCalendar? = null
    private var calendarType = CalendarType.CIVIL
    private var minDateCalendar: BaseCalendar? = null
    private var maxDateCalendar: BaseCalendar? = null
    private var pickType: PickType = PickType.NOTHING
    private var pickedSingleDayCalendar: BaseCalendar? = null
    private var pickedStartRangeCalendar: BaseCalendar? = null
    private var pickedEndRangeCalendar: BaseCalendar? = null

    private var onCancelListener: DialogInterface.OnCancelListener? = null
    private var onDismissListener: DialogInterface.OnDismissListener? = null
    private var onDateSetListener: OnDateSetListener? = null

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val parentView = rootView.parent as View
        parentView.setBackgroundColor(ContextCompat.getColor(activityContext, R.color.transparent))
        val params = parentView.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = activityContext.screenSize().y
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onInitViews(rootView: View) {
        currentDateCalendar = DateUtils.restoreCalendar(arguments?.getString("currentDateCalendar"))
        calendarType = currentDateCalendar?.calendarType ?: CalendarType.CIVIL

        minDateCalendar = DateUtils.restoreCalendar(arguments?.getString("minDateCalendar"))
        maxDateCalendar = DateUtils.restoreCalendar(arguments?.getString("maxDateCalendar"))

        pickType = PickType.valueOf(arguments?.getString("pickType") ?: PickType.NOTHING.name)
        pickedSingleDayCalendar = DateUtils.restoreCalendar(arguments?.getString("pickedSingleDayCalendar"))
        pickedStartRangeCalendar = DateUtils.restoreCalendar(arguments?.getString("pickedStartRangeCalendar"))
        pickedEndRangeCalendar = DateUtils.restoreCalendar(arguments?.getString("pickedEndRangeCalendar"))

        with(rootView) {

            calendarView.internalCalendarType = calendarType

            calendarView.internalMinDateCalendar = minDateCalendar
            calendarView.internalMaxDateCalendar = maxDateCalendar

            calendarView.internalPickType = pickType
            calendarView.internalPickedSingleDayCalendar = pickedSingleDayCalendar
            calendarView.internalPickedSingleDayCalendar = pickedSingleDayCalendar
            calendarView.internalPickedSingleDayCalendar = pickedSingleDayCalendar

            calendarView.onDayClickListener = this@DatePickerBottomSheetDialogFragment
            calendarView.goto(currentDateCalendar!!)

            pickedSingleDayCalendar?.apply {
                pickedTextView.text = when (calendarType) {
                    CalendarType.CIVIL -> longDateString
                    CalendarType.PERSIAN, CalendarType.HIJRI ->
                        PersianUtils.convertLatinDigitsToPersian(PersianUtils.convertLatinCommaToPersian(longDateString))
                }
            }

            positiveButton.setOnClickListener {
                when (pickType) {
                    PickType.SINGLE -> {
                        calendarView.pickedSingleDayCalendar?.apply {
                            onDateSetListener?.onDateSet(this)
                        }
                    }
                    else -> {
                    }
                }
                dismiss()
            }

            negativeButton.setOnClickListener { dismiss() }

            todayButton.setOnClickListener {
                val calendar = CalendarFactory.newInstance(calendarType)
//                when (pickType) {
//                    PickType.SINGLE -> {
//                        calendarView.internalPickedSingleDayCalendar = calendar
//                    }
//                    else -> {
//                    }
//                }
                calendarView.goto(calendar.year, calendar.month, true)
            }
        }
    }

    override fun onDayClick(calendarView: PrimeCalendarView, day: BaseCalendar) {
        with(rootView) {
            when (calendarView.pickType) {
                PickType.SINGLE -> {
                    calendarView.pickedSingleDayCalendar?.apply {
                        pickedTextView.text = when (calendarType) {
                            CalendarType.CIVIL -> longDateString
                            CalendarType.PERSIAN, CalendarType.HIJRI ->
                                PersianUtils.convertLatinDigitsToPersian(PersianUtils.convertLatinCommaToPersian(longDateString))
                        }
                    }
                }
                PickType.START_RANGE -> {
                }
                PickType.END_RANGE -> {
                }
                PickType.NOTHING -> {
                }
            }
        }
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        onCancelListener?.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss(dialog)
    }

    /**
     * Sets the minimal date supported by this DatePicker. Dates before (but not including) the
     * specified date will be disallowed from being selected.
     *
     * @param calendar a Calendar object set to the year, month, day desired as the mindate.
     */

    fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener) {
        this.onCancelListener = onCancelListener
    }

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener) {
        this.onDismissListener = onDismissListener
    }

    fun registerOnDateSetListener(onDateSetListener: OnDateSetListener): DatePickerBottomSheetDialogFragment {
        this.onDateSetListener = onDateSetListener
        return this
    }

    interface OnDateSetListener {

        fun onDateSet(calendar: BaseCalendar)
    }

    companion object {

        fun newInstance(
                currentDateCalendar: BaseCalendar,
                minDateCalendar: BaseCalendar? = null,
                maxDateCalendar: BaseCalendar? = null,
                pickType: PickType,
                pickedSingleDayCalendar: BaseCalendar? = null,
                pickedStartRangeCalendar: BaseCalendar? = null,
                pickedEndRangeCalendar: BaseCalendar? = null
        ): DatePickerBottomSheetDialogFragment {
            val fragment = DatePickerBottomSheetDialogFragment()
            val bundle = Bundle()
            bundle.putString("currentDateCalendar", DateUtils.storeCalendar(currentDateCalendar))
            bundle.putString("minDateCalendar", DateUtils.storeCalendar(minDateCalendar))
            bundle.putString("maxDateCalendar", DateUtils.storeCalendar(maxDateCalendar))
            bundle.putString("pickType", pickType.name)
            bundle.putString("pickedSingleDayCalendar", DateUtils.storeCalendar(pickedSingleDayCalendar))
            bundle.putString("pickedStartRangeCalendar", DateUtils.storeCalendar(pickedStartRangeCalendar))
            bundle.putString("pickedEndRangeCalendar", DateUtils.storeCalendar(pickedEndRangeCalendar))
            fragment.arguments = bundle
            return fragment
        }
    }

}
