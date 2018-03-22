package io.github.feelfreelinux.wykopmobilny.ui.modules.links.upcoming

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.*
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFeedFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.fragments.*
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinkAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationInterface
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferencesApi
import javax.inject.Inject

class UpcomingFragment : BaseFeedFragment<Link>(), UpcomingView {
    @Inject override lateinit var feedAdapter : LinkAdapter
    @Inject lateinit var presenter : UpcomingPresenter
    val navigation by lazy { activity as MainNavigationInterface }
    @Inject lateinit var linksPreferencesApi : LinksPreferencesApi
    lateinit var dataFragment : DataFragment<SortedPagedDataModel<List<Link>>>

    companion object {
        val DATA_FRAGMENT_TAG = "UPCOMING_FRAGMENT_TAG"

        fun newInstance() : UpcomingFragment {
            return UpcomingFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        navigation.activityToolbar.overflowIcon = ContextCompat.getDrawable(activity!!, R.drawable.ic_sort)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.upcoming_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                isRefreshing = true
                onRefresh()
            }
            R.id.sortByComments -> {
                presenter.sortBy = UpcomingPresenter.SORTBY_COMMENTS
                linksPreferencesApi.upcomingDefaultSort = UpcomingPresenter.SORTBY_COMMENTS
                setSubtitle()
                isRefreshing = true
                onRefresh()
            }
            R.id.sortByVotes -> {
                presenter.sortBy = UpcomingPresenter.SORTBY_VOTES
                linksPreferencesApi.upcomingDefaultSort = UpcomingPresenter.SORTBY_VOTES
                setSubtitle()
                isRefreshing = true
                onRefresh()
            }
            R.id.sortByDate -> {
                presenter.sortBy = UpcomingPresenter.SORTBY_DATE
                linksPreferencesApi.upcomingDefaultSort = UpcomingPresenter.SORTBY_DATE
                setSubtitle()
                isRefreshing = true
                onRefresh()
            }
            R.id.sortByActive -> {
                presenter.sortBy = UpcomingPresenter.SORTBY_ACTIVE
                linksPreferencesApi.upcomingDefaultSort = UpcomingPresenter.SORTBY_ACTIVE
                setSubtitle()
                isRefreshing = true
                onRefresh()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        dataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        presenter.sortBy = linksPreferencesApi.upcomingDefaultSort!!
        dataFragment.data?.apply {
            presenter.page = page
            presenter.sortBy = sortby
        }
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.wykopalisko)
        presenter.subscribe(this)
        setSubtitle()
        initAdapter(dataFragment.data?.model)
    }

    fun setSubtitle() {
        navigation.activityToolbar.setSubtitle(when (presenter.sortBy) {
            UpcomingPresenter.SORTBY_ACTIVE -> R.string.upcoming_sortby_active
            UpcomingPresenter.SORTBY_DATE -> R.string.upcoming_sortby_date
            UpcomingPresenter.SORTBY_VOTES -> R.string.upcoming_sortby_votes
            else -> R.string.upcoming_sortby_comments
        })
    }

    override fun loadData(shouldRefresh: Boolean) {
        presenter.getUpcomingLinks(shouldRefresh)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        dataFragment.data = SortedPagedDataModel(presenter.page, presenter.sortBy, data)
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