package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.blocked_entry_view.*

class BlockedViewHolder(override val containerView: View, val blockListener : (Int) -> Unit) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    companion object {
        fun inflateView(parent: ViewGroup, blockListener: (Int) -> Unit): BlockedViewHolder {
            return BlockedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.entry_list_item, parent, false), blockListener)
        }
    }
    fun bindView(entry : Entry) {
        //showHiddenTextView.text = ""
        showHiddenTextView.setOnClickListener {
            entry.isBlocked = false
            blockListener(adapterPosition)
        }    }

    fun bindView(comment : EntryComment) {
        //showHiddenTextView.text = ""
        showHiddenTextView.setOnClickListener {
            comment.isBlocked = false
            blockListener(adapterPosition)
        }    }

    fun bindView(link : Link) {
        showHiddenTextView.setOnClickListener {
            link.isBlocked = false
            blockListener(adapterPosition)
        }
    }

    fun bindView(linkComment : LinkComment) {
        showHiddenTextView.setOnClickListener {
            linkComment.isBlocked = false
            blockListener(adapterPosition)
        }
    }
}