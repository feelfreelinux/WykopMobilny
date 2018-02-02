package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.added

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity

class AddedLinksFragment : BaseFragment() {
    val username by lazy { (activity as ProfileActivity).username }
    companion object {
        val DATA_FRAGMENT_TAG = "ADDED_LINKS_FRAGMENT"
        fun newInstance() : AddedLinksFragment {
            val fragment = AddedLinksFragment()
            return fragment
        }
    }
}