package io.github.wykopmobilny.ui.modules.mikroblog.feed.hot

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.wykopmobilny.R
import io.github.wykopmobilny.base.BaseFragment
import io.github.wykopmobilny.base.BaseNavigationView
import io.github.wykopmobilny.models.dataclass.Entry
import io.github.wykopmobilny.ui.fragments.entries.EntriesFragment
import io.github.wykopmobilny.ui.modules.NavigatorApi
import io.github.wykopmobilny.ui.modules.input.BaseInputActivity
import io.github.wykopmobilny.ui.modules.mainnavigation.MainNavigationInterface
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class HotFragment : BaseFragment(R.layout.hot_fragment), BaseNavigationView, HotView {

    companion object {
        fun newInstance() = HotFragment()
    }

    @Inject
    lateinit var presenter: HotPresenter

    @Inject
    lateinit var settingsPreferences: SettingsPreferencesApi

    @Inject
    lateinit var navigatorApi: NavigatorApi

    @Inject
    lateinit var userManagerApi: UserManagerApi

    private val fab by lazy { navigation.floatingButton }
    private val entriesFragment by lazy { childFragmentManager.findFragmentById(R.id.entriesFragment) as EntriesFragment }
    private val navigation by lazy { activity as MainNavigationInterface }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener { navigatorApi.openAddEntryActivity(requireActivity()) }
        navigation.activityToolbar.overflowIcon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_hot)

        presenter.subscribe(this)
        presenter.period = settingsPreferences.hotEntriesScreen ?: "24"
        entriesFragment.loadDataListener = { presenter.loadData(it) }
        presenter.loadData(true)
    }

    override fun disableLoading() = entriesFragment.disableLoading()

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }

    override fun showHotEntries(entries: List<Entry>, isRefreshing: Boolean) =
        entriesFragment.addItems(entries, isRefreshing)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.hot_period, menu)
        navigation.activityToolbar.setTitle(
            when (presenter.period) {
                "24" -> R.string.period24
                "12" -> R.string.period12
                "6" -> R.string.period6
                "active" -> R.string.active_entries
                else -> R.string.newest_entries
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.period6 -> {
                presenter.period = "6"
                navigation.activityToolbar.setTitle(R.string.period6)
            }
            R.id.period12 -> {
                presenter.period = "12"
                navigation.activityToolbar.setTitle(R.string.period12)
            }
            R.id.period24 -> {
                presenter.period = "24"
                navigation.activityToolbar.setTitle(R.string.period24)
            }
            R.id.active -> {
                presenter.period = "active"
                navigation.activityToolbar.setTitle(R.string.active_entries)
            }
            R.id.newest -> {
                presenter.period = "newest"
                navigation.activityToolbar.setTitle(R.string.newest_entries)
            }
        }

        view?.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)?.isRefreshing = true
        presenter.loadData(true)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // TODO: get this callback work on hot/mywkop to update edited entry
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                BaseInputActivity.EDIT_ENTRY -> {
                    val entryBody = data?.getStringExtra("entryBody")
                    if (entryBody != null) {
                        presenter.loadData(true)
                    }
                }
            }
        }
    }
}
