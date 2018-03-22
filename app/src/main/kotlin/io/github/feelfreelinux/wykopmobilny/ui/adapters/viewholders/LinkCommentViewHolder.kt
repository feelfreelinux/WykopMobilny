package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.view.View
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment.LinkCommentPresenter
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.link_comment_layout.view.*
import kotlinx.android.synthetic.main.link_comment_list_item.view.*

class LinkCommentViewHolder(val view: View, val linkCommentReplyListener : (LinkComment) -> Unit, val linkCommentPresenter: LinkCommentPresenter, val userManagerApi: UserManagerApi, val settingsPreferencesApi: SettingsPreferencesApi) : RecyclableViewHolder(view) {
    fun bindView(comment : LinkComment, isUserAuthor: Boolean, highlightCommentId: Int) {
        view.replyTextView.setOnClickListener { linkCommentReplyListener.invoke(comment) }
        view.linkComment.setLinkCommentData(comment, linkCommentPresenter, userManagerApi, settingsPreferencesApi)
        view.linkComment.setStyleForComment(isUserAuthor, highlightCommentId) }

    override fun cleanRecycled() {
        view.apply {
            GlideApp.with(this).clear(view.linkComment.wykopEmbedView)
            view.replyTextView.setOnClickListener(null)
        }
    }
}