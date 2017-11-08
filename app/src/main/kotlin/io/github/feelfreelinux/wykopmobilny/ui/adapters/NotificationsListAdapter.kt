package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.NotificationViewHolder

class NotificationsListAdapter(val notificationItemClickListener : (Int) -> Unit) : BaseProgressAdapter<NotificationViewHolder, Notification>() {
    override fun createViewHolder(parent: ViewGroup): NotificationViewHolder
            = NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notifications_list_item, parent, false))

    override fun bindHolder(holder: NotificationViewHolder, position: Int) {
        holder.bindNotification(data[position], { notificationItemClickListener(position) })
    }
}
