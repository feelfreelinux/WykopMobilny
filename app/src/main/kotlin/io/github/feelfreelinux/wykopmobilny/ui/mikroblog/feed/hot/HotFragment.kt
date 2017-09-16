package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.hot
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.base.BaseNavigationFragment
import io.github.feelfreelinux.wykopmobilny.ui.add_user_input.launchNewEntryUserInput
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.BaseFeedRecyclerView
import kotlinx.android.synthetic.main.fragment_feed.view.*
import javax.inject.Inject

class HotFragment : BaseNavigationFragment(), HotView {
    @Inject lateinit var presenter : HotPresenter
    lateinit var feedRecyclerView : BaseFeedRecyclerView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val view = inflater?.inflate(R.layout.fragment_feed, container, false)
        navigation.activityToolbar.overflowIcon = ContextCompat.getDrawable(activity, R.drawable.ic_clock)
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)

        view?.feedRecyclerView.apply {
            feedRecyclerView = this@apply!!
            this.presenter = this@HotFragment.presenter
            initAdapter()
            onFabClickedListener = {
                this@HotFragment.context.launchNewEntryUserInput(null)
            }
        }

        return view
    }

    companion object {
        fun newInstance() : Fragment {
            return HotFragment()
        }
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
        presenter.loadData(1)
        return true
    }

    override fun addDataToAdapter(entryList: List<Entry>, shouldClearAdapter: Boolean)
        = feedRecyclerView.addDataToAdapter(entryList, shouldClearAdapter)
}