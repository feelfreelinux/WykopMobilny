package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.core.content.ContextCompat
import io.github.feelfreelinux.wykopmobilny.R

class DigVoteButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.MirkoButtonStyle
) : VoteButton(context, attrs, defStyleAttr) {

    fun setVoteState(voteState: String?) {
        when (voteState) {
            "dig" -> {
                val color = ContextCompat.getColor(context, R.color.plusPressedColor)
                setTextColor(color)
                setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(context, R.drawable.ic_buttontoolbar_wykop_digged),
                    null,
                    null,
                    null
                )
            }
            "bury" -> {
                isButtonSelected = true
                val color = ContextCompat.getColor(context, R.color.minusPressedColor)
                setTextColor(color)
                setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(context, R.drawable.ic_buttontoolbar_wykop_buried),
                    null,
                    null,
                    null
                )
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

    override fun setLightThemeDrawable() {
        // Do nothing
    }
}