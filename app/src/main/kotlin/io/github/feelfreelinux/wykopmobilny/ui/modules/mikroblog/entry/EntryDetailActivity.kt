package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
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
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.EntryLinkParser
import kotlinx.android.synthetic.main.activity_entry.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class EntryActivity : BaseActivity(), EntryDetailView, InputToolbarListener, SwipeRefreshLayout.OnRefreshListener {
    var entryId = 0

    companion object {
        val EXTRA_ENTRY_ID = "ENTRY_ID"
        val EXTRA_COMMENT_ID = "COMMENT_ID"
        val EXTRA_FRAGMENT_KEY = "ENTRY_ACTIVITY_#"

        fun createIntent(context: Context, entryId: Int, commentId: Int?): Intent {
            val intent = Intent(context, EntryActivity::class.java)
            intent.putExtra(EntryActivity.EXTRA_ENTRY_ID, entryId)
            commentId?.let {
                intent.putExtra(EntryActivity.EXTRA_COMMENT_ID, commentId)
            }
            return intent
        }
    }

    @Inject lateinit var clipboardHelper : ClipboardHelperApi
    @Inject lateinit var presenter: EntryDetailPresenter
    private lateinit var entryFragmentData: DataFragment<Entry>
    private val adapter by lazy { EntryDetailAdapter({ inputToolbar.addAddressant(it.nick) },
            intent.getIntExtra(EXTRA_COMMENT_ID, -1)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
        setSupportActionBar(toolbar)

        entryId =
                if (intent.data != null) EntryLinkParser.getEntryId(intent.dataString)!!
                else intent.getIntExtra(EXTRA_ENTRY_ID, -1)

        supportActionBar?.apply {
            title = null
            setDisplayHomeAsUpEnabled(true)
        }
        supportActionBar?.title = null

        // Prepare RecyclerView
        recyclerView.apply {
            prepare()
            // Set margin, adapter
            addItemDecoration(EntryCommentItemDecoration(resources.getDimensionPixelOffset(R.dimen.comment_section_left_margin)))
            this.adapter = this@EntryActivity.adapter
        }

        // Prepare InputToolbar
        inputToolbar.inputToolbarListener = this

        swiperefresh.setOnRefreshListener(this)
        entryFragmentData = supportFragmentManager.getDataFragmentInstance(EXTRA_FRAGMENT_KEY + entryId)

    }

    private fun loadData() {
        if (entryFragmentData.data != null)
            adapter.entry = entryFragmentData.data
        else {
            // Trigger data loading
            loadingView.isVisible = true
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
                swiperefresh.isRefreshing = true
                onRefresh()
            }
            R.id.share -> {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share))
                i.putExtra(Intent.EXTRA_TEXT, url)
                startActivity(Intent.createChooser(i, resources.getString(R.string.share)))
            }
        }
        return true
    }

    override fun onRefresh() {
        presenter.loadData()
    }

    override fun showEntry(entry: Entry) {
        adapter.entry = entry
        inputToolbar.setDefaultAddressant(entry.author.nick)
        inputToolbar.show()
        loadingView.isVisible = false
        swiperefresh.isRefreshing = false
        adapter.notifyDataSetChanged()
        if (intent.hasExtra(EXTRA_COMMENT_ID) && entryFragmentData.data == null) {
            entry.comments.forEachIndexed({ index, comment ->
                if (comment.id == intent.getIntExtra(EXTRA_COMMENT_ID, -1))
                    recyclerView.scrollToPosition(index + 1)
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

    override fun sendPhoto(photo: String?, body: String) {
        presenter.addComment(body, photo)
    }

    override fun sendPhoto(photo: TypedInputStream, body: String) {
        presenter.addComment(body, photo)
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

    val url : String
        get() = "https://wykop.pl/wpis/$entryId"
}