package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment.CommentPresenter
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.comment_list_item.view.*
import kotlinx.android.synthetic.main.entry_comment_layout.view.*

class CommentViewHolder(val view: View, private val addReceiverListener : (Author) -> Unit, val quoteListener : (EntryComment) -> Unit, val settingsPreferencesApi: SettingsPreferencesApi, val userManagerApi: UserManagerApi, val commentPresenter: CommentPresenter) : RecyclableViewHolder(view) {
    fun bindView(comment : EntryComment, isAuthorComment: Boolean, commentId : Int?, shouldEnableClickListener : Boolean = false) {
        view.entryComment.shouldEnableClickListener = shouldEnableClickListener
        view.entryComment.addReceiverListener = addReceiverListener
        view.entryComment.setCommentData(comment, userManagerApi, settingsPreferencesApi, commentPresenter)
        view.entryComment.setStyleForComment(isAuthorComment, commentId ?: -1)
        view.entryComment.quoteCommentListener = quoteListener

        view.entryComment.isVisible = !comment.isBlocked
        view.showHiddenTextView.isVisible = comment.isBlocked

        if (comment.isBlocked) {
            val text = SpannableString("Pokaż ukryty komentarz od @" + comment.author.nick)
            text.setSpan(ForegroundColorSpan(getGroupColor(comment.author.group)), text.length-(comment.author.nick.length+1), text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            view.showHiddenTextView.setText(text, TextView.BufferType.SPANNABLE)
        }

        view.showHiddenTextView.setOnClickListener {
            comment.isBlocked = false
            view.entryComment.isVisible = true
            view.showHiddenTextView.isVisible = false
        }
    }

    override fun cleanRecycled() {
        view.apply {
            GlideApp.with(this).clear(view.entryComment.entryImageView)
            view.entryComment.shouldEnableClickListener = false
            view.entryComment.entryContentTextView.text = null
            view.entryComment.addReceiverListener = null
            view.entryComment.quoteCommentListener = null
            view.entryComment.setOnClickListener(null)
        }
    }
}