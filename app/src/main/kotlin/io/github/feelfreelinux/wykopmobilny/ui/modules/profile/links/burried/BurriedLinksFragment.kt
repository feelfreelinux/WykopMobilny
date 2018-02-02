package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.burried

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity

class BurriedLinksFragment : BaseFragment() {
    val username by lazy { (activity as ProfileActivity).username }

    companion object {
        val DATA_FRAGMENT_TAG = "BURRIED_LINKS_FRAGMENT"
        fun newInstance() : BurriedLinksFragment {
            val fragment = BurriedLinksFragment()
            return fragment
        }
    }
}