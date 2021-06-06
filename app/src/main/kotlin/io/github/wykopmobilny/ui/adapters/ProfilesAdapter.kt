package io.github.wykopmobilny.ui.adapters

import android.view.ViewGroup
import io.github.wykopmobilny.databinding.ConversationListItemBinding
import io.github.wykopmobilny.models.dataclass.Author
import io.github.wykopmobilny.ui.adapters.viewholders.AuthorViewHolder
import io.github.wykopmobilny.utils.layoutInflater

class ProfilesAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<AuthorViewHolder>() {

    val items = ArrayList<Author>()

    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) =
        holder.bindView(items[position])

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder =
        AuthorViewHolder(ConversationListItemBinding.inflate(parent.layoutInflater, parent, false))
}
