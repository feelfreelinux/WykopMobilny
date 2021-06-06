package io.github.wykopmobilny.ui.modules.profile.actions

import android.os.Bundle
import android.view.View
import io.github.wykopmobilny.base.BaseEntryLinkFragment
import io.github.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class ActionsFragment : BaseEntryLinkFragment(), ActionsView {

    companion object {
        fun newInstance() = ActionsFragment()
    }

    @Inject
    lateinit var userManager: UserManagerApi

    @Inject
    lateinit var presenter: ActionsFragmentPresenter

    override var loadDataListener: (Boolean) -> Unit = { presenter.getActions() }
    private val username by lazy { (activity as ProfileActivity).username }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.subscribe(this)
        presenter.username = username
        entriesAdapter.entryActionListener = presenter
        entriesAdapter.linkActionListener = presenter
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }
        loadDataListener(true)
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }
}
