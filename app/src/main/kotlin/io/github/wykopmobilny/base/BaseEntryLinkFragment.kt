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
import io.github.wykopmobilny.models.dataclass.Entry
import io.github.wykopmobilny.models.dataclass.EntryLink
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.models.dataclass.Voter
import io.github.wykopmobilny.ui.adapters.EntryLinksAdapter
import io.github.wykopmobilny.ui.dialogs.VotersDialogListener
import io.github.wykopmobilny.ui.dialogs.createVotersDialogListener
import io.github.wykopmobilny.ui.fragments.entrylink.EntryLinkFragmentView
import io.github.wykopmobilny.utils.prepare
import io.github.wykopmobilny.utils.viewBinding
import javax.inject.Inject

open class BaseEntryLinkFragment : BaseFragment(R.layout.entries_fragment), EntryLinkFragmentView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var entriesAdapter: EntryLinksAdapter
    lateinit var votersDialogListener: VotersDialogListener

    protected val binding by viewBinding(EntriesFragmentBinding::bind)

    var showSearchEmptyView: Boolean
        get() = binding.empty.searchEmptyView.isVisible
        set(value) {
            binding.empty.searchEmptyView.isVisible = value
            if (value) {
                entriesAdapter.addData(emptyList(), true)
                entriesAdapter.disableLoading()
            }
        }

    open var loadDataListener: (Boolean) -> Unit = {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }

        // Setup views
        binding.swipeRefresh.setOnRefreshListener(this)
        binding.recyclerView.run {
            prepare()
            adapter = entriesAdapter
        }

        binding.loadingView.isVisible = true
    }

    override fun onRefresh() = loadDataListener(true)

    /**
     * Removes progressbar from adapter
     */
    override fun disableLoading() = entriesAdapter.disableLoading()

    /**
     * Use this function to add items to EntriesFragment
     * @param items List of entries to add
     * @param shouldRefresh If true adapter will refresh its data with provided items. False by default
     */
    override fun addItems(items: List<EntryLink>, shouldRefresh: Boolean) {
        entriesAdapter.addData(items, shouldRefresh)
        binding.swipeRefresh.isRefreshing = false
        binding.loadingView.isVisible = false

        // Scroll to top if refreshing list
        if (shouldRefresh) {
            (binding.recyclerView.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(0, 0)
        }
    }

    override fun updateEntry(entry: Entry) = entriesAdapter.updateEntry(entry)

    override fun updateLink(link: Link) = entriesAdapter.updateLink(link)

    override fun showVoters(voters: List<Voter>) = votersDialogListener(voters)

    override fun openVotersMenu() {
        val dialog = BottomSheetDialog(requireActivity())
        val votersDialogView = DialogVotersBinding.inflate(layoutInflater)
        votersDialogView.votersTextView.isVisible = false
        dialog.setContentView(votersDialogView.root)
        votersDialogListener = createVotersDialogListener(dialog, votersDialogView)
        dialog.show()
    }
}
