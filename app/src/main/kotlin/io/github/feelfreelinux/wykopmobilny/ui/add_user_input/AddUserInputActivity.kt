package io.github.feelfreelinux.wykopmobilny.ui.add_user_input

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.view.Menu
import android.view.MenuItem
import android.webkit.MimeTypeMap
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.ui.notifications.WykopNotificationManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.synthetic.main.activity_write_comment.*
import kotlinx.android.synthetic.main.activity_write_comment.view.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.InputStream
import javax.inject.Inject


const val EXTRA_INPUT_TYPE = "INPUT_TYLE"
const val EXTRA_ENTRY_ID = "ENTRY_ID"
const val EXTRA_RECEIVER = "PROFILE_RECEIVER"
const val USER_INPUT_NEW_ENTRY = 0
const val USER_INPUT_ENTRY_COMMENT = 1
const val USER_EDIT_ENTRY = 2
const val USER_EDIT_ENTRY_COMMENT = 3
const val USER_ACTION_INSERT_PHOTO = 4

fun Context.launchNewEntryUserInput(receiver: String?) {
    val intent = Intent(this, AddUserInputActivity::class.java)
    intent.putExtra(EXTRA_INPUT_TYPE, USER_INPUT_NEW_ENTRY)
    intent.putExtra(EXTRA_RECEIVER, receiver)
    startActivity(intent)
}

fun Context.launchEntryCommentUserInput(entryId : Int, receiver : String?) {
    val intent = Intent(this, AddUserInputActivity::class.java)
    intent.putExtra(EXTRA_INPUT_TYPE, USER_INPUT_ENTRY_COMMENT)
    intent.putExtra(EXTRA_ENTRY_ID, entryId)
    intent.putExtra(EXTRA_RECEIVER, receiver)
    startActivity(intent)
}

class AddUserInputActivity : BaseActivity(), AddUserInputView {
    override val inputType by lazy { intent.getIntExtra(EXTRA_INPUT_TYPE, -1) }
    override val receiver: String? by lazy { intent.getStringExtra(EXTRA_RECEIVER) }
    override val entryId: Int? by lazy { intent.getIntExtra(EXTRA_ENTRY_ID, -1) }

    @Inject lateinit var notificationManager : WykopNotificationManagerApi
    private val notificationId by lazy { notificationManager.getNewId() }
    private val markdownDialogCallbacks by lazy { MarkdownDialogActions(this, layoutInflater) as MarkdownDialogCallbacks}

    private val progressNotification : NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(this, "wykopmobilny-uploading")
                .setSmallIcon(R.drawable.ic_upload)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.sending_entry))
                .setOngoing(true)
    }

    override var photo: Uri? = null
    override var photoUrl: String? = null

    @Inject lateinit var presenter : AddUserInputPresenter
    override var textBody: String
        get() = body.text.toString()
        set(value) { body.setText(value, TextView.BufferType.EDITABLE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_comment)
        setSupportActionBar(toolbar)
        setToolbarTitle()
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)

        removeImage.setOnClickListener {
            image.setImageBitmap(null)
            imageCardView.isVisible = false
            photo = null
            photoUrl = null
        }

        menuInflater.inflate(R.menu.markdown_menu, markupToolbar.amvMenu.menu)
        markupToolbar.amvMenu.setOnMenuItemClickListener(onMarkdownMenuItemSelected)
    }

    private val onMarkdownMenuItemSelected : (MenuItem) -> Boolean = {
        // This handles title bar action's callbacks
        markdownDialogCallbacks.apply {
            when (it.itemId) {
                R.id.insert_photo -> showUploadPhotoDialog(
                        { insertImageFromUrl(it) },
                        { openGalleryImageChooser() })
                R.id.insert_emoticon -> showLennyfaceDialog(presenter.formatText)
                R.id.format_bold -> showMarkdownBoldDialog(presenter.formatText)
                R.id.format_italic -> showMarkdownItalicDialog(presenter.formatText)
                R.id.format_quote -> showMarkdownQuoteDialog(presenter.formatText)
                R.id.insert_link -> showMarkdownLinkDialog(presenter.formatText)
                R.id.insert_code -> showMarkdownSourceCodeDialog(presenter.formatText)
                R.id.insert_spoiler -> showMarkdownSpoilerDialog(presenter.formatText)
            }
        }
        true
    }

    private fun setToolbarTitle() {
        // Sets title for different input types
        supportActionBar?.apply {
            title = when (inputType) {
                USER_INPUT_ENTRY_COMMENT -> getString(R.string.add_comment)
                USER_INPUT_NEW_ENTRY -> getString(R.string.add_new_entry)
                USER_EDIT_ENTRY -> getString(R.string.edit_entry)
                USER_EDIT_ENTRY_COMMENT -> getString(R.string.edit_comment)
                else -> null
            }
        }
    }

    override var selectionPosition : Int
        get() = body.selectionStart
        set(pos) {body.setSelection(pos)}

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_comment, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.send -> presenter.sendInput()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                // Gallery chooser's callback
                USER_ACTION_INSERT_PHOTO -> {
                    data?.data?.apply {
                        insertImageFromContentUri(this)
                    }
                }
            }
        }
    }

    // Returns resolved photo output stream
    override fun getPhotoTypedInputStream(): TypedInputStream =
        TypedInputStream(photo?.queryFileName(contentResolver)!!,
                photo?.getMimeType(contentResolver)!!,
                contentResolver.openInputStream(photo))

    // Shows "sending entry" progress in notification
    override var showNotification : Boolean
        get() = false
        set(value) {
            if (value) {
                progressNotification.setProgress(0, 0, true)
                notificationManager.updateNotification(notificationId, progressNotification.build())
            } else notificationManager.cancelNotification(notificationId)
        }

    override fun exitActivity() {
        finish()
    }

    override fun onBackPressed() {
        if (!hasUserEditedContent()) exitActivity()
        else markdownDialogCallbacks.showUnsavedDialog( { exitActivity() } )
    }

    private fun hasUserEditedContent() : Boolean = !textBody.isNullOrEmpty() || photo != null

    private fun insertImageFromUrl(url : String) {
        if (!url.isEmpty() && url.contains("http")) {
            photo = null
            photoUrl = url
            imageCardView.isVisible = true
            image.loadImage(url)
        }
    }

    private fun insertImageFromContentUri(uri : Uri) {
        photo = uri
        photoUrl = null
        imageCardView.isVisible = true
        image.setImageURI(uri)
    }

    private fun openGalleryImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.insert_photo_galery)), USER_ACTION_INSERT_PHOTO)
    }

}