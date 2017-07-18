package io.github.feelfreelinux.wykopmobilny.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.github.kittinunf.fuel.android.core.Json
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApiManager
import io.github.feelfreelinux.wykopmobilny.adapters.MikroblogListAdapter
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import io.github.feelfreelinux.wykopmobilny.parseEntry
import kotlinx.android.synthetic.main.activity_mikroblog.*

class MikroblogEntryView : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener{
    override fun onRefresh() = createAdapter()

    lateinit var wam : WykopApiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mikroblog)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        swiperefresh.setOnRefreshListener(this)
        title = "Wpis /" + intent.getIntExtra("ENTRY_ID", 1337)
        // Get WykopApiManager
        wam = WykopApiManager(intent.getSerializableExtra("wamData") as WykopApiData)

        // Setup recycler view
        recyclerView.run {
            setHasFixedSize(true) // For better performance
            layoutManager = LinearLayoutManager(this@MikroblogEntryView)
        }
        progressHeader.visibility = View.VISIBLE // Show progressbar
        createAdapter()
    }

    fun createAdapter() {
        val list = ArrayList<Entry>()
        wam.getEntry(intent.getIntExtra("ENTRY_ID", 1337), object : WykopApiManager.WykopApiAction(){
            override fun success(json: Json) {
                val entry = parseEntry(json.obj())
                entry.isComment = true
                list.add(entry)
                val comments = json.obj().getJSONArray("comments")
                for (i in 0 .. comments.length()-1) {
                    val comment =  parseEntry(comments.getJSONObject(i))
                    comment.isComment = true
                    list.add(comment)
                }
                swiperefresh.isRefreshing = false
                progressHeader.visibility = View.GONE
                val adapter = MikroblogListAdapter(list, null, wam.getData())
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }
}
