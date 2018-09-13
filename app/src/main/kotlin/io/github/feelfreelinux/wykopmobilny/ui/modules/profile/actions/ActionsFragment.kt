package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.actions

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseEntryLinkFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class ActionsFragment : BaseEntryLinkFragment(), ActionsView {

    companion object {
        fun newInstance() = ActionsFragment()
    }

    @Inject lateinit var userManager: UserManagerApi
    @Inject lateinit var presenter: ActionsFragmentPresenter

    override var loadDataListener: (Boolean) -> Unit = { presenter.getActions() }
    val username by lazy { (activity as ProfileActivity).username }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        presenter.username = username
        entriesAdapter.entryActionListener = presenter
        entriesAdapter.linkActionListener = presenter
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }
        loadDataListener(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }
}