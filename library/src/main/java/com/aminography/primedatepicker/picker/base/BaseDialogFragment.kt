package com.aminography.primedatepicker.picker.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.aminography.primedatepicker.utils.screenSize

/**
 * @author aminography
 */
internal abstract class BaseDialogFragment(
    @LayoutRes private val layoutResId: Int
) : DialogFragment() {

    protected lateinit var rootView: View

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        rootView = View.inflate(context, layoutResId, null)
        dialog.setContentView(rootView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        onInitViews(rootView)
        initWindow()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    abstract fun onInitViews(rootView: View)

    private fun initWindow() {
        val size = requireContext().screenSize
        with(rootView) {
            layoutParams.width = (size.x * 0.90).toInt()
        }
    }

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