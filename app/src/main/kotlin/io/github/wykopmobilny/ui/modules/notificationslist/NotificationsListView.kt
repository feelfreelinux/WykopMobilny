package io.github.wykopmobilny.ui.modules.notificationslist

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.Notification

interface NotificationsListView : BaseView {
    fun addNotifications(notifications: List<Notification>, shouldClearAdapter: Boolean)
    fun disableLoading()
    fun showReadToast()
    fun showTooManyNotifications()
}
