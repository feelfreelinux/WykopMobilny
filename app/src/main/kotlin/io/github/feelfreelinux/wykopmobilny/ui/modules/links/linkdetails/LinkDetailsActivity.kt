package io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.UserManager
import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinkDetailsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import io.github.feelfreelinux.wykopmobilny.ui.widgets.InputToolbarListener
import io.github.feelfreelinux.wykopmobilny.utils.*
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.LinkParser
import kotlinx.android.synthetic.main.activity_link_details.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class LinkDetailsActivity : BaseActivity(), LinkDetailsView, SwipeRefreshLayout.OnRefreshListener, InputToolbarListener {
    companion object {
        val EXTRA_LINK = "LINK_PARCEL"
        val EXTRA_FRAGMENT_KEY = "LINK_ACTIVITY_#"
        val EXTRA_LINK_ID = "EXTRA_LINKID"
        val EXTRA_COMMENT_ID = "EXTRA_COMMENT_ID"
        val EXTRA_SORTBY = "EXTRA_SORTBY"

        fun createIntent(context: Context, link : Link): Intent {
            val intent = Intent(context, LinkDetailsActivity::class.java)
            intent.putExtra(EXTRA_LINK, link)
            return intent
        }

        fun createIntent(context: Context, linkId : Int, commentId : Int? = null): Intent {
            val intent = Intent(context, LinkDetailsActivity::class.java)
            intent.putExtra(EXTRA_LINK_ID, linkId)
            intent.putExtra(EXTRA_COMMENT_ID, commentId)
            return intent
        }
    }

    var replyLinkId : Int = 0

    val link by lazy { intent.getParcelableExtra<Link>(EXTRA_LINK) }
    val linkId by lazy {
        if (intent.hasExtra(EXTRA_LINK)) link.id else
        intent.getIntExtra(EXTRA_LINK_ID, -1)
    }

    val linkCommentId by lazy {
        intent.getIntExtra(EXTRA_COMMENT_ID, -1)
    }

    override fun getReplyCommentId(): Int {
        return if (replyLinkId != 0 && inputToolbar.textBody.contains("@")) replyLinkId
        else -1
    }

    @Inject lateinit var userManagerApi : UserManagerApi
    @Inject lateinit var suggestionsApi : SuggestApi
    @Inject lateinit var clipboardHelper : ClipboardHelperApi
    @Inject lateinit var presenter: LinkDetailsPresenter
    private lateinit var linkFragmentData: DataFragment<Pair<Link, String>> // link, sortby
    @Inject lateinit var adapter : LinkDetailsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_details)
        setSupportActionBar(toolbar)
        presenter.subscribe(this)
        adapter.collapseListener = {
            isCollapsed, id ->
            printout(isCollapsed.toString() + " s " + id.toString())
            adapter.link?.comments?.forEach {
                when (id) {
                    it.id -> {
                        it.isCollapsed = isCollapsed
                        it.isParentCollapsed = false
                    }
                    it.parentId -> it.isParentCollapsed = isCollapsed
                }
            }
            adapter.notifyDataSetChanged()
        }
        supportActionBar?.apply {
            title = null
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_sort)

        adapter.onReplyClickedListener = {
            replyLinkId = it.id
            inputToolbar.addAddressant(it.author.nick)
        }

        // Prepare RecyclerView
        recyclerView.apply {
            prepare()
            // Set margin, adapter
            this.adapter = this@LinkDetailsActivity.adapter
        }
        supportActionBar?.title = "Znalezisko"
        presenter.linkId = linkId
        if (intent.hasExtra(EXTRA_LINK)) {
            adapter.link = link
            adapter.notifyDataSetChanged()
        }
        adapter.highlightCommentId = linkCommentId

        // Prepare InputToolbar
        inputToolbar.setup(userManagerApi, suggestionsApi)
        inputToolbar.inputToolbarListener = this

        swiperefresh.setOnRefreshListener(this)
        linkFragmentData = supportFragmentManager.getDataFragmentInstance(EXTRA_FRAGMENT_KEY + linkId)
        if (linkFragmentData.data != null) {
            adapter.link = linkFragmentData.data?.first
            presenter.sortBy = linkFragmentData.data?.second ?: "best"
            adapter.notifyDataSetChanged()
            loadingView.isVisible = false
        } else {
            adapter.notifyDataSetChanged()
            loadingView.isVisible = true
            hideInputToolbar()
            presenter.loadComments()
            presenter.updateLink()
        }

        setSubtitle()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.link_details_menu, menu)
        return true
    }

    fun setSubtitle() {
        supportActionBar?.setSubtitle(when(presenter.sortBy) {
            "new" -> R.string.sortby_newest
            "old" -> R.string.sortby_oldest
            else -> R.string.sortby_best
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> onRefresh()
            R.id.sortbyBest -> {
                presenter.sortBy = "best"
                setSubtitle()
                presenter.loadComments()
                swiperefresh.isRefreshing = true
            }
            R.id.sortbyNewest -> {
                presenter.sortBy = "new"
                setSubtitle()
                presenter.loadComments()
                swiperefresh.isRefreshing = true
            }

            R.id.sortbyOldest -> {
                presenter.sortBy = "old"
                setSubtitle()
                presenter.loadComments()
                swiperefresh.isRefreshing = true
            }

            R.id.copyUrl -> copyUrl()

            R.id.share -> shareUrl()

            android.R.id.home -> finish()
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_SORTBY, presenter.sortBy)
        super.onSaveInstanceState(outState)
        adapter.link?.let { linkFragmentData.data = Pair(adapter.link!!, presenter.sortBy) }
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) supportFragmentManager.removeDataFragment(linkFragmentData)
    }


    override fun onRefresh() {
        presenter.loadComments()
        presenter.updateLink()
    }

    override fun showLinkComments(comments: List<LinkComment>) {
        adapter.link?.comments = comments
        loadingView.isVisible = false
        swiperefresh.isRefreshing = false
        adapter.notifyDataSetChanged()
        inputToolbar.show()
        if (linkCommentId != -1 && linkFragmentData.data == null) {
            scrollToComment(linkCommentId)
        }
    }

    override fun scrollToComment(id : Int) {
        adapter.link!!.comments.forEachIndexed({ index, comment ->
            if (comment.id == id) {
                recyclerView.scrollToPosition(index + 1)
            }
        })
    }

    override fun updateLink(link: Link) {
        link.comments = adapter.link?.comments ?: emptyList()
        adapter.link = link
        adapter.notifyDataSetChanged()
        inputToolbar.show()
    }

    override fun openGalleryImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.insert_photo_galery)), BaseInputActivity.USER_ACTION_INSERT_PHOTO)
    }

    override fun sendPhoto(photo: String?, body: String, containsAdultContent : Boolean) {
        presenter.sendReply(body, photo, containsAdultContent)
    }

    override fun sendPhoto(photo: TypedInputStream, body: String, containsAdultContent: Boolean) {
        presenter.sendReply(body, photo, containsAdultContent)
    }

    override fun hideInputToolbar() {
        inputToolbar.hide()
    }

    override fun hideInputbarProgress() {
        inputToolbar.showProgress(false)
    }

    override fun resetInputbarState() {
        hideInputbarProgress()
        inputToolbar.resetState()
        replyLinkId = 0
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                BaseInputActivity.USER_ACTION_INSERT_PHOTO -> {
                    inputToolbar.setPhoto(data?.data)
                }

                BaseInputActivity.REQUEST_CODE -> {
                    onRefresh()
                }
            }
        }
    }

    fun shareUrl() {
        ShareCompat.IntentBuilder
                .from(this)
                .setType("text/plain")
                .setChooserTitle(R.string.share)
                .setText(url)
                .startChooser()
    }

    fun copyUrl() {
        clipboardHelper.copyTextToClipboard(url, "linkUrl")
    }

    val url : String
        get() = "https://www.wykop.pl/link/$linkId"
}