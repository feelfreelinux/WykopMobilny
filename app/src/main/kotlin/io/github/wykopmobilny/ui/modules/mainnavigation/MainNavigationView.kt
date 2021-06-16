package io.github.wykopmobilny.ui.modules.mainnavigation

import io.github.wykopmobilny.base.BaseView

interface MainNavigationView : BaseView {
    fun showUsersMenu(value: Boolean)
    fun restartActivity()
    fun showNotImplementedToast()
    fun showNotificationsCount(notifications: Int)
    fun showHashNotificationsCount(hashNotifications: Int)
}
