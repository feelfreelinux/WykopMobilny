package io.github.feelfreelinux.wykopmobilny.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager
import io.github.feelfreelinux.wykopmobilny.utils.parseEntry
import org.json.JSONObject

fun Context.launchMikroblogEntryView(wamData: WykopApiData, entryId: Int) {
    val entryViewIntent = Intent(this, MikroblogEntryView::class.java)
    entryViewIntent.putExtra("wamData", wamData)
    entryViewIntent.putExtra("ENTRY_ID", entryId)
    startActivity(entryViewIntent)
}

class MikroblogEntryView : MikroblogListActivity() {
    var id = 0
    override fun loadData(page: Int, action: WykopApiManager.WykopApiAction) {
        wam.getEntry(id, object : WykopApiManager.WykopApiAction{
            override fun success(json: JSONObject) {
                val entry = parseEntry(json)
                entry.isComment = true
                list.add(entry)
                val comments = json.getJSONArray("comments")
                for (i in 0 .. comments.length()-1) {
                    val comment = parseEntry(comments.getJSONObject(i))
                    comment.isComment = true
                    comment.entryId = entry.id
                    list.add(comment)
                }
                action.success(JSONObject())
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        id = intent.getIntExtra("ENTRY_ID", 1337)
        title = "Wpis /" + id
        pagerEnabled = false
        super.onCreate(savedInstanceState)
    }
}