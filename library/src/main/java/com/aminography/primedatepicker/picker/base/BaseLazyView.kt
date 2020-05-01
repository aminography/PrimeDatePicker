package com.aminography.primedatepicker.picker.base

import android.view.View
import android.view.ViewStub
import androidx.annotation.LayoutRes

/**
 * @author aminography
 */
internal abstract class BaseLazyView(
    @LayoutRes private val layoutResId: Int,
    viewStub: ViewStub
) {

    val rootView: View

    init {
        viewStub.layoutResource = layoutResId
        rootView = viewStub.inflate()
    }

}