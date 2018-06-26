package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Upvoter
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import kotlinx.android.synthetic.main.conversation_list_item.view.*

class UpvoterViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    fun bindView(upvoter: Upvoter) {
        view.authorAvatarView.setAuthor(upvoter.author)
        view.userNameTextView.apply {
            text = upvoter.author.nick
            setTextColor(context.getGroupColor(upvoter.author.group))
        }
        view.entryDateTextView.text = upvoter.date.toPrettyDate()
        view.setOnClickListener { view.context.startActivity(ProfileActivity.createIntent(view.context, upvoter.author.nick)) }
    }
}