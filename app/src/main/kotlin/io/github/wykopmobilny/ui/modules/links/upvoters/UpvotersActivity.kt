package io.github.wykopmobilny.ui.modules.links.upvoters

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.isVisible
import io.github.wykopmobilny.R
import io.github.wykopmobilny.base.BaseActivity
import io.github.wykopmobilny.databinding.ActivityVoterslistBinding
import io.github.wykopmobilny.models.dataclass.Upvoter
import io.github.wykopmobilny.models.fragments.DataFragment
import io.github.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.wykopmobilny.models.fragments.removeDataFragment
import io.github.wykopmobilny.ui.adapters.UpvoterListAdapter
import io.github.wykopmobilny.utils.prepare
import io.github.wykopmobilny.utils.viewBinding
import javax.inject.Inject

class UpvotersActivity : BaseActivity(), androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener, UpvotersView {

    companion object {
        const val DATA_FRAGMENT_TAG = "UPVOTERS_LIST"
        const val EXTRA_LINK_ID = "LINK_ID_EXTRA"

        fun createIntent(linkId: Int, activity: Activity) =
            Intent(activity, UpvotersActivity::class.java).apply {
                putExtra(EXTRA_LINK_ID, linkId)
            }
    }

    @Inject
    lateinit var presenter: UpvotersPresenter

    @Inject
    lateinit var upvotersAdapter: UpvoterListAdapter

    private val binding by viewBinding(ActivityVoterslistBinding::inflate)

    private lateinit var upvotersDataFragment: DataFragment<List<Upvoter>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        upvotersDataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar?.title = resources.getString(R.string.upvoters)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.swiperefresh.setOnRefreshListener(this)
        binding.recyclerView.apply {
            prepare()
            adapter = upvotersAdapter
        }
        binding.swiperefresh.isRefreshing = false
        presenter.linkId = intent.getIntExtra(EXTRA_LINK_ID, -1)
        presenter.subscribe(this)

        if (upvotersDataFragment.data == null) {
            binding.loadingView.isVisible = true
            onRefresh()
        } else {
            upvotersAdapter.items.addAll(upvotersDataFragment.data!!)
            binding.loadingView.isVisible = false
        }
    }

    override fun showUpvoters(upvoter: List<Upvoter>) {
        binding.loadingView.isVisible = false
        binding.swiperefresh.isRefreshing = false
        upvotersAdapter.apply {
            items.clear()
            items.addAll(upvoter)
            notifyDataSetChanged()
        }
    }

    override fun onRefresh() = presenter.getUpvoters()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        upvotersDataFragment.data = upvotersAdapter.items
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }

    override fun onPause() {
        if (isFinishing) supportFragmentManager.removeDataFragment(upvotersDataFragment)
        super.onPause()
    }
}
