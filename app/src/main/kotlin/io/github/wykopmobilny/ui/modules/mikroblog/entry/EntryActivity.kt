package io.github.wykopmobilny.ui.modules.mikroblog.entry

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.wykopmobilny.R
import io.github.wykopmobilny.api.WykopImageFile
import io.github.wykopmobilny.api.suggest.SuggestApi
import io.github.wykopmobilny.base.BaseActivity
import io.github.wykopmobilny.databinding.ActivityEntryBinding
import io.github.wykopmobilny.databinding.DialogVotersBinding
import io.github.wykopmobilny.models.dataclass.Author
import io.github.wykopmobilny.models.dataclass.Entry
import io.github.wykopmobilny.models.dataclass.EntryComment
import io.github.wykopmobilny.models.dataclass.Voter
import io.github.wykopmobilny.ui.adapters.EntryAdapter
import io.github.wykopmobilny.ui.dialogs.VotersDialogListener
import io.github.wykopmobilny.ui.dialogs.createVotersDialogListener
import io.github.wykopmobilny.ui.dialogs.exitConfirmationDialog
import io.github.wykopmobilny.ui.fragments.entrycomments.EntryCommentViewListener
import io.github.wykopmobilny.ui.modules.input.BaseInputActivity
import io.github.wykopmobilny.ui.modules.input.BaseInputActivity.Companion.USER_ACTION_INSERT_PHOTO_CAMERA
import io.github.wykopmobilny.ui.widgets.InputToolbarListener
import io.github.wykopmobilny.utils.prepare
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.wykopmobilny.utils.viewBinding
import javax.inject.Inject

class EntryActivity :
    BaseActivity(),
    EntryDetailView,
    InputToolbarListener,
    SwipeRefreshLayout.OnRefreshListener,
    EntryCommentViewListener {

    companion object {
        const val EXTRA_ENTRY_ID = "ENTRY_ID"
        const val EXTRA_COMMENT_ID = "COMMENT_ID"
        const val EXTRA_IS_REVEALED = "IS_REVEALED"

        fun createIntent(
            context: Context,
            entryId: Int,
            commentId: Int?,
            isRevealed: Boolean
        ) =
            Intent(context, EntryActivity::class.java).apply {
                putExtra(EXTRA_ENTRY_ID, entryId)
                putExtra(EXTRA_IS_REVEALED, isRevealed)
                commentId?.let<Int, Unit> { putExtra(EXTRA_COMMENT_ID, commentId) }
            }
    }

    @Inject
    lateinit var presenter: EntryDetailPresenter

    @Inject
    lateinit var userManager: UserManagerApi

    @Inject
    lateinit var suggestionApi: SuggestApi

    @Inject
    lateinit var adapter: EntryAdapter

    lateinit var votersDialogListener: VotersDialogListener
    lateinit var contentUri: Uri

    private val binding by viewBinding(ActivityEntryBinding::inflate)

    override val enableSwipeBackLayout: Boolean = true
    val entryId by lazy { intent.getIntExtra(EXTRA_ENTRY_ID, -1) }
    private val isRevealed by lazy { intent.getBooleanExtra(EXTRA_IS_REVEALED, false) }
    private val highLightCommentId by lazy { intent.getIntExtra(EXTRA_COMMENT_ID, -1) }

    override fun openVotersMenu() {
        val dialog = BottomSheetDialog(this)
        val votersDialogView = DialogVotersBinding.inflate(layoutInflater)
        votersDialogView.votersTextView.isVisible = false
        dialog.setContentView(votersDialogView.root)
        votersDialogListener = createVotersDialogListener(dialog, votersDialogView)
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
        binding.inputToolbar.addAddressant(author.nick)
    }

    override fun quoteComment(comment: EntryComment) {
        binding.inputToolbar.addQuoteText(comment.body, comment.author.nick)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar.toolbar)

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
        binding.recyclerView.apply {
            prepare()
            // Set margin, adapter
            this.adapter = this@EntryActivity.adapter
        }

        // Prepare InputToolbar
        binding.inputToolbar.setup(userManager, suggestionApi)
        binding.inputToolbar.inputToolbarListener = this
        binding.swiperefresh.setOnRefreshListener(this)

        // Trigger data loading
        binding.loadingView.isVisible = true
        presenter.loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.entry_fragment_menu, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.refresh -> onRefresh()
        }
        return true
    }

    override fun onRefresh() {
        binding.swiperefresh.isRefreshing = true
        presenter.loadData()
    }

    override fun showEntry(entry: Entry) {
        adapter.entry = entry
        entry.comments.forEach { it.entryId = entry.id }
        binding.inputToolbar.setDefaultAddressant(entry.author.nick)
        binding.inputToolbar.setIfIsCommentingPossible(entry.isCommentingPossible)
        binding.inputToolbar.show()
        binding.loadingView.isVisible = false
        binding.swiperefresh.isRefreshing = false
        entry.embed?.isRevealed = isRevealed
        adapter.notifyDataSetChanged()
        if (highLightCommentId != -1) {
            entry.comments.forEachIndexed { index, comment ->
                if (comment.id == highLightCommentId) {
                    binding.recyclerView.scrollToPosition(index + 1)
                }
                binding.recyclerView.refreshDrawableState()
            }
        }
    }

    override fun hideInputToolbar() {
        binding.inputToolbar.hide()
    }

    override fun hideInputbarProgress() {
        binding.inputToolbar.showProgress(false)
    }

    override fun resetInputbarState() {
        binding.inputToolbar.resetState()
    }

    override fun openGalleryImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                getString(R.string.insert_photo_galery)
            ),
            BaseInputActivity.USER_ACTION_INSERT_PHOTO
        )
    }

    override fun onBackPressed() {
        if (binding.inputToolbar.hasUserEditedContent()) {
            exitConfirmationDialog(this) { finish() }?.show()
        } else {
            finish()
        }
    }

    override fun sendPhoto(photo: String?, body: String, containsAdultContent: Boolean) {
        presenter.addComment(body, photo, containsAdultContent)
    }

    override fun sendPhoto(photo: WykopImageFile, body: String, containsAdultContent: Boolean) {
        presenter.addComment(body, photo, containsAdultContent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (!presenter.isSubscribed) presenter.subscribe(this)

            when (requestCode) {
                BaseInputActivity.USER_ACTION_INSERT_PHOTO -> {
                    binding.inputToolbar.setPhoto(data?.data)
                }

                USER_ACTION_INSERT_PHOTO_CAMERA -> {
                    binding.inputToolbar.setPhoto(contentUri)
                }

                BaseInputActivity.EDIT_ENTRY_COMMENT -> {
                    binding.swiperefresh.isRefreshing = true
                    onRefresh()
                }

                BaseInputActivity.EDIT_ENTRY -> {
                    binding.swiperefresh.isRefreshing = true
                    onRefresh()
                }
            }
        }
    }

    override fun openCamera(uri: Uri) {
        contentUri = uri
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, USER_ACTION_INSERT_PHOTO_CAMERA)
    }
}
