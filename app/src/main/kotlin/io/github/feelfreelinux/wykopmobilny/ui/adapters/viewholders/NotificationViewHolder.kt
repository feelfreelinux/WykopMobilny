package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.notifications_list_item.view.*
import javax.inject.Inject

class NotificationViewHolder(val view : View) : RecyclerView.ViewHolder(view) {
    @Inject lateinit var notificationLinkHandler : WykopLinkHandlerApi

    init {
        WykopApp.uiInjector.inject(this)
    }

    fun bindNotification(notification: Notification) {
        view.apply {
            // Setup widgets
            body.setText(notification.body.removeHtml(), TextView.BufferType.SPANNABLE)
            date.text = notification.date.toPrettyDate()
            newNotificationMark.isVisible = notification.new

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

            cardView.setOnClickListener {
                notificationLinkHandler.handleUrl(getActivityContext()!!, notification.url)
            }
        }

    }
}