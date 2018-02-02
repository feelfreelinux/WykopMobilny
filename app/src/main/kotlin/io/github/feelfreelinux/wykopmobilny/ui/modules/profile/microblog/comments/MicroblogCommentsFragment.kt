package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.microblog.comments

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.utils.printout

class MicroblogCommentsFragment : BaseFragment() {
    val username by lazy { (activity as ProfileActivity).username }
    companion object {
        val DATA_FRAGMENT_TAG = "MICROBLOG_COMMENTS_FRAGMENT"

        fun newInstance() : MicroblogCommentsFragment {
            return MicroblogCommentsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}