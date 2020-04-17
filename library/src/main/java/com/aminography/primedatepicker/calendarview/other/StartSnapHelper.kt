package com.aminography.primedatepicker.calendarview.other

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * @author aminography
 */
class StartSnapHelper : LinearSnapHelper() {

    private var verticalHelper: OrientationHelper? = null
    private var horizontalHelper: OrientationHelper? = null

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ) = IntArray(2).also {
        it[0] = if (layoutManager.canScrollHorizontally())
            distanceToStart(targetView, getHorizontalHelper(layoutManager))
        else 0

        it[1] = if (layoutManager.canScrollVertically())
            distanceToStart(targetView, getVerticalHelper(layoutManager))
        else 0
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        return if (layoutManager is LinearLayoutManager) {
            if (layoutManager.canScrollHorizontally())
                getStartView(layoutManager, getHorizontalHelper(layoutManager))
            else getStartView(layoutManager, getVerticalHelper(layoutManager))
        } else super.findSnapView(layoutManager)
    }

    private fun distanceToStart(targetView: View, helper: OrientationHelper): Int =
        helper.getDecoratedStart(targetView) - helper.startAfterPadding

    private fun getStartView(layoutManager: RecyclerView.LayoutManager, helper: OrientationHelper): View? {
        if (layoutManager is LinearLayoutManager) {
            val firstChild = layoutManager.findFirstVisibleItemPosition()
            val isLastItem = layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1

            if (firstChild == RecyclerView.NO_POSITION || isLastItem) return null
            val child = layoutManager.findViewByPosition(firstChild)

            return if (helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2 && helper.getDecoratedEnd(child) > 0)
                child
            else if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1)
                null
            else layoutManager.findViewByPosition(firstChild + 1)
        }
        return super.findSnapView(layoutManager)
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (verticalHelper == null)
            verticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        return verticalHelper!!
    }

    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (horizontalHelper == null)
            horizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        return horizontalHelper!!
    }

}
