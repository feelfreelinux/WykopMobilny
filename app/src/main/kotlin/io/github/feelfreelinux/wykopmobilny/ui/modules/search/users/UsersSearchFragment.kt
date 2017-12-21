package io.github.feelfreelinux.wykopmobilny.ui.modules.search.users

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.ProfilesAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragmentNotifier
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragmentQuery
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.search_empty_view.*
import javax.inject.Inject

class UsersSearchFragment : BaseFragment(), UsersSearchView, SwipeRefreshLayout.OnRefreshListener, SearchFragmentNotifier {
    override fun showUsers(entryList: List<Author>) {
        loadingView.isVisible = false
        swiperefresh.isRefreshing = false
        searchEmptyView.isVisible = entryList.isEmpty()
        profilesAdapter.apply {
            dataset.clear()
            dataset.addAll(entryList)
            notifyDataSetChanged()
        }
    }
    var queryString = ""
    private lateinit var dataFragment: DataFragment<List<Author>>
    @Inject lateinit var presenter : UsersSearchPresenter
    private val profilesAdapter by lazy { ProfilesAdapter() }

    companion object {
        val DATA_FRAGMENT_TAG = "PROFILES_SEARCH_VIEW"
        fun newInstance(): Fragment {
            return UsersSearchFragment()
        }
    }

    override fun notifyQueryChanged() {
        val parent = (parentFragment as SearchFragmentQuery)
        if (queryString != parent.searchQuery) {
            queryString = parent.searchQuery
            if (::presenter.isInitialized) {
                onRefresh()
            } else { loadingView.isVisible = false }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        WykopApp.uiInjector.inject(this)
        dataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)

        presenter.subscribe(this)
        swiperefresh.setOnRefreshListener(this)

        recyclerView.apply {
            prepare()
            adapter = profilesAdapter
        }
        swiperefresh.isRefreshing = false

        if (dataFragment.data == null) {
            loadingView.isVisible = false
            notifyQueryChanged()
        } else {
            profilesAdapter.dataset.addAll(dataFragment.data!!)
            profilesAdapter.notifyDataSetChanged()
            loadingView.isVisible = false
        }
    }

    override fun onRefresh() {
        if (queryString.length > 2) {
            loadingView.isVisible = true
            presenter.searchProfiles(queryString)
        }
        else {
            loadingView.isVisible = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        dataFragment.data = profilesAdapter.dataset
    }

    override fun onDetach() {
        super.onDetach()
        presenter.unsubscribe()
    }

    override fun onPause() {
        super.onPause()
        if (isRemoving) supportFragmentManager.removeDataFragment(dataFragment)
    }

}