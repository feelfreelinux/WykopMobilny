package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.BaseNotificationsListActivity
import javax.inject.Inject

fun Context.startNotificationsListActivity() {
    val intent = Intent(this, NotificationsListActivity::class.java)
    startActivity(intent)
}
class NotificationsListActivity : BaseNotificationsListActivity() {
    @Inject lateinit var presenter : NotificationsListPresenter

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

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }
}