package io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.base

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs.showExceptionDialog

@Suppress("LeakingThis")
abstract class BaseVoteButton : TextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    abstract fun vote()
    abstract fun unvote()

    init {
        setBackgroundResource(R.drawable.button_background_state_list)
        setOnClickListener {
            if (isSelected) unvote()
            else vote()
        }
        setTypeface(null, Typeface.BOLD)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        val dpAsPixels = (4 * resources.displayMetrics.density + 0.5f).toInt()
        setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels)
        gravity = Gravity.CENTER
    }

    var voteCount : Int
        get() = text.toString().toInt()
        set(value) {
            text =  context.getString(R.string.votes_count, value)
        }

    var isButtonSelected: Boolean
        get() = isSelected
        set(value) { isSelected = value }

    fun showErrorDialog(e : Throwable) = context.showExceptionDialog(e)
}