package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import kotlinx.android.synthetic.main.notifications_list_item.view.*

class NotificationViewHolder(val view : View) : RecyclerView.ViewHolder(view) {

    fun bindNotification(notification: Notification, itemClickListener: () -> Unit) {
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
                    spannable.setSpan(ForegroundColorSpan(getGroupColor(notification.author.group)), 0, nickName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            } else {
                avatarView.isVisible = false
            }

            notificationItem.setOnClickListener {
                itemClickListener()
            }
        }

    }
}