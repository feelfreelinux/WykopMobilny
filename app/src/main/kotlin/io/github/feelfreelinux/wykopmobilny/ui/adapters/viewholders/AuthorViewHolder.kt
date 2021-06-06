package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import io.github.feelfreelinux.wykopmobilny.databinding.ConversationListItemBinding
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor

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
