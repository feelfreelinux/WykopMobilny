package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.ui.mainnavigation.MainNavigationInterface
import io.github.feelfreelinux.wykopmobilny.utils.*
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.EndlessScrollListener
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.ILoadMore
import kotlinx.android.synthetic.main.recycler_view_layout.view.*

abstract class BaseFeedFragment : BaseFragment(), ILoadMore, SwipeRefreshLayout.OnRefreshListener, BaseFeedView {
    var endlessScrollListener: EndlessScrollListener? = null

    protected val navActivity by lazy { activity as MainNavigationInterface }

    private val feedAdapter by lazy {
        FeedAdapter(FeedClickCallbacks(navActivity, kodein.instanceValue()))
    }

    abstract val presenter: BaseFeedPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.recycler_view_layout, container, false)

        // Prepare RecyclerView, and EndlessScrollListener
        val recyclerView = view?.recyclerView!!
        recyclerView.prepare()

        // Retrieve savedState endless scroll listener
        if (endlessScrollListener == null) {
            endlessScrollListener = EndlessScrollListener(this, (recyclerView.layoutManager as LinearLayoutManager))
        }
        else {
            endlessScrollListener?.mLayoutManager = (recyclerView.layoutManager as LinearLayoutManager)
            endlessScrollListener?.loadMoreListener = this
        }

        recyclerView.addOnScrollListener(endlessScrollListener)
        navActivity.setSwipeRefreshListener(this)

        recyclerView.adapter = feedAdapter

        // Create adapter if no data is saved
        if(feedAdapter.dataset.isEmpty()) {
            // Set needed flags
            navActivity.isLoading = true

            // Trigger data loading
            presenter.loadData(1)
        }

        return view
    }

    override fun addDataToAdapter(entryList: List<Entry>, shouldClearAdapter: Boolean) {
        navActivity.isRefreshing = false
        navActivity.isLoading = false
        feedAdapter.isLoading = false

        if (shouldClearAdapter) feedAdapter.dataset.clear()
        feedAdapter.dataset.addAll(entryList)
        if(shouldClearAdapter) feedAdapter.notifyDataSetChanged()

        else feedAdapter.notifyItemRangeInserted(
                feedAdapter.dataset.size - entryList.size,
                feedAdapter.dataset.size
        )
        feedAdapter.notifyDataSetChanged()
    }

    override fun onRefresh() {
        presenter.loadData(1)
    }

    override fun onLoadMore(page: Int) {
        feedAdapter.isLoading = true
        presenter.loadData(page)
    }
}