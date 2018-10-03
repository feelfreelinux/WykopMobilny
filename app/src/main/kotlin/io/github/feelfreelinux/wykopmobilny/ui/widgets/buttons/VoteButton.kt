package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

@Suppress("LeakingThis")
abstract class VoteButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.MirkoButtonStyle
) : TextView(context, attrs, defStyleAttr) {

    var voteCount: Int
        get() = text.toString().toInt()
        set(value) {
            text = context.getString(R.string.votes_count, value)
        }

    var isButtonSelected: Boolean
        get() = isSelected
        set(value) {
            isSelected = value
            setLightThemeDrawable()
        }

    lateinit var userManager: UserManagerApi
    lateinit var voteListener: () -> Unit
    lateinit var unvoteListener: () -> Unit

    abstract fun setLightThemeDrawable()

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

    fun showErrorDialog(e: Throwable) = context.showExceptionDialog(e)
}