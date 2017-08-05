package io.github.feelfreelinux.wykopmobilny.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.presenters.HotFeedPresenter

class TagFeedFragment : FeedFragment() {
    val entryTag by lazy { arguments.getString("TAG") }
    override val feedPresenter by lazy { HotFeedPresenter(apiManager, callbacks) }

    companion object {
        fun newInstance(entryTag: String): Fragment {
            val fragmentData = Bundle()
            val fragment = TagFeedFragment()
            fragmentData.putSerializable("TAG", entryTag)
            fragment.arguments = fragmentData
            return fragment
        }
    }
}