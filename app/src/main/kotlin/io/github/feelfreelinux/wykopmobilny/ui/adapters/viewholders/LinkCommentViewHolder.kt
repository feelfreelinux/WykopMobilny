package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment.LinkCommentPresenter
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.link_comment_layout.view.*
import kotlinx.android.synthetic.main.link_comment_list_item.view.*

class LinkCommentViewHolder(val view: View, val linkCommentReplyListener : (LinkComment) -> Unit, val linkCommentPresenter: LinkCommentPresenter, val userManagerApi: UserManagerApi, val settingsPreferencesApi: SettingsPreferencesApi) : RecyclableViewHolder(view) {
    fun bindView(comment : LinkComment, isUserAuthor: Boolean, highlightCommentId: Int) {
        view.replyTextView.setOnClickListener { linkCommentReplyListener.invoke(comment) }
        view.linkComment.setLinkCommentData(comment, linkCommentPresenter, userManagerApi, settingsPreferencesApi)
        view.linkComment.setStyleForComment(isUserAuthor, highlightCommentId)
        view.linkComment.isVisible = !comment.isBlocked
        view.showHiddenTextView.isVisible = comment.isBlocked

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
            GlideApp.with(this).clear(view.linkComment.wykopEmbedView)
            view.replyTextView.setOnClickListener(null)
        }
    }
}