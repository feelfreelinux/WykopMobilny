package io.github.wykopmobilny.ui.modules.notificationslist.notification

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import io.github.wykopmobilny.models.dataclass.Notification
import io.github.wykopmobilny.models.fragments.DataFragment
import io.github.wykopmobilny.models.fragments.PagedDataModel
import io.github.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.wykopmobilny.models.fragments.removeDataFragment
import io.github.wykopmobilny.ui.adapters.NotificationsListAdapter
import io.github.wykopmobilny.ui.modules.notificationslist.BaseNotificationsListFragment
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi
import javax.inject.Inject

class NotificationsListFragment : BaseNotificationsListFragment() {

    companion object {
        const val DATA_FRAGMENT_TAG = "NOTIFICATIONS_LIST_ACTIVITY"
        fun newInstance() = NotificationsListFragment()
    }

    @Inject
    override lateinit var linkHandler: WykopLinkHandlerApi

    @Inject
    override lateinit var notificationAdapter: NotificationsListAdapter

    @Inject
    lateinit var presenter: NotificationsListPresenter

    private lateinit var entryFragmentData: DataFragment<PagedDataModel<List<Notification>>>

    override fun loadMore() = presenter.loadData(false)

    override fun onRefresh() = presenter.loadData(true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.subscribe(this)

        entryFragmentData = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        if (entryFragmentData.data != null && entryFragmentData.data!!.model.isNotEmpty()) {
            binding.loadingView.isVisible = false
            presenter.page = entryFragmentData.data!!.page
            notificationAdapter.addData(entryFragmentData.data!!.model, true)
            notificationAdapter.disableLoading()
        } else {
            binding.loadingView.isVisible = true
            onRefresh()
        }
    }

    override fun markAsRead() = presenter.readNotifications()

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        if (isRemoving) supportFragmentManager.removeDataFragment(entryFragmentData)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        entryFragmentData.data = PagedDataModel(presenter.page, notificationAdapter.data)
    }

    override fun showTooManyNotifications() {
        // Do nothing
    }
}
