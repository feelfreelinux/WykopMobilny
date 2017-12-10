package io.github.feelfreelinux.wykopmobilny.ui.modules.search.users

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Conversation
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.ConversationsListAdapter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.ProfilesAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist.ConversationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist.ConversationsListPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragmentQuery
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.feed_fragment.*
import javax.inject.Inject

class UsersSearchFragment : BaseFragment(), UsersSearchView, SwipeRefreshLayout.OnRefreshListener {
    override fun showUsers(entryList: List<Author>) {
        loadingView.isVisible = false
        swiperefresh.isRefreshing = false
        profilesAdapter.apply {
            printout(entryList.size.toString())
            dataset.clear()
            dataset.addAll(entryList)
            notifyDataSetChanged()
        }
    }
    var queryString = ""
    private lateinit var conversationsDataFragment : DataFragment<List<Author>>
    @Inject lateinit var presenter : UsersSearchPresenter
    private val profilesAdapter by lazy { ProfilesAdapter() }

    companion object {
        val DATA_FRAGMENT_TAG = "PROFILES_SEARCH_VIEW"
        fun newInstance(): Fragment {
            return UsersSearchFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parent = (parentFragment as SearchFragmentQuery)
        if (queryString != parent.searchQuery) {
            queryString = parent.searchQuery
            if (::presenter.isInitialized) {
                onRefresh()
            }
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        conversationsDataFragment = fragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        WykopApp.uiInjector.inject(this)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.messages)
        swiperefresh.setOnRefreshListener(this)

        recyclerView.apply {
            prepare()
            adapter = profilesAdapter
        }
        swiperefresh.isRefreshing = false
        presenter.subscribe(this)

        if (conversationsDataFragment.data == null) {
            loadingView.isVisible = true
            onRefresh()
        } else {
            profilesAdapter.dataset.addAll(conversationsDataFragment.data!!)
            profilesAdapter.notifyDataSetChanged()
            loadingView.isVisible = false
        }
    }

    override fun onRefresh() {
        if (queryString.length > 2) presenter.searchProfiles(queryString)
        else {
            loadingView.isVisible = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        conversationsDataFragment.data = profilesAdapter.dataset
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun onPause() {
        super.onPause()
        if (isRemoving) fragmentManager.removeDataFragment(conversationsDataFragment)
    }
}