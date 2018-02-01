package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags

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

class HashTagsNotificationsListFragment : BaseNotificationsListFragment() {
    @Inject lateinit var presenter : HashTagsNotificationsListPresenter
    override @Inject lateinit var linkHandler : WykopLinkHandlerApi

    private lateinit var entryFragmentData : DataFragment<PagedDataModel<List<Notification>>>

    companion object {
        val DATA_FRAGMENT_TAG = "NOTIFICATIONS_HASH_TAG_LIST_ACTIVITY"
        fun newInstance() : Fragment {
            return HashTagsNotificationsListFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.hashtags_notifications_title)

        entryFragmentData = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        val pagedModel = entryFragmentData.data
        if (pagedModel != null && pagedModel.model.isNotEmpty()) {
            loadingView.isVisible = false
            presenter.page = pagedModel.page
            notificationAdapter.addData(pagedModel.model, true)
            notificationAdapter.disableLoading()
        } else {
            loadingView.isVisible = true
            onRefresh()
        }
    }

    override fun loadMore() {
        presenter.loadData(false)
    }

    override fun onRefresh() {
        presenter.loadData(true)
    }

    override fun markAsRead() {
        presenter.readNotifications()
    }

    override fun onPause() {
        super.onPause()
        if (isRemoving) supportFragmentManager.removeDataFragment(entryFragmentData)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        entryFragmentData.data =
                PagedDataModel(presenter.page, notificationAdapter.data)
    }

}