package io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
import io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments.LinkCommentViewListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import io.github.feelfreelinux.wykopmobilny.ui.widgets.InputToolbarListener
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.activity_link_details.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class LinkDetailsActivity : BaseActivity(), LinkDetailsView, androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener, InputToolbarListener, LinkCommentViewListener {
    override fun updateLinkComment(comment: LinkComment) {
        adapter.updateLinkComment(comment)
    }

    override fun replyComment(comment: LinkComment) {
        replyLinkId = comment.id
        inputToolbar.addAddressant(comment.author.nick)
    }

    override fun setCollapsed(comment: LinkComment, isCollapsed: Boolean) {
        adapter.link?.comments?.forEach {
            when (comment.id) {
                it.id -> {
                    it.isCollapsed = isCollapsed
                    it.isParentCollapsed = false
                }
                it.parentId -> it.isParentCollapsed = isCollapsed
            }
        }
        adapter.notifyDataSetChanged()
    }

    override val enableSwipeBackLayout: Boolean = true
    @Inject
    lateinit var settingsApi: SettingsPreferencesApi

    companion object {
        val EXTRA_LINK = "LINK_PARCEL"
        val EXTRA_LINK_ID = "EXTRA_LINKID"
        val EXTRA_COMMENT_ID = "EXTRA_COMMENT_ID"

        fun createIntent(context: Context, link: Link): Intent {
            val intent = Intent(context, LinkDetailsActivity::class.java)
            intent.putExtra(EXTRA_LINK, link)
            return intent
        }

        fun createIntent(context: Context, linkId: Int, commentId: Int? = null): Intent {
            val intent = Intent(context, LinkDetailsActivity::class.java)
            intent.putExtra(EXTRA_LINK_ID, linkId)
            intent.putExtra(EXTRA_COMMENT_ID, commentId)
            return intent
        }
    }

    var replyLinkId: Int = 0

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

    @Inject
    lateinit var userManagerApi: UserManagerApi
    @Inject
    lateinit var suggestionsApi: SuggestApi

    @Inject
    lateinit var presenter: LinkDetailsPresenter

    @Inject
    lateinit var linkPreferences: LinksPreferencesApi
    @Inject
    lateinit var adapter: LinkDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_details)
        setSupportActionBar(toolbar)
        presenter.subscribe(this)
        adapter.linkCommentViewListener = this
        adapter.linkHeaderActionListener = presenter
        adapter.linkCommentActionListener = presenter
        supportActionBar?.apply {
            title = null
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_sort)

        // Prepare RecyclerView
        recyclerView?.apply {
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

        presenter.sortBy = linkPreferences.linkCommentsDefaultSort ?: "best"
        adapter.notifyDataSetChanged()
        loadingView?.isVisible = true
        hideInputToolbar()
        if (adapter.link != null) {
            presenter.loadComments()
            presenter.updateLink()
        } else {
            presenter.loadLinkAndComments()
        }


        setSubtitle()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.link_details_menu, menu)
        return true
    }

    fun setSubtitle() {
        supportActionBar?.setSubtitle(when (presenter.sortBy) {
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
                linkPreferences.linkCommentsDefaultSort = "best"
                setSubtitle()
                presenter.loadComments()
                swiperefresh?.isRefreshing = true
            }
            R.id.sortbyNewest -> {
                presenter.sortBy = "new"
                linkPreferences.linkCommentsDefaultSort = "new"
                setSubtitle()
                presenter.loadComments()
                swiperefresh?.isRefreshing = true
            }

            R.id.sortbyOldest -> {
                presenter.sortBy = "old"
                linkPreferences.linkCommentsDefaultSort = "old"
                setSubtitle()
                presenter.loadComments()
                swiperefresh?.isRefreshing = true
            }

            android.R.id.home -> finish()
        }
        return true
    }

    override fun onRefresh() {
        presenter.loadComments()
        presenter.updateLink()
        adapter.notifyDataSetChanged()
    }

    override fun showLinkComments(comments: List<LinkComment>) {
        adapter.link?.comments = comments.toMutableList()
        // Auto-Collapse comments depending on settings
        if (settingsApi.hideLinkCommentsByDefault) {
            adapter.link?.comments?.forEach {
                if (it.parentId == it.id) {
                    it.isCollapsed = true
                } else {
                    it.isParentCollapsed = true
                }
            }
        }
        loadingView?.isVisible = false
        swiperefresh?.isRefreshing = false
        adapter.notifyDataSetChanged()
        inputToolbar.show()
        if (linkCommentId != -1 && adapter.link != null) {
            scrollToComment(linkCommentId)
        }
    }

    override fun scrollToComment(id: Int) {
        adapter.link!!.comments.forEachIndexed({ index, comment ->
            if (comment.id == id) {
                recyclerView?.scrollToPosition(index + 1)
            }
        })
    }

    override fun updateLink(link: Link) {
        link.comments = adapter.link?.comments ?: mutableListOf()
        adapter.updateLinkHeader(link)
        inputToolbar.show()
    }

    override fun openGalleryImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.insert_photo_galery)), BaseInputActivity.USER_ACTION_INSERT_PHOTO)
    }

    override fun sendPhoto(photo: String?, body: String, containsAdultContent: Boolean) {
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

                BaseInputActivity.EDIT_LINK_COMMENT -> {
                    val commentId = data?.getIntExtra("commentId", -1)
                    onRefresh()
                    scrollToComment(commentId ?: -1)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
        presenter.dispose()
    }
}