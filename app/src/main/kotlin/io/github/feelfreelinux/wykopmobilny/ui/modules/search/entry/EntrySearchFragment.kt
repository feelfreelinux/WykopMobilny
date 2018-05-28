package io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry


import android.os.Bundle
import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.base.BaseEntriesFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragment
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.entries_fragment.*
import javax.inject.Inject

class EntrySearchFragment : BaseEntriesFragment(), EntrySearchView {
    var query = ""

    lateinit var querySubscribe : Disposable

    @Inject
    lateinit var presenter: EntrySearchPresenter
    override var loadDataListener: (Boolean) -> Unit = {
        presenter.searchEntries(query, it)
    }

    @Inject
    lateinit var userManager: UserManagerApi

    companion object {
        fun newInstance(): Fragment {
            return  EntrySearchFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        entriesAdapter.entryActionListener = presenter
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }
        loadingView.isVisible = false

    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe(this)
        querySubscribe = (parentFragment as SearchFragment).querySubject.subscribe {
            swipeRefresh.isRefreshing = true
            query = it
            presenter.searchEntries(query, true)
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
        querySubscribe.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }
}