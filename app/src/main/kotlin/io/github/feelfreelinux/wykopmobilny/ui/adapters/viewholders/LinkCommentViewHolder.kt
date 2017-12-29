package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import kotlinx.android.synthetic.main.link_comment_layout.view.*

class LinkCommentViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bindView(comment : LinkComment) {
        view.entryContentTextView.text = comment.body
    }
}