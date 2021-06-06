package io.github.wykopmobilny.ui.modules.links.promoted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import io.github.wykopmobilny.R
import io.github.wykopmobilny.base.BaseActivity
import io.github.wykopmobilny.base.BaseLinksFragment
import io.github.wykopmobilny.base.BaseNavigationView
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.ui.modules.mainnavigation.MainNavigationInterface
import javax.inject.Inject

class PromotedFragment : BaseLinksFragment(), PromotedView, BaseNavigationView {

    companion object {
        fun newInstance() = PromotedFragment()
    }

    @Inject
    lateinit var presenter: PromotedPresenter

    @Inject
    lateinit var navigator: NewNavigatorApi

    private val navigation by lazy { activity as MainNavigationInterface }
    private val fab by lazy { navigation.floatingButton }

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
                binding.swipeRefresh.isRefreshing = true
                presenter.getPromotedLinks(true)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
