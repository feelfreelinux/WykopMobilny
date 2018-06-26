package io.github.feelfreelinux.wykopmobilny.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.EndlessProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.NotificationViewHolder
import javax.inject.Inject

class NotificationsListAdapter @Inject constructor() : EndlessProgressAdapter<androidx.recyclerview.widget.RecyclerView.ViewHolder, Notification>() {
    var itemClickListener : (Int) -> Unit = {}
    override fun getViewType(position: Int): Int = 2121

    override fun constructViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return   NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notifications_list_item, parent, false))
    }

    override fun bindHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NotificationViewHolder -> holder.bindNotification(data[position], itemClickListener)
        }
    }

    fun updateNotification(notification : Notification) {
        val position = data.indexOf(notification)
        dataset[position] = notification
        notifyItemChanged(position)
    }
}