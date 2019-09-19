package com.aminography.primedatepicker.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.view.View


/**
 * Created by Amin on 8/24/2018.
 */
abstract class BaseBottomSheetDialogFragment(@LayoutRes private val layoutResId: Int) : BottomSheetDialogFragment() {

    protected val activityContext: Context by lazy { activity!!.applicationContext }
    protected lateinit var rootView: View

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        rootView = View.inflate(context, layoutResId, null)
        dialog.setContentView(rootView)
        onInitViews(rootView)
    }

    abstract fun onInitViews(rootView: View)

    override fun show(manager: FragmentManager?, tag: String) {
        if (!isShowing) manager?.let {
            super.show(manager, tag)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isShowing = true
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        isShowing = false
    }

    companion object {
        var isShowing = false
    }

}