package io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.base

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.android.appKodein
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs.showExceptionDialog

abstract class BaseVoteButton : TextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val kodein = LazyKodein(appKodein)

    abstract val presenter : BaseVoteButtonPresenter

    init {
        setBackgroundResource(R.drawable.button_background_state_list)
        setOnClickListener {
            if (isSelected) presenter.vote()
            else presenter.unvote()
        }
    }

    var voteCount : Int
        get() = text.toString().toInt()
        set(value) {
            text =  value.toString()
        }

    var isButtonSelected: Boolean
        get() = isSelected
        set(value) { isSelected = value }

    fun showErrorDialog(e : Exception) = context.showExceptionDialog(e)
}