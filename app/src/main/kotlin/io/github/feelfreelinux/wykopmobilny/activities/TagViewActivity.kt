package io.github.feelfreelinux.wykopmobilny.activities

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager
import org.json.JSONObject

class TagViewActivity : MikroblogListActivity() {
    lateinit var tag : String
    override fun onCreate(savedInstanceState: Bundle?) {
        tag = intent.getStringExtra("TAG")
        setTitle("#"+tag)
        super.onCreate(savedInstanceState)
    }
    override fun loadData(page: Int, action: WykopApiManager.WykopApiAction) {
        wam.getTagEntries(page, tag, object : WykopApiManager.WykopApiAction {
            override fun success(json: JSONObject) {
                action.success(json.getJSONArray("items"))
            }
        })
    }

}