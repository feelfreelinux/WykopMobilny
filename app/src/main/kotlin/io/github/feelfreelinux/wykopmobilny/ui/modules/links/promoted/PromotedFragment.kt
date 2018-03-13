package io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted

import android.os.Bundle
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
import javax.inject.Inject

class PromotedFragment : BaseFeedFragment<Link>(), PromotedView {
    @Inject override lateinit var feedAdapter : LinkAdapter
    @Inject lateinit var presenter : PromotedPresenter
    lateinit var dataFragment : DataFragment<PagedDataModel<List<Link>>>

    companion object {
        val DATA_FRAGMENT_TAG = "PROMOTED_FRAGMENT_TAG"

        fun newInstance() : PromotedFragment {
            return PromotedFragment()
        }
    }

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
        dataFragment.data?.apply {
            presenter.page = page
        }
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.main_page)
        presenter.subscribe(this)
        initAdapter(dataFragment.data?.model)
    }

    override fun loadData(shouldRefresh: Boolean) {
        presenter.getPromotedLinks(shouldRefresh)
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