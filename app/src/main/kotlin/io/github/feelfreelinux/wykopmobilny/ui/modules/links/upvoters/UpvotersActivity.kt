package io.github.feelfreelinux.wykopmobilny.ui.modules.links.upvoters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Upvoter
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.UpvoterListAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist.ConversationsListFragment
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.activity_conversations_list.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class UpvotersActivity : BaseActivity(), androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener, UpvotersView {

    private lateinit var upvotersDataFragment: DataFragment<List<Upvoter>>
    @Inject lateinit var presenter: UpvotersPresenter
    @Inject lateinit var upvotersAdapter: UpvoterListAdapter

    companion object {
        val DATA_FRAGMENT_TAG = "UPVOTERS_LIST"
        val EXTRA_LINKID = "LINK_ID_EXTRA"
        fun createIntent(linkId : Int, activity: Activity): Intent {
            val intent = Intent(activity, UpvotersActivity::class.java)
            intent.putExtra(EXTRA_LINKID, linkId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        upvotersDataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        setContentView(R.layout.activity_voterslist)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.upvoters)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        swiperefresh.setOnRefreshListener(this)
        recyclerView?.apply {
            prepare()
            adapter = upvotersAdapter
        }
        swiperefresh?.isRefreshing = false
        presenter.linkId = intent.getIntExtra(EXTRA_LINKID, -1)
        presenter.subscribe(this)

        if (upvotersDataFragment.data == null) {
            loadingView?.isVisible = true
            onRefresh()
        } else {
            upvotersAdapter.items.addAll(upvotersDataFragment.data!!)
            loadingView?.isVisible = false
        }
    }

    override fun showUpvoters(upvoter: List<Upvoter>) {
        loadingView?.isVisible = false
        swiperefresh?.isRefreshing = false
        upvotersAdapter.apply {
            items.clear()
            items.addAll(upvoter)
            notifyDataSetChanged()
        }
    }

    override fun onRefresh() {
        presenter.getUpvoters()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        upvotersDataFragment.data = upvotersAdapter.items
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) supportFragmentManager.removeDataFragment(upvotersDataFragment)
    }
}