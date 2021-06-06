package io.github.wykopmobilny.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import io.github.wykopmobilny.R
import io.github.wykopmobilny.databinding.AuthorHeaderLayoutBinding
import io.github.wykopmobilny.models.dataclass.Author
import io.github.wykopmobilny.models.dataclass.drawBadge
import io.github.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.wykopmobilny.utils.api.getGroupColor
import io.github.wykopmobilny.utils.getActivityContext
import io.github.wykopmobilny.utils.layoutInflater

class AuthorHeaderView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val binding = AuthorHeaderLayoutBinding.inflate(layoutInflater, this)

    fun setAuthorData(author: Author, date: String, app: String? = null) {
        author.apply {
            binding.userNameTextView.apply {
                text = nick
                setOnClickListener { openProfile(author.nick) }
                setTextColor(context.getGroupColor(group))
            }
            binding.authorAvatarView.setAuthor(this)
            binding.authorAvatarView.setOnClickListener { openProfile(author.nick) }
            binding.patronBadgeTextView.isVisible = author.badge != null
            author.badge?.let { badge ->
                try {
                    badge.drawBadge(binding.patronBadgeTextView)
                } catch (exception: Throwable) {
                    Log.w(this::class.simpleName, "Couldn't draw badge", exception)
                }
            }
            binding.userNameTextView.setOnClickListener { openProfile(author.nick) }
            binding.entryDateTextView.text = app?.let { context.getString(R.string.date_with_user_app, date, it) } ?: date
        }
    }

    private fun openProfile(username: String) {
        val context = getActivityContext()!!
        context.startActivity(ProfileActivity.createIntent(context, username))
    }
}
