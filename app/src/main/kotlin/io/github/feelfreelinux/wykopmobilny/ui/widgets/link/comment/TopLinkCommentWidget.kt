package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment

import android.content.Context
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.top_link_comment_layout.view.*


class TopLinkCommentWidget(context: Context, attrs: AttributeSet) : BaseLinkCommentWidget(context, attrs), LinkCommentView {
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
        View.inflate(context, R.layout.top_link_comment_layout, this)
    }

    override fun setLinkCommentData(linkComment: LinkComment, linkPresenter: LinkCommentPresenter, userManager: UserManagerApi, settingsPreferencesApi: SettingsPreferencesApi) {
        super.setLinkCommentData(linkComment, linkPresenter, userManager, settingsPreferencesApi)
        setupHeader()
    }

    private fun setupHeader() {
        authorHeaderView.setAuthorData(comment.author, comment.date, comment.app)
    }
}

