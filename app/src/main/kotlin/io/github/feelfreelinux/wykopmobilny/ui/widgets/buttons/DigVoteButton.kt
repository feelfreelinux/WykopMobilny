package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import io.github.feelfreelinux.wykopmobilny.R


class DigVoteButton : VoteButton {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.attr.MirkoButtonStyle)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setLightThemeDrawable() {

    }

    fun setVoteState(voteState : String?) {
        when (voteState) {
            "dig" -> {
                val color = ContextCompat.getColor(context, R.color.plusPressedColor)
                setTextColor(color)
                setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_buttontoolbar_wykop_digged), null, null, null)
            }
            "bury" -> {
                isButtonSelected = true
                val color = ContextCompat.getColor(context, R.color.minusPressedColor)
                setTextColor(color)
                setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_buttontoolbar_wykop_buried), null, null, null)
            }
            else -> {
                val typedValue = TypedValue()
                context.theme.resolveAttribute(R.attr.textColorButtonToolbar, typedValue, true)
                val color = typedValue.data
                isButtonSelected = false
                setTextColor(color)
                val typedArray = context.obtainStyledAttributes(arrayOf(R.attr.wypokDrawable).toIntArray())
                setCompoundDrawablesWithIntrinsicBounds(typedArray.getDrawable(0), null, null, null)
                typedArray.recycle()
            }
        }
    }
}