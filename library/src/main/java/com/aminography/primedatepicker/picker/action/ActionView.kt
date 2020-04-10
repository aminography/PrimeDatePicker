package com.aminography.primedatepicker.picker.action

import android.graphics.Typeface
import android.view.ViewStub
import com.aminography.primedatepicker.Direction
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.picker.header.BaseLazyView
import kotlinx.android.synthetic.main.action_container.view.*

/**
 * @author aminography
 */
class ActionView(
    viewStub: ViewStub,
    direction: Direction
) : BaseLazyView(if (direction == Direction.LTR) R.layout.action_container else R.layout.action_container_rtl, viewStub) {

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