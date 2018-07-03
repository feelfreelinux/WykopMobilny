package io.github.feelfreelinux.wykopmobilny.ui.modules.links.related

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Upvoter
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.RelatedListAdapter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.UpvoterListAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist.ConversationsListFragment
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.activity_conversations_list.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class RelatedActivity : BaseActivity(), androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener, RelatedView {

    private lateinit var relatedDataFragment: DataFragment<List<Related>>
    @Inject lateinit var presenter: RelatedPresenter
    @Inject lateinit var relatedAdapter: RelatedListAdapter

    companion object {
        val DATA_FRAGMENT_TAG = "RELATED_LIST"
        val EXTRA_LINKID = "LINK_ID_EXTRA"
        fun createIntent(linkId : Int, activity: Activity): Intent {
            val intent = Intent(activity, RelatedActivity::class.java)
            intent.putExtra(EXTRA_LINKID, linkId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        relatedDataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        setContentView(R.layout.activity_voterslist)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.related)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        swiperefresh.setOnRefreshListener(this)
        recyclerView?.apply {
            prepare()
            adapter = relatedAdapter
        }
        swiperefresh?.isRefreshing = false
        presenter.linkId = intent.getIntExtra(EXTRA_LINKID, -1)
        presenter.subscribe(this)

        if (relatedDataFragment.data == null) {
            loadingView?.isVisible = true
            onRefresh()
        } else {
            relatedAdapter.dataset.addAll(relatedDataFragment.data!!)
            loadingView?.isVisible = false
        }
    }

    override fun showRelated(related: List<Related>) {
        loadingView?.isVisible = false
        swiperefresh?.isRefreshing = false
        relatedAdapter.apply {
            dataset.clear()
            dataset.addAll(related)
            notifyDataSetChanged()
        }
    }

    override fun onRefresh() {
        presenter.getRelated()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        relatedDataFragment.data = relatedAdapter.dataset
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
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
        if (isFinishing) supportFragmentManager.removeDataFragment(relatedDataFragment)
    }
}