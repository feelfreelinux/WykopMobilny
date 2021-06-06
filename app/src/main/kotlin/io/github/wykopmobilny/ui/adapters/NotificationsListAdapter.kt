package io.github.wykopmobilny.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.wykopmobilny.base.adapter.EndlessProgressAdapter
import io.github.wykopmobilny.databinding.HashtagNotificationHeaderListItemBinding
import io.github.wykopmobilny.databinding.NotificationsListItemBinding
import io.github.wykopmobilny.models.dataclass.Notification
import io.github.wykopmobilny.models.dataclass.NotificationHeader
import io.github.wykopmobilny.ui.adapters.viewholders.NotificationHeaderViewHolder
import io.github.wykopmobilny.ui.adapters.viewholders.NotificationViewHolder
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.layoutInflater
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi
import javax.inject.Inject

class NotificationsListAdapter @Inject constructor(
    val navigatorApi: NewNavigatorApi,
    val linkHandlerApi: WykopLinkHandlerApi
) : EndlessProgressAdapter<RecyclerView.ViewHolder, Notification>() {

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

    override fun getViewType(position: Int) = if (items[position] is NotificationHeader) TYPE_HEADER
    else TYPE_ITEM

    fun collapseAll() {
        dataset.forEach { it?.visible = false }
        notifyDataSetChanged()
    }

    fun expandAll() {
        dataset.forEach { it?.visible = true }
        notifyDataSetChanged()
    }

    override fun constructViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            TYPE_HEADER -> NotificationHeaderViewHolder(
                HashtagNotificationHeaderListItemBinding.inflate(parent.layoutInflater, parent, false),
                navigatorApi,
                collapseListener,
            )
            TYPE_ITEM -> NotificationViewHolder(
                NotificationsListItemBinding.inflate(parent.layoutInflater, parent, false),
                linkHandlerApi,
                updateHeader,
            )
            else -> error("unsupported type")
        }

    override fun bindHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NotificationViewHolder -> holder.bindNotification(items[position]!!)
            is NotificationHeaderViewHolder -> holder.bindView(items[position] as NotificationHeader)
        }
    }

    override fun getItemCount(): Int = items.size
}
