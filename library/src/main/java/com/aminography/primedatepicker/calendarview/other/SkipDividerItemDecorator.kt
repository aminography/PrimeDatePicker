package com.aminography.primedatepicker.calendarview.other

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminography.primedatepicker.calendarview.adapter.MonthListAdapter
import kotlin.math.roundToInt

/**
 * @author aminography
 */
internal class SkipDividerItemDecorator(
    private val divider: Drawable? = null
) : RecyclerView.ItemDecoration() {

    private val bounds = Rect()
    private var orientation: Int? = null

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || divider == null) return
        if (orientation == LinearLayout.VERTICAL) drawVertical(c, parent)
        else drawHorizontal(c, parent)
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int

        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            if (position != RecyclerView.NO_POSITION && position != parent.adapter!!.itemCount - 1 && (parent.adapter as MonthListAdapter).itemAt(position).hasDivider) {
                parent.getDecoratedBoundsWithMargins(child, bounds)
                val bottom = bounds.bottom + child.translationY.roundToInt()
                divider?.apply {
                    val top = bottom - intrinsicHeight
                    setBounds(left, top, right, bottom)
                    draw(canvas)
                }
            }
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top: Int
        val bottom: Int

        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(parent.paddingLeft, top, parent.width - parent.paddingRight, bottom)
        } else {
            top = 0
            bottom = parent.height
        }

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            if (position != RecyclerView.NO_POSITION && position != parent.adapter!!.itemCount - 1 && (parent.adapter as MonthListAdapter).itemAt(position).hasDivider) {
                parent.layoutManager!!.getDecoratedBoundsWithMargins(child, bounds)
                val right = bounds.right + child.translationX.roundToInt()
                divider?.apply {
                    val left = right - intrinsicWidth
                    setBounds(left, top, right, bottom)
                    draw(canvas)
                }
            }
        }
        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (orientation == null) orientation = getOrientation(parent)
        val position = parent.getChildAdapterPosition(view)
        if (position != RecyclerView.NO_POSITION && position != parent.adapter!!.itemCount - 1 && (parent.adapter as MonthListAdapter).itemAt(position).hasDivider) {
            if (divider == null) {
                outRect.set(0, 0, 0, 0)
                return
            }
            if (orientation == LinearLayout.VERTICAL) outRect.set(0, 0, 0, divider.intrinsicHeight)
            else outRect.set(0, 0, divider.intrinsicWidth, 0)
        } else {
            outRect.setEmpty()
        }
    }

    private fun getOrientation(parent: RecyclerView): Int {
        if (parent.layoutManager is LinearLayoutManager) return (parent.layoutManager as LinearLayoutManager).orientation
        else throw IllegalStateException("DividerItemDecoration can only be used with a LinearLayoutManager.")
    }

}