package io.github.feelfreelinux.wykopmobilny.ui.fragments.entries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.adapters.EntriesAdapter
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.entries_fragment.*
import javax.inject.Inject

// This fragment shows list of provided entries
class EntriesFragment : BaseFragment(), EntriesFragmentView {
    var loadDataListener : (Boolean) -> Unit = {}

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
     * User this function to add items to EntriesFragment
     * @param items List of entries to add
     * @param shouldRefresh If true adapter will refresh its data with provided items. False by default
     */
    fun addItems(items : List<Entry>, shouldRefresh : Boolean = false) {
        entriesAdapter.addData(items, false)
        swipeRefresh.isRefreshing = false
        loadingView.isVisible = false
    }

    override fun updateEntry(entry: Entry) {
        entriesAdapter.updateEntry(entry)
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }
}