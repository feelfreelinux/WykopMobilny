package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.base.BaseNavigationFragment
import io.github.feelfreelinux.wykopmobilny.utils.*
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.AutoHideFabListener
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.EndlessScrollListener
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.ILoadMore
import kotlinx.android.synthetic.main.recycler_view_layout.*
import kotlinx.android.synthetic.main.recycler_view_layout.view.*

abstract class BaseFeedFragment : BaseNavigationFragment(), ILoadMore, SwipeRefreshLayout.OnRefreshListener, BaseFeedView {
    private var endlessScrollListener: EndlessScrollListener? = null

    private val feedAdapter by lazy {
        FeedAdapter(FeedClickCallbacks(navigation, kodein.instanceValue()))
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

        // Add endlessScrolListener, and FabAutohide to recyclerview
        recyclerView.addOnScrollListener(endlessScrollListener)
        // recyclerView.addOnScrollListener(AutoHideFabListener({ navigation.shouldShowFab = it }))
        navigation.apply {
            navigation.setSwipeRefreshListener(this@BaseFeedFragment)
            navigation.shouldShowFab = false // We'll show it later.
            navigation.onFabClickListener = {
                navigation.openNewEntryUserInput(null)
            }
        }

        recyclerView.adapter = feedAdapter

        // Create adapter if no data is saved
        if(feedAdapter.dataset.isEmpty()) {
            // Set needed flags
            navigation.isLoading = true

            // Trigger data loading
            presenter.loadData(1)
        }

        return view
    }

    override fun addDataToAdapter(entryList: List<Entry>, shouldClearAdapter: Boolean) {
        navigation.isRefreshing = false
        navigation.isLoading = false
        feedAdapter.isLoading = false

        if (shouldClearAdapter) feedAdapter.dataset.clear()
        feedAdapter.dataset.addAll(entryList)

        if(shouldClearAdapter) feedAdapter.notifyDataSetChanged()
        else feedAdapter.notifyItemRangeInserted(
                feedAdapter.dataset.size,
                feedAdapter.dataset.size + entryList.size
        )

        if (feedAdapter.dataset.size == entryList.size) navigation.shouldShowFab = true // First time add only.
        if (shouldClearAdapter) recyclerView.smoothScrollToPosition(0)
    }

    override fun onRefresh() {
        presenter.loadData(1)
    }

    override fun onLoadMore(page: Int) {
        feedAdapter.isLoading = true
        presenter.loadData(page)
    }
}