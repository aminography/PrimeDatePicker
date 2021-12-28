package com.aminography.primedatepicker.picker.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.Paint.Style
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.utils.dp2px
import kotlin.math.max


/**
 * @author aminography
 */
@Suppress("ConstantConditionIf", "MemberVisibilityCanBePrivate", "unused")
internal class TwoLinesTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Interior Variables --------------------------------------------------------------------------

    private val dp = context.dp2px(1f)
    private fun dp(value: Float) = dp.times(value).toInt()

    private var topLabelPaint: Paint? = null
    private var bottomLabelPaint: Paint? = null

    private var viewWidth = 0

    // Control Variables ---------------------------------------------------------------------------

    var topLabelText: String = ""
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var bottomLabelText: String = ""
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var topLabelTextColor: Int = 0
        set(value) {
            field = value
            topLabelPaint?.color = value
            if (invalidate) invalidate()
        }

    var bottomLabelTextColor: Int = 0
        set(value) {
            field = value
            bottomLabelPaint?.color = value
            if (invalidate) invalidate()
        }

    var topLabelTextSize: Int = 0
        set(value) {
            field = value
            topLabelPaint?.textSize = value.toFloat()
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var bottomLabelTextSize: Int = 0
        set(value) {
            field = value
            bottomLabelPaint?.textSize = value.toFloat()
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var preferredMinWidth: Int = 0
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var gapBetweenLines: Int = 0
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    // Programmatically Control Variables ----------------------------------------------------------

    var typeface: Typeface? = null
        set(value) {
            field = value
            applyTypeface()
            if (invalidate) invalidate()
        }

    // ---------------------------------------------------------------------------------------------

    private var invalidate: Boolean = true

    fun doNotInvalidate(function: () -> Unit) {
        val previous = invalidate
        invalidate = false
        function.invoke()
        invalidate = previous
    }

    // ---------------------------------------------------------------------------------------------

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TwoLinesTextView, defStyleAttr, defStyleRes).run {
            doNotInvalidate {
                topLabelText = getString(R.styleable.TwoLinesTextView_topLabelText) ?: ""
                bottomLabelText = getString(R.styleable.TwoLinesTextView_bottomLabelText) ?: ""

                topLabelTextColor = getColor(R.styleable.TwoLinesTextView_topLabelTextColor, ContextCompat.getColor(context, R.color.defaultTwoLineTextColor))
                bottomLabelTextColor = getColor(R.styleable.TwoLinesTextView_bottomLabelTextColor, ContextCompat.getColor(context, R.color.defaultTwoLineTextColor))

                topLabelTextSize = getDimensionPixelSize(R.styleable.TwoLinesTextView_topLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultTwoLineTextSize))
                bottomLabelTextSize = getDimensionPixelSize(R.styleable.TwoLinesTextView_bottomLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultTwoLineTextSize))

                preferredMinWidth = getDimensionPixelSize(R.styleable.TwoLinesTextView_preferredMinWidth, 0)
                gapBetweenLines = getDimensionPixelSize(R.styleable.TwoLinesTextView_gapBetweenLines, 0)
            }
            recycle()
        }

        initPaints()
        applyTypeface()
        calculateSizes()

        if (isInEditMode) {
            invalidate()
        }
    }

    private fun calculateSizes() {
        val topLabelBounds = Rect()
        topLabelPaint?.getTextBounds(topLabelText, 0, topLabelText.length, topLabelBounds)

        val bottomLabelBounds = Rect()
        bottomLabelPaint?.getTextBounds(bottomLabelText, 0, bottomLabelText.length, bottomLabelBounds)

        viewWidth = max(topLabelBounds.width(), bottomLabelBounds.width())
        viewWidth = max(viewWidth, preferredMinWidth)
    }

    private fun initTopLabelPaint() {
        topLabelPaint = Paint().apply {
            textSize = topLabelTextSize.toFloat()
            color = topLabelTextColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
    }

    private fun initBottomLabelPaint() {
        bottomLabelPaint = Paint().apply {
            textSize = bottomLabelTextSize.toFloat()
            color = bottomLabelTextColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
    }

    private fun initPaints() {
        initTopLabelPaint()
        initBottomLabelPaint()
    }

    private fun applyTypeface() {
        topLabelPaint?.typeface = typeface
        bottomLabelPaint?.typeface = typeface
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = paddingLeft +
            viewWidth +
            paddingRight
        val height = paddingTop +
            topLabelTextSize +
            gapBetweenLines +
            bottomLabelTextSize +
            paddingBottom
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        drawTopLabel(canvas)
        drawBottomLabel(canvas)
    }

    private fun drawTopLabel(canvas: Canvas) {
        val x = paddingLeft + viewWidth / 2f
        var y = paddingTop + topLabelTextSize / 2f

        topLabelPaint?.run {
            y -= ((descent() + ascent()) / 2)

            canvas.drawText(
                topLabelText,
                x,
                y,
                this
            )
        }
    }

    private fun drawBottomLabel(canvas: Canvas) {
        val x = paddingLeft + viewWidth / 2f
        var y = paddingTop + topLabelTextSize + gapBetweenLines + bottomLabelTextSize / 2f

        bottomLabelPaint?.run {
            y -= ((descent() + ascent()) / 2)

            canvas.drawText(
                bottomLabelText,
                x,
                y,
                this
            )
        }
    }

    // Save/Restore States -------------------------------------------------------------------------

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)

        savedState.topLabelText = topLabelText
        savedState.bottomLabelText = bottomLabelText
        savedState.topLabelTextColor = topLabelTextColor
        savedState.bottomLabelTextColor = bottomLabelTextColor
        savedState.topLabelTextSize = topLabelTextSize
        savedState.bottomLabelTextSize = bottomLabelTextSize
        savedState.preferredMinWidth = preferredMinWidth
        savedState.gapBetweenLines = gapBetweenLines

        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)

        doNotInvalidate {
            topLabelText = savedState.topLabelText
            bottomLabelText = savedState.bottomLabelText
            topLabelTextColor = savedState.topLabelTextColor
            bottomLabelTextColor = savedState.bottomLabelTextColor
            topLabelTextSize = savedState.topLabelTextSize
            bottomLabelTextSize = savedState.bottomLabelTextSize
            preferredMinWidth = savedState.preferredMinWidth
            gapBetweenLines = savedState.gapBetweenLines
        }

        applyTypeface()
        calculateSizes()
    }

    private class SavedState : BaseSavedState {

        var topLabelText: String = ""
        var bottomLabelText: String = ""
        var topLabelTextColor: Int = 0
        var bottomLabelTextColor: Int = 0
        var topLabelTextSize: Int = 0
        var bottomLabelTextSize: Int = 0
        var preferredMinWidth: Int = 0
        var gapBetweenLines: Int = 0

        constructor(superState: Parcelable?) : super(superState)

        private constructor(input: Parcel) : super(input) {
            topLabelText = input.readString() ?: ""
            bottomLabelText = input.readString() ?: ""
            topLabelTextColor = input.readInt()
            bottomLabelTextColor = input.readInt()
            topLabelTextSize = input.readInt()
            bottomLabelTextSize = input.readInt()
            preferredMinWidth = input.readInt()
            gapBetweenLines = input.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(topLabelText)
            out.writeString(bottomLabelText)
            out.writeInt(topLabelTextColor)
            out.writeInt(bottomLabelTextColor)
            out.writeInt(topLabelTextSize)
            out.writeInt(bottomLabelTextSize)
            out.writeInt(preferredMinWidth)
            out.writeInt(gapBetweenLines)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(input: Parcel): SavedState = SavedState(input)
                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }

}
