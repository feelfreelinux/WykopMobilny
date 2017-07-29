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
import io.github.feelfreelinux.wykopmobilny.projectors.FeedClickActions
import io.github.feelfreelinux.wykopmobilny.objects.SingleEntry
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import io.github.feelfreelinux.wykopmobilny.utils.*

abstract class FeedFragment : Fragment(), ILoadMore, SwipeRefreshLayout.OnRefreshListener {
    lateinit var recyclerView : RecyclerView
    lateinit var endlessScrollListener : EndlessScrollListener
    val wamData by lazy { arguments.getSerializable("wamData") as WykopApiData }
    val wam by lazy { WykopApiManager(wamData, activity) }
    val navActivity by lazy { activity as NavigationActivity }
    lateinit var feedAdapter : FeedAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.activity_mikroblog, container, false)

        // Prepare RecyclerView, and EndlessScrollListener
        recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)!!
        recyclerView.prepare()
        endlessScrollListener =
                EndlessScrollListener(this, (recyclerView.layoutManager as LinearLayoutManager))
        recyclerView.addOnScrollListener(endlessScrollListener)

        // Create adapter
        feedAdapter = FeedAdapter(FeedClickActions(activity as NavigationActivity))
        recyclerView.adapter = feedAdapter

        // Set needed flags
        navActivity.isLoading = true
        navActivity.setSwipeRefreshListener(this)

        // Trigger data loading
        onLoadMore(1)
        return view
    }

    override fun onRefresh() {
        loadData(1, {
            result ->
            run {
                feedAdapter.entryList = emptyList()
                endlessScrollListener.resetState()
                addDataToAdapter(parseSingleEntryList(result as Array<SingleEntry>))
            }
        })
    }

    override fun onLoadMore(page: Int) {
        loadData(page, {
            result -> addDataToAdapter(parseSingleEntryList(result as Array<SingleEntry>))
        })
    }

    abstract fun loadData(page : Int, resultCallback : (Any) -> Unit)

    fun addDataToAdapter(list : List<Entry>) {
        val fullList = ArrayList<Entry>()
        fullList.addAll(feedAdapter.entryList)
        fullList.addAll(list)
        feedAdapter.entryList = fullList
        (activity as NavigationActivity).run {
            isLoading = false
            isRefreshing = false
        }

    }
}