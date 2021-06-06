package io.github.wykopmobilny.ui.adapters.viewholders

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import io.github.wykopmobilny.databinding.ConversationListItemBinding
import io.github.wykopmobilny.models.dataclass.Author
import io.github.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.wykopmobilny.utils.api.getGroupColor

class AuthorViewHolder(private val binding: ConversationListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindView(author: Author) {
        binding.authorAvatarView.setAuthor(author)
        binding.userNameTextView.apply {
            text = author.nick
            setTextColor(context.getGroupColor(author.group))
        }
        binding.entryDateTextView.isVisible = false
        binding.root.setOnClickListener { it.context.startActivity(ProfileActivity.createIntent(it.context, author.nick)) }
    }
}
