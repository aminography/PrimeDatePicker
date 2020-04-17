package com.aminography.primedatepicker.picker.header

import android.view.View
import android.view.ViewStub
import androidx.annotation.LayoutRes

/**
 * @author aminography
 */
abstract class BaseLazyView(
    @LayoutRes private val layoutResId: Int,
    viewStub: ViewStub
) {

    protected val rootView: View

    init {
        viewStub.layoutResource = layoutResId
        rootView = viewStub.inflate()
    }

}