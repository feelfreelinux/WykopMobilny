package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment.LinkCommentPresenter
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.link_comment_layout.view.*
import kotlinx.android.synthetic.main.link_comment_list_item.view.*

class LinkCommentViewHolder(val view: View, val linkCommentReplyListener : (LinkComment) -> Unit, val collapseListener : (Boolean, Int) -> Unit, val linkCommentPresenter: LinkCommentPresenter, val userManagerApi: UserManagerApi, val settingsPreferencesApi: SettingsPreferencesApi) : RecyclableViewHolder(view) {
    fun bindView(comment : LinkComment, isUserAuthor: Boolean, highlightCommentId: Int) {
        view.linkComment.collapseListener = collapseListener
        view.replyTextView.setOnClickListener { linkCommentReplyListener.invoke(comment) }
        view.linkComment.setLinkCommentData(comment, linkCommentPresenter, userManagerApi, settingsPreferencesApi)
        view.linkComment.setStyleForComment(isUserAuthor, highlightCommentId)
        view.hiddenRepliesView.isVisible = comment.isCollapsed && comment.childCommentCount > 0
        view.messageTextView.text = view.resources.getString(R.string.hidden_replies, comment.childCommentCount)
        view.hiddenRepliesView.setOnClickListener {
            view.linkComment.expandComment()
        }
        view.linkComment.showHiddenCommentsCountCard = { view.hiddenRepliesView.isVisible = it }
    }

    override fun cleanRecycled() {
        view.apply {
            GlideApp.with(this).clear(view.linkComment.commentImageView)
            view.linkComment.collapseListener = {_, _ ->}
            view.replyTextView.setOnClickListener(null)
            view.hiddenRepliesView.isVisible = false
            view.messageTextView.text = null
            view.hiddenRepliesView.setOnClickListener(null)
        }
    }
}