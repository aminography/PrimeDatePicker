package com.aminography.primedatepicker.calendarview

import android.content.Context
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseIntArray
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.calendarview.adapter.MonthListAdapter
import com.aminography.primedatepicker.calendarview.callback.IMonthViewHolderCallback
import com.aminography.primedatepicker.calendarview.dataholder.MonthDataHolder
import com.aminography.primedatepicker.calendarview.other.StartSnapHelper
import com.aminography.primedatepicker.calendarview.other.TouchControllableRecyclerView
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.common.Direction
import com.aminography.primedatepicker.common.LabelFormatter
import com.aminography.primedatepicker.common.OnDayPickedListener
import com.aminography.primedatepicker.common.OnMonthLabelClickListener
import com.aminography.primedatepicker.common.PickType
import com.aminography.primedatepicker.monthview.PrimeMonthView.Companion.DEFAULT_MONTH_LABEL_FORMATTER
import com.aminography.primedatepicker.monthview.PrimeMonthView.Companion.DEFAULT_WEEK_LABEL_FORMATTER
import com.aminography.primedatepicker.monthview.SimpleMonthView.Companion.DEFAULT_INTERPOLATOR
import com.aminography.primedatepicker.utils.DateUtils
import com.aminography.primedatepicker.utils.findDirection
import com.aminography.primedatepicker.utils.monthOffset
import java.util.Calendar
import java.util.LinkedHashMap
import java.util.Locale


/**
 * @author aminography
 */
@Suppress(
    "NotifyDataSetChanged",
    "PrivatePropertyName",
    "MemberVisibilityCanBePrivate",
    "UNNECESSARY_SAFE_CALL",
    "unused"
)
class PrimeCalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IMonthViewHolderCallback {

    // Interior Variables --------------------------------------------------------------------------
    private var needInvalidation = false

    private var adapter: MonthListAdapter
    private var recyclerView = TouchControllableRecyclerView(context)
    private lateinit var layoutManager: LinearLayoutManager
    private var dataList: MutableList<MonthDataHolder>? = null
    private var isInTransition = false
    private var isInLoading = false

    private var definedHeight: Int = 0
    private var detectedItemHeight: Float = 0f

    private var direction = Direction.LTR

    private var gotoYear: Int = 0
    private var gotoMonth: Int = 0

    val firstDayOfMonthCalendar: PrimeCalendar?
        get() = currentItemCalendar()

    // Listeners -----------------------------------------------------------------------------------

    var onDayPickedListener: OnDayPickedListener? = null
    var onMonthLabelClickListener: OnMonthLabelClickListener? = null

    // Common Control Variables --------------------------------------------------------------------

    override var monthLabelTextColor: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var weekLabelTextColor: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var dayLabelTextColor: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var todayLabelTextColor: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var pickedDayLabelTextColor: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var pickedDayInRangeLabelTextColor: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var pickedDayBackgroundColor: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var pickedDayInRangeBackgroundColor: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var disabledDayLabelTextColor: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var adjacentMonthDayLabelTextColor: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var monthLabelTextSize: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var weekLabelTextSize: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var dayLabelTextSize: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var monthLabelTopPadding: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var monthLabelBottomPadding: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var weekLabelTopPadding: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var weekLabelBottomPadding: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var dayLabelVerticalPadding: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var pickedDayBackgroundShapeType: BackgroundShapeType = BackgroundShapeType.CIRCLE
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var pickedDayRoundSquareCornerRadius: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var showTwoWeeksInLandscape: Boolean = false
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var showAdjacentMonthDays: Boolean = false
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var animateSelection: Boolean = false
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var animationDuration: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var animationInterpolator: Interpolator = DEFAULT_INTERPOLATOR
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var monthLabelFormatter: LabelFormatter = DEFAULT_MONTH_LABEL_FORMATTER
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var weekLabelFormatter: LabelFormatter = DEFAULT_WEEK_LABEL_FORMATTER
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var toFocusDay: PrimeCalendar? = null

    // Control Variables ---------------------------------------------------------------------------

    var loadFactor: Int = 0

    var maxTransitionLength: Int = 0

    var transitionSpeedFactor: Float = 0f
        set(value) {
            field = value
            recyclerView.speedFactor = value
        }

    var dividerColor: Int = 0
        set(value) {
            field = value
            if (invalidate) applyDividers()
        }

    var dividerThickness: Int = 0
        set(value) {
            field = value
            if (invalidate) applyDividers()
        }

    var dividerInsetLeft: Int = 0
        set(value) {
            field = value
            if (invalidate) applyDividers()
        }

    var dividerInsetRight: Int = 0
        set(value) {
            field = value
            if (invalidate) applyDividers()
        }

    var dividerInsetTop: Int = 0
        set(value) {
            field = value
            if (invalidate) applyDividers()
        }

    var dividerInsetBottom: Int = 0
        set(value) {
            field = value
            if (invalidate) applyDividers()
        }

    override var elementPaddingLeft: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var elementPaddingRight: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var elementPaddingTop: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var elementPaddingBottom: Int = 0
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    // Programmatically Control Variables ----------------------------------------------------------

    override var weekLabelTextColors: SparseIntArray? = null
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var typeface: Typeface? = null
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    override var pickedSingleDayCalendar: PrimeCalendar? = null
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
            notifyDayPicked(true)
        }

    override var pickedRangeStartCalendar: PrimeCalendar? = null
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
            notifyDayPicked(true)
        }

    override var pickedRangeEndCalendar: PrimeCalendar? = null
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
            notifyDayPicked(true)
        }

    override var pickedMultipleDaysMap: LinkedHashMap<String, PrimeCalendar>? = null
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
            notifyDayPicked(true)
        }

    var pickedMultipleDaysList: List<PrimeCalendar>
        get() = pickedMultipleDaysMap?.values?.toList() ?: arrayListOf()
        set(value) {
            linkedMapOf<String, PrimeCalendar>().apply {
                putAll(value.map { Pair(DateUtils.dateString(it) ?: "", it) })
            }.also { pickedMultipleDaysMap = it }
        }

    override var minDateCalendar: PrimeCalendar? = null
        set(value) {
            field = value
            var hasChanged = false
            doNotInvalidate {
                value.also { min ->
                    pickedSingleDayCalendar?.let { single ->
                        if (DateUtils.isBefore(single, min)) {
                            pickedSingleDayCalendar = min
                            hasChanged = true
                        }
                    }
                    pickedRangeStartCalendar?.let { start ->
                        if (DateUtils.isBefore(start, min)) {
                            pickedRangeStartCalendar = min
                            hasChanged = true
                        }
                    }
                    pickedRangeEndCalendar?.let { end ->
                        if (DateUtils.isBefore(end, min)) {
                            pickedRangeStartCalendar = null
                            pickedRangeEndCalendar = null
                            hasChanged = true
                        }
                    }
                }
            }
            if (invalidate) {
                val minOffset = value?.monthOffset ?: Int.MIN_VALUE
                findFirstVisibleItem()?.also { current ->
                    if (current.offset < minOffset) {
                        value?.run {
                            goto(year, month, false)
                        }
                    } else {
                        goto(current.year, current.month, false)
                    }
                }
            }
            notifyDayPicked(hasChanged)
        }

    override var maxDateCalendar: PrimeCalendar? = null
        set(value) {
            field = value
            var hasChanged = false
            doNotInvalidate {
                value.also { max ->
                    pickedSingleDayCalendar?.let { single ->
                        if (DateUtils.isAfter(single, max)) {
                            pickedSingleDayCalendar = max
                            hasChanged = true
                        }
                    }
                    pickedRangeStartCalendar?.let { start ->
                        if (DateUtils.isAfter(start, max)) {
                            pickedRangeStartCalendar = null
                            pickedRangeEndCalendar = null
                            hasChanged = true
                        }
                    }
                    pickedRangeEndCalendar?.let { end ->
                        if (DateUtils.isAfter(end, max)) {
                            pickedRangeEndCalendar = max
                            hasChanged = true
                        }
                    }
                }
            }
            if (invalidate) {
                val maxOffset = value?.monthOffset ?: Int.MAX_VALUE
                findLastVisibleItem()?.also { current ->
                    if (current.offset > maxOffset) {
                        value?.run {
                            goto(year, month, false)
                        }
                    } else {
                        goto(current.year, current.month, false)
                    }
                }
            }
            notifyDayPicked(hasChanged)
        }

    override var pickType: PickType = PickType.NOTHING
        set(value) {
            field = value
            doNotInvalidate {
                when (value) {
                    PickType.SINGLE -> {
                        pickedRangeStartCalendar = null
                        pickedRangeEndCalendar = null
                        pickedMultipleDaysMap = null
                    }
                    PickType.RANGE_START -> {
                        pickedSingleDayCalendar = null
                        pickedMultipleDaysMap = null
                    }
                    PickType.RANGE_END -> {
                        pickedSingleDayCalendar = null
                        pickedMultipleDaysMap = null
                    }
                    PickType.MULTIPLE -> {
                        pickedSingleDayCalendar = null
                        pickedRangeStartCalendar = null
                        pickedRangeEndCalendar = null
                    }
                    PickType.NOTHING -> {
                        pickedSingleDayCalendar = null
                        pickedRangeStartCalendar = null
                        pickedRangeEndCalendar = null
                        pickedMultipleDaysMap = null
                    }
                }
            }
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
            notifyDayPicked(true)
        }

    override var firstDayOfWeek: Int = -1
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    var calendarType = CalendarType.CIVIL
        set(value) {
            val previous = calendarType
            field = value
            direction = value.findDirection(locale)

            if (invalidate) {
                if (previous != value) {
                    layoutManager = createLayoutManager()
                    recyclerView.layoutManager = layoutManager
                    applyDividers()
                }
                goto(CalendarFactory.newInstance(value, locale), false)
            }
        }

    override var locale: Locale = Locale.getDefault()
        set(value) {
            field = value
            direction = value.findDirection(calendarType)
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    var flingOrientation = FlingOrientation.VERTICAL
        set(value) {
            field = value
            val calendar = CalendarFactory.newInstance(calendarType, locale)
            currentItemCalendar()?.let { current ->
                calendar.year = current.year
                calendar.month = current.month
            }
            layoutManager = createLayoutManager()
            recyclerView.layoutManager = layoutManager
            applyDividers()

            if (invalidate) goto(calendar, false)
        }

    override var developerOptionsShowGuideLines: Boolean = false
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    // ---------------------------------------------------------------------------------------------

    override var disabledDaysSet: MutableSet<String>? = null
        set(value) {
            field = value
            adapter?.takeIf { invalidate }?.notifyDataSetChanged()
        }

    var disabledDaysList: List<PrimeCalendar> = arrayListOf()
        set(value) {
            field = value
            mutableSetOf<String>().apply {
                addAll(value.map { DateUtils.dateString(it) ?: "" })
            }.also { disabledDaysSet = it }
        }

    // ---------------------------------------------------------------------------------------------

    private fun currentItemCalendar(): PrimeCalendar? = findFirstVisibleItem()?.run {
        CalendarFactory.newInstance(calendarType, locale).also {
            it.set(year, month, 1)
            if (firstDayOfWeek != -1) {
                it.firstDayOfWeek = firstDayOfWeek
            }
        }
    }

    private fun createLayoutManager(): LinearLayoutManager = when (flingOrientation) {
        FlingOrientation.VERTICAL -> LinearLayoutManager(context)
        FlingOrientation.HORIZONTAL -> LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            direction == Direction.RTL
        )
    }

    private fun applyDividers() {
        when (flingOrientation) {
            FlingOrientation.VERTICAL -> adapter?.setDivider(
                color = dividerColor,
                thickness = dividerThickness,
                insetLeft = dividerInsetLeft,
                insetRight = dividerInsetRight
            )
            FlingOrientation.HORIZONTAL -> adapter?.setDivider(
                color = dividerColor,
                thickness = dividerThickness,
                insetTop = dividerInsetTop,
                insetBottom = dividerInsetBottom
            )
        }
    }

    // ---------------------------------------------------------------------------------------------

    private var pickedDaysChanged: Boolean = false
    private var invalidate: Boolean = true

    fun invalidateAfter(block: (PrimeCalendarView) -> Unit) {
        val previous = invalidate
        invalidate = false
        block.invoke(this)
        invalidate = previous
        adapter?.notifyDataSetChanged()
    }

    fun doNotInvalidate(block: (PrimeCalendarView) -> Unit) {
        val previous = invalidate
        invalidate = false
        block.invoke(this)
        invalidate = previous
    }

    // ---------------------------------------------------------------------------------------------

    init {
        val layoutHeight =
            attrs?.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height")
        when {
            layoutHeight.equals(LayoutParams.MATCH_PARENT.toString()) ->
                definedHeight = LayoutParams.MATCH_PARENT
            layoutHeight.equals(LayoutParams.WRAP_CONTENT.toString()) ->
                definedHeight = LayoutParams.WRAP_CONTENT
            else -> context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.layout_height))
                .run {
                    definedHeight = getDimensionPixelSize(0, LayoutParams.WRAP_CONTENT)
                    recycle()
                }
        }

        context.obtainStyledAttributes(
            attrs,
            R.styleable.PrimeCalendarView,
            defStyleAttr,
            defStyleRes
        ).run {
            doNotInvalidate {
                calendarType = CalendarType.values()[
                    getInt(
                        R.styleable.PrimeCalendarView_calendarType,
                        DEFAULT_CALENDAR_TYPE.ordinal
                    )
                ]

                layoutManager = createLayoutManager()
                recyclerView.layoutManager = layoutManager

                flingOrientation = FlingOrientation.values()[
                    getInt(
                        R.styleable.PrimeCalendarView_flingOrientation,
                        DEFAULT_FLING_ORIENTATION.ordinal
                    )
                ]

                loadFactor = getInteger(
                    R.styleable.PrimeCalendarView_loadFactor,
                    resources.getInteger(R.integer.defaultLoadFactor)
                )
                maxTransitionLength = getInteger(
                    R.styleable.PrimeCalendarView_maxTransitionLength,
                    resources.getInteger(R.integer.defaultMaxTransitionLength)
                )
                transitionSpeedFactor = getFloat(
                    R.styleable.PrimeCalendarView_transitionSpeedFactor,
                    resources.getString(R.string.defaultTransitionSpeedFactor).toFloat()
                )

                dividerColor = getColor(
                    R.styleable.PrimeCalendarView_dividerColor,
                    ContextCompat.getColor(context, R.color.gray400)
                )
                dividerThickness = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_dividerThickness,
                    resources.getDimensionPixelSize(R.dimen.defaultDividerThickness)
                )
                dividerInsetLeft = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_dividerInsetLeft,
                    resources.getDimensionPixelSize(R.dimen.defaultDividerInsetLeft)
                )
                dividerInsetRight = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_dividerInsetRight,
                    resources.getDimensionPixelSize(R.dimen.defaultDividerInsetRight)
                )
                dividerInsetTop = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_dividerInsetTop,
                    resources.getDimensionPixelSize(R.dimen.defaultDividerInsetTop)
                )
                dividerInsetBottom = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_dividerInsetBottom,
                    resources.getDimensionPixelSize(R.dimen.defaultDividerInsetBottom)
                )

                elementPaddingLeft = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_elementPaddingLeft,
                    resources.getDimensionPixelSize(R.dimen.defaultElementPaddingLeft)
                )
                elementPaddingRight = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_elementPaddingRight,
                    resources.getDimensionPixelSize(R.dimen.defaultElementPaddingRight)
                )
                elementPaddingTop = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_elementPaddingTop,
                    resources.getDimensionPixelSize(R.dimen.defaultElementPaddingTop)
                )
                elementPaddingBottom = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_elementPaddingBottom,
                    resources.getDimensionPixelSize(R.dimen.defaultElementPaddingBottom)
                )

                // Common Attributes ---------------------------------------------------------------

                monthLabelTextColor = getColor(
                    R.styleable.PrimeCalendarView_monthLabelTextColor,
                    ContextCompat.getColor(context, R.color.blueGray200)
                )
                weekLabelTextColor = getColor(
                    R.styleable.PrimeCalendarView_weekLabelTextColor,
                    ContextCompat.getColor(context, R.color.red300)
                )
                dayLabelTextColor = getColor(
                    R.styleable.PrimeCalendarView_dayLabelTextColor,
                    ContextCompat.getColor(context, R.color.gray900)
                )
                todayLabelTextColor = getColor(
                    R.styleable.PrimeCalendarView_todayLabelTextColor,
                    ContextCompat.getColor(context, R.color.green400)
                )
                pickedDayLabelTextColor = getColor(
                    R.styleable.PrimeCalendarView_pickedDayLabelTextColor,
                    ContextCompat.getColor(context, R.color.white)
                )
                pickedDayInRangeLabelTextColor = getColor(
                    R.styleable.PrimeCalendarView_pickedDayInRangeLabelTextColor,
                    ContextCompat.getColor(context, R.color.white)
                )
                pickedDayBackgroundColor = getColor(
                    R.styleable.PrimeCalendarView_pickedDayBackgroundColor,
                    ContextCompat.getColor(context, R.color.red300)
                )
                pickedDayInRangeBackgroundColor = getColor(
                    R.styleable.PrimeCalendarView_pickedDayInRangeBackgroundColor,
                    ContextCompat.getColor(context, R.color.red300)
                )
                disabledDayLabelTextColor = getColor(
                    R.styleable.PrimeCalendarView_disabledDayLabelTextColor,
                    ContextCompat.getColor(context, R.color.gray400)
                )
                adjacentMonthDayLabelTextColor = getColor(
                    R.styleable.PrimeCalendarView_adjacentMonthDayLabelTextColor,
                    ContextCompat.getColor(context, R.color.gray400)
                )

                monthLabelTextSize = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_monthLabelTextSize,
                    resources.getDimensionPixelSize(R.dimen.defaultMonthLabelTextSize)
                )
                weekLabelTextSize = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_weekLabelTextSize,
                    resources.getDimensionPixelSize(R.dimen.defaultWeekLabelTextSize)
                )
                dayLabelTextSize = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_dayLabelTextSize,
                    resources.getDimensionPixelSize(R.dimen.defaultDayLabelTextSize)
                )

                monthLabelTopPadding = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_monthLabelTopPadding,
                    resources.getDimensionPixelSize(R.dimen.defaultMonthLabelTopPadding)
                )
                monthLabelBottomPadding = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_monthLabelBottomPadding,
                    resources.getDimensionPixelSize(R.dimen.defaultMonthLabelBottomPadding)
                )
                weekLabelTopPadding = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_weekLabelTopPadding,
                    resources.getDimensionPixelSize(R.dimen.defaultWeekLabelTopPadding)
                )
                weekLabelBottomPadding = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_weekLabelBottomPadding,
                    resources.getDimensionPixelSize(R.dimen.defaultWeekLabelBottomPadding)
                )
                dayLabelVerticalPadding = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_dayLabelVerticalPadding,
                    resources.getDimensionPixelSize(R.dimen.defaultDayLabelVerticalPadding)
                )

                pickedDayBackgroundShapeType = BackgroundShapeType.values()[
                    getInt(
                        R.styleable.PrimeCalendarView_pickedDayBackgroundShapeType,
                        DEFAULT_BACKGROUND_SHAPE_TYPE.ordinal
                    )
                ]
                pickedDayRoundSquareCornerRadius = getDimensionPixelSize(
                    R.styleable.PrimeCalendarView_pickedDayRoundSquareCornerRadius,
                    resources.getDimensionPixelSize(R.dimen.defaultPickedDayRoundSquareCornerRadius)
                )

                showTwoWeeksInLandscape = getBoolean(
                    R.styleable.PrimeCalendarView_showTwoWeeksInLandscape,
                    resources.getBoolean(R.bool.defaultShowTwoWeeksInLandscape)
                )
                showAdjacentMonthDays = getBoolean(
                    R.styleable.PrimeCalendarView_showAdjacentMonthDays,
                    resources.getBoolean(R.bool.defaultShowAdjacentMonthDays)
                )

                animateSelection = getBoolean(
                    R.styleable.PrimeCalendarView_animateSelection,
                    resources.getBoolean(R.bool.defaultAnimateSelection)
                )
                animationDuration = getInteger(
                    R.styleable.PrimeCalendarView_animationDuration,
                    resources.getInteger(R.integer.defaultAnimationDuration)
                )
            }
            recycle()
        }

        addView(recyclerView)
        recyclerView.speedFactor = transitionSpeedFactor
        recyclerView.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        recyclerView.addOnScrollListener(OnScrollListener())

        recyclerView.setHasFixedSize(true)
        StartSnapHelper().attachToRecyclerView(recyclerView)

        adapter = MonthListAdapter(recyclerView)
        adapter.iMonthViewHolderCallback = this
        recyclerView.adapter = adapter

        applyDividers()

        if (isInEditMode) {
            goto(CalendarFactory.newInstance(calendarType, locale), false)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        when (definedHeight) {
            ViewGroup.LayoutParams.MATCH_PARENT -> {
            }
            ViewGroup.LayoutParams.WRAP_CONTENT -> {
                if (detectedItemHeight > 0f) {
                    setMeasuredDimension(width, detectedItemHeight.toInt())
                }
            }
            in 0 until Int.MAX_VALUE -> {
                if (definedHeight > detectedItemHeight) {
                    setMeasuredDimension(width, definedHeight)
                } else {
                    setMeasuredDimension(width, detectedItemHeight.toInt())
                }
            }
        }
        recyclerView.height = measuredHeight
    }

    fun gotoNextMonth(animate: Boolean = true): Boolean {
        return CalendarFactory.newInstance(calendarType, locale).run {
            add(Calendar.MONTH, 1)
            goto(year, month, animate)
        }
    }

    fun gotoPreviousMonth(animate: Boolean = true): Boolean {
        return CalendarFactory.newInstance(calendarType, locale).run {
            add(Calendar.MONTH, -1)
            goto(year, month, animate)
        }
    }

    fun gotoNextYear(animate: Boolean = true): Boolean {
        return CalendarFactory.newInstance(calendarType, locale).run {
            add(Calendar.YEAR, 1)
            goto(year, month, animate)
        }
    }

    fun gotoPreviousYear(animate: Boolean = true): Boolean {
        return CalendarFactory.newInstance(calendarType, locale).run {
            add(Calendar.YEAR, -1)
            goto(year, month, animate)
        }
    }

    fun focusOnDay(calendar: PrimeCalendar) {
        toFocusDay = calendar
        findFirstVisibleItem()?.let { current ->
            if (current.year == calendar.year && current.month == calendar.month) {
                adapter?.notifyDataSetChanged()
            } else {
                goto(calendar, true)
            }
        } ?: goto(calendar, true)
    }

    fun goTo(calendar: PrimeCalendar, animate: Boolean = false): Boolean =
        goto(calendar, animate)

    fun goTo(year: Int, month: Int, animate: Boolean = false): Boolean =
        goto(year, month, animate)

    fun goto(calendar: PrimeCalendar, animate: Boolean = false): Boolean {
        doNotInvalidate {
            locale = calendar.locale
            calendarType = calendar.calendarType
            firstDayOfWeek = calendar.firstDayOfWeek
        }
        return goto(calendar.year, calendar.month, animate)
    }

    fun goto(year: Int, month: Int, animate: Boolean = false): Boolean {
        if (DateUtils.isOutOfRange(year, month, minDateCalendar, maxDateCalendar)) {
            return false
        }
        if (firstDayOfWeek == -1) {
            firstDayOfWeek = DateUtils.defaultWeekStartDay(calendarType)
        }

        dataList =
            createPivotList(calendarType, year, month, minDateCalendar, maxDateCalendar, loadFactor)
        if (animate) {
            findFirstVisibleItem()?.let { current ->
                val transitionList = createTransitionList(
                    calendarType,
                    current.year,
                    current.month,
                    year,
                    month,
                    maxTransitionLength
                )

                transitionList?.run {
                    val isForward = DateUtils.isBefore(current.year, current.month, year, month)
                    var isLastTransitionItemRemoved = false
                    var isFirstTransitionItemRemoved = false

                    if (isForward) {
                        maxDateCalendar?.let { max ->
                            val maxOffset = max.monthOffset
                            val targetOffset = year * 12 + month
                            if (maxOffset == targetOffset) {
                                removeAt(size - 1) // extra next padding item
                                isLastTransitionItemRemoved = true
                            }
                        }
                    } else {
                        minDateCalendar?.let { min ->
                            val minOffset = min.monthOffset
                            val targetOffset = year * 12 + month
                            if (minOffset == targetOffset) {
                                removeAt(0) // extra previous padding item
                                isFirstTransitionItemRemoved = true
                            }
                        }
                    }
                    adapter.replaceDataList(this)
                    isInTransition = true
                    recyclerView.touchEnabled = false

                    gotoYear = year
                    gotoMonth = month

                    if (isForward) {
                        recyclerView.fastScrollTo(1)
                        recyclerView.smoothScrollTo(if (isLastTransitionItemRemoved) size - 1 else size - 2)
                    } else {
                        recyclerView.fastScrollTo(size - 2)
                        recyclerView.smoothScrollTo(if (isFirstTransitionItemRemoved) 0 else 1)
                    }
                }
            }
        } else {
            dataList?.run {
                adapter.replaceDataList(this)
                findPositionInList(year, month, this)?.let {
                    recyclerView.fastScrollTo(it)
                }
            }
        }
        return true
    }

    private fun findPositionInList(year: Int, month: Int, list: List<MonthDataHolder>?): Int? {
        list?.apply {
            val dataHolder = get(0)
            val firstOffset = dataHolder.offset
            val targetOffset = year * 12 + month
            return targetOffset - firstOffset
        }
        return null
    }

    private fun findFirstVisibleItem(): MonthDataHolder? {
        var position = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (position == RecyclerView.NO_POSITION) {
            position = layoutManager.findFirstVisibleItemPosition()
        }
        if (position != RecyclerView.NO_POSITION) {
            return adapter.itemAt(position)
        }
        return null
    }

    private fun findLastVisibleItem(): MonthDataHolder? {
        var position = layoutManager.findLastCompletelyVisibleItemPosition()
        if (position == RecyclerView.NO_POSITION) {
            position = layoutManager.findLastVisibleItemPosition()
        }
        if (position != RecyclerView.NO_POSITION) {
            return adapter.itemAt(position)
        }
        return null
    }

    override fun setFadingEdgeLength(length: Int) {
        recyclerView.setFadingEdgeLength(length)
    }

    override fun setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled: Boolean) {
        recyclerView.isHorizontalFadingEdgeEnabled = horizontalFadingEdgeEnabled
    }

    override fun setVerticalFadingEdgeEnabled(verticalFadingEdgeEnabled: Boolean) {
        recyclerView.isVerticalFadingEdgeEnabled = verticalFadingEdgeEnabled
    }

    override fun onDayPicked(
        pickType: PickType,
        singleDay: PrimeCalendar?,
        startDay: PrimeCalendar?,
        endDay: PrimeCalendar?,
        multipleDays: List<PrimeCalendar>?
    ) {
        var hasChanged = false
        doNotInvalidate {
            when (pickType) {
                PickType.SINGLE -> {
                    pickedSingleDayCalendar = singleDay
                    hasChanged = true
                }
                PickType.RANGE_START -> {
                    startDay?.let {
                        if (DateUtils.isAfter(it, pickedRangeEndCalendar)) {
                            pickedRangeEndCalendar = null
                        }
                    }
                    pickedRangeStartCalendar = startDay
                    hasChanged = true
                }
                PickType.RANGE_END -> {
                    if (endDay != null) {
                        if (pickedRangeStartCalendar != null &&
                            !DateUtils.isBefore(endDay, pickedRangeStartCalendar)
                        ) {
                            pickedRangeEndCalendar = endDay
                            hasChanged = true
                        }
                    } else {
                        pickedRangeEndCalendar = endDay
                        hasChanged = true
                    }
                }
                PickType.MULTIPLE -> {
                    pickedMultipleDaysList = multipleDays ?: arrayListOf()
                    hasChanged = true
                }
                PickType.NOTHING -> {
                }
            }
        }

        if (animateSelection) {
            needInvalidation = true
        } else {
            adapter?.notifyDataSetChanged()
        }

        notifyDayPicked(hasChanged)
    }

    private fun notifyDayPicked(hasChanged: Boolean) {
        pickedDaysChanged = pickedDaysChanged or hasChanged
        if (invalidate && pickedDaysChanged) {
            onDayPickedListener?.onDayPicked(
                pickType,
                pickedSingleDayCalendar,
                pickedRangeStartCalendar,
                pickedRangeEndCalendar,
                pickedMultipleDaysList
            )
            pickedDaysChanged = false
        }
    }

    override fun onHeightDetect(height: Float) {
        if (detectedItemHeight == 0f && height > 0f) {
            detectedItemHeight = height + paddingTop + paddingBottom
            requestLayout()
            invalidate()
        }
    }

    override fun onMonthLabelClicked(calendar: PrimeCalendar, touchedX: Int, touchedY: Int) {
        onMonthLabelClickListener?.onMonthLabelClicked(calendar, touchedX, touchedY)
    }

    private inner class OnScrollListener : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(view: RecyclerView, newState: Int) {
            super.onScrollStateChanged(view, newState)
            when (newState) {
                RecyclerView.SCROLL_STATE_DRAGGING -> {
                }
                RecyclerView.SCROLL_STATE_SETTLING -> {
                }
                RecyclerView.SCROLL_STATE_IDLE -> {
                    if (isInTransition) {
                        postDelayed({
                            dataList?.run {
                                adapter.replaceDataList(this)
                                findPositionInList(gotoYear, gotoMonth, this)?.let {
                                    recyclerView.fastScrollTo(it)
                                }
                                isInTransition = false
                                recyclerView.touchEnabled = true
                            }
                        }, 10)
                    }
                }
            }
        }

        override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(view, dx, dy)
            if (needInvalidation) {
                needInvalidation = false
                adapter?.notifyDataSetChanged()
            }

            if (!isInTransition) {
                val factor = when (flingOrientation) {
                    FlingOrientation.VERTICAL -> dy
                    FlingOrientation.HORIZONTAL -> when (direction) {
                        Direction.LTR -> dx
                        Direction.RTL -> -dx
                    }
                }
                if (factor > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (!isInLoading && (visibleItemCount + lastVisibleItemPosition > totalItemCount)) {
                        isInLoading = true
                        val dataHolder = adapter.itemAt(totalItemCount - 1)
                        val offset = dataHolder.offset
                        val maxOffset = maxDateCalendar?.monthOffset ?: Int.MAX_VALUE

                        if (offset < maxOffset) {
                            val moreData = extendMoreList(
                                calendarType,
                                dataHolder.year,
                                dataHolder.month,
                                minDateCalendar,
                                maxDateCalendar,
                                loadFactor,
                                true
                            )

                            dataList?.apply {
                                addAll(size, moreData)
                            }?.let {
                                adapter.replaceDataList(it)
                            }
                        }
                        isInLoading = false
                    }
                } else if (factor < 0) {
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isInLoading && firstVisibleItemPosition == 0) {
                        isInLoading = true
                        val dataHolder = adapter.itemAt(0)
                        val offset = dataHolder.offset
                        val minOffset = minDateCalendar?.monthOffset ?: Int.MIN_VALUE

                        if (offset > minOffset) {
                            val moreData = extendMoreList(
                                calendarType,
                                dataHolder.year,
                                dataHolder.month,
                                minDateCalendar,
                                maxDateCalendar,
                                loadFactor,
                                false
                            )

                            dataList?.apply {
                                addAll(0, moreData)
                            }?.let {
                                adapter.replaceDataList(it)
                                recyclerView.fastScrollTo(moreData.size + 1)
                            }
                        }
                        isInLoading = false
                    }
                }
            }
        }
    }

    enum class FlingOrientation {
        VERTICAL,
        HORIZONTAL
    }

    // Save/Restore States -------------------------------------------------------------------------

    override fun onSaveInstanceState(): Parcelable {
        return SavedState(super.onSaveInstanceState())
            .also {
                it.calendarType = calendarType.ordinal
                it.locale = locale.language

                currentItemCalendar()?.run {
                    it.currentYear = year
                    it.currentMonth = month
                }

                it.flingOrientation = flingOrientation.ordinal

                it.minDateCalendar = DateUtils.storeCalendar(minDateCalendar)
                it.maxDateCalendar = DateUtils.storeCalendar(maxDateCalendar)

                it.pickType = pickType.name
                it.pickedSingleDayCalendar =
                    DateUtils.storeCalendar(pickedSingleDayCalendar)
                it.pickedRangeStartCalendar =
                    DateUtils.storeCalendar(pickedRangeStartCalendar)
                it.pickedRangeEndCalendar = DateUtils.storeCalendar(pickedRangeEndCalendar)

                it.pickedMultipleDaysList = pickedMultipleDaysMap
                    ?.values
                    ?.mapNotNull { day -> DateUtils.storeCalendar(day) }
                    ?: arrayListOf()

                disabledDaysSet?.run { it.disabledDaysList = toList() }

                it.loadFactor = loadFactor
                it.maxTransitionLength = maxTransitionLength
                it.transitionSpeedFactor = transitionSpeedFactor
                it.dividerColor = dividerColor
                it.dividerThickness = dividerThickness
                it.dividerInsetLeft = dividerInsetLeft
                it.dividerInsetRight = dividerInsetRight
                it.dividerInsetTop = dividerInsetTop
                it.dividerInsetBottom = dividerInsetBottom

                it.elementPaddingLeft = elementPaddingLeft
                it.elementPaddingRight = elementPaddingRight
                it.elementPaddingTop = elementPaddingTop
                it.elementPaddingBottom = elementPaddingBottom

                // Common Attributes -----------------------------------------------------------------------

                it.monthLabelTextColor = monthLabelTextColor
                it.weekLabelTextColor = weekLabelTextColor
                it.dayLabelTextColor = dayLabelTextColor
                it.todayLabelTextColor = todayLabelTextColor
                it.pickedDayLabelTextColor = pickedDayLabelTextColor
                it.pickedDayInRangeLabelTextColor = pickedDayInRangeLabelTextColor
                it.pickedDayBackgroundColor = pickedDayBackgroundColor
                it.pickedDayInRangeBackgroundColor = pickedDayInRangeBackgroundColor
                it.disabledDayLabelTextColor = disabledDayLabelTextColor
                it.adjacentMonthDayLabelTextColor = adjacentMonthDayLabelTextColor
                it.monthLabelTextSize = monthLabelTextSize
                it.weekLabelTextSize = weekLabelTextSize
                it.dayLabelTextSize = dayLabelTextSize
                it.monthLabelTopPadding = monthLabelTopPadding
                it.monthLabelBottomPadding = monthLabelBottomPadding
                it.weekLabelTopPadding = weekLabelTopPadding
                it.weekLabelBottomPadding = weekLabelBottomPadding
                it.dayLabelVerticalPadding = dayLabelVerticalPadding
                it.showTwoWeeksInLandscape = showTwoWeeksInLandscape
                it.showAdjacentMonthDays = showAdjacentMonthDays

                it.pickedDayBackgroundShapeType = pickedDayBackgroundShapeType.ordinal
                it.pickedDayRoundSquareCornerRadius = pickedDayRoundSquareCornerRadius

                it.animateSelection = animateSelection
                it.animationDuration = animationDuration
            }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)

        val currentYear = savedState.currentYear
        val currentMonth = savedState.currentMonth

        doNotInvalidate {
            calendarType = CalendarType.values()[savedState.calendarType]
            locale = Locale(savedState.locale ?: "en")

            layoutManager = createLayoutManager()
            recyclerView.layoutManager = layoutManager

            flingOrientation = FlingOrientation.values()[savedState.flingOrientation]

            minDateCalendar = DateUtils.restoreCalendar(savedState.minDateCalendar)
            maxDateCalendar = DateUtils.restoreCalendar(savedState.maxDateCalendar)

            pickType = savedState.pickType?.let { PickType.valueOf(it) } ?: PickType.NOTHING
            pickedSingleDayCalendar = DateUtils.restoreCalendar(savedState.pickedSingleDayCalendar)
            pickedRangeStartCalendar = DateUtils.restoreCalendar(savedState.pickedRangeStartCalendar)
            pickedRangeEndCalendar = DateUtils.restoreCalendar(savedState.pickedRangeEndCalendar)

            linkedMapOf<String, PrimeCalendar>().apply {
                savedState.pickedMultipleDaysList?.map {
                    val calendar = DateUtils.restoreCalendar(it)
                    Pair(DateUtils.dateString(calendar)!!, calendar!!)
                }?.also { putAll(it) }
            }.also { pickedMultipleDaysMap = it }

            savedState.disabledDaysList?.toMutableSet()?.let { disabledDaysSet = it }

            loadFactor = savedState.loadFactor
            maxTransitionLength = savedState.maxTransitionLength
            transitionSpeedFactor = savedState.transitionSpeedFactor
            dividerColor = savedState.dividerColor
            dividerThickness = savedState.dividerThickness
            dividerInsetLeft = savedState.dividerInsetLeft
            dividerInsetRight = savedState.dividerInsetRight
            dividerInsetTop = savedState.dividerInsetTop
            dividerInsetBottom = savedState.dividerInsetBottom

            elementPaddingLeft = savedState.elementPaddingLeft
            elementPaddingRight = savedState.elementPaddingRight
            elementPaddingTop = savedState.elementPaddingTop
            elementPaddingBottom = savedState.elementPaddingBottom

            // Common Attributes -------------------------------------------------------------------

            monthLabelTextColor = savedState.monthLabelTextColor
            weekLabelTextColor = savedState.weekLabelTextColor
            dayLabelTextColor = savedState.dayLabelTextColor
            todayLabelTextColor = savedState.todayLabelTextColor
            pickedDayLabelTextColor = savedState.pickedDayLabelTextColor
            pickedDayInRangeLabelTextColor = savedState.pickedDayInRangeLabelTextColor
            pickedDayBackgroundColor = savedState.pickedDayBackgroundColor
            pickedDayInRangeBackgroundColor = savedState.pickedDayInRangeBackgroundColor
            disabledDayLabelTextColor = savedState.disabledDayLabelTextColor
            adjacentMonthDayLabelTextColor = savedState.adjacentMonthDayLabelTextColor
            monthLabelTextSize = savedState.monthLabelTextSize
            weekLabelTextSize = savedState.weekLabelTextSize
            dayLabelTextSize = savedState.dayLabelTextSize
            monthLabelTopPadding = savedState.monthLabelTopPadding
            monthLabelBottomPadding = savedState.monthLabelBottomPadding
            weekLabelTopPadding = savedState.weekLabelTopPadding
            weekLabelBottomPadding = savedState.weekLabelBottomPadding
            dayLabelVerticalPadding = savedState.dayLabelVerticalPadding
            showTwoWeeksInLandscape = savedState.showTwoWeeksInLandscape
            showAdjacentMonthDays = savedState.showAdjacentMonthDays

            pickedDayBackgroundShapeType = BackgroundShapeType.values()[savedState.pickedDayBackgroundShapeType]
            pickedDayRoundSquareCornerRadius = savedState.pickedDayRoundSquareCornerRadius

            animateSelection = savedState.animateSelection
            animationDuration = savedState.animationDuration
        }

        applyDividers()
        goto(currentYear, currentMonth, false)
        notifyDayPicked(true)
    }

    private class SavedState : BaseSavedState {

        var calendarType: Int = 0
        var locale: String? = null
        var currentYear: Int = 0
        var currentMonth: Int = 0

        var flingOrientation: Int = 0

        var minDateCalendar: String? = null
        var maxDateCalendar: String? = null

        var pickType: String? = null
        var pickedSingleDayCalendar: String? = null
        var pickedRangeStartCalendar: String? = null
        var pickedRangeEndCalendar: String? = null
        var pickedMultipleDaysList: List<String>? = null

        var disabledDaysList: List<String>? = null

        var loadFactor: Int = 0
        var maxTransitionLength: Int = 0
        var transitionSpeedFactor: Float = 0f
        var dividerColor: Int = 0
        var dividerThickness: Int = 0
        var dividerInsetLeft: Int = 0
        var dividerInsetRight: Int = 0
        var dividerInsetTop: Int = 0
        var dividerInsetBottom: Int = 0

        var elementPaddingLeft: Int = 0
        var elementPaddingRight: Int = 0
        var elementPaddingTop: Int = 0
        var elementPaddingBottom: Int = 0

        // Common Attributes -----------------------------------------------------------------------

        var monthLabelTextColor: Int = 0
        var weekLabelTextColor: Int = 0
        var dayLabelTextColor: Int = 0
        var todayLabelTextColor: Int = 0
        var pickedDayLabelTextColor: Int = 0
        var pickedDayInRangeLabelTextColor: Int = 0
        var pickedDayBackgroundColor: Int = 0
        var pickedDayInRangeBackgroundColor: Int = 0
        var disabledDayLabelTextColor: Int = 0
        var adjacentMonthDayLabelTextColor: Int = 0
        var monthLabelTextSize: Int = 0
        var weekLabelTextSize: Int = 0
        var dayLabelTextSize: Int = 0
        var monthLabelTopPadding: Int = 0
        var monthLabelBottomPadding: Int = 0
        var weekLabelTopPadding: Int = 0
        var weekLabelBottomPadding: Int = 0
        var dayLabelVerticalPadding: Int = 0
        var showTwoWeeksInLandscape: Boolean = false
        var showAdjacentMonthDays: Boolean = false

        var pickedDayBackgroundShapeType: Int = 0
        var pickedDayRoundSquareCornerRadius: Int = 0

        var animateSelection: Boolean = false
        var animationDuration: Int = 0

        constructor(superState: Parcelable?) : super(superState)

        private constructor(input: Parcel) : super(input) {
            calendarType = input.readInt()
            locale = input.readString()
            currentYear = input.readInt()
            currentMonth = input.readInt()

            flingOrientation = input.readInt()

            minDateCalendar = input.readString()
            maxDateCalendar = input.readString()

            pickType = input.readString()
            pickedSingleDayCalendar = input.readString()
            pickedRangeStartCalendar = input.readString()
            pickedRangeEndCalendar = input.readString()
            input.readStringList(pickedMultipleDaysList ?: mutableListOf())

            disabledDaysList?.let { input.readStringList(it) }

            loadFactor = input.readInt()
            maxTransitionLength = input.readInt()
            transitionSpeedFactor = input.readFloat()
            dividerColor = input.readInt()
            dividerThickness = input.readInt()
            dividerInsetLeft = input.readInt()
            dividerInsetRight = input.readInt()
            dividerInsetTop = input.readInt()
            dividerInsetBottom = input.readInt()

            elementPaddingLeft = input.readInt()
            elementPaddingRight = input.readInt()
            elementPaddingTop = input.readInt()
            elementPaddingBottom = input.readInt()

            // Common Attributes -------------------------------------------------------------------

            monthLabelTextColor = input.readInt()
            weekLabelTextColor = input.readInt()
            dayLabelTextColor = input.readInt()
            todayLabelTextColor = input.readInt()
            pickedDayLabelTextColor = input.readInt()
            pickedDayInRangeLabelTextColor = input.readInt()
            pickedDayBackgroundColor = input.readInt()
            pickedDayInRangeBackgroundColor = input.readInt()
            disabledDayLabelTextColor = input.readInt()
            adjacentMonthDayLabelTextColor = input.readInt()
            monthLabelTextSize = input.readInt()
            weekLabelTextSize = input.readInt()
            dayLabelTextSize = input.readInt()
            monthLabelTopPadding = input.readInt()
            monthLabelBottomPadding = input.readInt()
            weekLabelTopPadding = input.readInt()
            weekLabelBottomPadding = input.readInt()
            dayLabelVerticalPadding = input.readInt()
            showTwoWeeksInLandscape = input.readInt() == 1
            showAdjacentMonthDays = input.readInt() == 1

            pickedDayBackgroundShapeType = input.readInt()
            pickedDayRoundSquareCornerRadius = input.readInt()

            animateSelection = input.readInt() == 1
            animationDuration = input.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(calendarType)
            out.writeString(locale)
            out.writeInt(currentYear)
            out.writeInt(currentMonth)

            out.writeInt(flingOrientation)

            out.writeString(minDateCalendar)
            out.writeString(maxDateCalendar)

            out.writeString(pickType)
            out.writeString(pickedSingleDayCalendar)
            out.writeString(pickedRangeStartCalendar)
            out.writeString(pickedRangeEndCalendar)
            out.writeStringList(pickedMultipleDaysList)

            out.writeStringList(disabledDaysList)

            out.writeInt(loadFactor)
            out.writeInt(maxTransitionLength)
            out.writeFloat(transitionSpeedFactor)
            out.writeInt(dividerColor)
            out.writeInt(dividerThickness)
            out.writeInt(dividerInsetLeft)
            out.writeInt(dividerInsetRight)
            out.writeInt(dividerInsetTop)
            out.writeInt(dividerInsetBottom)

            out.writeInt(elementPaddingLeft)
            out.writeInt(elementPaddingRight)
            out.writeInt(elementPaddingTop)
            out.writeInt(elementPaddingBottom)

            // Common Attributes -------------------------------------------------------------------

            out.writeInt(monthLabelTextColor)
            out.writeInt(weekLabelTextColor)
            out.writeInt(dayLabelTextColor)
            out.writeInt(todayLabelTextColor)
            out.writeInt(pickedDayLabelTextColor)
            out.writeInt(pickedDayInRangeLabelTextColor)
            out.writeInt(pickedDayBackgroundColor)
            out.writeInt(pickedDayInRangeBackgroundColor)
            out.writeInt(disabledDayLabelTextColor)
            out.writeInt(adjacentMonthDayLabelTextColor)
            out.writeInt(monthLabelTextSize)
            out.writeInt(weekLabelTextSize)
            out.writeInt(dayLabelTextSize)
            out.writeInt(monthLabelTopPadding)
            out.writeInt(monthLabelBottomPadding)
            out.writeInt(weekLabelTopPadding)
            out.writeInt(weekLabelBottomPadding)
            out.writeInt(dayLabelVerticalPadding)
            out.writeInt(if (showTwoWeeksInLandscape) 1 else 0)
            out.writeInt(if (showAdjacentMonthDays) 1 else 0)

            out.writeInt(pickedDayBackgroundShapeType)
            out.writeInt(pickedDayRoundSquareCornerRadius)

            out.writeInt(if (animateSelection) 1 else 0)
            out.writeInt(animationDuration)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(input: Parcel): SavedState = SavedState(input)
                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }

    companion object {
        private val DEFAULT_CALENDAR_TYPE = CalendarType.CIVIL
        private val DEFAULT_BACKGROUND_SHAPE_TYPE = BackgroundShapeType.CIRCLE
        private val DEFAULT_FLING_ORIENTATION = FlingOrientation.VERTICAL
    }

}
