package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.elements.holders.CommentViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.elements.holders.EntryViewHolder

class EntryDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var entry : Entry? = null
    private val ENTRY_HOLDER = 0
    private val COMMENT_HOLDER = 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder?.itemViewType) {
            ENTRY_HOLDER -> (holder as EntryViewHolder).bindView(entry!!)
            COMMENT_HOLDER -> (holder as CommentViewHolder).bindView(entry!!.comments[position - 1])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ENTRY_HOLDER
        else COMMENT_HOLDER
    }

    override fun getItemCount() : Int {
        entry?.comments?.let {
            return it.size + 1
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ENTRY_HOLDER -> EntryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.entry_list_item, parent, false))
            else -> CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.comment_list_item, parent, false))
        }
    }
}
