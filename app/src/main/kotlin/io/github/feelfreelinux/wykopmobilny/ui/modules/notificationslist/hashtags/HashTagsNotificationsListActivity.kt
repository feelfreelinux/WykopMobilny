package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.BaseNotificationsListActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.NotificationsListActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.NotificationsListPresenter
import javax.inject.Inject

fun Context.startHashTagsNotificationListActivity() {
    val intent = Intent(this, HashTagsNotificationsListActivity::class.java)
    startActivity(intent)
}

class HashTagsNotificationsListActivity : BaseNotificationsListActivity() {
    @Inject lateinit var presenter : HashTagsNotificationsListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)
        super.onCreate(savedInstanceState)
        supportActionBar?.setTitle(R.string.notifications_title)
    }

    override fun loadData(page: Int) {
        presenter.loadData(page)
    }

    override fun markAsRead() {
        presenter.readNotifications()
    }

}