package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import kotlinx.android.synthetic.main.author_header_layout.view.*

class AuthorHeaderView : ConstraintLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.author_header_layout, this) }

    fun setAuthorData(author : Author, date : String, app : String? = null) {
        author.apply {
            userNameTextView.apply {
                text = nick
                setOnClickListener { openProfile(author.nick) }
                setTextColor(context.getGroupColor(group))
            }
            authorAvatarView.setAuthor(this)
            authorAvatarView.setOnClickListener {
                openProfile(author.nick)
            }
            setOnClickListener { openProfile(author.nick) }
            entryDateTextView.text = date

            app?.let {
                entryDateTextView.text = context.getString(R.string.date_with_user_app, date, app)
            }
        }
    }

    fun openProfile(username : String) {
        getActivityContext()!!.startActivity(ProfileActivity.createIntent(getActivityContext()!!, username))
    }

    fun disableClickListeners() {
        authorAvatarView.setOnClickListener(null)
        setOnClickListener(null)
    }

}