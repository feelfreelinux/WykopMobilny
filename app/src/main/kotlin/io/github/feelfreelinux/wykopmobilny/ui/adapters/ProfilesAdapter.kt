package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.SimpleBaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Conversation
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.AuthorViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.ConversationViewHolder

class ProfilesAdapter : RecyclerView.Adapter<AuthorViewHolder>() {
    val dataset = ArrayList<Author>()

    override fun onBindViewHolder(holder: AuthorViewHolder?, position: Int) {
        holder?.bindView(dataset[position])
    }

    override fun getItemCount(): Int = dataset.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder =
            AuthorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.conversation_list_item, parent, false))
}