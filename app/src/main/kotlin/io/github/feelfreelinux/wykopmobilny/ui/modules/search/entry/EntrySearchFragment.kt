package io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry

import android.os.Bundle
import androidx.core.view.isVisible
import io.github.feelfreelinux.wykopmobilny.base.BaseEntriesFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragment
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.entries_fragment.*
import javax.inject.Inject

class EntrySearchFragment : BaseEntriesFragment(), EntrySearchView {

    companion object {
        fun newInstance() = EntrySearchFragment()
    }

    @Inject lateinit var presenter: EntrySearchPresenter

    @Inject lateinit var userManager: UserManagerApi

    var query = ""
    lateinit var querySubscribe: Disposable
    override var loadDataListener: (Boolean) -> Unit = { presenter.searchEntries(query, it) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        querySubscribe = (parentFragment as SearchFragment).querySubject.subscribe {
            swipeRefresh.isRefreshing = true
            query = it
            presenter.searchEntries(query, true)
        }
        entriesAdapter.entryActionListener = presenter
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }
        loadingView.isVisible = false
        showSearchEmptyView = true
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        querySubscribe.dispose()
        super.onDestroy()
    }
}
