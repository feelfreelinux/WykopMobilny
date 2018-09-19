package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.ui.adapters.NotificationsListAdapter
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.activity_notifications_list.*

abstract class BaseNotificationsListFragment : BaseFragment(), NotificationsListView,
    androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {

    abstract var notificationAdapter: NotificationsListAdapter
    abstract var linkHandler: WykopLinkHandlerApi

    abstract fun markAsRead()

    abstract fun loadMore()

    private fun onNotificationClicked(position: Int) {
        val notification = notificationAdapter.data[position]
        notification.new = false
        notificationAdapter.notifyDataSetChanged()
        notification.url?.let {
            printout(notification.url)
            linkHandler.handleUrl(notification.url, true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.activity_notifications_list, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        swiperefresh.setOnRefreshListener(this)

        notificationAdapter.loadNewDataListener = {
            loadMore()
        }

        notificationAdapter.itemClickListener = { onNotificationClicked(it) }
        recyclerView?.apply {
            prepare()
            adapter = notificationAdapter
        }
        swiperefresh?.isRefreshing = false
        super.onActivityCreated(savedInstanceState)
    }

    override fun addNotifications(notifications: List<Notification>, shouldClearAdapter: Boolean) {
        loadingView?.isVisible = false
        swiperefresh?.isRefreshing = false
        notificationAdapter.addData(if (!shouldClearAdapter) notifications.filterNot { notificationAdapter.data.contains(it) } else notifications,
            shouldClearAdapter)
    }

    override fun showReadToast() {
        onRefresh()
        Toast.makeText(context, R.string.read_notifications, Toast.LENGTH_SHORT).show()
    }

    override fun disableLoading() = notificationAdapter.disableLoading()

}