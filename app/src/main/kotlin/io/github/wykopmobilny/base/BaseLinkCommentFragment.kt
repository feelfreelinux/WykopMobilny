package io.github.wykopmobilny.base

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.wykopmobilny.R
import io.github.wykopmobilny.databinding.EntriesFragmentBinding
import io.github.wykopmobilny.models.dataclass.LinkComment
import io.github.wykopmobilny.ui.adapters.LinkCommentAdapter
import io.github.wykopmobilny.ui.fragments.linkcomments.LinkCommentsFragmentView
import io.github.wykopmobilny.utils.prepare
import io.github.wykopmobilny.utils.viewBinding
import javax.inject.Inject

open class BaseLinkCommentFragment :
    BaseFragment(R.layout.entries_fragment),
    LinkCommentsFragmentView,
    SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var linkCommentsAdapter: LinkCommentAdapter

    protected val binding by viewBinding(EntriesFragmentBinding::bind)

    var showSearchEmptyView: Boolean
        get() = binding.empty.searchEmptyView.isVisible
        set(value) {
            binding.empty.searchEmptyView.isVisible = value
            if (value) {
                linkCommentsAdapter.addData(emptyList(), true)
                linkCommentsAdapter.disableLoading()
            }
        }

    open var loadDataListener: (Boolean) -> Unit = {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linkCommentsAdapter.loadNewDataListener = { loadDataListener(true) }

        // Setup views
        binding.swipeRefresh.setOnRefreshListener(this)
        binding.recyclerView.run {
            prepare()
            adapter = linkCommentsAdapter
        }

        binding.loadingView.isVisible = true
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
        binding.swipeRefresh.isRefreshing = false
        binding.loadingView.isVisible = false

        // Scroll to top if refreshing list
        if (shouldRefresh) {
            (binding.recyclerView.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(0, 0)
        }
    }

    override fun onRefresh() = loadDataListener(true)

    override fun updateComment(comment: LinkComment) = linkCommentsAdapter.updateComment(comment)
}
