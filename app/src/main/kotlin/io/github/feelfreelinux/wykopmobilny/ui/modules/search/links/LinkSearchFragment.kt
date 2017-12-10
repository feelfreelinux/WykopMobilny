package io.github.feelfreelinux.wykopmobilny.ui.modules.search.links

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseFeedFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.FeedAdapter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinkAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragmentQuery
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry.EntrySearchPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry.EntrySearchView
import javax.inject.Inject

class LinkSearchFragment : BaseFeedFragment<Link>(), LinkSearchView {
    override val feedAdapter by lazy { LinkAdapter() }
    lateinit var dataFragment : DataFragment<PagedDataModel<List<Link>>>
    @Inject lateinit var presenter : LinkSearchPresenter
    var queryString = ""
    companion object {
        val DATA_FRAGMENT_TAG = "LINK_SEARCH_FRAGMENT"
        fun newInstance(): Fragment {
            return LinkSearchFragment()
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)
        dataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        dataFragment.data?.apply {
            presenter.page = page
        }
        presenter.subscribe(this)
        initAdapter(dataFragment.data?.model)
    }

    override fun loadData(shouldRefresh: Boolean) {
        if (queryString.length > 2) presenter.searchLinks(queryString, shouldRefresh)
        else {
            isLoading = false
            isRefreshing = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (::dataFragment.isInitialized)
        dataFragment.data = PagedDataModel(presenter.page , data)
    }
    override fun onDetach() {
        super.onDetach()
        presenter.unsubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRemoving) fragmentManager.removeDataFragment(dataFragment)
    }
}