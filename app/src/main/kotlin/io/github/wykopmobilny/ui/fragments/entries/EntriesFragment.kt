package io.github.wykopmobilny.ui.fragments.entries

import android.os.Bundle
import android.view.View
import io.github.wykopmobilny.base.BaseEntriesFragment
import javax.inject.Inject

// This fragment shows list of provided entries
class EntriesFragment : BaseEntriesFragment() {

    @Inject
    lateinit var presenter: EntriesFragmentPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.subscribe(this)
        entriesAdapter.entryActionListener = presenter
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }
}
