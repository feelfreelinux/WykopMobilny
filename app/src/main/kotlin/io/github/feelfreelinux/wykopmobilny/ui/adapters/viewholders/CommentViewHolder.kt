package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment.CommentPresenter
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
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