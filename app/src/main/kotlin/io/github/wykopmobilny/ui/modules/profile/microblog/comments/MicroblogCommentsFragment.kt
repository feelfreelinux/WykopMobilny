package io.github.wykopmobilny.ui.modules.profile.microblog.comments

import android.os.Bundle
import android.view.View
import io.github.wykopmobilny.base.BaseEntryCommentFragment
import io.github.wykopmobilny.ui.modules.profile.ProfileActivity
import javax.inject.Inject

class MicroblogCommentsFragment : BaseEntryCommentFragment(), MicroblogCommentsView {

    companion object {
        fun newInstance() = MicroblogCommentsFragment()
    }

    @Inject
    lateinit var presenter: MicroblogCommentsPresenter

    override var loadDataListener: (Boolean) -> Unit = { presenter.loadData(it) }
    private val username by lazy { (activity as ProfileActivity).username }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
