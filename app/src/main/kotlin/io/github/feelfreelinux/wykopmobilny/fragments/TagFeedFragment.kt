package io.github.feelfreelinux.wykopmobilny.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.presenters.TagFeedPresenter
import kotlinx.android.synthetic.main.toolbar.*

class TagFeedFragment : FeedFragment() {
    val entryTag by lazy { arguments.getString("TAG") }
    override val feedPresenter by lazy { TagFeedPresenter(entryTag, wam, callbacks) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(false)
        navActivity.toolbar.title = "#" + entryTag
        return super.onCreateView(inflater, container, savedInstanceState)
    }

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