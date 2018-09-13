package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.*
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.base.BaseNavigationView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntriesFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationInterface
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.entries_fragment.*
import javax.inject.Inject

class HotFragment : BaseFragment(), BaseNavigationView, HotView {
    val navigation by lazy { activity as MainNavigationInterface }
    @Inject lateinit var presenter : HotPresenter
    @Inject lateinit var settingsPreferences : SettingsPreferencesApi
    @Inject lateinit var navigatorApi : NavigatorApi
    @Inject lateinit var userManagerApi : UserManagerApi
    val fab by lazy { navigation.floatingButton }
    val entriesFragment by lazy { childFragmentManager.findFragmentById(R.id.entriesFragment) as EntriesFragment }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        fab.setOnClickListener {
            navigatorApi.openAddEntryActivity(activity!!)
        }
        navigation.activityToolbar.overflowIcon = ContextCompat.getDrawable(activity!!, R.drawable.ic_hot)
        return inflater.inflate(R.layout.hot_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        presenter.period = settingsPreferences.hotEntriesScreen ?: "24"
        entriesFragment.loadDataListener = { presenter.loadData(it) }
        presenter.loadData(true)
    }

    override fun disableLoading() {
        entriesFragment.disableLoading()
    }

    companion object {
        fun newInstance() : androidx.fragment.app.Fragment {
            return HotFragment()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun showHotEntries(entries: List<Entry>, isRefreshing: Boolean) {
        entriesFragment.addItems(entries, isRefreshing)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.hot_period, menu)
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.period6  -> {
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
            R.id.newest   -> {
                presenter.period = "newest"
                navigation.activityToolbar.setTitle(R.string.newest_entries)
            }
        }

        swipeRefresh.isRefreshing = true
        presenter.loadData(true)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //TODO: get this callback work on hot/mywkop to update edited entry
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