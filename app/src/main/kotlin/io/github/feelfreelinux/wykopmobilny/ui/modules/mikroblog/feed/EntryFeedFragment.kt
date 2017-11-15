package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.adapters.FeedAdapter
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.InfiniteScrollListener
import kotlinx.android.synthetic.main.entry_feed_fragment.*
abstract class EntryFeedFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, BaseEntryFeedView {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.entry_feed_fragment, container, false)
    }

    private val feedAdapter by lazy { FeedAdapter() }

    abstract fun loadData(shouldRefresh : Boolean)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        swiperefresh.setOnRefreshListener(this)
        recyclerView.prepare()
    }

    private fun setupInfiniteScrollListeners() {
        recyclerView.apply {
            clearOnScrollListeners()
            addOnScrollListener(InfiniteScrollListener({
                loadData(false)
            }, layoutManager as LinearLayoutManager))
        }
    }

    fun initAdapter(feedList: List<Entry>? = emptyList()) {
        recyclerView.adapter = feedAdapter
        setupInfiniteScrollListeners()

        if (feedList == null || feedList.isEmpty()) {
            // Create adapter if no data is saved
            if (feedAdapter.data.isEmpty()) {
                isLoading = true
                loadData(true) // Trigger data loading
            }
        } else {
            feedAdapter.addData(feedList, true)
            isLoading = false
        }
    }

    override fun onRefresh() {
        loadData(true)
    }

    override fun addDataToAdapter(entryList: List<Entry>, shouldClearAdapter: Boolean) {
        if (shouldClearAdapter) setupInfiniteScrollListeners()
        if (entryList.isNotEmpty()) {
            isRefreshing = false
            isLoading = false
            recyclerView.post {
                feedAdapter.addData(entryList, shouldClearAdapter)
                if (shouldClearAdapter) recyclerView.scrollToPosition(0)
            }
        }
    }

    override fun disableLoading() {
        recyclerView.clearOnScrollListeners()
        feedAdapter.disableLoading()
        isRefreshing = false
        isLoading = false
    }

    val entries: List<Entry>
        get() = feedAdapter.data

    var isLoading: Boolean
        get() = loadingView.isVisible
        set(value) {
            loadingView.isVisible = value
        }

    var isRefreshing: Boolean
        get() = swiperefresh.isRefreshing
        set(value) {
            swiperefresh.isRefreshing = value
        }
}