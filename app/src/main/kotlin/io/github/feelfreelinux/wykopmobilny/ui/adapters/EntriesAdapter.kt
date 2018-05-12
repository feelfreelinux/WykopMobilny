package io.github.feelfreelinux.wykopmobilny.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.base.adapter.EndlessProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry

class EntriesAdapter : EndlessProgressAdapter<androidx.recyclerview.widget.RecyclerView.ViewHolder, Entry>() {
    companion object {
        val TYPE_SURVEY = 121
        val TYPE_EMBED = 122
        val TYPE_NORMAL = 123
        val TYPE_EMBED_SURVEY = 124
        val TYPE_BLOCKED = 124
    }

    override fun getViewType(position: Int): Int {
        val entry = data[position]
        return if (entry.isBlocked) TYPE_BLOCKED
        else if (entry.embed != null && entry.survey == null) TYPE_EMBED_SURVEY
        else if (entry.embed == null && entry.survey != null) TYPE_SURVEY
        else if (entry.embed != null && entry.survey == null) TYPE_EMBED
        else TYPE_NORMAL
    }

    override fun constructViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}