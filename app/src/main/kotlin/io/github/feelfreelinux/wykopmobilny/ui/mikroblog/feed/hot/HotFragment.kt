package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.hot
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.BaseFeedFragment
import kotlinx.android.synthetic.main.toolbar.*

class HotFragment : BaseFeedFragment(), HotContract.View {
    override val presenter by lazy { HotPresenter(apiManager) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        navActivity.toolbar.overflowIcon = ContextCompat.getDrawable(activity, R.drawable.clock_icon)
        presenter.subscribe(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        fun newInstance() : Fragment {
            return HotFragment()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.hot_period, menu)
        navActivity.toolbar.setTitle(R.string.period24)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.period6  -> {
                presenter.period = "6"
                navActivity.toolbar.setTitle(R.string.period6)
            }
            R.id.period12 -> {
                presenter.period = "12"
                navActivity.toolbar.setTitle(R.string.period12)
            }
            R.id.period24 -> {
                presenter.period = "24"
                navActivity.toolbar.setTitle(R.string.period24)
            }
            R.id.newest   -> {
                presenter.period = "newest"
                navActivity.toolbar.setTitle(R.string.newest_entries)
            }
        }

        // Acts as refresh action
        navActivity.isRefreshing = true
        presenter.loadData(1)
        return true
    }


}