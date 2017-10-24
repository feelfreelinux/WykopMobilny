package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
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
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

fun Context.openConversationsList() {
    val intent = Intent(this, ConversationsListActivity::class.java)
    startActivity(intent)
}

class ConversationsListActivity : BaseActivity(), ConversationsListView, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var conversationsDataFragment : DataFragment<PagedDataModel<List<Conversation>>>
    @Inject lateinit var presenter : ConversationsListPresenter
    private val conversationsAdapter by lazy { ConversationsListAdapter() }

    companion object {
        val DATA_FRAGMENT_TAG = "CONVERSATIONS_LIST"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations_list)
        conversationsDataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        WykopApp.uiInjector.inject(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.messages)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        swiperefresh.setOnRefreshListener(this)

        recyclerView.apply {
            prepare()
            adapter = conversationsAdapter
            clearOnScrollListeners()
            addOnScrollListener(InfiniteScrollListener({ loadMore() }, layoutManager as LinearLayoutManager))
        }
        swiperefresh.isRefreshing = false
        presenter.subscribe(this)

        if (conversationsDataFragment.data == null) {
            loadingView.isVisible = true
            onRefresh()
        } else {
            conversationsAdapter.addData(conversationsDataFragment.data!!.model, true)
            presenter.page = conversationsDataFragment.data!!.page
            loadingView.isVisible = false
        }

    }

    private fun loadMore() {
        presenter.loadConversations(false)
    }

    override fun disableLoading() {
        conversationsAdapter.disableLoading()
    }

    override fun showConversations(items : List<Conversation>, shouldCleanAdapter : Boolean) {
        loadingView.isVisible = false
        swiperefresh.isRefreshing = false
        conversationsAdapter.addData(items, shouldCleanAdapter)
    }

    override fun onRefresh() {
        presenter.loadConversations(true)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        conversationsDataFragment.data = PagedDataModel(presenter.page, conversationsAdapter.data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) supportFragmentManager.removeDataFragment(conversationsDataFragment)
    }
}