package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagMeta
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add.createNewEntry
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.TagLinkParser
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class TagActivity : BaseActivity(), TagView {
    private lateinit var entryTag : String
    lateinit var tagDataFragment : DataFragment<PagedDataModel<List<Entry>>>
    @Inject lateinit var userManager : UserManagerApi
    @Inject lateinit var presenter : TagPresenter
    private var tagMeta : TagMeta? = null

    companion object {
        val EXTRA_TAG = "EXTRA_TAG"
        val EXTRA_TAG_DATA_FRAGMENT = "DATA_FRAGMENT_#"

        fun createIntent(context : Context, tag : String): Intent {
            val intent = Intent(context, TagActivity::class.java)
            intent.putExtra(TagActivity.EXTRA_TAG, tag)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        setSupportActionBar(toolbar)

        WykopApp.uiInjector.inject(this)

        entryTag = intent.getStringExtra(EXTRA_TAG)?: TagLinkParser.getTag(intent.data.toString())
        tagDataFragment = supportFragmentManager.getDataFragmentInstance(EXTRA_TAG_DATA_FRAGMENT + entryTag)
        tagDataFragment.data?.apply {
            presenter.page = page
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "#" + entryTag
        }

        presenter.tag = entryTag
        presenter.subscribe(this)
        feedRecyclerView.apply {
            presenter = this@TagActivity.presenter
            fab = this@TagActivity.fab
            initAdapter(tagDataFragment.data?.model)
            onFabClickedListener = {
                context.createNewEntry(null)
            }
        }
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tag_menu, menu)

        if (userManager.isUserAuthorized()) {
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
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        tagDataFragment.data = PagedDataModel(presenter.page, feedRecyclerView.entries)
    }

    override fun setMeta(tagMeta: TagMeta) {
        this.tagMeta = tagMeta
        invalidateOptionsMenu()
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) supportFragmentManager.removeDataFragment(tagDataFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun addDataToAdapter(entryList: List<Entry>, shouldClearAdapter: Boolean) =
        feedRecyclerView.addDataToAdapter(entryList, shouldClearAdapter)

    override fun disableLoading() {
        feedRecyclerView.disableLoading()
    }
}