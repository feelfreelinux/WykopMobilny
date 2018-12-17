package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import kotlinx.android.synthetic.main.author_header_layout.view.*

class AuthorHeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.constraintlayout.widget.ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.author_header_layout, this)
    }

    fun setAuthorData(author: Author, date: String, app: String? = null) {
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
            patronBadgeTextView.isVisible = author.badge != null
            author.badge?.let {
                patronBadgeTextView.text = author.badge!!.text
            }
            userNameTextView.setOnClickListener { openProfile(author.nick) }
            entryDateTextView.text = date

            app?.let {
                entryDateTextView.text = context.getString(R.string.date_with_user_app, date, app)
            }
        }
    }

    private fun openProfile(username: String) {
        val context = getActivityContext()!!
        context.startActivity(ProfileActivity.createIntent(context, username))
    }

}