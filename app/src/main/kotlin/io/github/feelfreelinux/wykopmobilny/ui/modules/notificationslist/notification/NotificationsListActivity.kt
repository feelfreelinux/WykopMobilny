package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.BaseNotificationsListActivity
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import kotlinx.android.synthetic.main.activity_notifications_list.*
import javax.inject.Inject

fun Context.startNotificationsListActivity() {
    val intent = Intent(this, NotificationsListActivity::class.java)
    startActivity(intent)
}
class NotificationsListActivity : BaseNotificationsListActivity() {
    private lateinit var entryFragmentData : DataFragment<PagedDataModel<List<Notification>>>

    companion object {
        val DATA_FRAGMENT_TAG = "NOTIFICATIONS_LIST_ACTIVITY"
    }

    override fun loadMore() {
        presenter.loadData(false)
    }

    override fun onRefresh() {
        presenter.loadData(true)
    }

    @Inject lateinit var presenter : NotificationsListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)
        super.onCreate(savedInstanceState)
        supportActionBar?.setTitle(R.string.notifications_title)

        entryFragmentData = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        if (entryFragmentData.data != null && entryFragmentData.data!!.model.isNotEmpty()) {
            loadingView.isVisible = false
            presenter.page = entryFragmentData.data!!.page
            notificationAdapter.addData(entryFragmentData.data!!.model, true)
        } else {
            loadingView.isVisible = true
            onRefresh()
        }
    }

    override fun markAsRead() {
        presenter.readNotifications()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) supportFragmentManager.removeDataFragment(entryFragmentData)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        entryFragmentData.data = PagedDataModel(presenter.page, notificationAdapter.data)
    }
}