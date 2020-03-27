package com.aminography.primedatepicker.fragment.header

import android.view.View
import android.view.ViewStub
import androidx.annotation.LayoutRes

/**
 * @author aminography
 */
abstract class BaseHeaderView(
    @LayoutRes private val layoutResId: Int,
    viewStub: ViewStub
) {

    protected val rootView: View

    init {
        viewStub.layoutResource = layoutResId
        rootView = viewStub.inflate()
    }

}