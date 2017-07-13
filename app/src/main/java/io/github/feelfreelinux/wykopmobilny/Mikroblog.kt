package io.github.feelfreelinux.wykopmobilny

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.github.kittinunf.fuel.android.core.Json
import io.github.feelfreelinux.wykopmobilny.adapters.MikroblogListAdapter
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import kotlinx.android.synthetic.main.activity_mikroblog.*

class Mikroblog : AppCompatActivity() {
    val appkey = ""
    val secret = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mikroblog)

        // Get shared prefs, check is user connection data in memory
        val sharedPrefs = getSharedPreferences("io.github.feelfreelinux.wykopmobilny", 0) // 0 = MODE.Private
        val login =  sharedPrefs.getString("login", "")
        val accountKey =  sharedPrefs.getString("userkey", "")

        var wam = WykopApiManager(login, accountKey, secret, appkey)

        // Setup recycler view
        recyclerView.run {
            setHasFixedSize(true) // For better performance
            layoutManager = LinearLayoutManager(this@Mikroblog)
        }

        wam.initAction = object : WykopApiManager.WykopApiAction() {
            override fun success(json: Json) {
                var list = ArrayList<Entry>()
                val adapter = MikroblogListAdapter(list, object : LoadMoreListener() {
                    override fun loadMore() {
                        loading = true
                        wam.getHot(page, "24", object : WykopApiManager.WykopApiAction(){
                            override fun success(json: Json) {
                                (0 .. json.array().length()-1).mapTo(list) { parseEntry(json.array().getJSONObject(it)) }
                                recyclerView.adapter.notifyDataSetChanged()
                                loading = false
                            }
                        })
                        page++
                    }
                })
                recyclerView.adapter = adapter
                adapter.loadMoreListener.loadMore()
            }
        }
    }
}
