package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.base

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

@Suppress("LeakingThis")
abstract class BaseVoteButton : TextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.attr.MirkoButtonStyle)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Inject lateinit var userManager : UserManagerApi
    @Inject lateinit var settingsApi : SettingsPreferencesApi

    abstract fun vote()
    abstract fun unvote()

    init {
        WykopApp.uiInjector.inject(this)
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.voteButtonStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
        setTextColor(ContextCompat.getColorStateList(context, typedValue.resourceId))
        setLightThemeDrawable()
        setOnClickListener {
            userManager.runIfLoggedIn(context) {
                if (isSelected) unvote()
                else vote()
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

    fun showErrorDialog(e : Throwable) = context.showExceptionDialog(e)
}