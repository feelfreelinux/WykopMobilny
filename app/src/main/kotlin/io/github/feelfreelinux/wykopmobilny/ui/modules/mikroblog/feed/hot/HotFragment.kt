package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.base.BaseNavigationFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add.createNewEntry
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.BaseFeedList
import kotlinx.android.synthetic.main.fragment_feed.view.*
import javax.inject.Inject

class HotFragment : BaseNavigationFragment(), HotView {
    @Inject lateinit var presenter : HotPresenter
    lateinit var feedRecyclerView : BaseFeedList
    lateinit var entriesDataFragment : DataFragment<PagedDataModel<List<Entry>>>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val view = inflater?.inflate(R.layout.fragment_feed, container, false)
        navigation.activityToolbar.overflowIcon = ContextCompat.getDrawable(activity, R.drawable.ic_clock)
        WykopApp.uiInjector.inject(this)
        entriesDataFragment = fragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        presenter.subscribe(this)
        entriesDataFragment.data?.apply { presenter.page = page }

        view?.feedRecyclerView.apply {
            feedRecyclerView = this@apply!!
            this.presenter = this@HotFragment.presenter
            fab = this@HotFragment.fab
            onFabClickedListener = {
                this@HotFragment.context.createNewEntry(null)
            }
            initAdapter(entriesDataFragment.data?.model)
        }

        return view
    }

    companion object {
        val DATA_FRAGMENT_TAG = "HOT_DATA_FRAGMENT"
        fun newInstance() : Fragment {
            return HotFragment()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        entriesDataFragment.data = PagedDataModel(presenter.page , feedRecyclerView.entries)
    }

    override fun onPause() {
        super.onPause()
        if (isRemoving) fragmentManager.removeDataFragment(entriesDataFragment)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.unsubscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.hot_period, menu)
        navigation.activityToolbar.setTitle(R.string.period24)
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
            R.id.newest   -> {
                presenter.period = "newest"
                navigation.activityToolbar.setTitle(R.string.newest_entries)
            }
        }

        feedRecyclerView.isRefreshing = true
        presenter.loadData(true)
        return true
    }

    override fun addDataToAdapter(entryList: List<Entry>, shouldClearAdapter: Boolean)
        = feedRecyclerView.addDataToAdapter(entryList, shouldClearAdapter)
}