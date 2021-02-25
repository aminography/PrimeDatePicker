package com.aminography.primedatepicker.picker

import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.FragmentManager
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.common.Direction
import com.aminography.primedatepicker.common.OnDayPickedListener
import com.aminography.primedatepicker.common.OnMonthLabelClickListener
import com.aminography.primedatepicker.common.PickType
import com.aminography.primedatepicker.picker.action.ActionBarView
import com.aminography.primedatepicker.picker.base.BaseLazyView
import com.aminography.primedatepicker.picker.callback.BaseDayPickCallback
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.picker.callback.RangeDaysPickCallback
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.aminography.primedatepicker.picker.go.GotoView
import com.aminography.primedatepicker.picker.selection.SelectionBarView
import com.aminography.primedatepicker.picker.selection.multiple.MultipleDaysSelectionBarView
import com.aminography.primedatepicker.picker.selection.range.RangeDaysSelectionBarView
import com.aminography.primedatepicker.picker.selection.single.SingleDaySelectionBarView
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.aminography.primedatepicker.picker.theme.base.ThemeFactory
import com.aminography.primedatepicker.picker.theme.base.applyTheme
import com.aminography.primedatepicker.utils.*
import kotlinx.android.synthetic.main.fragment_date_picker_bottom_sheet.view.*
import kotlinx.coroutines.CoroutineScope
import java.util.*

/**
 * `PrimeDatePickerBottomSheet` contains the logic of picking days in a bottom sheet view.
 *
 * @author aminography
 */
@Suppress("unused")
internal class PrimeDatePickerImpl(
    private var onDismiss: (() -> Unit)? = null
) : PrimeDatePicker, OnDayPickedListener, OnMonthLabelClickListener {

    private lateinit var context: Context
    private lateinit var coroutineScope: CoroutineScope

    private var onCancelListener: DialogInterface.OnCancelListener? = null
    private var onDismissListener: DialogInterface.OnDismissListener? = null
    private var onDayPickCallback: BaseDayPickCallback? = null
    private var onDayPickedListener: OnDayPickedListener? = null

    private var internalPickType: PickType = PickType.NOTHING
    private lateinit var initialDateCalendar: PrimeCalendar

    private val calendarType: CalendarType
        get() = initialDateCalendar.calendarType

    private val locale: Locale
        get() = initialDateCalendar.locale

    override val pickType: PickType
        get() = internalPickType

    private lateinit var rootView: View
    private lateinit var selectionBarView: SelectionBarView
    private var gotoView: BaseLazyView? = null
    private var direction: Direction = Direction.LTR
    private var typeface: Typeface? = null
    private var autoSelectPickEndDay: Boolean = true
    private lateinit var themeFactory: ThemeFactory

    internal fun onCreate(context: Context, coroutineScope: CoroutineScope) {
        this.context = context
        this.coroutineScope = coroutineScope
    }

    internal fun onInitViews(rootView: View, arguments: Bundle?) {
        this.rootView = rootView

        initialDateCalendar = DateUtils.restoreCalendar(
            arguments?.getString("initialDateCalendar")
        ) ?: CivilCalendar()

        arguments?.getInt("firstDayOfWeek", -1)?.takeIf {
            it != -1
        }?.let {
            initialDateCalendar.firstDayOfWeek = it
        }

        arguments?.getBoolean("autoSelectPickEndDay", true)?.let {
            autoSelectPickEndDay = it
        }

        arguments?.getString("pickType")?.let { internalPickType = PickType.valueOf(it) }
        arguments?.getBoolean("initiallyPickEndDay")?.takeIf { it }?.let {
            internalPickType = PickType.RANGE_END
        }

        direction = calendarType.findDirection(locale)

        themeFactory = arguments?.getSerializable("themeFactory") as? ThemeFactory
            ?: LightThemeFactory()

        themeFactory.context = context

        themeFactory.typefacePath?.let {
            typeface = Typeface.createFromAsset(context.assets, it)
        }

        with(rootView) {
            themeFactory.let {
                ImageViewCompat.setImageTintList(
                    cardBackgroundImageView,
                    ColorStateList.valueOf(it.dialogBackgroundColor)
                )
                generateTopGradientDrawable(it.gotoViewBackgroundColor).let { drawable ->
                    circularRevealFrameLayout.setBackgroundDrawableCompat(drawable)
                }
            }
            typeface?.let { calendarView.typeface = it }

            calendarView.doNotInvalidate {
                if (it.pickType == PickType.NOTHING) {
                    it.calendarType = calendarType

                    val minFeasibleDate = CalendarFactory.newInstance(calendarType).also { calendar ->
                        calendar.set(1, 0, 1)
                    }
                    val maxFeasibleDate = CalendarFactory.newInstance(calendarType).also { calendar ->
                        calendar.set(9999, 11, 29)
                    }

                    it.minDateCalendar = DateUtils.restoreCalendar(arguments?.getString("minDateCalendar"))?.takeIf { min ->
                        min.after(minFeasibleDate)
                    } ?: minFeasibleDate

                    it.maxDateCalendar = DateUtils.restoreCalendar(arguments?.getString("maxDateCalendar"))?.takeIf { max ->
                        max.before(maxFeasibleDate)
                    } ?: maxFeasibleDate

                    arguments?.getStringArrayList("disabledDaysList")?.run {
                        it.disabledDaysList = map { list -> DateUtils.restoreCalendar(list)!! }
                    }

                    it.pickType = internalPickType

                    it.pickedSingleDayCalendar = DateUtils.restoreCalendar(arguments?.getString("pickedSingleDayCalendar"))
                    it.pickedRangeStartCalendar = DateUtils.restoreCalendar(arguments?.getString("pickedRangeStartCalendar"))
                    it.pickedRangeEndCalendar = DateUtils.restoreCalendar(arguments?.getString("pickedRangeEndCalendar"))
                    arguments?.getStringArrayList("pickedMultipleDaysList")?.run {
                        it.pickedMultipleDaysList = map { list -> DateUtils.restoreCalendar(list)!! }
                    }

                    it.applyTheme(themeFactory)
                }
            }

            initActionBar()
            initSelectionBar()

            calendarView.onDayPickedListener = this@PrimeDatePickerImpl
            calendarView.onMonthLabelClickListener = this@PrimeDatePickerImpl
            calendarView.goto(initialDateCalendar)
        }
    }

    internal fun onResume() {
        // To be sure of calendar view state restoration is done.
        with(rootView) {
            fab.isExpanded = false
            if (calendarView.pickType != PickType.NOTHING) {
                internalPickType = calendarView.pickType
                when (internalPickType) {
                    PickType.RANGE_START, PickType.RANGE_END -> {
                        (selectionBarView as RangeDaysSelectionBarView).run {
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
                    toast(context.forceLocaleStrings(locale, R.string.no_day_is_selected)[0])
                } else {
                    (onDayPickCallback as? SingleDayPickCallback)?.onSingleDayPicked(
                        calendarView.pickedSingleDayCalendar!!
                    )
                    onDismiss?.invoke()
                }
            }
            PickType.RANGE_START, PickType.RANGE_END -> {
                if (calendarView.pickedRangeStartCalendar == null || calendarView.pickedRangeEndCalendar == null) {
                    toast(context.forceLocaleStrings(locale, R.string.no_range_is_selected)[0])
                } else {
                    (onDayPickCallback as? RangeDaysPickCallback)?.onRangeDaysPicked(
                        calendarView.pickedRangeStartCalendar!!,
                        calendarView.pickedRangeEndCalendar!!
                    )
                    onDismiss?.invoke()
                }
            }
            PickType.MULTIPLE -> {
                if (calendarView.pickedMultipleDaysList.isEmpty()) {
                    toast(context.forceLocaleStrings(locale, R.string.no_day_is_selected)[0])
                } else {
                    (onDayPickCallback as? MultipleDaysPickCallback)?.onMultipleDaysPicked(
                        calendarView.pickedMultipleDaysList
                    )
                    onDismiss?.invoke()
                }
            }
            PickType.NOTHING -> {
            }
        }
    }

    private fun initActionBar() {
        with(rootView) {
            ActionBarView(actionBarViewStub, direction).also {
                it.locale = locale
                it.typeface = typeface
                it.onTodayButtonClick = {
                    initialDateCalendar.clone().also { calendar ->
                        calendar.timeInMillis = System.currentTimeMillis()
                    }.let { calendar ->
                        calendarView.goto(calendar, true)
                    }
                }
                it.onPositiveButtonClick = { handleOnPositiveButtonClick(calendarView) }
                it.onNegativeButtonClick = { onDismiss?.invoke() }
                it.applyTheme(themeFactory)
            }
        }
    }

    private fun initSelectionBar() {
        when (pickType) {
            PickType.SINGLE -> initSelectionBarSingle(typeface)
            PickType.RANGE_START, PickType.RANGE_END -> initSelectionBarRange(typeface)
            PickType.MULTIPLE -> initSelectionBarMultiple(typeface)
            PickType.NOTHING -> {
            }
        }
        with(rootView) {
            ImageViewCompat.setImageTintList(
                selectionBarBackgroundImageView,
                ColorStateList.valueOf(themeFactory.selectionBarBackgroundColor)
            )
        }
    }

    private fun initSelectionBarSingle(typeface: Typeface?) {
        with(rootView) {
            selectionBarView = SingleDaySelectionBarView(selectionBarViewStub).also {
                it.applyTheme(themeFactory)
                it.locale = locale
                it.typeface = typeface
                it.pickedDay = calendarView.pickedSingleDayCalendar
                it.onPickedDayClickListener = {
                    calendarView.pickedSingleDayCalendar?.let { day ->
                        day.firstDayOfWeek = initialDateCalendar.firstDayOfWeek
                        calendarView.focusOnDay(day)
                    }
                }
            }
        }
    }

    private fun initSelectionBarRange(typeface: Typeface?) {
        with(rootView) {
            selectionBarView = RangeDaysSelectionBarView(selectionBarViewStub, direction).also {
                it.applyTheme(themeFactory)
                it.locale = locale
                it.typeface = typeface
                it.pickType = calendarView.pickType
                it.pickedRangeStartDay = calendarView.pickedRangeStartCalendar
                it.pickedRangeEndDay = calendarView.pickedRangeEndCalendar
                it.onRangeStartClickListener = {
                    calendarView.pickType = PickType.RANGE_START
                    calendarView.pickedRangeStartCalendar?.let { day ->
                        day.firstDayOfWeek = initialDateCalendar.firstDayOfWeek
                        calendarView.goto(day, true)
                    }
                }
                it.onRangeEndClickListener = {
                    calendarView.pickType = PickType.RANGE_END
                    calendarView.pickedRangeEndCalendar?.let { day ->
                        day.firstDayOfWeek = initialDateCalendar.firstDayOfWeek
                        calendarView.goto(day, true)
                    }
                }
            }
        }
    }

    private fun initSelectionBarMultiple(typeface: Typeface?) {
        with(rootView) {
            selectionBarView = MultipleDaysSelectionBarView(selectionBarViewStub, direction, coroutineScope).also {
                it.applyTheme(themeFactory)
                it.locale = locale
                it.typeface = typeface
                it.onPickedDayClickListener = { day ->
                    day.firstDayOfWeek = initialDateCalendar.firstDayOfWeek
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
        multipleDays: List<PrimeCalendar>
    ) {
        when (pickType) {
            PickType.SINGLE -> {
                (selectionBarView as SingleDaySelectionBarView).pickedDay = singleDay
            }
            PickType.RANGE_START, PickType.RANGE_END -> {
                (selectionBarView as RangeDaysSelectionBarView).let {
                    it.pickedRangeStartDay = startDay
                    it.pickedRangeEndDay = endDay

                    if (autoSelectPickEndDay && pickType == PickType.RANGE_START && endDay == null) {
                        it.animateBackground(false)
                        it.onRangeEndClickListener?.invoke()
                    }
                }
            }
            PickType.MULTIPLE -> {
                (selectionBarView as MultipleDaysSelectionBarView).pickedDays = multipleDays
            }
            PickType.NOTHING -> {
            }
        }
        onDayPickedListener?.onDayPicked(pickType, singleDay, startDay, endDay, multipleDays)
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
                    it.applyTheme(themeFactory)
                }
            }
            (gotoView as? GotoView)?.also {
                it.calendar = calendar
                it.onCloseClickListener = { expandGoto(false, touchedX, touchedY) }
                it.onGoClickListener = { year, month ->
                    expandGoto(false, touchedX, touchedY)
                    postDelayed({
                        initialDateCalendar.clone().let { calendar ->
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

    internal fun onCancel(dialog: DialogInterface) {
        onCancelListener?.onCancel(dialog)
    }

    internal fun onDismiss(dialog: DialogInterface) {
        onDismissListener?.onDismiss(dialog)
        onCancelListener = null
        onDismissListener = null
        onDayPickCallback = null
        onDayPickedListener = null
        onDismiss = null
    }

    override fun show(manager: FragmentManager, tag: String?) {
        // do nothing!
    }

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        onCancelListener = listener
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        onDismissListener = listener
    }

    override fun setDayPickCallback(callback: BaseDayPickCallback?) {
        onDayPickCallback = callback
    }

    override fun setOnEachDayPickedListener(listener: OnDayPickedListener?) {
        onDayPickedListener = listener
    }

    private fun toast(text: String) =
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

}
