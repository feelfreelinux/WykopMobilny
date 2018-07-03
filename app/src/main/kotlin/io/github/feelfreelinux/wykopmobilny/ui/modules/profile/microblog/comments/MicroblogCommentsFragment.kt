package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.microblog.comments

import android.os.Bundle
import androidx.fragment.app.Fragment
import io.github.feelfreelinux.wykopmobilny.base.BaseEntryCommentFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import javax.inject.Inject

class MicroblogCommentsFragment : BaseEntryCommentFragment(), MicroblogCommentsView {
    val username by lazy { (activity as ProfileActivity).username }
    @Inject
    lateinit var presenter: MicroblogCommentsPresenter
    override var loadDataListener: (Boolean) -> Unit = {
        presenter.loadData(it)
    }

    companion object {
        fun newInstance(): androidx.fragment.app.Fragment {
            return MicroblogCommentsFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        presenter.username = username
        entryCommentsAdapter.entryCommentActionListener = presenter
        entryCommentsAdapter.loadNewDataListener = { loadDataListener(false) }
        presenter.loadData(true)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }
}