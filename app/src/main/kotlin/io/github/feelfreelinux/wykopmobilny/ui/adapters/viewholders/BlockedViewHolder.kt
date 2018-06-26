package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.*
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.blocked_entry_view.*

class BlockedViewHolder(override val containerView: View, val blockListener : (Int) -> Unit) : androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView), LayoutContainer {
    companion object {
        fun inflateView(parent: ViewGroup, blockListener: (Int) -> Unit): BlockedViewHolder {
            return BlockedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.blocked_entry_view, parent, false), blockListener)
        }
    }


    fun bindView(entry : Entry) {
        showText("wpis", entry.author)

        containerView.setOnClickListener {
            entry.isBlocked = false
            blockListener(adapterPosition)
        }

    }

    fun bindView(comment : EntryComment) {
        showText("komentarz", comment.author)

        containerView.setOnClickListener {
            comment.isBlocked = false
            blockListener(adapterPosition)
        }
    }

    fun bindView(link : Link) {
        showText("link", link.author)

        containerView.setOnClickListener {
            link.isBlocked = false
            blockListener(adapterPosition)
        }
    }

    fun bindView(linkComment : LinkComment) {
        showText("komentarz", linkComment.author)

        containerView.setOnClickListener {
            linkComment.isBlocked = false
            blockListener(adapterPosition)
        }
    }

    fun showText(type : String, author : Author?) {
        val text = SpannableString("Pokaż ukryty $type od @" + author?.nick)
        text.setSpan(ForegroundColorSpan(getGroupColor(author?.group ?: 1)), text.length-((author?.nick?.length ?: 0 )+1), text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        showHiddenTextView.setText(text, TextView.BufferType.SPANNABLE)
    }
}