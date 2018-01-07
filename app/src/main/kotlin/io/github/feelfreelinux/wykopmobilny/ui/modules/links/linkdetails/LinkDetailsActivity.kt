package io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinkDetailsAdapter
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.activity_link_details.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class LinkDetailsActivity : BaseActivity(), LinkDetailsView, SwipeRefreshLayout.OnRefreshListener {
    companion object {
        val EXTRA_LINK = "LINK_PARCEL"
        val EXTRA_FRAGMENT_KEY = "LINK_ACTIVITY_#"

        fun createIntent(context: Context, link : Link): Intent {
            val intent = Intent(context, LinkDetailsActivity::class.java)
            intent.putExtra(EXTRA_LINK, link)
            return intent
        }
    }

    @Inject lateinit var clipboardHelper : ClipboardHelperApi
    @Inject lateinit var presenter: LinkDetailsPresenter
    private lateinit var linkFragmentData: DataFragment<Link>
    private val link by lazy { intent.getParcelableExtra(EXTRA_LINK) as Link }
    private val adapter by lazy { LinkDetailsAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_details)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = null
            setDisplayHomeAsUpEnabled(true)
        }
        supportActionBar?.title = null
        presenter.linkId = link.id

        // Prepare RecyclerView
        recyclerView.apply {
            prepare()
            // Set margin, adapter
            this.adapter = this@LinkDetailsActivity.adapter
        }

        adapter.link = link
        adapter.notifyDataSetChanged()
        // Prepare InputToolbar
        // inputToolbar.inputToolbarListener = this

        swiperefresh.setOnRefreshListener(this)
        linkFragmentData = supportFragmentManager.getDataFragmentInstance(EXTRA_FRAGMENT_KEY + link.id)
        if (linkFragmentData.data != null) {
            adapter.link = linkFragmentData.data
            adapter.notifyDataSetChanged()
            loadingView.isVisible = false
        } else {
            adapter.link = link
            adapter.notifyDataSetChanged()
            loadingView.isVisible = true
            presenter.loadComments()
        }
    }

    private fun loadData() {
        if (linkFragmentData.data != null)
            adapter.link = linkFragmentData.data
        else {
            // Trigger data loading
            loadingView.isVisible = true
            presenter.loadComments()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe(this)
        presenter.linkId = link.id
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.entry_fragment_menu, menu)
        return true
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        linkFragmentData.data = adapter.link
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) supportFragmentManager.removeDataFragment(linkFragmentData)
        presenter.unsubscribe()
    }


    override fun onRefresh() {
        presenter.loadComments()
    }

    override fun showLinkComments(comments: List<LinkComment>) {
        adapter.link?.comments = comments
        loadingView.isVisible = false
        swiperefresh.isRefreshing = false
        adapter.notifyDataSetChanged()
        /*if (intent.hasExtra(EXTRA_COMMENT_ID) && entryFragmentData.data == null) {
            entry.comments.forEachIndexed({ index, comment ->
                if (comment.id == intent.getIntExtra(EXTRA_COMMENT_ID, -1))
                    recyclerView.scrollToPosition(index + 1)
            })
        }*/
    }
}