package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index

import android.os.Bundle
import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.base.BaseEntryLinkFragment
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class MyWykopIndexFragment : BaseEntryLinkFragment() {
    @Inject
    lateinit var presenter: MyWykopIndexPresenter
    override var loadDataListener: (Boolean) -> Unit = {
        presenter.loadData(it)
    }

    @Inject
    lateinit var userManager: UserManagerApi

    companion object {
        fun newInstance(): Fragment {
            val fragment = MyWykopIndexFragment()
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        entriesAdapter.entryActionListener = presenter
        entriesAdapter.linkActionListener = presenter
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }
        presenter.loadData(true)
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }
}