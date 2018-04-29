package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment.LinkCommentPresenter
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.top_link_comment_layout.view.*
import kotlinx.android.synthetic.main.link_top_comment_list_item.view.*

class TopLinkCommentViewHolder(val view: View, val linkCommentReplyListener : (LinkComment) -> Unit, val collapseListener : (Boolean, Int) -> Unit, val linkCommentPresenter: LinkCommentPresenter, val userManagerApi: UserManagerApi, val settingsPreferencesApi: SettingsPreferencesApi) : RecyclableViewHolder(view) {
    fun bindView(comment : LinkComment, isUserAuthor: Boolean, highlightCommentId: Int) {
        view.linkComment.collapseListener = collapseListener
        view.replyTextView.setOnClickListener { linkCommentReplyListener.invoke(comment) }
        view.linkComment.setLinkCommentData(comment, linkCommentPresenter, userManagerApi, settingsPreferencesApi)
        view.linkComment.setStyleForComment(isUserAuthor, highlightCommentId)
        view.messageTextView.isVisible = comment.isCollapsed && comment.childCommentCount > 0
        view.messageTextView.text = view.resources.getString(R.string.hidden_replies, comment.childCommentCount)
        view.messageTextView.setOnClickListener {
            view.linkComment.expandComment()
        }
        view.linkComment.showHiddenCommentsCountCard = { view.messageTextView.isVisible = it }

        view.linkComment.isVisible = !comment.isBlocked
        view.showHiddenTextView.isVisible = comment.isBlocked && !settingsPreferencesApi.hideBlacklistedViews

        if (comment.isBlocked) {
            val text = SpannableString("Pokaż ukryty komentarz od @" + comment.author.nick)
            text.setSpan(ForegroundColorSpan(getGroupColor(comment.author.group)), text.length-(comment.author.nick.length+1), text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            view.showHiddenTextView.setText(text, TextView.BufferType.SPANNABLE)
        }

        view.showHiddenTextView.setOnClickListener {
            comment.isBlocked = false
            view.linkComment.isVisible = true
            view.showHiddenTextView.isVisible = false
        }
    }

    override fun cleanRecycled() {
        view.apply {
            GlideApp.with(this).clear(view.linkComment.commentImageView)
            view.linkComment.collapseListener = {_, _ ->}
            view.replyTextView.setOnClickListener(null)
            view.messageTextView.isVisible = false
            view.messageTextView.text = null
            view.messageTextView.setOnClickListener(null)
        }
    }
}