package io.github.feelfreelinux.wykopmobilny.base

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.EntryLinksAdapter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.CreateVotersDialogListener
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.VotersDialogListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entrylink.EntryLinkFragmentView
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.dialog_voters.view.*
import kotlinx.android.synthetic.main.entries_fragment.*
import kotlinx.android.synthetic.main.search_empty_view.*
import javax.inject.Inject

open class BaseEntryLinkFragment : BaseFragment(), EntryLinkFragmentView, androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        loadDataListener(true)
    }

    var showSearchEmptyView: Boolean
        get() = searchEmptyView.isVisible
        set(value) {
            searchEmptyView.isVisible = value
            if (value) {
                entriesAdapter.addData(emptyList(), true)
                entriesAdapter.disableLoading()
            }
        }

    open var loadDataListener : (Boolean) -> Unit = {}
    lateinit var votersDialogListener : VotersDialogListener


    @Inject
    lateinit var entriesAdapter : EntryLinksAdapter

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
    override fun addItems(items : List<EntryLink>, shouldRefresh : Boolean) {
        entriesAdapter.addData(items, shouldRefresh)
        swipeRefresh.isRefreshing = false
        loadingView.isVisible = false

        // Scroll to top if refreshing list
        if (shouldRefresh) {
            (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).scrollToPositionWithOffset(0, 0)
        }
    }

    override fun updateEntry(entry: Entry) {
        entriesAdapter.updateEntry(entry)
    }

    override fun updateLink(link: Link) {
        entriesAdapter.updateLink(link)
    }

    override fun showVoters(voters : List<Voter>) {
        votersDialogListener(voters)
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