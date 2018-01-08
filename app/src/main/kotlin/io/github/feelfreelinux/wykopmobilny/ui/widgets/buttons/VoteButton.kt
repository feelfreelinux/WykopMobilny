package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

@Suppress("LeakingThis")
class VoteButton : TextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.attr.MirkoButtonStyle)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    lateinit var userManager : UserManagerApi
    lateinit var settingsApi : SettingsPreferencesApi

    lateinit var voteListener : () -> Unit
    lateinit var unvoteListener : () -> Unit
    init {
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.voteButtonStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
        setTextColor(ContextCompat.getColorStateList(context, typedValue.resourceId))
    }

    fun setup(userManagerApi: UserManagerApi, settingsPreferencesApi: SettingsPreferencesApi) {
        userManager = userManagerApi
        settingsApi = settingsPreferencesApi
        setLightThemeDrawable()
        setOnClickListener {
            userManager.runIfLoggedIn(context) {
                isEnabled = false
                if (isSelected) unvoteListener()
                else voteListener()
            }
        }
    }

    fun setLightThemeDrawable() {
        if (isSelected) {
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_plus_activ, 0, 0, 0);
        } else {
            if (settingsApi.useDarkTheme) {
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_plus, 0, 0, 0);
            } else {
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_plus_light, 0, 0, 0);
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
        set(value) {
            isSelected = value
            setLightThemeDrawable()
        }

    fun showErrorDialog(e : Throwable) {
        context.showExceptionDialog(e)
    }
}