package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.notifications_list_item.view.*

class NotificationViewHolder(
    val view: View,
    val linkHandlerApi: WykopLinkHandlerApi,
    val updateHeader: (String) -> Unit
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    fun bindNotification(notification: Notification) {
        view.apply {
            // Setup widgets
            body.setText(notification.body.removeHtml(), TextView.BufferType.SPANNABLE)
            date.text = notification.date.toPrettyDate()
            unreadLine.isVisible = notification.new
            unreadMark.isVisible = notification.new
            unreadDotMark.isVisible = notification.new

            if (notification.author != null) {
                avatarView.isVisible = true
                avatarView.setAuthor(notification.author)

                if (notification.author.nick.isNotEmpty()) {
                    // Color nickname
                    val nickName = notification.body.substringBefore(" ") // nick
                    val spannable = body.text as Spannable
                    spannable.setSpan(
                        ForegroundColorSpan(getGroupColor(notification.author.group)),
                        0,
                        nickName.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            } else {
                avatarView.isVisible = false
            }

            notificationItem.setOnClickListener {
                if (notification.new) {
                    updateHeader(notification.tag)
                }
                notification.new = false
                unreadLine.isVisible = false
                unreadMark.isVisible = false
                unreadDotMark.isVisible = false
                linkHandlerApi.handleUrl(notification.url ?: "https://www.wykop.pl/ludzie/feelfree")
            }
        }
    }
}
