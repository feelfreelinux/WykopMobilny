package io.github.wykopmobilny.ui.modules.favorite.links

import android.os.Bundle
import android.view.View
import io.github.wykopmobilny.base.BaseLinksFragment
import javax.inject.Inject

class LinksFavoriteFragment : BaseLinksFragment(), LinksFavoriteView {

    companion object {
        fun newInstance() = LinksFavoriteFragment()
    }

    @Inject
    lateinit var presenter: LinksFavoritePresenter

    override var loadDataListener: (Boolean) -> Unit = {
        presenter.loadData(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
