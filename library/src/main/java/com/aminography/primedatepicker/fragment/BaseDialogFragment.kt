package com.aminography.primedatepicker.fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Created by Amin on 6/5/2018.
 */
abstract class BaseDialogFragment(@LayoutRes private val layoutResId: Int) : DialogFragment() {

    protected val activityContext: Context by lazy { activity!! }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutResId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitViews(view, savedInstanceState)
    }

    abstract fun onInitViews(rootView: View, savedInstanceState: Bundle?)

    open fun show(manager: FragmentManager?) {
        if (!isShowing) manager?.let {
            //            it.beginTransaction().commitAllowingStateLoss()
            show(manager, tag)
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