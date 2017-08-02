package io.github.feelfreelinux.wykopmobilny.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.android.FragmentInjector
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.activities.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.adapters.FeedAdapter
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.projectors.FeedClickActions
import io.github.feelfreelinux.wykopmobilny.objects.SingleEntry
import io.github.feelfreelinux.wykopmobilny.utils.*

abstract class FeedFragment : Fragment(), ILoadMore, SwipeRefreshLayout.OnRefreshListener {
    private val kodein = LazyKodein(appKodein)

    lateinit var recyclerView: RecyclerView
    var endlessScrollListener: EndlessScrollListener? = null

    protected val wam: WykopApiManager by kodein.instance()
    protected val navActivity by lazy { activity as NavigationActivity }
    protected var feedAdapter: FeedAdapter? = null

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

        // Create adapter if no data is saved
        if (feedAdapter == null) {
            feedAdapter = FeedAdapter(FeedClickActions(activity as NavigationActivity), wam)
            recyclerView.adapter = feedAdapter

            // Set needed flags
            navActivity.isLoading = true

            // Trigger data loading
            onLoadMore(1)
        } else
            recyclerView.adapter = feedAdapter // Get data from saved instance

        return view
    }

    override fun onRefresh() {
        loadData(1, {
            result ->
            run {
                feedAdapter?.entryList = emptyList()
                endlessScrollListener?.resetState()
                addDataToAdapter(parseSingleEntryList(result as Array<SingleEntry>))
            }
        })
    }

    override fun onLoadMore(page: Int) {
        loadData(page, {
            result ->
            addDataToAdapter(parseSingleEntryList(result as Array<SingleEntry>))
        })
    }

    abstract fun loadData(page: Int, resultCallback: (Any) -> Unit)

    fun addDataToAdapter(list: List<Entry>) {
        val fullList = ArrayList<Entry>()
        feedAdapter?.entryList?.let { fullList.addAll(it) }
        fullList.addAll(list)
        feedAdapter?.entryList = fullList
        (activity as NavigationActivity).run {
            isLoading = false
            isRefreshing = false
        }

    }
}