package io.github.feelfreelinux.wykopmobilny.ui.modules.search.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.ui.adapters.ProfilesAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragment
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.search_empty_view.*
import javax.inject.Inject

class UsersSearchFragment : BaseFragment(), UsersSearchView, androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {

    @Inject lateinit var presenter: UsersSearchPresenter

    var queryString = ""
    lateinit var querySubscribe: Disposable

    private val profilesAdapter by lazy { ProfilesAdapter() }

    companion object {
        fun newInstance() = UsersSearchFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.feed_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter.subscribe(this)
        querySubscribe = (parentFragment as SearchFragment).querySubject.subscribe {
            swiperefresh.isRefreshing = true
            queryString = it
            presenter.searchProfiles(queryString)
        }
        swiperefresh.setOnRefreshListener(this)

        recyclerView?.apply {
            prepare()
            adapter = profilesAdapter
        }
        swiperefresh?.isRefreshing = false
        loadingView?.isVisible = false

    }

    override fun onRefresh() {
        if (queryString.length > 2) {
            loadingView?.isVisible = true
            presenter.searchProfiles(queryString)
        } else {
            loadingView?.isVisible = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
        querySubscribe.dispose()
    }

    override fun showUsers(entryList: List<Author>) {
        loadingView?.isVisible = false
        swiperefresh?.isRefreshing = false
        searchEmptyView.isVisible = entryList.isEmpty()
        profilesAdapter.apply {
            items.clear()
            items.addAll(entryList)
            notifyDataSetChanged()
        }
    }

}