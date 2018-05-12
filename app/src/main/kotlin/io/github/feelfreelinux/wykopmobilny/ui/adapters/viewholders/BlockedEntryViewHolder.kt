package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.blocked_entry_view.*

class BlockedEntryViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bindView(entry : Entry) {
        showHiddenTextView.text = ""
    }
}