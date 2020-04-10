package com.aminography.primedatepicker.picker.action

import android.graphics.Typeface
import android.view.ViewStub
import com.aminography.primedatepicker.Direction
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.picker.header.BaseLazyView
import com.aminography.primedatepicker.tools.forceLocaleStrings
import kotlinx.android.synthetic.main.action_container.view.*
import java.util.*

/**
 * @author aminography
 */
class ActionView(
    viewStub: ViewStub,
    direction: Direction
) : BaseLazyView(if (direction == Direction.LTR) R.layout.action_container else R.layout.action_container_rtl, viewStub) {

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
                rootView.todayButton.text = strings[0]
                rootView.positiveButton.text = strings[1]
                rootView.negativeButton.text = strings[2]
            }
        }

    var typeface: Typeface? = null
        set(value) {
            field = value
            rootView.todayButton.typeface = value
            rootView.positiveButton.typeface = value
            rootView.negativeButton.typeface = value
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