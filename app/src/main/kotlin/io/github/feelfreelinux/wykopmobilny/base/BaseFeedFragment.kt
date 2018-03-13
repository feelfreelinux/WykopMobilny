package io.github.feelfreelinux.wykopmobilny.base

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.BaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.InfiniteScrollListener
import kotlinx.android.synthetic.main.entry_list_item.*
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.search_empty_view.*

abstract class BaseFeedFragment<T : Any> : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    var showSearchEmptyView: Boolean
        get() = searchEmptyView.isVisible
        set(value) {
            searchEmptyView.isVisible = value
            if (value) {
                feedAdapter.addData(emptyList(), true)
                feedAdapter.disableLoading()
            }
        }

    abstract val feedAdapter : BaseProgressAdapter<T>

    abstract fun loadData(shouldRefresh : Boolean)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        swiperefresh.setOnRefreshListener(this)
        recyclerView?.prepare()
    }

    private fun setupInfiniteScrollListeners() {
        recyclerView?.apply {
            clearOnScrollListeners()
            addOnScrollListener(InfiniteScrollListener({
                loadData(false)
            }, layoutManager as LinearLayoutManager))
        }
    }

    fun initAdapter(feedList: List<T>? = emptyList()) {
        if (!(feedAdapter as RecyclerView.Adapter<*>).hasObservers()) feedAdapter.setHasStableIds(true)
        recyclerView?.adapter = feedAdapter as RecyclerView.Adapter<*>

        setupInfiniteScrollListeners()

        if (feedList == null || feedList.isEmpty()) {
            // Create adapter if no data is saved
            if (feedAdapter.data.isEmpty()) {
                isLoading = true
                loadData(true) // Trigger data loading
            }
        } else {
            feedAdapter.addData(feedList.filterNot { feedAdapter.data.contains(it) }, true)
            isLoading = false
        }
    }

    override fun onRefresh() {
        if (!isRefreshing) isRefreshing = true
        loadData(true)
    }

    fun addDataToAdapter(entryList: List<T>, shouldClearAdapter: Boolean) {
        if (shouldClearAdapter) setupInfiniteScrollListeners()
        if (entryList.isNotEmpty()) {
            isRefreshing = false
            isLoading = false
            recyclerView?.post {
                feedAdapter.addData(entryList.filterNot { !shouldClearAdapter && feedAdapter.data.contains(it) }, shouldClearAdapter)
                if (shouldClearAdapter) recyclerView?.scrollToPosition(0)
            }
        }
    }

    fun disableLoading() {
        recyclerView?.clearOnScrollListeners()
        feedAdapter.disableLoading()
        isRefreshing = false
        isLoading = false
    }

    val data: List<T>
        get() = feedAdapter.data

    var isLoading: Boolean
        get() = loadingView?.isVisible ?: false
        set(value) {
            loadingView?.isVisible = value
        }

    var isRefreshing: Boolean
        get() = swiperefresh?.isRefreshing ?: true
        set(value) {
            swiperefresh?.isRefreshing = value
        }
}