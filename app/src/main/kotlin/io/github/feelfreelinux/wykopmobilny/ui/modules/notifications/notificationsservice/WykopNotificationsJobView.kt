package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification

interface WykopNotificationsJobView : BaseView {
    fun showNotification(notification: Notification)
    fun showNotificationsCount(count: Int)
    fun cancelNotification()
}
