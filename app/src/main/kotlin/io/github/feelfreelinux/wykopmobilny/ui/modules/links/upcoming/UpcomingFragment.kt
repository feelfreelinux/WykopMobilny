package io.github.feelfreelinux.wykopmobilny.ui.modules.links.upcoming

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.*
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFeedFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinkAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationInterface
import javax.inject.Inject

class UpcomingFragment : BaseFeedFragment<Link>(), UpcomingView {
    @Inject override lateinit var feedAdapter : LinkAdapter
    @Inject lateinit var presenter : UpcomingPresenter
    val navigation by lazy { activity as MainNavigationInterface }
    lateinit var dataFragment : DataFragment<PagedDataModel<List<Link>>>

    companion object {
        val DATA_FRAGMENT_TAG = "UPCOMING_FRAGMENT_TAG"

        fun newInstance() : UpcomingFragment {
            return UpcomingFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        navigation.activityToolbar.overflowIcon = ContextCompat.getDrawable(activity!!, R.drawable.ic_sort)
        navigation.activityToolbar.subtitle = getString(R.string.upcoming_sortby_comments)
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
                navigation.activityToolbar.setSubtitle(R.string.upcoming_sortby_comments)
                onRefresh()
            }
            R.id.sortByVotes -> {
                presenter.sortBy = UpcomingPresenter.SORTBY_VOTES
                navigation.activityToolbar.setSubtitle(R.string.upcoming_sortby_votes)
                onRefresh()
            }
            R.id.sortByDate -> {
                presenter.sortBy = UpcomingPresenter.SORTBY_DATE
                navigation.activityToolbar.setSubtitle(R.string.upcoming_sortby_date)
                onRefresh()
            }
            R.id.sortByActive -> {
                presenter.sortBy = UpcomingPresenter.SORTBY_ACTIVE
                navigation.activityToolbar.setSubtitle(R.string.upcoming_sortby_active)
                onRefresh()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        dataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        dataFragment.data?.apply {
            presenter.page = page
        }
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.wykopalisko)
        presenter.subscribe(this)
        initAdapter(dataFragment.data?.model)
    }

    override fun loadData(shouldRefresh: Boolean) {
        presenter.getUpcomingLinks(shouldRefresh)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        dataFragment.data = PagedDataModel(presenter.page , data)
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