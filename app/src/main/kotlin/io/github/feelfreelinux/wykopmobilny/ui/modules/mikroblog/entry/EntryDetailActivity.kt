package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.core.app.ShareCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.EntryAdapter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.CreateVotersDialogListener
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.ExitConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.VotersDialogListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments.EntryCommentViewListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import io.github.feelfreelinux.wykopmobilny.ui.widgets.InputToolbarListener
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.activity_entry.*
import kotlinx.android.synthetic.main.dialog_voters.view.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject
import android.os.Environment.DIRECTORY_PICTURES
import androidx.core.content.FileProvider
import io.github.feelfreelinux.wykopmobilny.api.WykopImageFile
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity.Companion.USER_ACTION_INSERT_PHOTO_CAMERA
import kotlinx.android.synthetic.main.abc_popup_menu_item_layout.*
import java.io.File
import java.io.IOException
import java.util.*


class EntryActivity : BaseActivity(), EntryDetailView, InputToolbarListener, SwipeRefreshLayout.OnRefreshListener, EntryCommentViewListener {
    lateinit var votersDialogListener : VotersDialogListener
    lateinit var contentUri: Uri

    override fun openVotersMenu() {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val votersDialogView = layoutInflater.inflate(R.layout.dialog_voters, null)
        votersDialogView.votersTextView.isVisible = false
        dialog.setContentView(votersDialogView)
        votersDialogListener = CreateVotersDialogListener(dialog, votersDialogView)
        dialog.show()
    }

    override fun showVoters(voters: List<Voter>) {
        votersDialogListener(voters)
    }

    override fun updateEntry(entry: Entry) {
        adapter.updateEntry(entry)
    }

    override fun updateComment(comment: EntryComment) {
        adapter.updateComment(comment)
    }

    override fun addReply(author: Author) {
        inputToolbar.addAddressant(author.nick)
    }

    override fun quoteComment(comment: EntryComment) {
        inputToolbar.addQuoteText(comment.body, comment.author.nick)
    }

    override val enableSwipeBackLayout: Boolean = true
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

    val highLightCommentId by lazy { intent.getIntExtra(EXTRA_COMMENT_ID, -1) }

    @Inject lateinit var presenter: EntryDetailPresenter
    @Inject lateinit var userManager : UserManagerApi
    @Inject lateinit var suggestionApi : SuggestApi
    @Inject lateinit var adapter : EntryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
        setSupportActionBar(toolbar)

        presenter.subscribe(this)
        adapter.commentId = highLightCommentId
        adapter.commentViewListener = this
        adapter.commentActionListener = presenter
        adapter.entryActionListener = presenter

        presenter.entryId = entryId
        supportActionBar?.apply {
            title = null
            setDisplayHomeAsUpEnabled(true)
        }
        supportActionBar?.title = getString(R.string.entry)

        // Prepare RecyclerView
        recyclerView?.apply {
            prepare()
            // Set margin, adapter
            this.adapter = this@EntryActivity.adapter
        }

        // Prepare InputToolbar
        inputToolbar.setup(userManager, suggestionApi)
        inputToolbar.inputToolbarListener = this
        swiperefresh.setOnRefreshListener(this)

        // Trigger data loading
        loadingView?.isVisible = true
        presenter.loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.entry_fragment_menu, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.refresh -> onRefresh()
        }
        return true
    }

    override fun onRefresh() {
        swiperefresh.isRefreshing = true
        presenter.loadData()
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
        if (highLightCommentId != -1) {
            entry.comments.forEachIndexed({ index, comment ->
                if (comment.id == highLightCommentId) {
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

    override fun sendPhoto(photo: WykopImageFile, body: String, containsAdultContent: Boolean) {
        presenter.addComment(body, photo, containsAdultContent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (!presenter.isSubscribed) presenter.subscribe(this)

            when (requestCode) {
                BaseInputActivity.USER_ACTION_INSERT_PHOTO -> {
                    inputToolbar.setPhoto(data?.data)
                }

                USER_ACTION_INSERT_PHOTO_CAMERA -> {
                    inputToolbar.setPhoto(contentUri)

                }

                BaseInputActivity.EDIT_ENTRY_COMMENT -> {
                    swiperefresh.isRefreshing = true
                    onRefresh()
                }

                BaseInputActivity.EDIT_ENTRY -> {
                    swiperefresh.isRefreshing = true
                    onRefresh()
                }
            }
        }
    }

    override fun openCamera(uri : Uri) {
        contentUri = uri
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, BaseInputActivity.USER_ACTION_INSERT_PHOTO_CAMERA)
    }
}