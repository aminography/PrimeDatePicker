package com.aminography.primedatepicker.picker.selection.range

import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.ViewStub
import android.view.animation.OvershootInterpolator
import androidx.core.widget.ImageViewCompat
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.localizeDigits
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.common.Direction
import com.aminography.primedatepicker.common.LabelFormatter
import com.aminography.primedatepicker.common.PickType
import com.aminography.primedatepicker.picker.base.BaseLazyView
import com.aminography.primedatepicker.picker.selection.SelectionBarView
import com.aminography.primedatepicker.utils.forceLocaleStrings
import kotlinx.android.synthetic.main.selection_bar_range_days_container.view.*
import java.util.*

/**
 * @author aminography
 */
internal class RangeDaysSelectionBarView(
    viewStub: ViewStub,
    private val direction: Direction
) : BaseLazyView(
    if (direction == Direction.LTR) R.layout.selection_bar_range_days_container
    else R.layout.selection_bar_range_days_container_rtl, viewStub
), SelectionBarView {

    var locale: Locale? = null
        set(value) {
            field = value
            value?.let {
                val strings = rootView.context.forceLocaleStrings(
                    it,
                    R.string.prime_date_picker_from,
                    R.string.prime_date_picker_to
                )
                rootView.rangeStartTextView.topLabelText = strings[0]
                rootView.rangeEndTextView.topLabelText = strings[1]
            }
        }

    var typeface: Typeface? = null
        set(value) {
            field = value
            rootView.rangeStartTextView.typeface = value
            rootView.rangeEndTextView.typeface = value
        }

    var labelFormatter: LabelFormatter? = null

    var itemBackgroundColor: Int = 0
        set(value) {
            field = value
            ImageViewCompat.setImageTintList(
                rootView.backImageView,
                ColorStateList.valueOf(value)
            )
        }

    var pickType: PickType? = null
        set(value) {
            field = value
            when (value) {
                PickType.RANGE_START -> animateBackground(true, 0)
                PickType.RANGE_END -> animateBackground(false, 0)
                else -> {
                }
            }
        }

    var pickedRangeStartDay: PrimeCalendar? = null
        set(value) {
            field = value
            rootView.rangeStartTextView.bottomLabelText = value?.let {
                labelFormatter?.invoke(it)?.localizeDigits(value.locale)
            } ?: ""
        }

    var pickedRangeEndDay: PrimeCalendar? = null
        set(value) {
            field = value
            rootView.rangeEndTextView.bottomLabelText = value?.let {
                labelFormatter?.invoke(it)?.localizeDigits(value.locale)
            } ?: ""
        }

    var onRangeStartClickListener: (() -> Unit)? = null
        set(value) {
            field = value
            rootView.rangeStartBackView.setOnClickListener {
                animateBackground(true)
                value?.invoke()
            }
        }

    var onRangeEndClickListener: (() -> Unit)? = null
        set(value) {
            field = value
            rootView.rangeEndBackView.setOnClickListener {
                animateBackground(false)
                value?.invoke()
            }
        }

    internal fun animateBackground(isStart: Boolean, duration: Long = 300) {
        rootView.backImageView.post {
            ObjectAnimator.ofFloat(
                rootView.backImageView,
                "translationX",
                if (isStart) 0f
                else rootView.backImageView.width.toFloat().let {
                    if (direction == Direction.LTR) it else -it
                }
            ).also {
                it.interpolator = OvershootInterpolator()
                it.duration = duration
                it.start()
            }
        }
    }
}