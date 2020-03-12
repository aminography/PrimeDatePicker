package com.aminography.primedatepicker.fragment

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
import com.aminography.primedatepicker.tools.dp2px
import kotlin.math.max


/**
 * @author aminography
 */
@Suppress("ConstantConditionIf", "MemberVisibilityCanBePrivate", "unused")
class TwoLineTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0,
        @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Interior Variables --------------------------------------------------------------------------

    private val dp = context.dp2px(1f)
    private fun dp(value: Float) = dp.times(value).toInt()

    private var firstLabelPaint: Paint? = null
    private var secondLabelPaint: Paint? = null

    private var viewWidth = 0
    private var firstLabelHeight = 0
    private var secondLabelHeight = 0

    // Control Variables ---------------------------------------------------------------------------

    var firstLabelText: String = ""
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var secondLabelText: String = ""
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var firstLabelTextColor: Int = 0
        set(value) {
            field = value
            firstLabelPaint?.color = value
            if (invalidate) invalidate()
        }

    var secondLabelTextColor: Int = 0
        set(value) {
            field = value
            secondLabelPaint?.color = value
            if (invalidate) invalidate()
        }

    var firstLabelTextSize: Int = 0
        set(value) {
            field = value
            firstLabelPaint?.textSize = value.toFloat()
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var secondLabelTextSize: Int = 0
        set(value) {
            field = value
            secondLabelPaint?.textSize = value.toFloat()
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var firstLabelTopPadding: Int = 0
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var firstLabelBottomPadding: Int = 0
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var secondLabelTopPadding: Int = 0
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var secondLabelBottomPadding: Int = 0
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
        context.obtainStyledAttributes(attrs, R.styleable.TwoLineTextView, defStyleAttr, defStyleRes).apply {
            doNotInvalidate {

                firstLabelTextColor = getColor(R.styleable.TwoLineTextView_firstLabelTextColor, ContextCompat.getColor(context, R.color.defaultTwoLineTextColor))
                secondLabelTextColor = getColor(R.styleable.TwoLineTextView_secondLabelTextColor, ContextCompat.getColor(context, R.color.defaultTwoLineTextColor))

                firstLabelTextSize = getDimensionPixelSize(R.styleable.TwoLineTextView_firstLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultTwoLineTextSize))
                secondLabelTextSize = getDimensionPixelSize(R.styleable.TwoLineTextView_secondLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultTwoLineTextSize))

                firstLabelTopPadding = getDimensionPixelSize(R.styleable.TwoLineTextView_firstLabelTopPadding, 0)
                firstLabelBottomPadding = getDimensionPixelSize(R.styleable.TwoLineTextView_firstLabelBottomPadding, 0)
                secondLabelTopPadding = getDimensionPixelSize(R.styleable.TwoLineTextView_secondLabelTopPadding, 0)
                secondLabelBottomPadding = getDimensionPixelSize(R.styleable.TwoLineTextView_secondLabelBottomPadding, 0)
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
        firstLabelHeight = firstLabelTextSize + firstLabelTopPadding + firstLabelBottomPadding
        secondLabelHeight = secondLabelTextSize + secondLabelTopPadding + secondLabelBottomPadding

        val firstLabelBounds = Rect()
        firstLabelPaint?.getTextBounds(firstLabelText, 0, firstLabelText.length, firstLabelBounds)

        val secondLabelBounds = Rect()
        secondLabelPaint?.getTextBounds(secondLabelText, 0, secondLabelText.length, secondLabelBounds)

        viewWidth = max(firstLabelBounds.width(), secondLabelBounds.width())
    }

    private fun initFirstLabelPaint() {
        firstLabelPaint = Paint().apply {
            textSize = firstLabelTextSize.toFloat()
            color = firstLabelTextColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
    }

    private fun initSecondLabelPaint() {
        secondLabelPaint = Paint().apply {
            textSize = secondLabelTextSize.toFloat()
            color = secondLabelTextColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
    }

    private fun initPaints() {
        initFirstLabelPaint()
        initSecondLabelPaint()
    }

    private fun applyTypeface() {
        firstLabelPaint?.typeface = typeface
        secondLabelPaint?.typeface = typeface
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = paddingLeft +
                viewWidth +
                paddingRight
        val height = paddingTop +
                firstLabelHeight +
                secondLabelHeight +
                paddingBottom
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        drawFirstLabel(canvas)
        drawSecondLabel(canvas)
    }

    private fun drawFirstLabel(canvas: Canvas) {
        val x = paddingLeft + viewWidth / 2f
        var y = paddingTop +
                (firstLabelHeight - firstLabelTopPadding - firstLabelBottomPadding) / 2f +
                firstLabelTopPadding

        firstLabelPaint?.apply {
            y -= ((descent() + ascent()) / 2)
        }

        firstLabelPaint?.apply {
            canvas.drawText(
                    firstLabelText,
                    x,
                    y,
                    this
            )
        }
    }

    private fun drawSecondLabel(canvas: Canvas) {
        val x = paddingLeft + viewWidth / 2f
        var y = paddingTop +
                firstLabelHeight +
                (secondLabelHeight - secondLabelTopPadding - secondLabelBottomPadding) / 2f +
                secondLabelTopPadding

        secondLabelPaint?.apply {
            y -= ((descent() + ascent()) / 2)
        }

        secondLabelPaint?.apply {
            canvas.drawText(
                    secondLabelText,
                    x,
                    y,
                    this
            )
        }
    }

    // Save/Restore States -------------------------------------------------------------------------

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)

        savedState.firstLabelTextColor = firstLabelTextColor
        savedState.secondLabelTextColor = secondLabelTextColor
        savedState.firstLabelTextSize = firstLabelTextSize
        savedState.secondLabelTextSize = secondLabelTextSize
        savedState.firstLabelTopPadding = firstLabelTopPadding
        savedState.firstLabelBottomPadding = firstLabelBottomPadding
        savedState.secondLabelTopPadding = secondLabelTopPadding
        savedState.secondLabelBottomPadding = secondLabelBottomPadding

        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)

        doNotInvalidate {
            firstLabelTextColor = savedState.firstLabelTextColor
            secondLabelTextColor = savedState.secondLabelTextColor
            firstLabelTextSize = savedState.firstLabelTextSize
            secondLabelTextSize = savedState.secondLabelTextSize
            firstLabelTopPadding = savedState.firstLabelTopPadding
            firstLabelBottomPadding = savedState.firstLabelBottomPadding
            secondLabelTopPadding = savedState.secondLabelTopPadding
            secondLabelBottomPadding = savedState.secondLabelBottomPadding
        }

        applyTypeface()
        calculateSizes()
    }

    private class SavedState : BaseSavedState {

        internal var firstLabelTextColor: Int = 0
        internal var secondLabelTextColor: Int = 0
        internal var firstLabelTextSize: Int = 0
        internal var secondLabelTextSize: Int = 0
        internal var firstLabelTopPadding: Int = 0
        internal var firstLabelBottomPadding: Int = 0
        internal var secondLabelTopPadding: Int = 0
        internal var secondLabelBottomPadding: Int = 0

        internal constructor(superState: Parcelable?) : super(superState)

        private constructor(input: Parcel) : super(input) {
            firstLabelTextColor = input.readInt()
            secondLabelTextColor = input.readInt()
            firstLabelTextSize = input.readInt()
            secondLabelTextSize = input.readInt()
            firstLabelTopPadding = input.readInt()
            firstLabelBottomPadding = input.readInt()
            secondLabelTopPadding = input.readInt()
            secondLabelBottomPadding = input.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(firstLabelTextColor)
            out.writeInt(secondLabelTextColor)
            out.writeInt(firstLabelTextSize)
            out.writeInt(secondLabelTextSize)
            out.writeInt(firstLabelTopPadding)
            out.writeInt(firstLabelBottomPadding)
            out.writeInt(secondLabelTopPadding)
            out.writeInt(secondLabelBottomPadding)
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
