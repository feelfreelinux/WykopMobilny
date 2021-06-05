package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Conversation
import io.github.feelfreelinux.wykopmobilny.ui.adapters.ConversationsListAdapter
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.activity_conversations_list.*
import javax.inject.Inject

class ConversationsListFragment : BaseFragment(), ConversationsListView, SwipeRefreshLayout.OnRefreshListener {

    @Inject lateinit var presenter: ConversationsListPresenter
    private val conversationsAdapter by lazy { ConversationsListAdapter() }

    companion object {
        const val DATA_FRAGMENT_TAG = "CONVERSATIONS_LIST"

        fun newInstance() = ConversationsListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.activity_conversations_list, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.messages)
        swiperefresh.setOnRefreshListener(this)

        recyclerView?.apply {
            prepare()
            adapter = conversationsAdapter
        }
        swiperefresh?.isRefreshing = false
        presenter.subscribe(this)

        loadingView.isVisible = true
        onRefresh()
    }

    override fun showConversations(conversations: List<Conversation>) {
        loadingView?.isVisible = false
        swiperefresh?.isRefreshing = false
        conversationsAdapter.apply {
            items.clear()
            items.addAll(conversations)
            notifyDataSetChanged()
        }
    }

    override fun onRefresh() = presenter.loadConversations()

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }
}
