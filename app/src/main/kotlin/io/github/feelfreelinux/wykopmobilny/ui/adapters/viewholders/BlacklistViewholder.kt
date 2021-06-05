package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.blacklist_blocked_item.view.*

class BlacklistViewholder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    fun bind(blockedName: String, unblockListener: (String) -> Unit) {
        view.blockedTextView.text = blockedName
        view.unlockImageView.setOnClickListener {
            unblockListener(blockedName)
        }
    }
}
