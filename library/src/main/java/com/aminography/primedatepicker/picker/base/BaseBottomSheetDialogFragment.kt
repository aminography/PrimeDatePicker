package com.aminography.primedatepicker.picker.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import com.aminography.primedatepicker.utils.screenSize
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * @author aminography
 */
internal abstract class BaseBottomSheetDialogFragment(
    @LayoutRes private val layoutResId: Int
) : BottomSheetDialogFragment() {

    protected lateinit var rootView: View

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        rootView = View.inflate(context, layoutResId, null)
        dialog.setContentView(rootView)
        onInitViews(rootView)

        val parentView = rootView.parent as View
        parentView.setBackgroundColor(Color.TRANSPARENT)

        val params = parentView.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior
        if (behavior is BottomSheetBehavior<*>) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = requireContext().screenSize.y
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    abstract fun onInitViews(rootView: View)

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isShowing) manager.let {
            super.show(manager, tag)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isShowing = true
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        isShowing = false
    }

    companion object {
        var isShowing = false
    }

}