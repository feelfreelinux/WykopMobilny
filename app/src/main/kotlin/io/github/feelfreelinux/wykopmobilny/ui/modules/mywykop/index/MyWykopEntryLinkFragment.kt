package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index

import android.os.Bundle
import androidx.fragment.app.Fragment
import io.github.feelfreelinux.wykopmobilny.base.BaseEntryLinkFragment
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class MyWykopEntryLinkFragment : BaseEntryLinkFragment() {
    val extraType by lazy { arguments?.getString(EXTRA_TYPE) }

    @Inject
    lateinit var presenter: MyWykopEntryLinkPresenter
    override var loadDataListener: (Boolean) -> Unit = {
        when (extraType) {
            TYPE_INDEX -> presenter.loadIndex(it)
            TYPE_TAGS -> presenter.loadTags(it)
            TYPE_USERS -> presenter.loadUsers(it)
        }
    }

    @Inject
    lateinit var userManager: UserManagerApi

    companion object {
        val TYPE_INDEX = "TYPE_INDEX"
        val TYPE_TAGS = "TYPE_TAGS"
        val TYPE_USERS = "TYPE_USERS"
        val EXTRA_TYPE = "TYPE"

        /**
         * Type determines data coming to this fragment.
         * @param type Type string TYPE_INDEX / TYPE_TAGS / TYPE_USERS
         */
        fun newInstance(type : String): androidx.fragment.app.Fragment {
            val fragment = MyWykopEntryLinkFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        entriesAdapter.entryActionListener = presenter
        entriesAdapter.linkActionListener = presenter
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }
        loadDataListener(true)
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