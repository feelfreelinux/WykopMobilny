package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.microblog.entries

import android.os.Bundle
import androidx.fragment.app.Fragment
import io.github.feelfreelinux.wykopmobilny.base.BaseEntriesFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import javax.inject.Inject

class MicroblogEntriesFragment : BaseEntriesFragment(), MicroblogEntriesView {
    val username by lazy { (activity as ProfileActivity).username }

    @Inject
    lateinit var presenter: MicroblogEntriesPresenter
    override var loadDataListener: (Boolean) -> Unit = {
        presenter.loadData(it)
    }

    companion object {
        fun newInstance(): androidx.fragment.app.Fragment {
            return MicroblogEntriesFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        presenter.username = username
        entriesAdapter.entryActionListener = presenter
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }
        presenter.loadData(true)
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }
}