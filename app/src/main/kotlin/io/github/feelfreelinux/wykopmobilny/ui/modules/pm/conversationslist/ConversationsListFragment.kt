package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Conversation
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.ConversationsListAdapter
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.InfiniteScrollListener
import kotlinx.android.synthetic.main.activity_conversations_list.*
import javax.inject.Inject

class ConversationsListFragment : BaseFragment(), ConversationsListView, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var conversationsDataFragment : DataFragment<List<Conversation>>
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
        conversationsDataFragment = fragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        WykopApp.uiInjector.inject(this)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.messages)
        swiperefresh.setOnRefreshListener(this)

        recyclerView.apply {
            prepare()
            adapter = conversationsAdapter
        }
        swiperefresh.isRefreshing = false
        presenter.subscribe(this)

        if (conversationsDataFragment.data == null) {
            loadingView.isVisible = true
            onRefresh()
        } else {
            conversationsAdapter.dataset.addAll(conversationsDataFragment.data!!)
            loadingView.isVisible = false
        }
    }

    override fun showConversations(items : List<Conversation>) {
        loadingView.isVisible = false
        swiperefresh.isRefreshing = false
        conversationsAdapter.apply {
            dataset.clear()
            dataset.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun onRefresh() {
        presenter.loadConversations()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        conversationsDataFragment.data = conversationsAdapter.dataset
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun onPause() {
        super.onPause()
        if (isRemoving) fragmentManager.removeDataFragment(conversationsDataFragment)
    }
}