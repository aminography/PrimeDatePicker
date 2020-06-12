package com.aminography.primedatepicker.monthview.painters

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import com.aminography.primedatepicker.common.Direction
import com.aminography.primedatepicker.monthview.DayState

/**
 * @author aminography
 */
internal class DayLabelsPainter {

    var shouldAnimateDayBackground: ((Int) -> Boolean)? = null
    var findDayState: ((Int) -> DayState)? = null
    var findDayLabelTextColor: ((Int, DayState) -> Int)? = null
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

    var showBesideMonthDays: Boolean = false

    fun draw(
        canvas: Canvas,
        direction: Direction,
        cellWidth: Float,
        cellHeight: Float,
        xPositions: FloatArray,
        yPosition: Float,
        radius: Float,
        animatedRadius: Float,
        daysInMonth: Int,
        daysInPreviousMonth: Int,
        rowCount: Int,
        columnCount: Int,
        startingColumn: Int,
        developerOptionsShowGuideLines: Boolean
    ) {
        var row = 0
        var column = startingColumn

        var y = yPosition

        if (showBesideMonthDays) {
            for (i in 0 until startingColumn) {
                val x = xPositions[i]
                val dayOfMonth = daysInPreviousMonth - startingColumn + i + 1
                drawDayLabel(canvas, x, y, dayOfMonth, DayState.BESIDE_MONTH)
            }
        }

        for (dayOfMonth in 1..daysInMonth) {
            val x = xPositions[column]

            val pickedDayState = findDayState?.invoke(dayOfMonth) ?: DayState.NORMAL
            val targetRadius = if (shouldAnimateDayBackground?.invoke(dayOfMonth) == true) animatedRadius else radius

            drawDayBackground(canvas, direction, cellWidth, x, y, targetRadius, pickedDayState)
            drawDayLabel(canvas, x, y, dayOfMonth, pickedDayState)
            if (developerOptionsShowGuideLines) {
                drawGuideLines(canvas, cellWidth, cellHeight, x, y)
            }

            column++
            if (column == columnCount) {
                row++
                column = 0
                y += cellHeight
            }
        }

        if (showBesideMonthDays) {
            for (dayOfMonth in 1..15) {
                val x = xPositions[column]

                drawDayLabel(canvas, x, y, dayOfMonth, DayState.BESIDE_MONTH)

                column++
                if (column == columnCount) {
                    if (++row == rowCount) break
                    column = 0
                    y += cellHeight
                }
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
        dayState: DayState
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

        when (dayState) {
            DayState.PICKED_SINGLE,
            DayState.START_OF_RANGE_SINGLE -> {
                drawCircle()
            }
            DayState.START_OF_RANGE -> {
                when (direction) {
                    Direction.LTR -> drawRightHalfRect()
                    Direction.RTL -> drawLeftHalfRect()
                }
                drawCircle()
            }
            DayState.IN_RANGE -> {
                drawRect()
            }
            DayState.END_OF_RANGE -> {
                when (direction) {
                    Direction.LTR -> drawLeftHalfRect()
                    Direction.RTL -> drawRightHalfRect()
                }
                drawCircle()
            }
            else -> {
                // nothing!
            }
        }
    }

    private fun drawDayLabel(
        canvas: Canvas,
        x: Float,
        y: Float,
        dayOfMonth: Int,
        dayState: DayState
    ) {
        dayLabelPaint.run {
            color = findDayLabelTextColor?.invoke(dayOfMonth, dayState) ?: Color.BLACK
            canvas.drawText(
                dayLabelFormatter?.invoke(dayOfMonth) ?: "$dayOfMonth",
                x,
                y - (descent() + ascent()) / 2,
                this
            )
        }
    }

    private fun drawGuideLines(
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