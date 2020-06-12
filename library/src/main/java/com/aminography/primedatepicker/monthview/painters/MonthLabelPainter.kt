package com.aminography.primedatepicker.monthview.painters

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface

/**
 * @author aminography
 */
internal class MonthLabelPainter {

    private val monthLabelPaint: Paint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
    }

    var monthLabelTextColor: Int = 0
        set(value) {
            field = value
            monthLabelPaint.color = value
        }

    var monthLabelTextSize: Int = 0
        set(value) {
            field = value
            monthLabelPaint.textSize = value.toFloat()
        }

    var typeface: Typeface? = null
        set(value) {
            field = value
            monthLabelPaint.typeface = value
        }

    fun draw(
        canvas: Canvas,
        cellWidth: Float,
        cellHeight: Float,
        xPosition: Float,
        yPosition: Float,
        monthLabel: String,
        developerOptionsShowGuideLines: Boolean
    ) {
        canvas.drawText(
            monthLabel,
            xPosition,
            yPosition - ((monthLabelPaint.descent() + monthLabelPaint.ascent()) / 2),
            monthLabelPaint
        )

        if (developerOptionsShowGuideLines) {
            drawGuideLines(canvas, cellWidth, cellHeight, xPosition, yPosition)
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