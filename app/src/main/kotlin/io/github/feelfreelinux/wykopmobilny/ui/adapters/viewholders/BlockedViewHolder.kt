package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import kotlinx.android.extensions.LayoutContainer

class BlockedViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bindView(entry : Entry) {
        //showHiddenTextView.text = ""
    }

    fun bindView(comment : EntryComment) {
        //showHiddenTextView.text = ""
    }
}