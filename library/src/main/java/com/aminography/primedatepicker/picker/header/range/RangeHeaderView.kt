package com.aminography.primedatepicker.picker.header.range

import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.ViewStub
import android.view.animation.OvershootInterpolator
import androidx.core.widget.ImageViewCompat
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.Direction
import com.aminography.primedatepicker.LabelFormatter
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.picker.header.BaseLazyView
import com.aminography.primedatepicker.picker.header.HeaderView
import com.aminography.primedatepicker.tools.forceLocaleStrings
import kotlinx.android.synthetic.main.range_days_header.view.*
import java.util.*

/**
 * @author aminography
 */
class RangeHeaderView(
    viewStub: ViewStub,
    private val direction: Direction
) : BaseLazyView(if (direction == Direction.LTR) R.layout.range_days_header else R.layout.range_days_header_rtl, viewStub), HeaderView {

    var locale: Locale? = null
        set(value) {
            field = value
            value?.let {
                val strings = rootView.context.forceLocaleStrings(
                    it,
                    R.string.prime_date_picker_from,
                    R.string.prime_date_picker_to
                )
                rootView.rangeStartTextView.firstLabelText = strings[0]
                rootView.rangeEndTextView.firstLabelText = strings[1]
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
                PickType.RANGE_START -> animateBackground(true)
                PickType.RANGE_END -> animateBackground(false)
                else -> {
                }
            }
        }

    var pickedRangeStartDay: PrimeCalendar? = null
        set(value) {
            field = value
            rootView.rangeStartTextView.secondLabelText = value?.let { labelFormatter?.invoke(it) }
                ?: ""
        }

    var pickedRangeEndDay: PrimeCalendar? = null
        set(value) {
            field = value
            rootView.rangeEndTextView.secondLabelText = value?.let { labelFormatter?.invoke(it) }
                ?: ""
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

    private fun animateBackground(isStart: Boolean) {
        ObjectAnimator.ofFloat(
            rootView.backImageView,
            "translationX",
            if (isStart) 0f
            else rootView.backImageView.width.toFloat().let {
                if (direction == Direction.LTR) it else -it
            }
        ).apply {
            interpolator = OvershootInterpolator()
            duration = 300
            start()
        }
    }

}