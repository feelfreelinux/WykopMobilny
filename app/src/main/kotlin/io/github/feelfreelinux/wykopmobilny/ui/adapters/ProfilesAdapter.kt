package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.AuthorViewHolder

class ProfilesAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<AuthorViewHolder>() {

    val items = ArrayList<Author>()

    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) =
        holder.bindView(items[position])

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder =
        AuthorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.conversation_list_item, parent, false))
}