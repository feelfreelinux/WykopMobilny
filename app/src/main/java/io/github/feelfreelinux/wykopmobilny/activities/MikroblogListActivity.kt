package io.github.feelfreelinux.wykopmobilny.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.github.kittinunf.fuel.android.core.Json
import io.github.feelfreelinux.wykopmobilny.utils.LoadMoreListener
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager
import io.github.feelfreelinux.wykopmobilny.adapters.MikroblogListAdapter
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import io.github.feelfreelinux.wykopmobilny.utils.parseEntry
import kotlinx.android.synthetic.main.activity_mikroblog.*

abstract class MikroblogListActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    lateinit var wam : WykopApiManager
    lateinit var adapter : MikroblogListAdapter
    var list = ArrayList<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mikroblog)

        // Get WykopApiManager
        wam = WykopApiManager(intent.getSerializableExtra("wamData") as WykopApiData, this)

        swiperefresh.setOnRefreshListener(this)
        // Setup recycler view
        recyclerView.run {
            setHasFixedSize(true) // For better performance
            layoutManager = LinearLayoutManager(this@MikroblogListActivity)
        }
        swiperefresh.isRefreshing = false
        showProgress(true)
        createAdapter()
    }

    fun createAdapter() {
        list.clear()
        adapter = MikroblogListAdapter(list, object : LoadMoreListener() {
            override fun loadMore() {
                loading = true
                loadData(page,  object : WykopApiManager.WykopApiAction(){
                    override fun success(json: Json) {
                        (0 .. json.array().length()-1).mapTo(list) { parseEntry(json.array().getJSONObject(it)) }
                        recyclerView.adapter.notifyDataSetChanged()
                        swiperefresh.isRefreshing = false
                        showProgress(false)
                        loading = false
                    }
                })
                page++
            }
        }, wam.getData())
        recyclerView.adapter = adapter
        adapter.loadMoreListener?.loadMore()
    }

    abstract fun loadData(page : Int, action : WykopApiManager.WykopApiAction)

    fun showProgress(shouldShow : Boolean) {
        if (shouldShow)
            progressHeader.visibility = View.VISIBLE
        else progressHeader.visibility = View.GONE
    }

    override fun onRefresh() =
        createAdapter()

    override fun onBackPressed() {
        finish()
    }
}
