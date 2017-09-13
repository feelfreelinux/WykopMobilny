package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.android.appKodein
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.instanceValue
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.EndlessScrollListener
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.ILoadMore
import io.github.feelfreelinux.wykopmobilny.utils.wykopactionhandler.WykopActionHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykopactionhandler.WykopActionHandlerImpl
import kotlinx.android.synthetic.main.feed_recyclerview.view.*

class BaseFeedRecyclerView : FrameLayout, ILoadMore, SwipeRefreshLayout.OnRefreshListener, BaseFeedView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val kodein = LazyKodein(appKodein)
    private var endlessScrollListener : EndlessScrollListener? = null

    var presenter : BaseFeedPresenter? = null
    var recyclerView : RecyclerView
    var fab : FloatingActionButton
    var loadingView : ProgressBar

    private val feedAdapter by lazy {
        FeedAdapter(
                WykopActionHandlerImpl(context, kodein.instanceValue()) as WykopActionHandler
        )
    }

    init {
        val view = View.inflate(context, R.layout.feed_recyclerview, this)
        recyclerView = view.recyclerView
        fab = view.fab
        loadingView = view.loadingView

        recyclerView.prepare()

        // Retrieve savedState endless scroll listener
        if (endlessScrollListener == null) {
            endlessScrollListener = EndlessScrollListener(this, (recyclerView.layoutManager as LinearLayoutManager))
        }
        else {
            endlessScrollListener?.mLayoutManager = (recyclerView.layoutManager as LinearLayoutManager)
            endlessScrollListener?.loadMoreListener = this
        }
    }

    fun initAdapter() {
        // Add endlessScrolListener, and FabAutohide to recyclerview
        recyclerView.addOnScrollListener(endlessScrollListener)
        // recyclerView.addOnScrollListener(AutoHideFabListener({ navigation.shouldShowFab = it }))
        fab.isVisible = false // We'll show it later.


        recyclerView.adapter = feedAdapter

        // Create adapter if no data is saved
        if(feedAdapter.dataset.isEmpty()) {
            isLoading = true
            presenter?.loadData(1) // Trigger data loading
        }
    }

    override fun onLoadMore(page: Int) {
        feedAdapter.isLoading = true
        presenter?.loadData(page)
    }

    override fun onRefresh() {
        presenter?.loadData(1)
    }

    override fun addDataToAdapter(entryList: List<Entry>, shouldClearAdapter: Boolean) {
        isRefreshing = false
        isLoading = false
        feedAdapter.addData(entryList, shouldClearAdapter)

        if (feedAdapter.dataset.size == entryList.size) fab.isVisible = true // First time add only.
        if (shouldClearAdapter) recyclerView.smoothScrollToPosition(0)
    }

    override fun showErrorDialog(e: Exception) =
        context.showExceptionDialog(e)

    var isLoading : Boolean
        get() = loadingView.isVisible
        set(value) { loadingView.isVisible = value }

    var isRefreshing : Boolean
        get() = swiperefresh.isRefreshing
        set(value) { swiperefresh.isRefreshing = value }
}