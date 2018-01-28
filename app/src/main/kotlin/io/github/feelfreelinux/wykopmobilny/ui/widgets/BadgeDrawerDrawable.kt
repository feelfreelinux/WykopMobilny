package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.support.v7.graphics.drawable.DrawerArrowDrawable

class BadgeDrawerDrawable(context: Context) : DrawerArrowDrawable(context) {

    private val backgroundPaint: Paint = Paint()
    private val textPaint: Paint
    var text: String? = null
        set(text) {
            if (this.text != text) {
                field = text
                invalidateSelf()
            }
        }
    var isEnabled = true
        set(enabled) {
            if (this.isEnabled != enabled) {
                field = enabled
                invalidateSelf()
            }
        }

    var backgroundColor: Int
        get() = backgroundPaint.color
        set(color) {
            if (backgroundPaint.color != color) {
                backgroundPaint.color = color
                invalidateSelf()
            }
        }

    var textColor: Int
        get() = textPaint.color
        set(color) {
            if (textPaint.color != color) {
                textPaint.color = color
                invalidateSelf()
            }
        }

    init {

        backgroundPaint.color = Color.RED
        backgroundPaint.isAntiAlias = true

        textPaint = Paint()
        textPaint.color = Color.WHITE
        textPaint.isAntiAlias = true
        textPaint.typeface = Typeface.DEFAULT_BOLD
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = SIZE_FACTOR * intrinsicHeight
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (!isEnabled) {
            return
        }

        if (this.text.isNullOrEmpty()) {
            return
        }

        val bounds = bounds
        val x = (1 - HALF_SIZE_FACTOR) * bounds.width()
        val y = HALF_SIZE_FACTOR * bounds.height()
        canvas.drawCircle(x, y, SIZE_FACTOR * bounds.width(), backgroundPaint)

        val textBounds = Rect()
        textPaint.getTextBounds(this.text, 0, this.text!!.length, textBounds)
        canvas.drawText(this.text!!, x, y + textBounds.height() / 2, textPaint)
    }

    companion object {
        // Fraction of the drawable's intrinsic size we want the badge to be.
        private val SIZE_FACTOR = .35f
        private val HALF_SIZE_FACTOR = SIZE_FACTOR / 2
    }
}