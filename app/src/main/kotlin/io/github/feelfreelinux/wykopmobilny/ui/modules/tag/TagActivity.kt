package io.github.feelfreelinux.wykopmobilny.ui.modules.tag

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagStateResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.activity_tag.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class TagActivity : BaseActivity(), TagActivityView {
    val tagString by lazy { intent.getStringExtra(EXTRA_TAG) }
    @Inject lateinit var navigator : NavigatorApi
    @Inject lateinit var presenter : TagActivityPresenter
    @Inject lateinit var userManagerApi : UserManagerApi
    private var tagMeta : TagMetaResponse? = null
    val tagPagerAdapter by lazy { TagPagerAdapter(tagString, resources, supportFragmentManager) }

    companion object {
        val EXTRA_TAG = "EXTRA_TAG"

        fun createIntent(context : Context, tag : String): Intent {
            val intent = Intent(context, TagActivity::class.java)
            intent.putExtra(TagActivity.EXTRA_TAG, tag)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)
        setSupportActionBar(toolbar)
        presenter.subscribe(this)
        presenter.tag = tagString
        fab.setOnClickListener {
            navigator.openAddEntryActivity(this, null, "#$tagString")
        }
        fab.isVisible = userManagerApi.isUserAuthorized()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "\n#" + tagString
        }

        pager.adapter = tagPagerAdapter
        tabLayout.setupWithViewPager(pager)
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
                    (tagPagerAdapter.registeredFragments.valueAt(i) as TagFragmentNotifier)
                            .onRefresh()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setMeta(tagMeta: TagMetaResponse) {
        this.tagMeta = tagMeta
        invalidateOptionsMenu()
    }

    override fun setObserveState(tagState: TagStateResponse) {
        tagMeta?.isBlocked = tagState.isBlocked
        tagMeta?.isObserved = tagState.isObserved
        invalidateOptionsMenu()
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing)
            for (i in 0 until tagPagerAdapter.registeredFragments.size()) {
                (tagPagerAdapter.registeredFragments.valueAt(i) as TagFragmentNotifier)
                        .removeDataFragment()
            }
    }
}