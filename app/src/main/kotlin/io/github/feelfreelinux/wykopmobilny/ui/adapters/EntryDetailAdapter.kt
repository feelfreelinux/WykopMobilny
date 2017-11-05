package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.CommentViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.EntryViewHolder

class EntryDetailAdapter(private val context: Context,
                         private val addReceiverListener: (Author) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val ENTRY_HOLDER = 0
        private const val COMMENT_HOLDER = 1
    }

    var entry: Entry? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder?.itemViewType == ENTRY_HOLDER) {
            (holder as EntryViewHolder).bindView(entry!!)
        } else {
            val comment = entry!!.comments[position - 1]
            val entryAuthor = entry?.author
            val commentAuthor = comment.author
            (holder as CommentViewHolder).bindView(comment, entryAuthor == commentAuthor)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ENTRY_HOLDER
        else COMMENT_HOLDER
    }

    override fun getItemCount(): Int {
        entry?.comments?.let {
            return it.size + 1
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ENTRY_HOLDER -> EntryViewHolder(LayoutInflater.from(context).inflate(R.layout.entry_list_item, parent, false))
            else -> CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.comment_list_item, parent, false), addReceiverListener)
        }
    }
}
