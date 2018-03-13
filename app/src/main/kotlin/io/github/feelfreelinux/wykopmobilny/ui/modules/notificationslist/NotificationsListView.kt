package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification

interface NotificationsListView : BaseView {
    fun addNotifications(notifications : List<Notification>, shouldClearAdapter : Boolean)
    fun disableLoading()
    fun showReadToast()
}