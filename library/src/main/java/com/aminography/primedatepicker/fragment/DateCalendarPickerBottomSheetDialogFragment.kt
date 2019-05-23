package com.aminography.primedatepicker.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primecalendar.hijri.HijriCalendar
import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primedatepicker.*
import com.aminography.primedatepicker.tools.CurrentCalendarType
import com.aminography.primedatepicker.tools.PersianUtils
import com.aminography.primedatepicker.tools.TimeUtils
import com.aminography.primedatepicker.tools.Utils
import java.util.*

class DateCalendarPickerBottomSheetDialogFragment : BaseBottomSheetDialogFragment(R.layout.fragment_date_calendar_picker_bottom_sheet) {

    private val mBaseCalendar = CalendarFactory.newInstance(CurrentCalendarType.type)
    private var mCallBack: OnDateSetListener? = null
    private val mListeners = HashSet<OnDateChangedListener>()
    private var mOnCancelListener: DialogInterface.OnCancelListener? = null
    private var mOnDismissListener: DialogInterface.OnDismissListener? = null
    private var container: FrameLayout? = null

    private var mBaseMonthListView: BaseMonthListView? = null
    private var mCurrentView = UNINITIALIZED
    private var mMinYear = DEFAULT_START_YEAR
    private var mMaxYear = DEFAULT_END_YEAR
    private var mMinDate: BaseCalendar? = null
    private var mMaxDate: BaseCalendar? = null
    private var mHighlightedDays: Array<BaseCalendar>? = null
    private var mSelectableDays: Array<BaseCalendar>? = null
    private var mDelayAnimation = true

    private var mDayPickerDescription: String? = null
    private var mSelectDay: String? = null
    private var mYearPickerDescription: String? = null
    private var mSelectYear: String? = null
    private var fontName: String? = null
    private var mTimeTextView: TextView? = null

    @ColorInt
    private var mainColor: Int? = null

    private var mWeekStart = when (CurrentCalendarType.type) {
        CalendarType.CIVIL -> Calendar.SUNDAY
        CalendarType.PERSIAN -> Calendar.SATURDAY
        CalendarType.HIJRI -> Calendar.SATURDAY
    }

    private val mDatePickerController = object : DatePickerController {

        override val minDate: BaseCalendar?
            get() = mMinDate

        override val maxDate: BaseCalendar?
            get() = mMaxDate

        override val highlightedDays: Array<BaseCalendar>?
            get() = mHighlightedDays

        override val selectableDays: Array<BaseCalendar>?
            get() = mSelectableDays

        override val selectedDay: BaseCalendar
            get() = mBaseCalendar
//            get() = BaseCalendar(mBaseCalendar)

        override// Ensure no years can be selected outside of the given minimum date
        val minYear: Int
            get() {
                if (mSelectableDays != null) {
                    return mSelectableDays!![0].year
                }
                return if (mMinDate != null && mMinDate!!.year > mMinYear) mMinDate!!.year else mMinYear
            }

        override// Ensure no years can be selected outside of the given maximum date
        val maxYear: Int
            get() {
                if (mSelectableDays != null) {
                    return mSelectableDays!![mSelectableDays!!.size - 1].year
                }
                return if (mMaxDate != null && mMaxDate!!.year < mMaxYear) mMaxDate!!.year else mMaxYear
            }

        override var firstDayOfWeek: Int
            get() = mWeekStart
            set(startOfWeek) {
                if (startOfWeek < Calendar.SUNDAY || startOfWeek > Calendar.SATURDAY) {
                    throw IllegalArgumentException("Value must be between Calendar.SUNDAY and " + "Calendar.SATURDAY")
                }
                mWeekStart = startOfWeek
                if (mBaseMonthListView != null) {
                    mBaseMonthListView!!.onChange()
                }
            }

        override var typeface: String?
            get() = fontName
            set(fontName) {
                this@DateCalendarPickerBottomSheetDialogFragment.fontName = fontName
            }

        override fun onYearSelected(year: Int) {
            mBaseCalendar.setDate(year, mBaseCalendar.month, mBaseCalendar.dayOfMonth)
            updatePickers()
            setCurrentView(MONTH_AND_DAY_VIEW)
        }

        override fun onDayOfMonthSelected(year: Int, month: Int, day: Int) {
            mBaseCalendar.setDate(year, month, day)
            updatePickers()

            val date = when (CurrentCalendarType.type) {
                CalendarType.CIVIL -> TimeUtils.formatSimpleDate(mBaseCalendar)
                CalendarType.PERSIAN -> PersianUtils.convertLatinDigitsToPersian(TimeUtils.formatSimpleDate(mBaseCalendar))
                CalendarType.HIJRI -> PersianUtils.convertLatinDigitsToPersian(TimeUtils.formatSimpleDate(mBaseCalendar))
            }
            mTimeTextView?.text = date
        }

        override fun registerOnDateChangedListener(listener: OnDateChangedListener) {
            mListeners.add(listener)
        }

        override fun unregisterOnDateChangedListener(listener: OnDateChangedListener) {
            mListeners.remove(listener)
        }

    }

    override fun onInitViews(rootView: View) {
        with(rootView) {
            findViewById<Button>(R.id.positiveButton).setOnClickListener {
                mCallBack?.onDateSet(mBaseCalendar)
                dismiss()
            }

            findViewById<Button>(R.id.negativeButton).setOnClickListener { dismiss() }

            findViewById<Button>(R.id.todayButton).setOnClickListener {
                val calendarDay = Utils.newCalendar()
                mBaseMonthListView!!.goTo(calendarDay, false, true, true)
                mDatePickerController.onDayOfMonthSelected(calendarDay.year, calendarDay.month, calendarDay.dayOfMonth)
            }

            val currentView = MONTH_AND_DAY_VIEW

            mBaseMonthListView = SimpleMonthListView(activity!!.applicationContext, null, mDatePickerController, mainColor)
            mDayPickerDescription = resources.getString(R.string.mdtp_day_picker_description)
            mSelectDay = resources.getString(R.string.mdtp_select_day)
            mYearPickerDescription = resources.getString(R.string.mdtp_year_picker_description)
            mSelectYear = resources.getString(R.string.mdtp_select_year)

            rootView.setBackgroundColor(ContextCompat.getColor(activityContext, R.color.white))

            mTimeTextView = rootView.findViewById(R.id.dateText)
            mTimeTextView?.typeface = when (CurrentCalendarType.type) {
                CalendarType.CIVIL -> null
                CalendarType.PERSIAN -> null
                CalendarType.HIJRI -> null
//                CalendarType.PERSIAN -> Typeface.createFromAsset(context.assets, TypefaceHelper.FONT_PATH)
//                CalendarType.HIJRI -> Typeface.createFromAsset(context.assets, TypefaceHelper.FONT_PATH)
            }
            mainColor?.apply {
                mTimeTextView?.setBackgroundColor(this)
            }

            container = rootView.findViewById(R.id.container)
            container!!.addView(mBaseMonthListView)

            setCurrentView(currentView)

            // Added by Amin ---------------------------------------------------------------------------
            if (mMinDate != null) setMinDate(mMinDate)
            if (mMaxDate != null) setMaxDate(mMaxDate)
            mDatePickerController.onDayOfMonthSelected(mBaseCalendar.year, mBaseCalendar.month, mBaseCalendar.dayOfMonth)
            // Added by Amin ---------------------------------------------------------------------------
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            val behavior: BottomSheetBehavior<*>
            if (bottomSheet != null) {
                behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                            behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                })
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        if (savedInstanceState != null) {
            mBaseCalendar.setDate(
                    savedInstanceState.getInt(KEY_SELECTED_YEAR),
                    savedInstanceState.getInt(KEY_SELECTED_MONTH),
                    savedInstanceState.getInt(KEY_SELECTED_DAY)
            )
        }
    }

    fun initialize(callBack: OnDateSetListener, year: Int, monthOfYear: Int, dayOfMonth: Int, @ColorInt mainColor: Int? = null) {
        mCallBack = callBack
        this.mainColor = mainColor
        mBaseCalendar.setDate(year, monthOfYear, dayOfMonth)
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        if (mOnCancelListener != null) {
            mOnCancelListener!!.onCancel(dialog)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        if (mOnDismissListener != null) {
            mOnDismissListener!!.onDismiss(dialog)
        }
    }

    private fun setCurrentView(viewIndex: Int) {

        when (viewIndex) {
            MONTH_AND_DAY_VIEW -> {
                //                ObjectAnimator pulseAnimator = Utils.getPulseAnimator(mMonthAndDayView, 0.9f,
                //                        1.05f);
                if (mDelayAnimation) {
                    //                    pulseAnimator.setStartDelay(ANIMATION_DELAY);
                    mDelayAnimation = false
                }
                mBaseMonthListView!!.onDateChanged()
                if (mCurrentView != viewIndex) {
                    //                    mMonthAndDayView.setSelected(true);
                    //                    mYearView.setSelected(false);
                    mCurrentView = viewIndex
                }
                //                pulseAnimator.start();
            }
        }
    }

    fun setYearRange(startYear: Int, endYear: Int) {
        if (endYear < startYear) {
            throw IllegalArgumentException("Year end must be larger than or equal to year start")
        }

        mMinYear = startYear
        mMaxYear = endYear
        if (mBaseMonthListView != null) {
            mBaseMonthListView!!.onChange()
        }
    }

    /**
     * Sets the minimal date supported by this DatePicker. Dates before (but not including) the
     * specified date will be disallowed from being selected.
     *
     * @param calendar a Calendar object set to the year, month, day desired as the mindate.
     */
    fun setMinDate(calendar: BaseCalendar?) {
        mMinDate = calendar
        mMinDate!!.set(Calendar.HOUR_OF_DAY, 0)
        mMinDate!!.set(Calendar.MINUTE, 0)
        mMinDate!!.set(Calendar.SECOND, 0)
        mMinDate!!.set(Calendar.MILLISECOND, 0)

        if (mBaseMonthListView != null) {
            mBaseMonthListView!!.onChange()
        }
        Log.d(TAG, "setMinDate: " + mMinDate!!.year + "-" + mMinDate!!.month + "-" + mMinDate!!.dayOfMonth)
    }

    /**
     * Sets the minimal date supported by this DatePicker. Dates after (but not including) the
     * specified date will be disallowed from being selected.
     *
     * @param calendar a Calendar object set to the year, month, day desired as the maxdate.
     */
    fun setMaxDate(calendar: BaseCalendar?) {
        mMaxDate = calendar
        mMaxDate!!.set(Calendar.HOUR_OF_DAY, 0)
        mMaxDate!!.set(Calendar.MINUTE, 0)
        mMaxDate!!.set(Calendar.SECOND, 0)
        mMaxDate!!.set(Calendar.MILLISECOND, 0)

        if (mBaseMonthListView != null) {
            mBaseMonthListView!!.onChange()
        }
        Log.d(TAG, "setMaxDate: " + mMaxDate!!.year + "-" + mMaxDate!!.month + "-" + mMaxDate!!.dayOfMonth)
    }

    /**
     * Sets an array of dates which should be highlighted when the picker is drawn
     *
     * @param highlightedDays an Array of Calendar objects containing the dates to be highlighted
     */
    fun setHighlightedDays(highlightedDays: Array<BaseCalendar>) {
        // Sort the array to optimize searching over it later on
        Arrays.sort(highlightedDays)
        this.mHighlightedDays = highlightedDays
    }

    /**
     * Set's a list of days which are the only valid selections.
     * Setting this value will take precedence over using setMinDate() and setMaxDate()
     *
     * @param selectableDays an Array of Calendar Objects containing the selectable dates
     */
    fun setSelectableDays(selectableDays: Array<BaseCalendar>) {
        // Sort the array to optimize searching over it later on
        Arrays.sort(selectableDays)
        this.mSelectableDays = selectableDays
    }

    fun setOnDateSetListener(listener: OnDateSetListener) {
        mCallBack = listener
    }

    fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener) {
        mOnCancelListener = onCancelListener
    }

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener) {
        mOnDismissListener = onDismissListener
    }

    private fun updatePickers() {
        for (listener in mListeners) {
            listener.onDateChanged()
        }
    }

    /**
     * The callback used to indicate the user is done filling in the date.
     */
    interface OnDateSetListener {

        fun onDateSet(calendar: BaseCalendar)
    }

    /**
     * The callback used to notify other date picker components of a change in selected date.
     */
    interface OnDateChangedListener {

        fun onDateChanged()
    }

    companion object {

        private val TAG = "DatePickerDialog"

        private const val UNINITIALIZED = -1
        private const val MONTH_AND_DAY_VIEW = 0
        private const val YEAR_VIEW = 1

        private const val KEY_SELECTED_YEAR = "year"
        private const val KEY_SELECTED_MONTH = "month"
        private const val KEY_SELECTED_DAY = "day"

        private var DEFAULT_START_YEAR = 1950
        private var DEFAULT_END_YEAR = 2050

//        private const val DEFAULT_START_YEAR = 1350
//        private const val DEFAULT_END_YEAR = 1450

        private const val ANIMATION_DURATION = 300
        private const val ANIMATION_DELAY = 500

        /**
         * @param callBack        How the parent is notified that the date is set.
         * @param initYear        The initial year of the dialog.
         * @param initMonthOfYear The initial month of the dialog.
         * @param initDayOfMonth  The initial day of the dialog.
         */
        fun newInstance(
                callBack: OnDateSetListener,
                initCalendar: BaseCalendar,
                @ColorInt mainColor: Int? = null
        ): DateCalendarPickerBottomSheetDialogFragment {

            val initYear = initCalendar.year
            val initMonthOfYear = initCalendar.month
            val initDayOfMonth = initCalendar.dayOfMonth

            DEFAULT_START_YEAR = initYear - 10
            DEFAULT_END_YEAR = initYear + 10

            CurrentCalendarType.type = when (initCalendar) {
                is CivilCalendar -> CalendarType.CIVIL
                is PersianCalendar -> CalendarType.PERSIAN
                is HijriCalendar -> CalendarType.HIJRI
                else -> CalendarType.CIVIL
            }

            val fragment = DateCalendarPickerBottomSheetDialogFragment()
            fragment.initialize(callBack, initYear, initMonthOfYear, initDayOfMonth, mainColor)
            return fragment
        }
    }

}
