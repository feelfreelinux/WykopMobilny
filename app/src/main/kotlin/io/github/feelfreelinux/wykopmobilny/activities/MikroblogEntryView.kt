package io.github.feelfreelinux.wykopmobilny.activities

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager
import io.github.feelfreelinux.wykopmobilny.utils.parseEntry
import org.json.JSONArray
import org.json.JSONObject

class MikroblogEntryView : MikroblogListActivity() {
    var entryId = 0
    override fun loadData(page: Int, action: WykopApiManager.WykopApiAction) {
        wam.getEntry(entryId, object : WykopApiManager.WykopApiAction{
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
                navigationActivity.isRefreshing = false
                navigationActivity.isLoading = false
            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        entryId = arguments.getInt("ENTRY_ID", 1337)
        pagerEnabled = false
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        fun newInstance(data : WykopApiData, entryId : Int) : Fragment {
            val fragmentData = Bundle()
            val fragment = MikroblogEntryView()
            fragmentData.putSerializable("wamData", data)
            fragmentData.putSerializable("ENTRY_ID", entryId)
            fragment.arguments = fragmentData
            return fragment
        }
    }
}