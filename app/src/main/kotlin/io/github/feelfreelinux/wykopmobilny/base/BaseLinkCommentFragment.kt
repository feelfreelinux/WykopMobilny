package io.github.feelfreelinux.wykopmobilny.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinkCommentAdapter
import io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments.LinkCommentsFragmentView
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.entries_fragment.*
import kotlinx.android.synthetic.main.search_empty_view.*
import javax.inject.Inject

open class BaseLinkCommentFragment : BaseFragment(), LinkCommentsFragmentView, SwipeRefreshLayout.OnRefreshListener {

    @Inject lateinit var linkCommentsAdapter: LinkCommentAdapter

    var showSearchEmptyView: Boolean
        get() = searchEmptyView.isVisible
        set(value) {
            searchEmptyView.isVisible = value
            if (value) {
                linkCommentsAdapter.addData(emptyList(), true)
                linkCommentsAdapter.disableLoading()
            }
        }

    open var loadDataListener: (Boolean) -> Unit = {}

    // Inflate view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.entries_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        linkCommentsAdapter.loadNewDataListener = { loadDataListener(true) }

        // Setup views
        swipeRefresh.setOnRefreshListener(this)
        recyclerView.run {
            prepare()
            adapter = linkCommentsAdapter
        }

        loadingView.isVisible = true
    }

    /**
     * Removes progressbar from adapter
     */
    override fun disableLoading() = linkCommentsAdapter.disableLoading()

    /**
     * Use this function to add items to EntriesFragment
     * @param items List of entries to add
     * @param shouldRefresh If true adapter will refresh its data with provided items. False by default
     */
    override fun addItems(items: List<LinkComment>, shouldRefresh: Boolean) {
        linkCommentsAdapter.addData(items, shouldRefresh)
        swipeRefresh?.isRefreshing = false
        loadingView?.isVisible = false

        // Scroll to top if refreshing list
        if (shouldRefresh) {
            (recyclerView?.layoutManager as? androidx.recyclerview.widget.LinearLayoutManager)?.scrollToPositionWithOffset(0, 0)
        }
    }

    override fun onRefresh() = loadDataListener(true)

    override fun updateComment(comment: LinkComment) = linkCommentsAdapter.updateComment(comment)
}