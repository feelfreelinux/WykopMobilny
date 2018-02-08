package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Upvoter
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import kotlinx.android.synthetic.main.conversation_list_item.view.*

class UpvoterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bindView(upvoter: Upvoter) {
        view.authorHeaderView.setAuthorData(upvoter.author, upvoter.date.toPrettyDate())
    }
}