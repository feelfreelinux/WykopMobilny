package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.added

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseFeedFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinkAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import kotlinx.android.synthetic.main.feed_fragment.*
import javax.inject.Inject

class AddedLinksFragment : BaseFeedFragment<Link>(), AddedLinksView {
    val username by lazy { (activity as ProfileActivity).username }
    @Inject override lateinit var feedAdapter : LinkAdapter
    @Inject lateinit var presenter : AddedLinksPresenter
    lateinit var dataFragment : DataFragment<PagedDataModel<List<Link>>>
    companion object {
        val DATA_FRAGMENT_TAG = "ADDED_LINKS_FRAGMENT"
        fun newInstance() : AddedLinksFragment {
            val fragment = AddedLinksFragment()
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        presenter.username = username
        dataFragment = childFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        dataFragment.data?.apply {
            presenter.page = page
        }
        presenter.subscribe(this)
        initAdapter(dataFragment.data?.model)
    }

    override fun loadData(shouldRefresh: Boolean) {
        presenter.loadData(shouldRefresh)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::dataFragment.isInitialized)
        dataFragment.data = PagedDataModel(presenter.page , data)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.unsubscribe()
    }
}