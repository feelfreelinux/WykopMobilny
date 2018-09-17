package io.github.feelfreelinux.wykopmobilny.ui.fragments.entries

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseEntriesFragment
import javax.inject.Inject

// This fragment shows list of provided entries
class EntriesFragment : BaseEntriesFragment() {

    @Inject
    lateinit var presenter: EntriesFragmentPresenter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        entriesAdapter.entryActionListener = presenter
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }
}