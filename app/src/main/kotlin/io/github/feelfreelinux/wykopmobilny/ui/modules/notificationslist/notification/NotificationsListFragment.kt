package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification

import android.os.Bundle
import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.BaseNotificationsListFragment
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.activity_notifications_list.*
import javax.inject.Inject

class NotificationsListFragment : BaseNotificationsListFragment() {
    private lateinit var entryFragmentData : DataFragment<PagedDataModel<List<Notification>>>
    override @Inject lateinit var linkHandler : WykopLinkHandlerApi

    companion object {
        val DATA_FRAGMENT_TAG = "NOTIFICATIONS_LIST_ACTIVITY"
        fun newInstance() : Fragment {
            return NotificationsListFragment()
        }
    }

    override fun loadMore() {
        presenter.loadData(false)
    }

    override fun onRefresh() {
        presenter.loadData(true)
    }

    @Inject lateinit var presenter : NotificationsListPresenter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.notifications_title)

        entryFragmentData = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        if (entryFragmentData.data != null && entryFragmentData.data!!.model.isNotEmpty()) {
            loadingView.isVisible = false
            presenter.page = entryFragmentData.data!!.page
            notificationAdapter.addData(entryFragmentData.data!!.model, true)
            notificationAdapter.disableLoading()
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
        if (isRemoving) supportFragmentManager.removeDataFragment(entryFragmentData)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        entryFragmentData.data = PagedDataModel(presenter.page, notificationAdapter.data)
    }
}