package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.microblog.comments

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseEntryCommentFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import javax.inject.Inject

class MicroblogCommentsFragment : BaseEntryCommentFragment(), MicroblogCommentsView {

    companion object {
        fun newInstance() = MicroblogCommentsFragment()
    }

    @Inject lateinit var presenter: MicroblogCommentsPresenter

    override var loadDataListener: (Boolean) -> Unit = { presenter.loadData(it) }
    private val username by lazy { (activity as ProfileActivity).username }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        presenter.username = username
        entryCommentsAdapter.entryCommentActionListener = presenter
        entryCommentsAdapter.loadNewDataListener = { loadDataListener(false) }
        presenter.loadData(true)
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }
}