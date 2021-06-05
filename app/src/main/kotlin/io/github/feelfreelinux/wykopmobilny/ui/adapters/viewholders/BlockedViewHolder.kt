package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.feelfreelinux.wykopmobilny.databinding.BlockedEntryViewBinding
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.layoutInflater
import kotlinx.android.extensions.LayoutContainer

class BlockedViewHolder(
    val binding: BlockedEntryViewBinding,
    val blockListener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root), LayoutContainer {

    companion object {
        fun inflateView(parent: ViewGroup, blockListener: (Int) -> Unit) =
            BlockedViewHolder(BlockedEntryViewBinding.inflate(parent.layoutInflater, parent, false), blockListener)
    }

    override val containerView = binding.root

    fun bindView(entry: Entry) {
        showText("wpis", entry.author)

        containerView.setOnClickListener {
            entry.isBlocked = false
            blockListener(adapterPosition)
        }
    }

    fun bindView(comment: EntryComment) {
        showText("komentarz", comment.author)

        containerView.setOnClickListener {
            comment.isBlocked = false
            blockListener(adapterPosition)
        }
    }

    fun bindView(link: Link) {
        showText("link", link.author)

        containerView.setOnClickListener {
            link.isBlocked = false
            blockListener(adapterPosition)
        }
    }

    fun bindView(linkComment: LinkComment) {
        showText("komentarz", linkComment.author)

        containerView.setOnClickListener {
            linkComment.isBlocked = false
            blockListener(adapterPosition)
        }
    }

    fun showText(type: String, author: Author?) {
        val text = SpannableString("Pokaż ukryty $type od @" + author?.nick)
        text.setSpan(
            ForegroundColorSpan(getGroupColor(author?.group ?: 1)),
            text.length - ((author?.nick?.length ?: 0) + 1),
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.showHiddenTextView.setText(text, TextView.BufferType.SPANNABLE)
    }
}
