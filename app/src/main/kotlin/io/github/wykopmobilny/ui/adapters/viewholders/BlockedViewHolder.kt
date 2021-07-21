package io.github.wykopmobilny.ui.adapters.viewholders

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.wykopmobilny.databinding.BlockedEntryViewBinding
import io.github.wykopmobilny.models.dataclass.Author
import io.github.wykopmobilny.models.dataclass.Entry
import io.github.wykopmobilny.models.dataclass.EntryComment
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.models.dataclass.LinkComment
import io.github.wykopmobilny.utils.api.getGroupColor
import io.github.wykopmobilny.utils.layoutInflater

class BlockedViewHolder(
    private val binding: BlockedEntryViewBinding,
    private val blockListener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun inflateView(parent: ViewGroup, blockListener: (Int) -> Unit) =
            BlockedViewHolder(BlockedEntryViewBinding.inflate(parent.layoutInflater, parent, false), blockListener)
    }

    fun bindView(entry: Entry) {
        showText("wpis", entry.author)

        itemView.setOnClickListener {
            entry.isBlocked = false
            blockListener(bindingAdapterPosition)
        }
    }

    fun bindView(comment: EntryComment) {
        showText("komentarz", comment.author)

        itemView.setOnClickListener {
            comment.isBlocked = false
            blockListener(bindingAdapterPosition)
        }
    }

    fun bindView(link: Link) {
        showText("link", link.author)

        itemView.setOnClickListener {
            link.isBlocked = false
            blockListener(bindingAdapterPosition)
        }
    }

    fun bindView(linkComment: LinkComment) {
        showText("komentarz", linkComment.author)

        itemView.setOnClickListener {
            linkComment.isBlocked = false
            blockListener(bindingAdapterPosition)
        }
    }

    private fun showText(type: String, author: Author?) {
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
