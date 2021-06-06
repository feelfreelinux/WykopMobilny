package io.github.wykopmobilny.ui.widgets.buttons

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.core.content.ContextCompat
import io.github.wykopmobilny.R
import io.github.wykopmobilny.utils.getActivityContext

class PlusVoteButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.MirkoButtonStyle
) : VoteButton(context, attrs, defStyleAttr) {

    init {
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.voteButtonStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
        setTextColor(ContextCompat.getColorStateList(context, typedValue.resourceId))
    }

    override fun setLightThemeDrawable() {
        if (isSelected) {
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buttontoolbar_plus_activ, 0, 0, 0)
        } else {
            val typedArray = context.obtainStyledAttributes(
                arrayOf(R.attr.plusDrawable).toIntArray()
            )
            setCompoundDrawablesWithIntrinsicBounds(typedArray.getDrawable(0), null, null, null)
            typedArray.recycle()
        }
    }
}
