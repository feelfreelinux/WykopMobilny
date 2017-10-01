package io.github.feelfreelinux.wykopmobilny.ui.elements.buttons.vote.base

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferences
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

@Suppress("LeakingThis")
abstract class BaseVoteButton : TextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.attr.MirkoButtonStyle)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Inject lateinit var userManager : UserManagerApi

    abstract fun vote()
    abstract fun unvote()

    init {
        WykopApp.uiInjector.inject(this)

        setBackgroundResource(R.drawable.button_background_state_list)
        setOnClickListener {
            userManager.runIfLoggedIn(context) {
                if (isSelected) unvote()
                else vote()
            }
        }
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