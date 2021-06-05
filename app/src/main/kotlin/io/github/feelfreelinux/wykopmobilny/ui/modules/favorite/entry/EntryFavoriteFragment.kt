package io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.entry

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseEntriesFragment
import javax.inject.Inject

class EntryFavoriteFragment : BaseEntriesFragment(), EntryFavoriteView {

    companion object {
        fun newInstance() = EntryFavoriteFragment()
    }

    @Inject
    lateinit var presenter: EntryFavoritePresenter
    override var loadDataListener: (Boolean) -> Unit = { presenter.loadData(it) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        entriesAdapter.entryActionListener = presenter
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }
        presenter.loadData(true)
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }
}
