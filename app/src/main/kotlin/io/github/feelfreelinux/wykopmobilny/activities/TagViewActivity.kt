package io.github.feelfreelinux.wykopmobilny.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager
import org.json.JSONObject

fun Context.launchTagViewActivity(wamData: WykopApiData, tag: String) {
    val tagIntent = Intent(this, TagViewActivity::class.java)
    tagIntent.putExtra("wamData", wamData)
    tagIntent.putExtra("TAG", tag)
    startActivity(tagIntent)
}

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