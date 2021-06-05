package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.EndlessProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.models.dataclass.NotificationHeader
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.NotificationHeaderViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.NotificationViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Inject

class NotificationsListAdapter @Inject constructor(
    val navigatorApi: NewNavigatorApi,
    val linkHandlerApi: WykopLinkHandlerApi
) : EndlessProgressAdapter<androidx.recyclerview.widget.RecyclerView.ViewHolder, Notification>() {

    companion object {
        const val TYPE_HEADER = 2123
        const val TYPE_ITEM = 2124
    }

    var itemClickListener: (Int) -> Unit = {}
    private val items: List<Notification?>
        get() = dataset.filter { it?.visible ?: true || it is NotificationHeader }

    private val updateHeader: (String) -> Unit = { tag ->
        (dataset.find { it is NotificationHeader && it.tag == tag } as? NotificationHeader)?.apply {
            notificationsCount -= 1
            notifyItemChanged(items.indexOf(this))
        }
    }

    private val collapseListener: (Boolean, String) -> Unit = { visibility, tagStr ->
        dataset
            .filter { it?.tag == tagStr }
            .forEach {
                it?.visible = visibility
            }
        notifyDataSetChanged()
    }

    override fun getViewType(position: Int) = when {
        items[position] is NotificationHeader -> TYPE_HEADER
        else -> TYPE_ITEM
    }

    fun collapseAll() {
        dataset.forEach { it?.visible = false }
        notifyDataSetChanged()
    }

    fun expandAll() {
        dataset.forEach { it?.visible = true }
        notifyDataSetChanged()
    }

    override fun constructViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> NotificationHeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.hashtag_notification_header_list_item,
                    parent,
                    false
                ),
                navigatorApi, collapseListener
            )
            else -> NotificationViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.notifications_list_item, parent, false),
                linkHandlerApi,
                updateHeader
            )
        }
    }

    override fun bindHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NotificationViewHolder -> holder.bindNotification(items[position]!!)
            is NotificationHeaderViewHolder -> holder.bindView(items[position] as NotificationHeader)
        }
    }

    override fun getItemCount(): Int = items.size
}
