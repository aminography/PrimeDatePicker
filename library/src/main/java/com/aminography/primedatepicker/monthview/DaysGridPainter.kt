package com.aminography.primedatepicker.monthview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import com.aminography.primedatepicker.common.Direction

/**
 * @author aminography
 */
internal class DaysGridPainter {

    var shouldAnimateDayBackground: ((Int) -> Boolean)? = null
    var findPickedDayState: ((Int) -> PickedDayState)? = null
    var findDayLabelTextColor: ((Int, PickedDayState) -> Int)? = null
    var dayLabelFormatter: ((Int) -> String)? = null

    private val dayLabelPaint: Paint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            isFakeBoldText = false
        }
    }

    private val selectedDayCirclePaint: Paint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
    }

    private val selectedDayRectPaint: Paint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
    }

    var dayLabelTextSize: Int = 0
        set(value) {
            field = value
            dayLabelPaint.textSize = value.toFloat()
        }

    var typeface: Typeface? = null
        set(value) {
            field = value
            dayLabelPaint.typeface = value
        }

    var pickedDayCircleBackgroundColor: Int = 0
        set(value) {
            field = value
            selectedDayCirclePaint.color = value
        }

    var pickedDayInRangeBackgroundColor: Int = 0
        set(value) {
            field = value
            selectedDayRectPaint.color = value
        }

    fun drawDayLabels(
        canvas: Canvas,
        direction: Direction,
        cellWidth: Float,
        cellHeight: Float,
        radius: Float,
        animatedRadius: Float,
        xPositions: FloatArray,
        yPosition: Float,
        daysInMonth: Int,
        columnCount: Int,
        startingColumn: Int,
        developerOptionsShowGuideLines: Boolean
    ) {
        var column = startingColumn
        var y = yPosition
        for (dayOfMonth in 1..daysInMonth) {
            val x = xPositions[column]

            val pickedDayState = findPickedDayState?.invoke(dayOfMonth) ?: PickedDayState.NOTHING
            val targetRadius = if (shouldAnimateDayBackground?.invoke(dayOfMonth) == true) animatedRadius else radius

            drawDayBackground(canvas, direction, cellWidth, x, y, targetRadius, pickedDayState)
            drawDayLabel(canvas, x, y, dayOfMonth, pickedDayState)
            if (developerOptionsShowGuideLines) {
                drawDayLabelGuideLines(canvas, cellWidth, cellHeight, x, y)
            }

            column++
            if (column == columnCount) {
                column = 0
                y += cellHeight
            }
        }
    }

    private fun drawDayBackground(
        canvas: Canvas,
        direction: Direction,
        cellWidth: Float,
        x: Float,
        y: Float,
        radius: Float,
        pickedDayState: PickedDayState
    ) {
        val halfCellWidth = cellWidth / 2

        fun drawCircle() = canvas.drawCircle(
            x,
            y,
            radius,
            selectedDayCirclePaint
        )

        fun drawRect() = canvas.drawRect(
            x - halfCellWidth,
            y - radius,
            x + halfCellWidth,
            y + radius,
            selectedDayRectPaint
        )

        fun drawLeftHalfRect() = canvas.drawRect(
            x - halfCellWidth,
            y - radius,
            x,
            y + radius,
            selectedDayRectPaint
        )

        fun drawRightHalfRect() = canvas.drawRect(
            x,
            y - radius,
            x + halfCellWidth,
            y + radius,
            selectedDayRectPaint
        )

        when (pickedDayState) {
            PickedDayState.PICKED_SINGLE,
            PickedDayState.START_OF_RANGE_SINGLE -> {
                drawCircle()
            }
            PickedDayState.START_OF_RANGE -> {
                when (direction) {
                    Direction.LTR -> drawRightHalfRect()
                    Direction.RTL -> drawLeftHalfRect()
                }
                drawCircle()
            }
            PickedDayState.IN_RANGE -> {
                drawRect()
            }
            PickedDayState.END_OF_RANGE -> {
                when (direction) {
                    Direction.LTR -> drawLeftHalfRect()
                    Direction.RTL -> drawRightHalfRect()
                }
                drawCircle()
            }
            PickedDayState.NOTHING -> {
            }
        }
    }

    private fun drawDayLabel(
        canvas: Canvas,
        x: Float,
        y: Float,
        dayOfMonth: Int,
        pickedDayState: PickedDayState
    ) {
        dayLabelPaint.run {
            color = findDayLabelTextColor?.invoke(dayOfMonth, pickedDayState) ?: Color.BLACK
            canvas.drawText(
                dayLabelFormatter?.invoke(dayOfMonth) ?: "$dayOfMonth",
                x,
                y - (descent() + ascent()) / 2,
                this
            )
        }
    }

    private fun drawDayLabelGuideLines(
        canvas: Canvas,
        cellWidth: Float,
        cellHeight: Float,
        x: Float,
        y: Float
    ) {
        val halfCellWidth = cellWidth / 2
        val halfCellHeight = cellHeight / 2
        Paint().apply {
            isAntiAlias = true
            color = Color.GRAY
            style = Paint.Style.STROKE
            canvas.drawRect(
                x - halfCellWidth,
                y - halfCellHeight,
                x + halfCellWidth,
                y + halfCellHeight,
                this
            )
        }
        Paint().apply {
            isAntiAlias = true
            color = Color.RED
            style = Paint.Style.FILL
            canvas.drawCircle(
                x,
                y,
                2f,
                this
            )
        }
    }
}