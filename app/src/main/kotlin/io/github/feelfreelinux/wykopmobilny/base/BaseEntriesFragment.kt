package io.github.feelfreelinux.wykopmobilny.base

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.EntriesAdapter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.CreateVotersDialogListener
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.VotersDialogListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntriesFragmentView
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_voters.view.*
import kotlinx.android.synthetic.main.entries_fragment.*
import kotlinx.android.synthetic.main.search_empty_view.*
import javax.inject.Inject

open class BaseEntriesFragment : BaseFragment(), EntriesFragmentView, androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
    var showSearchEmptyView: Boolean
        get() = searchEmptyView.isVisible
        set(value) {
            searchEmptyView.isVisible = value
        }

    override fun onRefresh() {
        loadDataListener(true)
    }

    open var loadDataListener : (Boolean) -> Unit = {}
    lateinit var votersDialogListener : VotersDialogListener
    @Inject lateinit var entriesApi : EntriesApi
    val subjectDisposable by lazy { CompositeDisposable() }

    @Inject
    lateinit var entriesAdapter : EntriesAdapter

    // Inflate view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.entries_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }

        // Setup views
        swipeRefresh.setOnRefreshListener(this)
        recyclerView.run {
            prepare()
            adapter = entriesAdapter
        }

        loadingView.isVisible = true

        val schedulers = WykopSchedulers()
        subjectDisposable.addAll(
                entriesApi.entryVoteSubject
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ updateEntryVoteState(it.entryId, it.voteResponse.voteCount, true) }),
                entriesApi.entryUnVoteSubject
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ updateEntryVoteState(it.entryId, it.voteResponse.voteCount, false) })
        )
    }

    private fun updateEntryVoteState(entryId : Int, voteCount : Int, isVoted : Boolean) {
        entriesAdapter.data.firstOrNull { it.id == entryId }?.apply {
            this.voteCount = voteCount
            this.isVoted = isVoted
            entriesAdapter.updateEntry(this)
        }
    }

    /**
     * Removes progressbar from adapter
     */
    override fun disableLoading() {
        entriesAdapter.disableLoading()
    }

    /**
     * Use this function to add items to EntriesFragment
     * @param items List of entries to add
     * @param shouldRefresh If true adapter will refresh its data with provided items. False by default
     */
    override fun addItems(items : List<Entry>, shouldRefresh : Boolean) {
        entriesAdapter.addData(items, shouldRefresh)
        swipeRefresh?.isRefreshing = false
        loadingView?.isVisible = false

        // Scroll to top if refreshing list
        if (shouldRefresh) {
            (recyclerView.layoutManager as? androidx.recyclerview.widget.LinearLayoutManager)?.scrollToPositionWithOffset(0, 0)
        }
    }

    override fun updateEntry(entry: Entry) {
        entriesAdapter.updateEntry(entry)
    }

    override fun showVoters(voters : List<Voter>) {
        votersDialogListener(voters)
    }

    override fun onDestroy() {
        super.onDestroy()
        subjectDisposable.dispose()
    }

    override fun openVotersMenu() {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(activity!!)
        val votersDialogView = activity!!.layoutInflater.inflate(R.layout.dialog_voters, null)
        votersDialogView.votersTextView.isVisible = false
        dialog.setContentView(votersDialogView)
        votersDialogListener = CreateVotersDialogListener(dialog, votersDialogView)
        dialog.show()
    }
}