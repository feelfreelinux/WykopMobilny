package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.published

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity

class PublishedLinksFragment : BaseFragment() {
    val username by lazy { (activity as ProfileActivity).username }
    companion object {
        val DATA_FRAGMENT_TAG = "PUBLISHED_LINKS_FRAGMENT"

        fun newInstance() : PublishedLinksFragment {
            val fragment = PublishedLinksFragment()
            return fragment
        }
    }
}