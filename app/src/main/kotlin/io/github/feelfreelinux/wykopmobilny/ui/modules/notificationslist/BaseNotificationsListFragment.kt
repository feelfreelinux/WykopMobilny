package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.databinding.ActivityNotificationsListBinding
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.ui.adapters.NotificationsListAdapter
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.viewBinding
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

abstract class BaseNotificationsListFragment :
    BaseFragment(R.layout.activity_notifications_list),
    NotificationsListView,
    SwipeRefreshLayout.OnRefreshListener {

    protected val binding by viewBinding(ActivityNotificationsListBinding::bind)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swiperefresh.setOnRefreshListener(this)

        notificationAdapter.loadNewDataListener = {
            loadMore()
        }

        notificationAdapter.itemClickListener = { onNotificationClicked(it) }
        binding.recyclerView.apply {
            prepare()
            adapter = notificationAdapter
        }
        binding.swiperefresh.isRefreshing = false
    }

    override fun addNotifications(notifications: List<Notification>, shouldClearAdapter: Boolean) {
        binding.loadingView.isVisible = false
        binding.swiperefresh.isRefreshing = false
        notificationAdapter.addData(
            if (!shouldClearAdapter) notifications.filterNot { notificationAdapter.data.contains(it) } else notifications,
            shouldClearAdapter
        )
    }

    override fun showReadToast() {
        onRefresh()
        Toast.makeText(context, R.string.read_notifications, Toast.LENGTH_SHORT).show()
    }

    override fun disableLoading() = notificationAdapter.disableLoading()
}
