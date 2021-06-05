package io.github.feelfreelinux.wykopmobilny.ui.modules.search.links

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseLinksFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragment
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.entries_fragment.*
import javax.inject.Inject

class LinkSearchFragment : BaseLinksFragment(), LinkSearchView {

    companion object {
        fun newInstance() = LinkSearchFragment()
    }

    @Inject lateinit var userManager: UserManagerApi

    @Inject lateinit var presenter: LinkSearchPresenter

    var query = ""
    lateinit var querySubscribe: Disposable
    override var loadDataListener: (Boolean) -> Unit = {
        presenter.searchLinks(query, it)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        querySubscribe = (parentFragment as SearchFragment).querySubject.subscribe {
            swipeRefresh.isRefreshing = true
            query = it
            presenter.searchLinks(query, true)
        }
        linksAdapter.linksActionListener = presenter
        linksAdapter.loadNewDataListener = { loadDataListener(false) }
        loadingView.isVisible = false
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        querySubscribe.dispose()
        super.onDestroy()
    }
}
