package io.github.wykopmobilny.base

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.wykopmobilny.R
import io.github.wykopmobilny.databinding.DialogVotersBinding
import io.github.wykopmobilny.databinding.EntriesFragmentBinding
import io.github.wykopmobilny.models.dataclass.EntryComment
import io.github.wykopmobilny.models.dataclass.Voter
import io.github.wykopmobilny.ui.adapters.EntryCommentAdapter
import io.github.wykopmobilny.ui.dialogs.VotersDialogListener
import io.github.wykopmobilny.ui.dialogs.createVotersDialogListener
import io.github.wykopmobilny.ui.fragments.entrycomments.EntryCommentsFragmentView
import io.github.wykopmobilny.utils.prepare
import io.github.wykopmobilny.utils.viewBinding
import javax.inject.Inject

open class BaseEntryCommentFragment :
    BaseFragment(R.layout.entries_fragment),
    EntryCommentsFragmentView,
    SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var entryCommentsAdapter: EntryCommentAdapter
    lateinit var votersDialogListener: VotersDialogListener

    protected val binding by viewBinding(EntriesFragmentBinding::bind)

    open var loadDataListener: (Boolean) -> Unit = {}
    var showSearchEmptyView: Boolean
        get() = binding.empty.searchEmptyView.isVisible
        set(value) {
            binding.empty.searchEmptyView.isVisible = value
            if (value) {
                entryCommentsAdapter.addData(emptyList(), true)
                entryCommentsAdapter.disableLoading()
            }
        }

    override fun openVotersMenu() {
        val dialog = BottomSheetDialog(requireActivity())
        val votersDialogView = DialogVotersBinding.inflate(layoutInflater)
        votersDialogView.votersTextView.isVisible = false
        dialog.setContentView(votersDialogView.root)
        votersDialogListener = createVotersDialogListener(dialog, votersDialogView)
        dialog.show()
    }

    override fun showVoters(voters: List<Voter>) = votersDialogListener(voters)

    // Inflate view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        entryCommentsAdapter.loadNewDataListener = { loadDataListener(false) }

        // Setup views
        binding.swipeRefresh.setOnRefreshListener(this)
        binding.recyclerView.run {
            prepare()
            adapter = entryCommentsAdapter
        }

        binding.loadingView.isVisible = true
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
        binding.swipeRefresh.isRefreshing = false
        binding.loadingView.isVisible = false

        // Scroll to top if refreshing list
        if (shouldRefresh) {
            (binding.recyclerView.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(0, 0)
        }
    }

    override fun updateComment(comment: EntryComment) = entryCommentsAdapter.updateComment(comment)
}
