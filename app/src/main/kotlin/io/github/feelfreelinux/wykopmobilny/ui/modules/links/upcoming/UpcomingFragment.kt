package io.github.feelfreelinux.wykopmobilny.ui.modules.links.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseLinksFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationInterface
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferencesApi
import kotlinx.android.synthetic.main.entries_fragment.*
import javax.inject.Inject

class UpcomingFragment : BaseLinksFragment(), UpcomingView {

    companion object {
        fun newInstance() = UpcomingFragment()
    }

    @Inject lateinit var presenter: UpcomingPresenter

    @Inject lateinit var linksPreferencesApi: LinksPreferencesApi

    override var loadDataListener: (Boolean) -> Unit = { presenter.getUpcomingLinks(it) }

    val navigation by lazy { activity as MainNavigationInterface }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        navigation.activityToolbar.overflowIcon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_sort)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.upcoming_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                swipeRefresh.isRefreshing = true
                loadDataListener(true)
            }
            R.id.sortByComments -> {
                presenter.sortBy = UpcomingPresenter.SORTBY_COMMENTS
                linksPreferencesApi.upcomingDefaultSort = UpcomingPresenter.SORTBY_COMMENTS
                setSubtitle()
                swipeRefresh.isRefreshing = true
                loadDataListener(true)
            }
            R.id.sortByVotes -> {
                presenter.sortBy = UpcomingPresenter.SORTBY_VOTES
                linksPreferencesApi.upcomingDefaultSort = UpcomingPresenter.SORTBY_VOTES
                setSubtitle()
                swipeRefresh.isRefreshing = true
                loadDataListener(true)
            }
            R.id.sortByDate -> {
                presenter.sortBy = UpcomingPresenter.SORTBY_DATE
                linksPreferencesApi.upcomingDefaultSort = UpcomingPresenter.SORTBY_DATE
                setSubtitle()
                swipeRefresh.isRefreshing = true
                loadDataListener(true)
            }
            R.id.sortByActive -> {
                presenter.sortBy = UpcomingPresenter.SORTBY_ACTIVE
                linksPreferencesApi.upcomingDefaultSort = UpcomingPresenter.SORTBY_ACTIVE
                setSubtitle()
                swipeRefresh.isRefreshing = true
                loadDataListener(true)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.wykopalisko)
        presenter.subscribe(this)
        presenter.sortBy = linksPreferencesApi.upcomingDefaultSort!!
        linksAdapter.linksActionListener = presenter
        linksAdapter.loadNewDataListener = { loadDataListener(false) }
        setSubtitle()
        loadDataListener(true)
    }

    private fun setSubtitle() {
        navigation.activityToolbar.setSubtitle(
            when (presenter.sortBy) {
                UpcomingPresenter.SORTBY_ACTIVE -> R.string.upcoming_sortby_active
                UpcomingPresenter.SORTBY_DATE -> R.string.upcoming_sortby_date
                UpcomingPresenter.SORTBY_VOTES -> R.string.upcoming_sortby_votes
                else -> R.string.upcoming_sortby_comments
            }
        )
    }
}
