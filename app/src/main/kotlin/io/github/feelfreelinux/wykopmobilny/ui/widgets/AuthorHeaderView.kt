package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import kotlinx.android.synthetic.main.author_header_layout.view.*
import javax.inject.Inject

class AuthorHeaderView : ConstraintLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Inject lateinit var settingsApi : SettingsPreferencesApi

    init {
        View.inflate(context, R.layout.author_header_layout, this)
        WykopApp.uiInjector.inject(this)
    }

    fun setAuthorData(author : Author, date : String, app : String? = null) {
        author.apply {
            userNameTextView.apply {
                text = nick
                setTextColor(getGroupColor(group, settingsApi.useDarkTheme))
            }
            authorAvatarView.setAuthor(this)
            val prettyDate = if (date.isNotEmpty()) date.toPrettyDate() else ""
            entryDateTextView.text = prettyDate

            app?.let {
                entryDateTextView.text = context.getString(R.string.date_with_user_app, prettyDate, app)
            }
        }
    }

}