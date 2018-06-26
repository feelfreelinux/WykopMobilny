package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import kotlinx.android.synthetic.main.conversation_list_item.view.*

class AuthorViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    fun bindView(author : Author) {
        view.authorAvatarView.setAuthor(author)
        view.userNameTextView.apply {
            text = author.nick
            setTextColor(context.getGroupColor(author.group))
        }
        view.entryDateTextView.isVisible = false
        view.setOnClickListener { view.context.startActivity(ProfileActivity.createIntent(view.context, author.nick)) }
    }
}