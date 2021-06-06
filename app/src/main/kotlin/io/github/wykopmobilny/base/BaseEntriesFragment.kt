package io.github.wykopmobilny.base

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.wykopmobilny.R
import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.databinding.DialogVotersBinding
import io.github.wykopmobilny.databinding.EntriesFragmentBinding
import io.github.wykopmobilny.models.dataclass.Entry
import io.github.wykopmobilny.models.dataclass.Voter
import io.github.wykopmobilny.ui.adapters.EntriesAdapter
import io.github.wykopmobilny.ui.dialogs.VotersDialogListener
import io.github.wykopmobilny.ui.dialogs.createVotersDialogListener
import io.github.wykopmobilny.ui.fragments.entries.EntriesFragmentView
import io.github.wykopmobilny.utils.prepare
import io.github.wykopmobilny.utils.viewBinding
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

open class BaseEntriesFragment : BaseFragment(R.layout.entries_fragment), EntriesFragmentView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var entriesApi: EntriesApi

    @Inject
    lateinit var entriesAdapter: EntriesAdapter

    protected val binding by viewBinding(EntriesFragmentBinding::bind)

    open var loadDataListener: (Boolean) -> Unit = {}
    lateinit var votersDialogListener: VotersDialogListener
    private val subjectDisposable by lazy { CompositeDisposable() }

    var showSearchEmptyView: Boolean
        get() = binding.empty.searchEmptyView.isVisible
        set(value) {
            binding.empty.searchEmptyView.isVisible = value
        }

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

        val schedulers = WykopSchedulers()
        subjectDisposable.addAll(
            entriesApi.entryVoteSubject
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe { updateEntryVoteState(it.entryId, it.voteResponse.voteCount, true) },
            entriesApi.entryUnVoteSubject
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe { updateEntryVoteState(it.entryId, it.voteResponse.voteCount, false) }
        )
    }

    override fun onDestroy() {
        subjectDisposable.dispose()
        super.onDestroy()
    }

    private fun updateEntryVoteState(entryId: Int, voteCount: Int, isVoted: Boolean) {
        entriesAdapter.data.firstOrNull { it.id == entryId }?.apply {
            this.voteCount = voteCount
            this.isVoted = isVoted
            entriesAdapter.updateEntry(this)
        }
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
    override fun addItems(items: List<Entry>, shouldRefresh: Boolean) {
        entriesAdapter.addData(items, shouldRefresh)
        binding.swipeRefresh.isRefreshing = false
        binding.loadingView.isVisible = false

        // Scroll to top if refreshing list
        if (shouldRefresh) {
            (binding.recyclerView.layoutManager as? androidx.recyclerview.widget.LinearLayoutManager)?.scrollToPositionWithOffset(0, 0)
        }
    }

    override fun updateEntry(entry: Entry) = entriesAdapter.updateEntry(entry)

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
