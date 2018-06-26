package io.github.feelfreelinux.wykopmobilny.ui.modules.search.links


import android.os.Bundle
import androidx.fragment.app.Fragment
import io.github.feelfreelinux.wykopmobilny.base.BaseLinksFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragment
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.entries_fragment.*
import javax.inject.Inject

class LinkSearchFragment : BaseLinksFragment(), LinkSearchView {
    var query = ""

    lateinit var querySubscribe : Disposable

    @Inject
    lateinit var presenter: LinkSearchPresenter
    override var loadDataListener: (Boolean) -> Unit = {
        presenter.searchLinks(query, it)
    }

    @Inject
    lateinit var userManager: UserManagerApi

    companion object {
        fun newInstance(): androidx.fragment.app.Fragment {
            return  LinkSearchFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        linksAdapter.linksActionListener = presenter
        linksAdapter.loadNewDataListener = { loadDataListener(false) }
        loadingView.isVisible = false

    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe(this)
        querySubscribe = (parentFragment as SearchFragment).querySubject.subscribe {
            swipeRefresh.isRefreshing = true
            query = it
            presenter.searchLinks(query, true)
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
        querySubscribe.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }
}