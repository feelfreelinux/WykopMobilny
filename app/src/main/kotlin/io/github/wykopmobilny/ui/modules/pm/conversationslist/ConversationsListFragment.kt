package io.github.wykopmobilny.ui.modules.pm.conversationslist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.wykopmobilny.R
import io.github.wykopmobilny.base.BaseActivity
import io.github.wykopmobilny.base.BaseFragment
import io.github.wykopmobilny.databinding.ActivityConversationsListBinding
import io.github.wykopmobilny.models.dataclass.Conversation
import io.github.wykopmobilny.ui.adapters.ConversationsListAdapter
import io.github.wykopmobilny.utils.prepare
import io.github.wykopmobilny.utils.viewBinding
import javax.inject.Inject

class ConversationsListFragment :
    BaseFragment(R.layout.activity_conversations_list),
    ConversationsListView,
    SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var presenter: ConversationsListPresenter

    private val binding by viewBinding(ActivityConversationsListBinding::bind)

    private val conversationsAdapter by lazy { ConversationsListAdapter() }

    companion object {
        const val DATA_FRAGMENT_TAG = "CONVERSATIONS_LIST"

        fun newInstance() = ConversationsListFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.messages)
        binding.swiperefresh.setOnRefreshListener(this)

        binding.recyclerView.apply {
            prepare()
            adapter = conversationsAdapter
        }
        binding.swiperefresh.isRefreshing = false
        presenter.subscribe(this)

        binding.loadingView.isVisible = true
        onRefresh()
    }

    override fun showConversations(conversations: List<Conversation>) {
        binding.loadingView.isVisible = false
        binding.swiperefresh.isRefreshing = false
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
