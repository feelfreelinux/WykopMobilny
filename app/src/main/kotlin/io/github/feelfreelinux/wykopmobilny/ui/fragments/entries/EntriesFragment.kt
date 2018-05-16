package io.github.feelfreelinux.wykopmobilny.ui.fragments.entries

import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.EntriesAdapter
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.appendNewSpan
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.dialog_voters.view.*
import kotlinx.android.synthetic.main.entries_fragment.*
import javax.inject.Inject

// This fragment shows list of provided entries
class EntriesFragment : BaseFragment(), EntriesFragmentView {
    var loadDataListener : (Boolean) -> Unit = {}
    var votersDialogListener : (List<Voter>) -> Unit = {}

    @Inject
    lateinit var presenter : EntriesFragmentPresenter

    @Inject
    lateinit var entriesAdapter : EntriesAdapter

    // Inflate view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.entries_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        entriesAdapter.entryActionListener = presenter
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }

        // Setup views
        swipeRefresh.setOnRefreshListener({ loadDataListener(false) })
        recyclerView.run {
            prepare()
            adapter = entriesAdapter
        }

        loadingView.isVisible = true
    }

    /**
     * Removes progressbar from adapter
     */
    fun disableLoading() {
        entriesAdapter.disableLoading()
    }

    /**
     * Use this function to add items to EntriesFragment
     * @param items List of entries to add
     * @param shouldRefresh If true adapter will refresh its data with provided items. False by default
     */
    fun addItems(items : List<Entry>, shouldRefresh : Boolean = false) {
        entriesAdapter.addData(items, shouldRefresh)
        swipeRefresh.isRefreshing = false
        loadingView.isVisible = false

        // Scroll to top if refreshing list
        if (shouldRefresh) {
            (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
        }
    }

    override fun updateEntry(entry: Entry) {
        entriesAdapter.updateEntry(entry)
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

    override fun showVoters(voters : List<Voter>) {
        votersDialogListener(voters)
    }

    override fun openVotersMenu() {
        val dialog = BottomSheetDialog(activity!!)
        val votersDialogView = activity!!.layoutInflater.inflate(R.layout.dialog_voters, null)
        votersDialogView.votersTextView.isVisible = false
        dialog.setContentView(votersDialogView)
        votersDialogListener = {
            if (dialog.isShowing) {
                votersDialogView.progressView.isVisible = false
                votersDialogView.votersTextView.isVisible = true
                val spannableStringBuilder = SpannableStringBuilder()
                it
                        .map { it.author }
                        .forEachIndexed { index, author ->
                            val span = ForegroundColorSpan(getGroupColor(author.group))
                            spannableStringBuilder.appendNewSpan(author.nick, span, 0)
                            if (index < it.size - 1) spannableStringBuilder.append(", ")
                        }
                if (spannableStringBuilder.isEmpty()) {
                    votersDialogView.votersTextView.gravity = Gravity.CENTER
                    spannableStringBuilder.append(context?.getString(R.string.dialogNoVotes))
                }
                votersDialogView.votersTextView.text = spannableStringBuilder
            }
        }
        dialog.show()
    }
}