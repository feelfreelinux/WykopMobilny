package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.comments

import android.os.Bundle
import androidx.fragment.app.Fragment
import io.github.feelfreelinux.wykopmobilny.base.BaseLinkCommentFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import javax.inject.Inject

class ProfileLinkCommentsFragment : BaseLinkCommentFragment(), ProfileLinkCommentsView {
    val username by lazy { (activity as ProfileActivity).username }
    @Inject
    lateinit var presenter: ProfileLinksFragmentPresenter
    override var loadDataListener: (Boolean) -> Unit = {
        presenter.loadData(it)
    }

    companion object {
        fun newInstance(): androidx.fragment.app.Fragment {
            return ProfileLinkCommentsFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        presenter.username = username
        linkCommentsAdapter.linkCommentActionListener = presenter
        linkCommentsAdapter.loadNewDataListener = { loadDataListener(false) }
        presenter.loadData(true)
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }
}