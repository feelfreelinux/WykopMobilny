package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.EndlessProgressAdapter
import io.github.feelfreelinux.wykopmobilny.base.adapter.SimpleBaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.NotificationViewHolder

class NotificationsListAdapter(val notificationItemClickListener : (Int) -> Unit) : EndlessProgressAdapter<NotificationViewHolder, Notification>() {
    override fun getViewType(position: Int): Int {
        return 1337
    }

    override fun constructViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder =
            NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notifications_list_item, parent, false))

    override fun bindHolder(holder: NotificationViewHolder, position: Int) {
        holder.bindNotification(data[position], { notificationItemClickListener(position) })
    }
}
