package io.github.feelfreelinux.wykopmobilny.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.adapters.EntryDetailsAdapter
import io.github.feelfreelinux.wykopmobilny.adapters.FeedAdapter
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import io.github.feelfreelinux.wykopmobilny.utils.EndlessScrollListener
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager
import io.github.feelfreelinux.wykopmobilny.utils.parseEntry
import io.github.feelfreelinux.wykopmobilny.utils.printout
import org.json.JSONArray
import org.json.JSONObject

/*
 * This is base class for every mikroblog - entry showing activities
 * Extend it, and pass your data in overrided loadData() function
*/
abstract class MikroblogListActivity : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    lateinit var endlessScrollListener: EndlessScrollListener
    lateinit var loadMoreAction: WykopApiManager.WykopApiAction

    val wam by lazy { WykopApiManager(arguments.getSerializable("wamData") as WykopApiData, activity) }
    val navigationActivity by lazy { activity as NavigationActivity }
    lateinit var recyclerView: RecyclerView
    var pagerEnabled = true
    var list = ArrayList<Entry>()

    val feedAdapter = FeedAdapter(
            entryVoteClickListener = {
                entry, result ->
                wam.entryVote(entry,
                        successCallback = { result(true, it) },
                        failureCallback = { result(false, 0) })
            },
            tagClickListener = { tag ->
                printout(tag)
                navigationActivity.navActions.openFragment(TagViewActivity.newInstance(wam.getData(), tag))
            },
            commentClickListener = { id ->
                navigationActivity.navActions.openFragment(MikroblogEntryView.newInstance(wam.getData(), id))
            }
    )

    val entryDetailsAdapter = EntryDetailsAdapter(
            tagClickListener = {
                navigationActivity.navActions.openFragment(TagViewActivity.newInstance(wam.getData(), tag))
            }
    )


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater?.inflate(R.layout.activity_mikroblog, container, false)
        // Setup recycler view
        if (savedInstanceState != null && savedInstanceState.containsKey("entryList")) list = savedInstanceState.getSerializable("entryList") as ArrayList<Entry>

        recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView
        recyclerView.run {
            setHasFixedSize(true) // For better performance
            layoutManager = LinearLayoutManager(activity)
        }

        navigationActivity.setSwipeRefreshListener(this)

        if (pagerEnabled) {
            loadMoreAction = object : WykopApiManager.WykopApiAction {
                override fun success(json: JSONArray) {
                    (0..json.length() - 1).mapTo(list) { parseEntry(json.getJSONObject(it)) }
                    recyclerView.adapter.notifyDataSetChanged()
                    navigationActivity.isRefreshing = false
                    navigationActivity.isLoading = false
                }
            }

            endlessScrollListener = object : EndlessScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    loadData(page, loadMoreAction)
                }

            }

            recyclerView.addOnScrollListener(endlessScrollListener)
        }

        if (list.size == 0) {
            navigationActivity.isRefreshing = false
            navigationActivity.isLoading = true
            createAdapter()
        } else {
            recyclerView.adapter = feedAdapter
            recyclerView.adapter.notifyDataSetChanged()
            endlessScrollListener.startingPageIndex = (list.size / 25) + 1
            endlessScrollListener.resetState()
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null && savedInstanceState.containsKey("entryList")) {
            /*val entryList = savedInstanceState.getSerializable("entryList") as ArrayList<Entry>
            printout(entryList.size.toString())
            list.addAll(entryList)*/
        }
    }

    fun createAdapter() {
        list.clear()

        if (pagerEnabled) {
            feedAdapter.entryList = list
            recyclerView.adapter = feedAdapter
            loadData(1, loadMoreAction)
            endlessScrollListener.resetState()
        } else {
            entryDetailsAdapter.entryData = list
            recyclerView.adapter = entryDetailsAdapter
            loadData(0, object : WykopApiManager.WykopApiAction { // Child class will handle parsing data by itself
                override fun success(json: JSONObject) {
                    recyclerView.adapter.notifyDataSetChanged()
                    navigationActivity.isRefreshing = false
                }
            })
        }
    }

    abstract fun loadData(page: Int, action: WykopApiManager.WykopApiAction)

    override fun onRefresh() =
            createAdapter()

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (!navigationActivity.isLoading && !navigationActivity.isRefreshing)
            outState?.putSerializable("entryList", list)
    }
}
