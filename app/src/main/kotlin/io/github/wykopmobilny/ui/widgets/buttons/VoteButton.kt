package io.github.wykopmobilny.ui.widgets.buttons

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import io.github.wykopmobilny.R
import io.github.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.wykopmobilny.utils.usermanager.UserManagerApi

abstract class VoteButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.MirkoButtonStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

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
