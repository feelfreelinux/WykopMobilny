package io.github.feelfreelinux.wykopmobilny.activities

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager
import org.json.JSONObject

class TagViewActivity : MikroblogListActivity() {
    lateinit var _tag : String
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _tag = arguments.getString("TAG")
        (activity as AppCompatActivity).supportActionBar?.title = "#" + _tag
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun loadData(page: Int, action: WykopApiManager.WykopApiAction) {
        wam.getTagEntries(page, _tag, object : WykopApiManager.WykopApiAction {
            override fun success(json: JSONObject) {
                action.success(json.getJSONArray("items"))
            }
        })
    }
    companion object {
        fun newInstance(data : WykopApiData, _tag : String) : Fragment {
            val fragmentData = Bundle()
            val fragment = TagViewActivity()
            fragmentData.putSerializable("wamData", data)
            fragmentData.putSerializable("TAG", _tag)
            fragment.arguments = fragmentData
            return fragment
        }
    }

}