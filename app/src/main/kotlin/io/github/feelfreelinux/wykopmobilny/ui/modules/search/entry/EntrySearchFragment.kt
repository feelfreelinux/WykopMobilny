package io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry

import android.os.Bundle
import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.base.BaseFeedFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.FeedAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragmentNotifier
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragmentQuery
import javax.inject.Inject

class EntrySearchFragment : BaseFeedFragment<Entry>(), EntrySearchView, SearchFragmentNotifier {
    @Inject override lateinit var feedAdapter : FeedAdapter
    lateinit var dataFragment : DataFragment<PagedDataModel<List<Entry>>>
    @Inject lateinit var presenter : EntrySearchPresenter
    var queryString = ""
    companion object {
        val DATA_FRAGMENT_TAG = "EXTRA_SEARCH_FRAGMENT"
        fun newInstance(): Fragment {
            return EntrySearchFragment()
        }
    }

    override fun notifyQueryChanged() {
        val parent = (parentFragment as SearchFragmentQuery)
        if (queryString != parent.searchQuery) {
            queryString = parent.searchQuery
            if (::presenter.isInitialized) {
                loadData(true)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        dataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        dataFragment.data?.apply {
            presenter.page = page
        }
        notifyQueryChanged()
        initAdapter(dataFragment.data?.model)
    }

    override fun loadData(shouldRefresh: Boolean) {
        if (queryString.length > 2) presenter.searchEntries(queryString, shouldRefresh)
        else {
            isLoading = false
            isRefreshing = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        dataFragment.data = PagedDataModel(presenter.page , data)
    }
    override fun onDetach() {
        super.onDetach()
        presenter.unsubscribe()
    }

    override fun removeDataFragment() {
        supportFragmentManager.removeDataFragment(dataFragment)
    }
}