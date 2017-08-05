package io.github.feelfreelinux.wykopmobilny.fragments
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.presenters.HotFeedPresenter
import kotlinx.android.synthetic.main.toolbar.*

class HotFeedFragment : FeedFragment() {
    override val feedPresenter by lazy { HotFeedPresenter(wam, callbacks) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        navActivity.toolbar.overflowIcon = ContextCompat.getDrawable(activity, R.drawable.clock_icon)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        fun newInstance() : Fragment {
            return HotFeedFragment()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.hot_period, menu)
        navActivity.toolbar.setTitle(R.string.period24)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.period6  -> {
                feedPresenter.period = "6"
                navActivity.toolbar.setTitle(R.string.period6)
            }
            R.id.period12 -> {
                feedPresenter.period = "12"
                navActivity.toolbar.setTitle(R.string.period12)
            }
            R.id.period24 -> {
                feedPresenter.period = "24"
                navActivity.toolbar.setTitle(R.string.period24)
            }
            R.id.newest   -> {
                feedPresenter.period = "newest"
                navActivity.toolbar.setTitle(R.string.newest_entries)
            }
        }

        // Acts as refresh action
        navActivity.isRefreshing = true
        feedPresenter.loadData(1)
        return true
    }


}