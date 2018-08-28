package io.github.feelfreelinux.wykopmobilny.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.EndlessProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.models.dataclass.NotificationHeader
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ObservedTagResponse
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.NotificationHeaderViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.NotificationViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.ObservedTagViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Inject

class NotificationsListAdapter @Inject constructor(val navigatorApi: NewNavigatorApi, val linkHandlerApi: WykopLinkHandlerApi) : EndlessProgressAdapter<androidx.recyclerview.widget.RecyclerView.ViewHolder, Notification>() {
    var itemClickListener : (Int) -> Unit = {}
    val newData: List<Notification?>
        get() = dataset.filter { it?.visible ?: true || it is NotificationHeader }

    companion object {
        const val TYPE_HEADER = 2123
        const val TYPE_ITEM = 2124
    }

    override fun getViewType(position: Int): Int {
        return if (newData[position] is NotificationHeader) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    val collapseListener : (Boolean, String) -> Unit = {
        visibility, tagStr ->

        for (notif in dataset.filter { it?.tag == tagStr }) {
            notif?.visible = visibility
        }

        notifyDataSetChanged()
    }

    override fun constructViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> NotificationHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.hashtag_notification_header_list_item, parent , false), navigatorApi, collapseListener)
            else -> NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notifications_list_item, parent, false), linkHandlerApi)
        }
    }

    override fun bindHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NotificationViewHolder -> holder.bindNotification(newData[position]!!)
            is NotificationHeaderViewHolder -> holder.bindView(newData[position] as NotificationHeader)
        }
    }

    override fun getItemCount(): Int = newData.size
    fun updateNotification(notification : Notification) {
        val position = dataset.indexOf(notification)
        dataset[position] = notification
        notifyItemChanged(position)
    }
}