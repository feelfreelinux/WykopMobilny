package io.github.feelfreelinux.wykopmobilny.ui.fragments.notifications

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification


interface NotificationsView : BaseView {
    fun updateNotification(notification : Notification)
    fun disableLoading()
    fun addItems(items : List<Notification>, shouldRefresh : Boolean = false)
}