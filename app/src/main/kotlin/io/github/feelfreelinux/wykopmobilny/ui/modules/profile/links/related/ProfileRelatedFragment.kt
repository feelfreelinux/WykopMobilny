package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.related

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity

class ProfileRelatedFragment : BaseFragment() {
    val username by lazy { (activity as ProfileActivity).username }
    companion object {
        val DATA_FRAGMENT_TAG = "PROFILE_RELATED_LINKS_FRAGMENT"

        fun newInstance() : ProfileRelatedFragment {
            val fragment = ProfileRelatedFragment()
            return fragment
        }
    }
}