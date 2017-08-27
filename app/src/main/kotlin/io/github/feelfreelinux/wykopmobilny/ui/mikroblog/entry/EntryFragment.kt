package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.FeedClickCallbacks
import io.github.feelfreelinux.wykopmobilny.decorators.EntryCommentItemDecoration
import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.ui.mainnavigation.MainNavigationInterface
import io.github.feelfreelinux.wykopmobilny.utils.instanceValue
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.recycler_view_layout.view.*

val EXTRA_ENTRY_ID = "ENTRY_ID"

class EntryFragment : BaseFragment(), EntryContract.View, SwipeRefreshLayout.OnRefreshListener {
    private val entryId by lazy { arguments.getInt(EXTRA_ENTRY_ID) }
    private val navigation by lazy { activity as MainNavigationInterface }

    val presenter by lazy { EntryPresenter(kodein.instanceValue(), entryId) }
    private val adapter by lazy { EntryAdapter(FeedClickCallbacks(navigation, kodein.instanceValue())) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.recycler_view_layout, container, false)
        presenter.subscribe(this)
        // Prepare RecyclerView
        view?.recyclerView!!.apply {
            prepare()
            // Set margin, adapter
            addItemDecoration(EntryCommentItemDecoration(resources.getDimensionPixelOffset(R.dimen.comment_section_left_margin)))
            this.adapter = this@EntryFragment.adapter
        }

        // Set needed flags
        navigation.isLoading = true
        navigation.setSwipeRefreshListener(this)

        // Trigger data loading
        presenter.loadData()
        return view
    }

    override fun onRefresh() {presenter.loadData()}

    override fun showEntry(entry: Entry) {
        adapter.entry = entry
        navigation.isLoading = false
        navigation.isRefreshing = false
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(id: Int): Fragment {
            val fragmentData = Bundle()
            val fragment = EntryFragment()
            fragmentData.putInt(EXTRA_ENTRY_ID, id)
            fragment.arguments = fragmentData
            return fragment
        }
    }
}