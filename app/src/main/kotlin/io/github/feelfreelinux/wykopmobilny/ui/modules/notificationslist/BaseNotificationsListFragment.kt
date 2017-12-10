package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.ui.adapters.NotificationsListAdapter
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.InfiniteScrollListener
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.activity_notifications_list.*

abstract class BaseNotificationsListFragment : BaseFragment(), NotificationsListView, SwipeRefreshLayout.OnRefreshListener {
    val notificationAdapter by lazy { NotificationsListAdapter({ onNotificationClicked(it) }) }
    abstract var linkHandler : WykopLinkHandlerApi

    abstract fun markAsRead()

    abstract fun loadMore()

    private fun onNotificationClicked(position : Int) {
        val notification = notificationAdapter.data[position]
        notification.new = false
        notificationAdapter.notifyDataSetChanged()
        notification.url?.let {
            linkHandler.handleUrl(activity, notification.url, true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.activity_notifications_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        swiperefresh.setOnRefreshListener(this)

        recyclerView.apply {
            prepare()
            adapter = notificationAdapter
            clearOnScrollListeners()
            setInfiniteScrollListener()
        }
        swiperefresh.isRefreshing = false
        super.onActivityCreated(savedInstanceState)
    }

    private fun setInfiniteScrollListener() {
        recyclerView?.apply {
            clearOnScrollListeners()
            addOnScrollListener(InfiniteScrollListener(
                    { loadMore() }, layoutManager as LinearLayoutManager))
        }
    }

    override fun addNotifications(notifications: List<Notification>, shouldClearAdapter : Boolean) {
        if (shouldClearAdapter) setInfiniteScrollListener()
        if (notifications.isNotEmpty()) {
            loadingView.isVisible = false
            swiperefresh.isRefreshing = false
            notificationAdapter.addData(notifications, shouldClearAdapter)
        }
    }

    override fun showReadToast() {
        onRefresh()
        Toast.makeText(context, R.string.read_notifications, Toast.LENGTH_SHORT).show()
    }

    override fun disableLoading() {
        recyclerView.clearOnScrollListeners()
        notificationAdapter.disableLoading()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notification_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.markAsRead -> markAsRead()
        }
        return super.onOptionsItemSelected(item)
    }

}