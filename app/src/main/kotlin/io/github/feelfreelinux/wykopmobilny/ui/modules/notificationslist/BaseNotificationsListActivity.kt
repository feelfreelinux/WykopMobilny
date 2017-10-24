package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.ui.adapters.NotificationsListAdapter
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.InfiniteScrollListener
import kotlinx.android.synthetic.main.activity_notifications_list.*
import kotlinx.android.synthetic.main.toolbar.*

abstract class BaseNotificationsListActivity : BaseActivity(), NotificationsListView, SwipeRefreshLayout.OnRefreshListener {
    val notificationAdapter by lazy { NotificationsListAdapter() }

    abstract fun markAsRead()

    abstract fun loadMore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications_list)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        swiperefresh.setOnRefreshListener(this)

        recyclerView.apply {
            prepare()
            adapter = notificationAdapter
            clearOnScrollListeners()
            addOnScrollListener(InfiniteScrollListener({ loadMore() }, layoutManager as LinearLayoutManager))
        }
        swiperefresh.isRefreshing = false
    }

    override fun addNotifications(notifications: List<Notification>, shouldClearAdapter : Boolean) {
        if (notifications.isNotEmpty()) {
            recyclerView.post {
                loadingView.isVisible = false
                swiperefresh.isRefreshing = false
                notificationAdapter.addData(notifications, shouldClearAdapter)
            }
        }
    }

    override fun showReadToast() {
        onRefresh()
        Toast.makeText(this, R.string.read_notifications, Toast.LENGTH_SHORT).show()
    }

    override fun disableLoading() {
        notificationAdapter.disableLoading()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.notification_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.markAsRead -> markAsRead()
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}