package com.aminography.primedatepicker.picker.action

import android.graphics.Typeface
import android.view.ViewStub
import com.aminography.primedatepicker.common.Direction
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.picker.base.BaseLazyView
import com.aminography.primedatepicker.utils.forceLocaleStrings
import kotlinx.android.synthetic.main.action_bar_container.view.*
import java.util.*

/**
 * @author aminography
 */
internal class ActionBarView(
    viewStub: ViewStub,
    direction: Direction
) : BaseLazyView(if (direction == Direction.LTR) R.layout.action_bar_container else R.layout.action_bar_container_rtl, viewStub) {

    var locale: Locale? = null
        set(value) {
            field = value
            value?.let {
                val strings = rootView.context.forceLocaleStrings(
                    it,
                    R.string.action_today,
                    R.string.action_select,
                    R.string.action_cancel
                )
                rootView.todayTwoLineTextView.topLabelText = strings[0]
                rootView.positiveTwoLineTextView.topLabelText = strings[1]
                rootView.negativeTwoLineTextView.topLabelText = strings[2]
            }
        }

    var typeface: Typeface? = null
        set(value) {
            field = value
            rootView.todayTwoLineTextView.typeface = value
            rootView.positiveTwoLineTextView.typeface = value
            rootView.negativeTwoLineTextView.typeface = value
        }

    var onTodayButtonClick: (() -> Unit)? = null
        set(value) {
            field = value
            rootView.todayButton.setOnClickListener {
                value?.invoke()
            }
        }

    var onPositiveButtonClick: (() -> Unit)? = null
        set(value) {
            field = value
            rootView.positiveButton.setOnClickListener {
                value?.invoke()
            }
        }

    var onNegativeButtonClick: (() -> Unit)? = null
        set(value) {
            field = value
            rootView.negativeButton.setOnClickListener {
                value?.invoke()
            }
        }

}