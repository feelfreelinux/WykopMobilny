package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.comments

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity

class ProfileLinkCommentsFragment : BaseFragment() {
    val username by lazy { (activity as ProfileActivity).username }
    companion object {
        val DATA_FRAGMENT_TAG = "PROFILE_LINK_COMMENTS_FRAGMENT"
        fun newInstance() : ProfileLinkCommentsFragment {
            val fragment = ProfileLinkCommentsFragment()
            return fragment
        }
    }
}