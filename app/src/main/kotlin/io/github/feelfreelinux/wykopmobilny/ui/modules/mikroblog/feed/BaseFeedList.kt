package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.adapters.FeedAdapter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.InfiniteScrollListener
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.entry_list_item.view.*
import kotlinx.android.synthetic.main.feed_recyclerview.view.*
import javax.inject.Inject

class BaseFeedList : CoordinatorLayout, SwipeRefreshLayout.OnRefreshListener, BaseFeedView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Inject lateinit var userManager : UserManagerApi
    var presenter : BaseFeedPresenter? = null
    var onFabClickedListener = {}
    var shouldShowFab = true
    var fab : View? = null

    private val feedAdapter by lazy { FeedAdapter() }

    init {
        View.inflate(context, R.layout.feed_recyclerview, this)
        swiperefresh.setOnRefreshListener(this)
        WykopApp.uiInjector.inject(this)
        shouldShowFab = userManager.isUserAuthorized()
        recyclerView.prepare()
    }

    fun initAdapter(feedList: List<Entry>? = emptyList()) {
        // Add endlessScrolListener, and FabAutohide to recyclerview
        fab?.isVisible = false // We'll show it later.
        fab?.setOnClickListener { onFabClickedListener.invoke() }

        recyclerView.apply {
            adapter = feedAdapter
            clearOnScrollListeners()
            addOnScrollListener(InfiniteScrollListener({ presenter?.loadData(false) }, layoutManager as LinearLayoutManager))
        }
        if (feedList == null || feedList.isEmpty()) {
            // Create adapter if no data is saved
            if (feedAdapter.data.isEmpty()) {
                isLoading = true
                presenter?.loadData(true) // Trigger data loading
            }
        } else {
            feedAdapter.addData(feedList, true)
            isLoading = false
        }
    }

    override fun onRefresh() {
        presenter?.loadData(true)
    }

    override fun addDataToAdapter(entryList: List<Entry>, shouldClearAdapter: Boolean) {
        if (entryList.isNotEmpty()) {
            isRefreshing = false
            isLoading = false
            recyclerView.post {
                feedAdapter.addData(entryList, shouldClearAdapter)

                if (feedAdapter.data.size == entryList.size) fab?.isVisible = shouldShowFab // First time add only.
                if (shouldClearAdapter) recyclerView.smoothScrollToPosition(0)
            }
        }
    }

    val entries : List<Entry>
        get() = feedAdapter.data

    override fun showErrorDialog(e: Throwable) =
        context.showExceptionDialog(e)

    var isLoading : Boolean
        get() = loadingView.isVisible
        set(value) { loadingView.isVisible = value }

    var isRefreshing : Boolean
        get() = swiperefresh.isRefreshing
        set(value) { swiperefresh.isRefreshing = value }
}