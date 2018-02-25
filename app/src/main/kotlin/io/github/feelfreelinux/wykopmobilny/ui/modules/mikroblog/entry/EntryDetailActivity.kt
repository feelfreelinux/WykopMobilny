package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.EntryDetailAdapter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.decorators.EntryCommentItemDecoration
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.ExitConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import io.github.feelfreelinux.wykopmobilny.ui.widgets.InputToolbarListener
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.api.convertMarkdownToHtml
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.activity_entry.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class EntryActivity : BaseActivity(), EntryDetailView, InputToolbarListener, SwipeRefreshLayout.OnRefreshListener {
    val entryId by lazy { intent.getIntExtra(EXTRA_ENTRY_ID, -1) }
    val isRevealed by lazy { intent.getBooleanExtra(EXTRA_IS_REVEALED, false) }

    companion object {
        val EXTRA_ENTRY_ID = "ENTRY_ID"
        val EXTRA_COMMENT_ID = "COMMENT_ID"
        val EXTRA_FRAGMENT_KEY = "ENTRY_ACTIVITY_#"
        val EXTRA_IS_REVEALED = "IS_REVEALED"

        fun createIntent(context: Context, entryId: Int, commentId: Int?, isRevealed: Boolean): Intent {
            val intent = Intent(context, EntryActivity::class.java)
            intent.putExtra(EntryActivity.EXTRA_ENTRY_ID, entryId)
            intent.putExtra(EntryActivity.EXTRA_IS_REVEALED, isRevealed)
            commentId?.let {
                intent.putExtra(EntryActivity.EXTRA_COMMENT_ID, commentId)
            }
            return intent
        }
    }

    @Inject lateinit var clipboardHelper : ClipboardHelperApi
    @Inject lateinit var presenter: EntryDetailPresenter
    @Inject lateinit var userManager : UserManagerApi
    @Inject lateinit var suggestionApi : SuggestApi
    private lateinit var entryFragmentData: DataFragment<Entry>
    @Inject lateinit var adapter : EntryDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
        setSupportActionBar(toolbar)
        adapter.commentId = intent.getIntExtra(EXTRA_COMMENT_ID, -1)
        adapter.addReceiverListener = { inputToolbar.addAddressant(it.nick) }
        adapter.quoteCommentListener = {
            inputToolbar.addQuoteText(it.body, it.author.nick)
        }

        supportActionBar?.apply {
            title = null
            setDisplayHomeAsUpEnabled(true)
        }
        supportActionBar?.title = null

        // Prepare RecyclerView
        recyclerView?.apply {
            prepare()
            // Set margin, adapter
            addItemDecoration(EntryCommentItemDecoration(resources.getDimensionPixelOffset(R.dimen.comment_section_left_margin)))
            this.adapter = this@EntryActivity.adapter
        }

        // Prepare InputToolbar
        inputToolbar.setup(userManager, suggestionApi)
        inputToolbar.inputToolbarListener = this

        swiperefresh.setOnRefreshListener(this)
        entryFragmentData = supportFragmentManager.getDataFragmentInstance(EXTRA_FRAGMENT_KEY + entryId)

    }

    private fun loadData() {
        if (entryFragmentData.data != null)
            adapter.entry = entryFragmentData.data
        else {
            // Trigger data loading
            loadingView?.isVisible = true
            presenter.loadData()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe(this)
        presenter.entryId = entryId
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.entry_fragment_menu, menu)
        return true
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        entryFragmentData.data = adapter.entry
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) supportFragmentManager.removeDataFragment(entryFragmentData)
        presenter.unsubscribe()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.copyUrl -> clipboardHelper.copyTextToClipboard(url, "entryUrl")
            R.id.refresh -> {
                swiperefresh?.isRefreshing = true
                onRefresh()
            }
            R.id.share -> shareUrl()
        }
        return true
    }

    override fun onRefresh() {
        presenter.loadData()
        adapter.notifyDataSetChanged()
    }

    override fun showEntry(entry: Entry) {
        adapter.entry = entry
        entry.comments.forEach { it.entryId = entry.id }
        inputToolbar.setDefaultAddressant(entry.author.nick)
        inputToolbar.show()
        loadingView?.isVisible = false
        swiperefresh?.isRefreshing = false
        entry.embed?.isRevealed = isRevealed
        adapter.notifyDataSetChanged()
        if (intent.hasExtra(EXTRA_COMMENT_ID) && entryFragmentData.data == null) {
            entry.comments.forEachIndexed({ index, comment ->
                if (comment.id == intent.getIntExtra(EXTRA_COMMENT_ID, -1)) {
                    recyclerView?.scrollToPosition(index + 1)
                }
                recyclerView?.refreshDrawableState()
            })
        }
    }

    override fun hideInputToolbar() {
        inputToolbar.hide()
    }

    override fun hideInputbarProgress() {
        inputToolbar.showProgress(false)
    }

    override fun resetInputbarState() {
        inputToolbar.resetState()
    }

    override fun openGalleryImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.insert_photo_galery)), BaseInputActivity.USER_ACTION_INSERT_PHOTO)
    }

    override fun onBackPressed() {
        if (inputToolbar.hasUserEditedContent()) {
            ExitConfirmationDialog(this, {
                finish()
            })?.show()
        } else finish()
    }

    override fun sendPhoto(photo: String?, body: String, containsAdultContent: Boolean) {
        presenter.addComment(body, photo, containsAdultContent)
    }

    override fun sendPhoto(photo: TypedInputStream, body: String, containsAdultContent: Boolean) {
        presenter.addComment(body, photo, containsAdultContent)
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

                BaseInputActivity.EDIT_ENTRY_COMMENT -> {
                    val commentId = data?.getIntExtra("commentId", -1)
                    val commentBody = data?.getStringExtra("commentBody")
                    if (commentId != -1 && commentBody != null) {
                        entryFragmentData.data?.comments?.filter { it.id == commentId }?.get(0)?.body = commentBody.convertMarkdownToHtml()
                        onRefresh()
                    }
                }

                BaseInputActivity.EDIT_ENTRY -> {
                    val entryBody = data?.getStringExtra("entryBody")?.convertMarkdownToHtml()
                    if (entryBody != null) {
                        entryFragmentData.data?.body = entryBody
                        onRefresh()
                    }
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

    val url : String
        get() = "https://wykop.pl/wpis/$entryId"
}