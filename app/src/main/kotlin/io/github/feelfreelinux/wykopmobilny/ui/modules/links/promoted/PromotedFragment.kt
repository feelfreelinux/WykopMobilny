package io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseLinksFragment
import io.github.feelfreelinux.wykopmobilny.base.BaseNavigationView
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationInterface
import kotlinx.android.synthetic.main.entries_fragment.*
import javax.inject.Inject

class PromotedFragment : BaseLinksFragment(), PromotedView, BaseNavigationView {

    companion object {
        fun newInstance() = PromotedFragment()
    }

    @Inject lateinit var presenter: PromotedPresenter

    @Inject lateinit var navigator: NewNavigatorApi

    val navigation by lazy { activity as MainNavigationInterface }
    val fab by lazy { navigation.floatingButton }

    override var loadDataListener: (Boolean) -> Unit = { presenter.getPromotedLinks(it) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.refresh_layout, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                swipeRefresh.isRefreshing = true
                presenter.getPromotedLinks(true)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fab.setOnClickListener { navigator.openAddLinkActivity() }
        presenter.subscribe(this)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.main_page)

        linksAdapter.linksActionListener = presenter
        linksAdapter.loadNewDataListener = { loadDataListener(false) }
        presenter.getPromotedLinks(true)
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }
}
