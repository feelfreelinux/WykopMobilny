package io.github.feelfreelinux.wykopmobilny.fragments
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.activities.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.adapters.FeedAdapter
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.callbacks.FeedClickCallbacks
import io.github.feelfreelinux.wykopmobilny.presenters.FeedPresenter
import io.github.feelfreelinux.wykopmobilny.presenters.FeedViewCallbacks
import io.github.feelfreelinux.wykopmobilny.utils.*

abstract class FeedFragment : Fragment(), ILoadMore, SwipeRefreshLayout.OnRefreshListener {
    lateinit var recyclerView : RecyclerView
    var endlessScrollListener : EndlessScrollListener? = null
    val navActivity by lazy { activity as NavigationActivity }
    val wam by lazy { navActivity.wam }
    val callbacks by lazy { FeedClickCallbacks(navActivity) }
    var feedAdapter : FeedAdapter? = null
    abstract val feedPresenter : FeedPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.recycler_view_layout, container, false)

        // Prepare RecyclerView, and EndlessScrollListener
        recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)!!
        recyclerView.prepare()

        // Retrieve savedState endless scroll listener
        if (endlessScrollListener == null) endlessScrollListener =
                EndlessScrollListener(this, (recyclerView.layoutManager as LinearLayoutManager))
        else {
            endlessScrollListener?.mLayoutManager = (recyclerView.layoutManager as LinearLayoutManager)
            endlessScrollListener?.loadMoreListener = this
        }

        recyclerView.addOnScrollListener(endlessScrollListener)
        navActivity.setSwipeRefreshListener(this)

        feedPresenter.dataLoadedListener = {
            isRefreshed ->
            run {
                // if (isRefreshed) recyclerView.scrollToPosition(10)
                feedAdapter?.notifyDataSetChanged()
                navActivity.isLoading = false
                navActivity.isRefreshing = false
                // Scroll to top after loading
                if (isRefreshed) recyclerView.smoothScrollToPosition(0)
            }
        }

        // Create adapter if no data is saved
        if(feedAdapter == null) {
            feedAdapter = FeedAdapter(feedPresenter)
            recyclerView.adapter = feedAdapter

            // Set needed flags
            navActivity.isLoading = true

            // Trigger data loading
            feedPresenter.loadData(1)
        } else
            recyclerView.adapter = feedAdapter // Get data from saved instance

        return view
    }

    override fun onRefresh() {
        feedPresenter.loadData(1)
    }

    override fun onLoadMore(page: Int) {
        feedPresenter.loadData(page)
    }
}