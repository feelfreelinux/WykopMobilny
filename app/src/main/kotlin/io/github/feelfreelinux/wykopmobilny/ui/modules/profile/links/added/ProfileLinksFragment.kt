package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.added

import android.os.Bundle
import androidx.fragment.app.Fragment
import io.github.feelfreelinux.wykopmobilny.base.BaseLinksFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class ProfileLinksFragment : BaseLinksFragment(), ProfileLinksView {
    val extraType by lazy { arguments?.getString(EXTRA_TYPE) }
    val username by lazy { (activity as ProfileActivity).username }

    @Inject
    lateinit var presenter: ProfileLinksPresenter
    override var loadDataListener: (Boolean) -> Unit = {
        when (extraType) {
            TYPE_PUBLISHED -> presenter.loadPublished(it)
            TYPE_DIGGED -> presenter.loadDigged(it)
            TYPE_BURRIED -> presenter.loadBurried(it)
            TYPE_ADDED -> presenter.loadAdded(it)
        }
    }

    @Inject
    lateinit var userManager: UserManagerApi

    companion object {
        val TYPE_PUBLISHED = "TYPE_PUBLISHED"
        val TYPE_DIGGED = "TYPE_DIGGED"
        val TYPE_BURRIED = "TYPE_BURRIED"
        val TYPE_ADDED = "TYPE_ADDED"
        val EXTRA_TYPE = "TYPE"

        /**
         * Type determines data coming to this fragment.
         * @param type Type string TYPE_PUBLISHED / TYPE_DIGGED / TYPE_BURRIED / TYPE_ADDED
         */
        fun newInstance(type : String): androidx.fragment.app.Fragment {
            val fragment = ProfileLinksFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        presenter.username = username
        linksAdapter.linksActionListener = presenter
        linksAdapter.loadNewDataListener = { loadDataListener(false) }
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