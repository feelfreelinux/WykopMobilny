package io.github.wykopmobilny.ui.modules.search.users

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.wykopmobilny.R
import io.github.wykopmobilny.base.BaseFragment
import io.github.wykopmobilny.databinding.FeedFragmentBinding
import io.github.wykopmobilny.models.dataclass.Author
import io.github.wykopmobilny.ui.adapters.ProfilesAdapter
import io.github.wykopmobilny.ui.modules.search.SearchFragment
import io.github.wykopmobilny.utils.prepare
import io.github.wykopmobilny.utils.viewBinding
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class UsersSearchFragment : BaseFragment(R.layout.feed_fragment), UsersSearchView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var presenter: UsersSearchPresenter

    private val binding by viewBinding(FeedFragmentBinding::bind)

    var queryString = ""
    lateinit var querySubscribe: Disposable

    private val profilesAdapter by lazy { ProfilesAdapter() }

    companion object {
        fun newInstance() = UsersSearchFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.subscribe(this)
        querySubscribe = (parentFragment as SearchFragment).querySubject.subscribe {
            binding.swiperefresh.isRefreshing = true
            queryString = it
            presenter.searchProfiles(queryString)
        }
        binding.swiperefresh.setOnRefreshListener(this)

        binding.recyclerView.apply {
            prepare()
            adapter = profilesAdapter
        }
        binding.swiperefresh.isRefreshing = false
        binding.loadingView.isVisible = false
    }

    override fun onRefresh() {
        if (queryString.length > 2) {
            binding.loadingView.isVisible = true
            presenter.searchProfiles(queryString)
        } else {
            binding.loadingView.isVisible = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
        querySubscribe.dispose()
    }

    override fun showUsers(entryList: List<Author>) {
        binding.loadingView.isVisible = false
        binding.swiperefresh.isRefreshing = false
        binding.empty.searchEmptyView.isVisible = entryList.isEmpty()
        profilesAdapter.apply {
            items.clear()
            items.addAll(entryList)
            notifyDataSetChanged()
        }
    }
}
