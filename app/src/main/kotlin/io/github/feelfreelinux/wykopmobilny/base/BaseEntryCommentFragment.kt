package io.github.feelfreelinux.wykopmobilny.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.databinding.DialogVotersBinding
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.EntryCommentAdapter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.VotersDialogListener
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.createVotersDialogListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments.EntryCommentsFragmentView
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.entries_fragment.*
import kotlinx.android.synthetic.main.search_empty_view.*
import javax.inject.Inject

open class BaseEntryCommentFragment : BaseFragment(), EntryCommentsFragmentView, SwipeRefreshLayout.OnRefreshListener {

    @Inject lateinit var entryCommentsAdapter: EntryCommentAdapter
    lateinit var votersDialogListener: VotersDialogListener

    open var loadDataListener: (Boolean) -> Unit = {}
    var showSearchEmptyView: Boolean
        get() = searchEmptyView.isVisible
        set(value) {
            searchEmptyView.isVisible = value
            if (value) {
                entryCommentsAdapter.addData(emptyList(), true)
                entryCommentsAdapter.disableLoading()
            }
        }

    override fun openVotersMenu() {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(requireActivity())
        val votersDialogView = DialogVotersBinding.inflate(layoutInflater)
        votersDialogView.votersTextView.isVisible = false
        dialog.setContentView(votersDialogView.root)
        votersDialogListener = createVotersDialogListener(dialog, votersDialogView)
        dialog.show()
    }

    override fun showVoters(voters: List<Voter>) = votersDialogListener(voters)

    // Inflate view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.entries_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        entryCommentsAdapter.loadNewDataListener = { loadDataListener(false) }

        // Setup views
        swipeRefresh.setOnRefreshListener(this)
        recyclerView.run {
            prepare()
            adapter = entryCommentsAdapter
        }

        loadingView.isVisible = true
    }

    override fun onRefresh() = loadDataListener(true)

    /**
     * Removes progressbar from adapter
     */
    override fun disableLoading() = entryCommentsAdapter.disableLoading()

    /**
     * Use this function to add items to EntriesFragment
     * @param items List of entries to add
     * @param shouldRefresh If true adapter will refresh its data with provided items. False by default
     */
    override fun addItems(items: List<EntryComment>, shouldRefresh: Boolean) {
        entryCommentsAdapter.addData(items, shouldRefresh)
        swipeRefresh?.isRefreshing = false
        loadingView?.isVisible = false

        // Scroll to top if refreshing list
        if (shouldRefresh) {
            (recyclerView?.layoutManager as? androidx.recyclerview.widget.LinearLayoutManager)?.scrollToPositionWithOffset(0, 0)
        }
    }

    override fun updateComment(comment: EntryComment) = entryCommentsAdapter.updateComment(comment)
}
