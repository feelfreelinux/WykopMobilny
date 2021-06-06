package io.github.wykopmobilny.ui.modules.tag

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.wykopmobilny.R
import io.github.wykopmobilny.api.responses.ObserveStateResponse
import io.github.wykopmobilny.api.responses.TagMetaResponse
import io.github.wykopmobilny.base.BaseActivity
import io.github.wykopmobilny.databinding.ActivityTagBinding
import io.github.wykopmobilny.ui.modules.NavigatorApi
import io.github.wykopmobilny.utils.loadImage
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.wykopmobilny.utils.viewBinding
import javax.inject.Inject

class TagActivity : BaseActivity(), TagActivityView {

    companion object {
        private const val EXTRA_TAG = "EXTRA_TAG"

        fun createIntent(context: Context, tag: String): Intent {
            val intent = Intent(context, TagActivity::class.java)
            intent.putExtra(EXTRA_TAG, tag)
            return intent
        }
    }

    @Inject
    lateinit var navigator: NavigatorApi

    @Inject
    lateinit var presenter: TagActivityPresenter

    @Inject
    lateinit var userManagerApi: UserManagerApi

    private val binding by viewBinding(ActivityTagBinding::inflate)

    override val enableSwipeBackLayout: Boolean = true
    private val tagString by lazy { intent.getStringExtra(EXTRA_TAG)!! }
    private var tagMeta: TagMetaResponse? = null
    private val tagPagerAdapter by lazy { TagPagerAdapter(tagString, resources, supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar.toolbar)
        presenter.subscribe(this)
        presenter.tag = tagString
        binding.fab.setOnClickListener {
            navigator.openAddEntryActivity(this, null, "#$tagString")
        }
        binding.fab.isVisible = userManagerApi.isUserAuthorized()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "\n#$tagString"
        }

        binding.pager.adapter = tagPagerAdapter
        tagMeta?.let {
            setMeta(tagMeta!!)
        }
        binding.tabLayout.setupWithViewPager(binding.pager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tag_menu, menu)
        if (userManagerApi.isUserAuthorized()) {
            tagMeta?.apply {
                menu.apply {
                    if (isObserved) {
                        findItem(R.id.action_unobserve).isVisible = true
                    } else if (!isBlocked) {
                        findItem(R.id.action_observe).isVisible = true
                        findItem(R.id.action_block).isVisible = true
                    } else if (isBlocked) {
                        findItem(R.id.action_unblock).isVisible = true
                    }
                }
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_observe -> presenter.observeTag()
            R.id.action_unobserve -> presenter.unobserveTag()
            R.id.action_block -> presenter.blockTag()
            R.id.action_unblock -> presenter.unblockTag()
            android.R.id.home -> finish()
            R.id.refresh -> {
                for (i in 0 until tagPagerAdapter.registeredFragments.size()) {
                    (tagPagerAdapter.registeredFragments.valueAt(i) as? SwipeRefreshLayout.OnRefreshListener)?.onRefresh()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setMeta(tagMeta: TagMetaResponse) {
        this.tagMeta = tagMeta
        binding.backgroundImg.isVisible = tagMeta.background != null
        tagMeta.background?.let { background ->
            binding.backgroundImg.loadImage(background)
            binding.toolbar.toolbar.setBackgroundResource(R.drawable.gradient_toolbar_up)
        }
        invalidateOptionsMenu()
    }

    override fun setObserveState(tagState: ObserveStateResponse) {
        tagMeta?.isBlocked = tagState.isBlocked
        tagMeta?.isObserved = tagState.isObserved
        invalidateOptionsMenu()
    }
}
