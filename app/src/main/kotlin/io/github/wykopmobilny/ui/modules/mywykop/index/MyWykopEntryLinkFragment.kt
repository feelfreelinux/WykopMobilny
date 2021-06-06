package io.github.wykopmobilny.ui.modules.mywykop.index

import android.os.Bundle
import android.view.View
import io.github.wykopmobilny.base.BaseEntryLinkFragment
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class MyWykopEntryLinkFragment : BaseEntryLinkFragment() {

    companion object {
        const val TYPE_INDEX = "TYPE_INDEX"
        const val TYPE_TAGS = "TYPE_TAGS"
        const val TYPE_USERS = "TYPE_USERS"
        const val EXTRA_TYPE = "TYPE"

        /**
         * Type determines data coming to this fragment.
         * @param type Type string TYPE_INDEX / TYPE_TAGS / TYPE_USERS
         */
        fun newInstance(type: String): androidx.fragment.app.Fragment {
            val fragment = MyWykopEntryLinkFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var userManager: UserManagerApi

    @Inject
    lateinit var presenter: MyWykopEntryLinkPresenter

    private val extraType by lazy { arguments?.getString(EXTRA_TYPE) }

    override var loadDataListener: (Boolean) -> Unit = {
        when (extraType) {
            TYPE_INDEX -> presenter.loadIndex(it)
            TYPE_TAGS -> presenter.loadTags(it)
            TYPE_USERS -> presenter.loadUsers(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.subscribe(this)
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
