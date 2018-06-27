package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Conversation
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.ConversationsListAdapter
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.activity_conversations_list.*
import javax.inject.Inject

class ConversationsListFragment : BaseFragment(), ConversationsListView, androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
    @Inject lateinit var presenter : ConversationsListPresenter
    private val conversationsAdapter by lazy { ConversationsListAdapter() }

    companion object {
        val DATA_FRAGMENT_TAG = "CONVERSATIONS_LIST"
        fun newInstance(): Fragment {
            return ConversationsListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_conversations_list, container, false)
    }

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

        loadingView?.isVisible = true
        onRefresh()
    }

    override fun showConversations(items : List<Conversation>) {
        loadingView?.isVisible = false
        swiperefresh?.isRefreshing = false
        conversationsAdapter.apply {
            dataset.clear()
            dataset.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun onRefresh() {
        presenter.loadConversations()
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
        presenter.dispose()
    }
}