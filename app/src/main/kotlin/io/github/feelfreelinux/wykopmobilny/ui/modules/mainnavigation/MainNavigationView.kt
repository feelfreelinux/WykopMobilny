package io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.WykopMobilnyUpdate
import io.github.feelfreelinux.wykopmobilny.models.scraper.Blacklist

interface MainNavigationView : BaseView {
    fun showUsersMenu(value : Boolean)
    fun restartActivity()
    fun showNotImplementedToast()
    fun showNotificationsCount(notifications : Int)
    fun showHashNotificationsCount(hashNotifications : Int)
    fun checkUpdate(wykopMobilnyUpdate: WykopMobilnyUpdate)
    fun importBlacklist(blacklist : Blacklist)
}