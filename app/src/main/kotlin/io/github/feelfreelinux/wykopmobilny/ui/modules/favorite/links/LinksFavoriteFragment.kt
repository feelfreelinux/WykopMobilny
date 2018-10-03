package io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.links

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseLinksFragment
import javax.inject.Inject

class LinksFavoriteFragment : BaseLinksFragment(), LinksFavoriteView {

    companion object {
        fun newInstance() = LinksFavoriteFragment()
    }

    @Inject lateinit var presenter: LinksFavoritePresenter

    override var loadDataListener: (Boolean) -> Unit = {
        presenter.loadData(it)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        linksAdapter.linksActionListener = presenter
        linksAdapter.loadNewDataListener = { loadDataListener(false) }
        presenter.loadData(true)
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }
}