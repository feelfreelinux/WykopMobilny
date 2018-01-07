package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment.CommentPresenter
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.comment_list_item.view.*

class CommentViewHolder(val view: View, private val addReceiverListener : (Author) -> Unit, val settingsPreferencesApi: SettingsPreferencesApi, val userManagerApi: UserManagerApi, val commentPresenter: CommentPresenter) : RecyclerView.ViewHolder(view) {
    fun bindView(comment : EntryComment, isAuthorComment: Boolean, commentId : Int?) {
        view.entryComment.addReceiverListener = addReceiverListener
        view.entryComment.setCommentData(comment, userManagerApi, settingsPreferencesApi, commentPresenter)
        view.entryComment.setStyleForComment(isAuthorComment, commentId ?: -1)
    }
}