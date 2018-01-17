package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment.LinkCommentPresenter
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.link_comment_layout.view.*
import kotlinx.android.synthetic.main.link_comment_list_item.view.*

class LinkCommentViewHolder(val view: View, val linkCommentReplyListener : (LinkComment) -> Unit, val linkCommentPresenter: LinkCommentPresenter, val userManagerApi: UserManagerApi, val settingsPreferencesApi: SettingsPreferencesApi) : RecyclerView.ViewHolder(view) {
    fun bindView(comment : LinkComment, isUserAuthor: Boolean, highlightCommentId: Int) {
        val margin = if (comment.id != comment.parentId) 8f else 0f
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin, view.resources.displayMetrics)
        view.setPadding(px.toInt(), 0, 0, 0)
        view.replyTextView.setOnClickListener { linkCommentReplyListener.invoke(comment) }
        view.linkComment.setLinkCommentData(comment, linkCommentPresenter, userManagerApi, settingsPreferencesApi)
        view.linkComment.setStyleForComment(isUserAuthor, highlightCommentId)
    }
}