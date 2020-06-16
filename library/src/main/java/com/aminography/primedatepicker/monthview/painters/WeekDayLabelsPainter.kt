package com.aminography.primedatepicker.monthview.painters

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface

/**
 * @author aminography
 */
internal class WeekDayLabelsPainter {

    var findWeekDayLabelTextColor: ((Int) -> Int)? = null
    var weekDayLabelFormatter: ((Int) -> String)? = null

    private val weekLabelPaint: Paint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
    }

    var weekLabelTextColor: Int = 0
        set(value) {
            field = value
            weekLabelPaint.color = value
        }

    var weekLabelTextSize: Int = 0
        set(value) {
            field = value
            weekLabelPaint.textSize = value.toFloat()
        }

    var typeface: Typeface? = null
        set(value) {
            field = value
            weekLabelPaint.typeface = value
        }

    fun draw(
        canvas: Canvas,
        cellWidth: Float,
        cellHeight: Float,
        xPositions: FloatArray,
        yPosition: Float,
        columnCount: Int,
        firstDayOfWeek: Int,
        developerOptionsShowGuideLines: Boolean
    ) {
        for (i in 0 until columnCount) {
            val dayOfWeek = (i + firstDayOfWeek) % columnCount
            val x = xPositions[i]

            weekLabelPaint.color = findWeekDayLabelTextColor?.invoke(dayOfWeek % 7) ?: weekLabelTextColor
            canvas.drawText(
                weekDayLabelFormatter?.invoke(dayOfWeek % 7) ?: "${dayOfWeek % 7}",
                x,
                yPosition - ((weekLabelPaint.descent() + weekLabelPaint.ascent()) / 2),
                weekLabelPaint
            )

            if (developerOptionsShowGuideLines) {
                drawGuideLines(canvas, cellWidth, cellHeight, x, yPosition)
            }
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
            color = Color.GREEN
            style = Paint.Style.FILL
            alpha = 50
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