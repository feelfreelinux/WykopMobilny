package io.github.feelfreelinux.wykopmobilny.ui.modules.links.downvoters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Downvoter
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.DownvoterListAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist.ConversationsListFragment
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.activity_conversations_list.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class DownvotersActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener, DownvotersView {

    private lateinit var downvotersDataFragment: DataFragment<List<Downvoter>>
    @Inject lateinit var presenter: DownvotersPresenter
    @Inject lateinit var downvotersAdapter: DownvoterListAdapter

    companion object {
        val DATA_FRAGMENT_TAG = "DOWNVOTERS_LIST"
        val EXTRA_LINKID = "LINK_ID_EXTRA"
        fun createIntent(linkId : Int, activity: Activity): Intent {
            val intent = Intent(activity, DownvotersActivity::class.java)
            intent.putExtra(EXTRA_LINKID, linkId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        downvotersDataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        setContentView(R.layout.activity_voterslist)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.downvoters)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        swiperefresh.setOnRefreshListener(this)
        recyclerView.apply {
            prepare()
            adapter = downvotersAdapter
        }
        swiperefresh.isRefreshing = false
        presenter.linkId = intent.getIntExtra(EXTRA_LINKID, -1)
        presenter.subscribe(this)

        if (downvotersDataFragment.data == null) {
            loadingView.isVisible = true
            onRefresh()
        } else {
            downvotersAdapter.dataset.addAll(downvotersDataFragment.data!!)
            loadingView.isVisible = false
        }
    }

    override fun showDownvoters(downvoters: List<Downvoter>) {
        loadingView.isVisible = false
        swiperefresh.isRefreshing = false
        downvotersAdapter.apply {
            dataset.clear()
            dataset.addAll(downvoters)
            notifyDataSetChanged()
        }
    }

    override fun onRefresh() {
        presenter.getDownvoters()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        downvotersDataFragment.data = downvotersAdapter.dataset
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) supportFragmentManager.removeDataFragment(downvotersDataFragment)
    }
}