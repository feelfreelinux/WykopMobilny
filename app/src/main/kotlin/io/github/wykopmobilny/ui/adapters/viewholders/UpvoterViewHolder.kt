package io.github.wykopmobilny.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import io.github.wykopmobilny.databinding.ConversationListItemBinding
import io.github.wykopmobilny.models.dataclass.Upvoter
import io.github.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.wykopmobilny.utils.api.getGroupColor
import io.github.wykopmobilny.utils.toPrettyDate

class UpvoterViewHolder(private val binding: ConversationListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindView(upvoter: Upvoter) {
        binding.authorAvatarView.setAuthor(upvoter.author)
        binding.userNameTextView.apply {
            text = upvoter.author.nick
            setTextColor(context.getGroupColor(upvoter.author.group))
        }
        binding.entryDateTextView.text = upvoter.date.toPrettyDate()
        binding.root.setOnClickListener { it.context.startActivity(ProfileActivity.createIntent(it.context, upvoter.author.nick)) }
    }
}
