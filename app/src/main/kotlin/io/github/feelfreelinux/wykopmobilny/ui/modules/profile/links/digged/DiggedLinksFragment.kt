package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.digged

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseFeedFragment
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinkAdapter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinkCommentAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import javax.inject.Inject

class DiggedLinksFragment : BaseFeedFragment<Link>(), DiggedLinksView {
    val username by lazy { (activity as ProfileActivity).username }
    @Inject override lateinit var feedAdapter : LinkAdapter
    @Inject lateinit var presenter : DiggedLinksPresenter
    lateinit var dataFragment : DataFragment<PagedDataModel<List<Link>>>

    companion object {
        val DATA_FRAGMENT_TAG = "DIGGED_LINKS_FRAGMENT"

        fun newInstance() : DiggedLinksFragment {
            val fragment = DiggedLinksFragment()
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