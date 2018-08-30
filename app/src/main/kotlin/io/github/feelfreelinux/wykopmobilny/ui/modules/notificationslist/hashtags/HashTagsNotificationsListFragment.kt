package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.NotificationsListAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.BaseNotificationsListFragment
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.activity_notifications_list.*
import javax.inject.Inject

class HashTagsNotificationsListFragment : BaseNotificationsListFragment() {
    @Inject lateinit var presenter : HashTagsNotificationsListPresenter
    @Inject
    override lateinit var linkHandler : WykopLinkHandlerApi
    @Inject
    override lateinit var notificationAdapter : NotificationsListAdapter

    private lateinit var entryFragmentData : DataFragment<PagedDataModel<List<Notification>>>

    @Inject
    lateinit var settingsApi: SettingsPreferencesApi

    companion object {
        val DATA_FRAGMENT_TAG = "NOTIFICATIONS_HASH_TAG_LIST_ACTIVITY"
        fun newInstance() : Fragment {
            return HashTagsNotificationsListFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        super.onCreate(savedInstanceState)
        entryFragmentData = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        val pagedModel = entryFragmentData.data
        if (pagedModel != null && pagedModel.model.isNotEmpty()) {
            loadingView?.isVisible = false
            presenter.page = pagedModel.page
            notificationAdapter.addData(pagedModel.model, true)
            notificationAdapter.disableLoading()
        } else {
            loadingView?.isVisible = true
            onRefresh()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.hashtag_notification_menu, menu)
        menu.findItem(R.id.groupNotifications).isChecked = settingsApi.groupNotifications
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.groupNotifications -> {
                item.isChecked = !item.isChecked
                settingsApi.groupNotifications = item.isChecked
                onRefresh()
                return true
            }

            R.id.collapseAll -> {
                notificationAdapter.collapseAll()
                Toast.makeText(context, "Schowano wszystkie powiadomienia", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun loadMore() {
        if (settingsApi.groupNotifications) {
            presenter.loadAllNotifications(false)
        } else {
            presenter.loadData(false)
        }
    }

    override fun onRefresh() {
        if (settingsApi.groupNotifications) {
            presenter.loadAllNotifications(true)
        } else {
            presenter.loadData(true)
        }
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