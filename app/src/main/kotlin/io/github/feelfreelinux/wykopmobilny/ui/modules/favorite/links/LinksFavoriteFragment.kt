package io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.links

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseFeedFragment
import io.github.feelfreelinux.wykopmobilny.base.BaseLinksFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinkAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.FavoriteFragmentNotifier
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.LinksFragment
import io.github.feelfreelinux.wykopmobilny.utils.printout
import javax.inject.Inject

class LinksFavoriteFragment : BaseLinksFragment(), LinksFavoriteView {
    @Inject lateinit var presenter : LinksFavoritePresenter

    override var loadDataListener: (Boolean) -> Unit = {
        presenter.loadData(it)
    }

    companion object {
        fun newInstance() : LinksFavoriteFragment {
            return LinksFavoriteFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        linksAdapter.linksActionListener = presenter
        linksAdapter.loadNewDataListener = { loadDataListener(false) }
        presenter.loadData(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }
}