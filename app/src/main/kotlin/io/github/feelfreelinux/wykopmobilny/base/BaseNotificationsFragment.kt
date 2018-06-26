package io.github.feelfreelinux.wykopmobilny.base

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.*
import io.github.feelfreelinux.wykopmobilny.ui.adapters.NotificationsListAdapter
import io.github.feelfreelinux.wykopmobilny.ui.fragments.notifications.NotificationsView
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.entries_fragment.*
import kotlinx.android.synthetic.main.search_empty_view.*
import javax.inject.Inject

open class BaseNotificationsFragment : BaseFragment(), NotificationsView {
    var showSearchEmptyView: Boolean
        get() = searchEmptyView.isVisible
        set(value) {
            searchEmptyView.isVisible = value
            if (value) {
                notificationsAdapter.addData(emptyList(), true)
                notificationsAdapter.disableLoading()
            }
        }

    open var loadDataListener : (Boolean) -> Unit = {}

    @Inject
    lateinit var notificationsAdapter : NotificationsListAdapter


    // Inflate view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.entries_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notificationsAdapter.loadNewDataListener = { loadDataListener(false) }

        // Setup views
        swipeRefresh.setOnRefreshListener({ loadDataListener(false) })
        recyclerView.run {
            prepare()
            adapter = notificationsAdapter
        }

        loadingView.isVisible = true
    }

    /**
     * Removes progressbar from adapter
     */
    override fun disableLoading() {
        notificationsAdapter.disableLoading()
    }

    /**
     * Use this function to add items to EntriesFragment
     * @param items List of entries to add
     * @param shouldRefresh If true adapter will refresh its data with provided items. False by default
     */
    override fun addItems(items : List<Notification>, shouldRefresh : Boolean) {
        notificationsAdapter.addData(items, shouldRefresh)
        swipeRefresh.isRefreshing = false
        loadingView.isVisible = false

        // Scroll to top if refreshing list
        if (shouldRefresh) {
            (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).scrollToPositionWithOffset(0, 0)
        }
    }

    override fun updateNotification(notification : Notification) {
        notificationsAdapter.updateNotification(notification)
    }
}