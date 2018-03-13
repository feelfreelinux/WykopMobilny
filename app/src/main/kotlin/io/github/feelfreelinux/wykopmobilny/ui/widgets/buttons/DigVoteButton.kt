package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext

class DigVoteButton : VoteButton {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.attr.MirkoButtonStyle)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.voteButtonStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
        setTextColor(ContextCompat.getColorStateList(context, typedValue.resourceId))
    }

    override fun setLightThemeDrawable() {
        if (isSelected) {
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buttontoolbar_wykop_activ, 0, 0, 0);
        } else {
            val typedArray = context.obtainStyledAttributes(arrayOf(
                    R.attr.wypokDrawable).toIntArray())
            setCompoundDrawablesWithIntrinsicBounds(typedArray.getDrawable(0), null, null, null)
            typedArray.recycle()

        }
    }

    fun setVoteState(voteState : String?) {
        when (voteState) {
            "dig" -> {
                isButtonSelected = true
                setBackgroundColor(ContextCompat.getColor(context, R.color.plusPressedColor))
            }
            "bury" -> {
                isButtonSelected = true
                setBackgroundColor(ContextCompat.getColor(context, R.color.minusPressedColor))
            }
            else -> {
                isButtonSelected = false
                setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            }
        }
    }
}