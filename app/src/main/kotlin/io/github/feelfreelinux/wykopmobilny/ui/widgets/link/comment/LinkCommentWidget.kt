package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment

import android.content.Context
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.link_comment_layout.view.*

class LinkCommentWidget(context: Context, attrs: AttributeSet) : BaseLinkCommentWidget(context, attrs), LinkCommentView {
    override fun bindViews() {
        commentContent = commentContentTextView
        commentImageView = wykopEmbedView
        replyButton = replyTextView
        authorBadgeStrip = authorBadgeStripView
        collapseButton = collapseButtonImageView
        plusButton = plusVoteButton
        minusButton = minusVoteButton
        shareButton = shareTextView
        moreOptionsButton = moreOptionsTextView
    }

    init {
        View.inflate(context, R.layout.link_comment_layout, this)
    }

    override fun setLinkCommentData(linkComment: LinkComment, linkPresenter: LinkCommentPresenter, userManager: UserManagerApi, settingsPreferencesApi: SettingsPreferencesApi) {
        super.setLinkCommentData(linkComment, linkPresenter, userManager, settingsPreferencesApi)
        setupHeader()
    }

    private fun setupHeader() {
        comment.author.apply {
            avatarView.setAuthor(this)
            avatarView.setOnClickListener { openProfile(nick) }
            authorTextView.apply {
                text = nick
                setTextColor(context.getGroupColor(group))
                setOnClickListener { openProfile(nick) }
            }
            dateTextView.text = comment.date.replace(" temu", "")
        }
    }
}