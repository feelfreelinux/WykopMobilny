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
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.NotificationsListAdapter
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.EndlessScrollListener
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.ILoadMore
import kotlinx.android.synthetic.main.activity_notifications_list.*
import kotlinx.android.synthetic.main.toolbar.*

abstract class BaseNotificationsListActivity : BaseActivity(), ILoadMore, NotificationsListView, SwipeRefreshLayout.OnRefreshListener {
    val adapter by lazy { NotificationsListAdapter() }

    private lateinit var entryFragmentData : DataFragment<List<Notification>>

    companion object {
        val DATA_FRAGMENT_TAG = "NOTIFICATIONS_LIST_ACTIVITY"
    }

    abstract fun loadData(page : Int)

    abstract fun markAsRead()

    override fun onLoadMore(page: Int) {
        adapter.isLoading = true
        loadData(page)
    }

    override fun onRefresh() {
        loadData(1)
        endlessScrollListener.resetState()
        adapter.isLoading = false
    }

    lateinit var endlessScrollListener : EndlessScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications_list)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView.adapter = adapter
        recyclerView.prepare()

        endlessScrollListener = EndlessScrollListener(this, recyclerView.layoutManager as LinearLayoutManager)
        recyclerView.addOnScrollListener(endlessScrollListener)
        swiperefresh.setOnRefreshListener(this)
        entryFragmentData = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)

        swiperefresh.isRefreshing = false

        if (entryFragmentData.data != null && entryFragmentData.data!!.isNotEmpty()) {
            loadingView.isVisible = false
            adapter.addDataToAdapter(entryFragmentData.data!!, true)
        } else {
            loadingView.isVisible = true
            onRefresh()
        }
    }

    override fun addNotifications(notifications: List<Notification>, shouldClearAdapter : Boolean) {
        if (notifications.isNotEmpty()) {
            recyclerView.post {
                loadingView.isVisible = false
                swiperefresh.isRefreshing = false
                adapter.addDataToAdapter(notifications, shouldClearAdapter)
            }
        }
    }

    override fun showReadToast() {
        onRefresh()
        Toast.makeText(this, R.string.read_notifications, Toast.LENGTH_SHORT).show()
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

    override fun onPause() {
        super.onPause()
        if (isFinishing) supportFragmentManager.removeDataFragment(entryFragmentData)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        entryFragmentData.data = adapter.dataset.filterNotNull()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        entryFragmentData.data?.apply {
            adapter.addDataToAdapter(this, true)
        }
    }

}