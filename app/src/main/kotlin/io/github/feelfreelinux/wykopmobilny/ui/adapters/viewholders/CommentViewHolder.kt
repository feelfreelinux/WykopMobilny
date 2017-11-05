package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Comment
import kotlinx.android.synthetic.main.comment_list_item.view.*

class CommentViewHolder(val view: View, private val addReceiverListener : (Author) -> Unit) : RecyclerView.ViewHolder(view) {
    fun bindView(comment : Comment, isAuthorComment: Boolean) {
        view.comment.addReceiverListener = addReceiverListener
        view.comment.setCommentData(comment)
        view.comment.setStyleForComment(isAuthorComment)
    }
}