package io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation

import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface MainNavigationView : BaseView {
    fun showUsersMenu(value : Boolean)
    fun restartActivity()
    fun showNotImplementedToast()
    fun showNotificationsCount(notifications : Int)
    fun showHashNotificationsCount(hashNotifications : Int)

}