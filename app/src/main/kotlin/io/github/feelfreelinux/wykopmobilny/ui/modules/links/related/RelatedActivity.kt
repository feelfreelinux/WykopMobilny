package io.github.feelfreelinux.wykopmobilny.ui.modules.links.related

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.RelatedListAdapter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.addRelatedDialog
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.activity_conversations_list.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class RelatedActivity : BaseActivity(), androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener, RelatedView {

    companion object {
        const val DATA_FRAGMENT_TAG = "RELATED_LIST"
        const val EXTRA_LINKID = "LINK_ID_EXTRA"

        fun createIntent(linkId: Int, activity: Activity) =
            Intent(activity, RelatedActivity::class.java).apply {
                putExtra(EXTRA_LINKID, linkId)
            }
    }

    @Inject lateinit var presenter: RelatedPresenter
    @Inject lateinit var relatedAdapter: RelatedListAdapter
    @Inject lateinit var userManager: UserManagerApi

    private lateinit var relatedDataFragment: DataFragment<List<Related>>

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
            relatedAdapter.items.addAll(relatedDataFragment.data!!)
            loadingView?.isVisible = false
        }
    }

    override fun showRelated(related: List<Related>) {
        loadingView?.isVisible = false
        swiperefresh?.isRefreshing = false
        relatedAdapter.apply {
            items.clear()
            items.addAll(related)
            notifyDataSetChanged()
        }
    }

    override fun onRefresh() {
        presenter.getRelated()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        relatedDataFragment.data = relatedAdapter.items
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_related_menu, menu)
        menu?.findItem(R.id.add_related)?.isVisible = userManager.isUserAuthorized()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_related -> {
                addRelatedDialog(this) { url, description ->
                    presenter.addRelated(description, url)
                }.show()
            }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }

    override fun onPause() {
        if (isFinishing) supportFragmentManager.removeDataFragment(relatedDataFragment)
        super.onPause()
    }
}