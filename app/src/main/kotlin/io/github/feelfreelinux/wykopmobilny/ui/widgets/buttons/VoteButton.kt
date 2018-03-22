package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

@Suppress("LeakingThis")
abstract class VoteButton : TextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.attr.MirkoButtonStyle)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    lateinit var userManager : UserManagerApi

    lateinit var voteListener : () -> Unit
    lateinit var unvoteListener : () -> Unit

    fun setup(userManagerApi: UserManagerApi) {
        userManager = userManagerApi
        setLightThemeDrawable()
        setOnClickListener {
            userManager.runIfLoggedIn(context) {
                isEnabled = false
                if (isSelected) unvoteListener()
                else voteListener()
            }
        }
    }

    abstract fun setLightThemeDrawable()

    var voteCount : Int
        get() = text.toString().toInt()
        set(value) {
            text =  context.getString(R.string.votes_count, value)
        }

    var isButtonSelected: Boolean
        get() = isSelected
        set(value) {
            isSelected = value
            setLightThemeDrawable()
        }

    fun showErrorDialog(e : Throwable) {
        context.showExceptionDialog(e)
    }
}