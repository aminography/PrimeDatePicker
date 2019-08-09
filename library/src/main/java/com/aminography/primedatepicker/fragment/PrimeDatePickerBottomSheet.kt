package com.aminography.primedatepicker.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.tools.DateUtils
import com.aminography.primedatepicker.tools.screenSize
import kotlinx.android.synthetic.main.fragment_date_picker_bottom_sheet.view.*
import org.jetbrains.anko.support.v4.toast

class PrimeDatePickerBottomSheet : BaseBottomSheetDialogFragment(R.layout.fragment_date_picker_bottom_sheet), OnDayPickedListener {

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
        val currentDateCalendar = DateUtils.restoreCalendar(arguments?.getString("currentDateCalendar"))
        val calendarType = currentDateCalendar?.calendarType ?: CalendarType.CIVIL
        val typefacePath = arguments?.getString("typefacePath")

        with(rootView) {

            calendarView.doNotInvalidate {
                if (calendarView.pickType == PickType.NOTHING) {
                    calendarView.calendarType = calendarType

                    calendarView.minDateCalendar = DateUtils.restoreCalendar(arguments?.getString("minDateCalendar"))
                    calendarView.maxDateCalendar = DateUtils.restoreCalendar(arguments?.getString("maxDateCalendar"))

                    calendarView.pickType = PickType.valueOf(arguments?.getString("pickType")
                            ?: PickType.NOTHING.name)
                    calendarView.pickedSingleDayCalendar = DateUtils.restoreCalendar(arguments?.getString("pickedSingleDayCalendar"))
                    calendarView.pickedRangeStartCalendar = DateUtils.restoreCalendar(arguments?.getString("pickedRangeStartCalendar"))
                    calendarView.pickedRangeEndCalendar = DateUtils.restoreCalendar(arguments?.getString("pickedRangeEndCalendar"))
                }
            }

            typefacePath?.let {
                Typeface.createFromAsset(activityContext.assets, it).apply {
                    calendarView.typeface = this
                    pickedTextView.typeface = this
                    rangeStartTextView.typeface = this
                    rangeEndTextView.typeface = this
                }
            }

            calendarView.onDayPickedListener = this@PrimeDatePickerBottomSheet
            calendarView.goto(currentDateCalendar!!)

            positiveButton.setOnClickListener {
                when (calendarView.pickType) {
                    PickType.SINGLE -> {
                        if (calendarView.pickedSingleDayCalendar == null) {
                            toast("No day is selected!")
                        } else {
                            onDayPickedListener?.onSingleDayPicked(calendarView.pickedSingleDayCalendar!!)
                            dismiss()
                        }
                    }
                    PickType.RANGE_START, PickType.RANGE_END -> {
                        if (calendarView.pickedRangeStartCalendar == null || calendarView.pickedRangeEndCalendar == null) {
                            toast("No range is selected!")
                        } else {
                            onDayPickedListener?.onRangeDaysPicked(calendarView.pickedRangeStartCalendar!!, calendarView.pickedRangeEndCalendar!!)
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

            rangeStartLinearLayout.setOnClickListener {
                calendarView.pickType = PickType.RANGE_START
                rangeStartLinearLayout.isSelected = true
                rangeEndLinearLayout.isSelected = false
            }

            rangeEndLinearLayout.setOnClickListener {
                calendarView.pickType = PickType.RANGE_END
                rangeStartLinearLayout.isSelected = false
                rangeEndLinearLayout.isSelected = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        with(rootView) {
            when (calendarView.pickType) {
                PickType.SINGLE -> {
                    rangeLinearLayout.visibility = View.GONE
                    singleLinearLayout.visibility = View.VISIBLE

                    calendarView.pickedSingleDayCalendar?.apply {
                        pickedTextView.text = shortDateString
                    }
                }
                PickType.RANGE_START, PickType.RANGE_END -> {
                    rangeLinearLayout.visibility = View.VISIBLE
                    singleLinearLayout.visibility = View.GONE

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
                }
                PickType.NOTHING -> {
                }
            }
        }
    }

    override fun onDayPicked(pickType: PickType, singleDay: PrimeCalendar?, startDay: PrimeCalendar?, endDay: PrimeCalendar?) {
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
                    rangeEndTextView.text = endDay?.let {
                        it.shortDateString
                    } ?: ""
                }
                PickType.RANGE_END -> {
                    endDay?.apply {
                        rangeEndTextView.text = shortDateString
                    }
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
    }

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
                typefacePath: String? = null
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
            bundle.putString("typefacePath", typefacePath)
            fragment.arguments = bundle
            return fragment
        }
    }

}
