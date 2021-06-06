package io.github.wykopmobilny.ui.modules.notifications.notificationsservice

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.Notification

interface WykopNotificationsJobView : BaseView {
    fun showNotification(notification: Notification)
    fun showNotificationsCount(count: Int)
    fun cancelNotification()
}
