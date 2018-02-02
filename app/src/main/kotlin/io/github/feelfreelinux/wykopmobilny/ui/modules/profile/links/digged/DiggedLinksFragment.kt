package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.digged

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity

class DiggedLinksFragment : BaseFragment() {
    val username by lazy { (activity as ProfileActivity).username }
    companion object {
        val DATA_FRAGMENT_TAG = "DIGGED_LINKS_FRAGMENT"

        fun newInstance() : DiggedLinksFragment {
            val fragment = DiggedLinksFragment()
            return fragment
        }
    }
}