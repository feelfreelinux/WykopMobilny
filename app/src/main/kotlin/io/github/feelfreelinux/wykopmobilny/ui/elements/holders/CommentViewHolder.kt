package io.github.feelfreelinux.wykopmobilny.ui.elements.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Comment
import kotlinx.android.synthetic.main.comment_list_item.view.*


class CommentViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bindView(comment : Comment) {
        view.comment.setCommentData(comment)
    }
}