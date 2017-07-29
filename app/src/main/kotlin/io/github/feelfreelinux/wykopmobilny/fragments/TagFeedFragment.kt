package io.github.feelfreelinux.wykopmobilny.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.objects.TagFeedEntries
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
class TagFeedFragment : FeedFragment() {
    val _tag by lazy {arguments.getString("TAG")}

    override fun loadData(page: Int, resultCallback: (Any) -> Unit) {
        wam.getTagEntries(page, _tag, {
            result -> resultCallback((result as TagFeedEntries).items)
        })
    }

    companion object {
        fun newInstance(data: WykopApiData, _tag: String): Fragment {
            val fragmentData = Bundle()
            val fragment = TagFeedFragment()
            fragmentData.putSerializable("wamData", data)
            fragmentData.putSerializable("TAG", _tag)
            fragment.arguments = fragmentData
            return fragment
        }
    }
}