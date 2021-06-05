package io.github.feelfreelinux.wykopmobilny.ui.modules.links.related

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.databinding.ActivityVoterslistBinding
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.RelatedListAdapter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.addRelatedDialog
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.viewBinding
import javax.inject.Inject

class RelatedActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener, RelatedView {

    companion object {
        private const val DATA_FRAGMENT_TAG = "RELATED_LIST"
        private const val EXTRA_LINKID = "LINK_ID_EXTRA"

        fun createIntent(linkId: Int, activity: Activity) =
            Intent(activity, RelatedActivity::class.java).apply {
                putExtra(EXTRA_LINKID, linkId)
            }
    }

    @Inject
    lateinit var presenter: RelatedPresenter

    @Inject
    lateinit var relatedAdapter: RelatedListAdapter

    @Inject
    lateinit var userManager: UserManagerApi

    private val binding by viewBinding(ActivityVoterslistBinding::inflate)

    private lateinit var relatedDataFragment: DataFragment<List<Related>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        relatedDataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar?.title = resources.getString(R.string.related)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.swiperefresh.setOnRefreshListener(this)
        binding.recyclerView.apply {
            prepare()
            adapter = relatedAdapter
        }
        binding.swiperefresh.isRefreshing = false
        presenter.linkId = intent.getIntExtra(EXTRA_LINKID, -1)
        presenter.subscribe(this)

        if (relatedDataFragment.data == null) {
            binding.loadingView.isVisible = true
            onRefresh()
        } else {
            relatedAdapter.items.addAll(relatedDataFragment.data!!)
            binding.loadingView.isVisible = false
        }
    }

    override fun showRelated(related: List<Related>) {
        binding.loadingView.isVisible = false
        binding.swiperefresh.isRefreshing = false
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
