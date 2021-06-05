package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.microblog.entries

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseEntriesFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import javax.inject.Inject

class MicroblogEntriesFragment : BaseEntriesFragment(), MicroblogEntriesView {

    companion object {
        fun newInstance() = MicroblogEntriesFragment()
    }

    @Inject lateinit var presenter: MicroblogEntriesPresenter
    private val username by lazy { (activity as ProfileActivity).username }
    override var loadDataListener: (Boolean) -> Unit = { presenter.loadData(it) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        presenter.username = username
        entriesAdapter.entryActionListener = presenter
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }
        presenter.loadData(true)
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }
}
