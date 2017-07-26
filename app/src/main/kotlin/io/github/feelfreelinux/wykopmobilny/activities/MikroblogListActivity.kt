package io.github.feelfreelinux.wykopmobilny.activities

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager

import io.github.feelfreelinux.wykopmobilny.adapters.MikroblogListAdapter
import io.github.feelfreelinux.wykopmobilny.adapters.entryOpenListener
import io.github.feelfreelinux.wykopmobilny.adapters.tagClickListener
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import io.github.feelfreelinux.wykopmobilny.utils.EndlessScrollListener
import io.github.feelfreelinux.wykopmobilny.utils.parseEntry
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.activity_mikroblog.*
import kotlinx.android.synthetic.main.activity_navigation.*
import org.json.JSONArray
import org.json.JSONObject

/*
 * This is base class for every mikroblog - entry showing activities
 * Extend it, and pass your data in overrided loadData() function
*/
abstract class MikroblogListActivity : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    lateinit var adapter : MikroblogListAdapter
    lateinit var endlessScrollListener : EndlessScrollListener
    lateinit var loadMoreAction : WykopApiManager.WykopApiAction

    val wam by lazy {WykopApiManager(arguments.getSerializable("wamData") as WykopApiData, activity)}
    val navigationActivity by lazy {activity as NavigationActivity}
    lateinit var recyclerView : RecyclerView
    var pagerEnabled = true
    var list = ArrayList<Entry>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater?.inflate(R.layout.activity_mikroblog, container, false)
        navigationActivity.setSwipeRefreshListener(this)
        // Setup recycler view
        recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView
        recyclerView.run {
            setHasFixedSize(true) // For better performance
            layoutManager = LinearLayoutManager(activity)
        }
        if (pagerEnabled) {
            loadMoreAction = object : WykopApiManager.WykopApiAction {
                override fun success(json: JSONArray) {
                    (0 .. json.length()-1).mapTo(list) { parseEntry(json.getJSONObject(it)) }
                    recyclerView.adapter.notifyDataSetChanged()
                    navigationActivity.setRefreshing(false)
                    navigationActivity.showLoading(false)
                }
            }

            endlessScrollListener = object : EndlessScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    loadData(page, loadMoreAction)
                }

            }

            recyclerView.addOnScrollListener(endlessScrollListener)
        }
        navigationActivity.setRefreshing(false)
        navigationActivity.showLoading(true)
        createAdapter()
        return view
    }

    fun createAdapter() {
        list.clear()
        adapter = MikroblogListAdapter(list, pagerEnabled, {
            _tag -> navigationActivity.navActions.openFragment(TagViewActivity.newInstance(wam.getData(), _tag))
        }, {
            entryId -> navigationActivity.navActions.openFragment(MikroblogEntryView.newInstance(wam.getData(), entryId))
        }, wam.getData())

        if (pagerEnabled) {
            recyclerView.adapter = adapter
            loadData(1, loadMoreAction)
            endlessScrollListener.resetState()
        } else {
            recyclerView.adapter = adapter
            loadData(0, object : WykopApiManager.WykopApiAction{ // Child class will handle parsing data by itself
                override fun success(json: JSONObject) {
                    recyclerView.adapter.notifyDataSetChanged()
                    navigationActivity.setRefreshing(false)
                }
            })
        }
    }

    abstract fun loadData(page: Int, action: WykopApiManager.WykopApiAction)



    override fun onRefresh() =
        createAdapter()
}
