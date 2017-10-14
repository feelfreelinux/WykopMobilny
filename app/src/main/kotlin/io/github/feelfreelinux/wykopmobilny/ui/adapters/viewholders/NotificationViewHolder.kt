package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import kotlinx.android.synthetic.main.notifications_list_item.view.*

class NotificationViewHolder(val view : View) : RecyclerView.ViewHolder(view) {
    val wykopLinkHandler = WykopLinkHandler(view.context)

    fun bindNotification(notification: Notification) {
        view.apply {
            // Setup widgets
            avatarView.setAuthor(notification.author)
            body.prepareBody(notification.body, wykopLinkHandler)
            date.text = notification.date.toPrettyDate()
            newNotificationMark.isVisible = notification.new

            if (notification.author.nick.isNotEmpty()) {
                // Color nickname
                val nickName = notification.body.substringBefore(" ") // nick
                val spannable = body.text as Spannable
                spannable.setSpan(ForegroundColorSpan(getGroupColor(notification.author.group)), 0, nickName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            cardView.setOnClickListener {
                wykopLinkHandler.handleUrl(notification.url)
            }

            // @TODO hacky fix, should be done other way
            body.setOnClickListener {
                cardView.performClick()
            }
        }

    }
}