package com.aminography.primedatepicker.picker.component

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView

/**
 * @author aminography
 */
internal class ColoredNumberPicker : NumberPicker {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun addView(child: View) {
        super.addView(child)
        updateView(child)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        updateView(child)
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        super.addView(child, params)
        updateView(child)
    }

    private fun updateView(view: View) {
        if (view is TextView) {
            labelTextSize?.let { view.setTextSize(TypedValue.COMPLEX_UNIT_PX, it.toFloat()) }
            labelTextColor?.let { view.setTextColor(it) }
            typeface?.let { view.typeface = typeface }
        }
    }

    fun applyDividerColor() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            val pickerFields = NumberPicker::class.java.declaredFields
            for (pf in pickerFields) {
                if (pf.name == "mSelectionDivider") {
                    pf.isAccessible = true
                    try {
                        dividerColor?.let {
                            val colorDrawable = ColorDrawable(it)
                            pf.set(this, colorDrawable)
                        }
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                    } catch (e: Resources.NotFoundException) {
                        e.printStackTrace()
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }
                    break
                }
            }
        } else {
            // TODO: finding a way to customize the divider, because of:
            //  @UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
            //  private final Drawable mSelectionDivider;
        }
    }

    fun fixInputFilter() {
        val field = NumberPicker::class.java.getDeclaredField("mInputText")
        field.isAccessible = true
        (field.get(this) as EditText).filters = arrayOfNulls(0)
    }

    companion object {
        internal var labelTextSize: Int? = null
        internal var labelTextColor: Int? = null
        internal var dividerColor: Int? = null
        internal var typeface: Typeface? = null
    }

}