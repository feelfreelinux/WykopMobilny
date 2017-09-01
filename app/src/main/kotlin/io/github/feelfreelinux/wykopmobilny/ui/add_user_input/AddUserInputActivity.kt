package io.github.feelfreelinux.wykopmobilny.ui.add_user_input

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import kotlinx.android.synthetic.main.activity_write_comment.*
import kotlinx.android.synthetic.main.activity_write_comment.view.*
import kotlinx.android.synthetic.main.toolbar.*
import android.R.attr.data
import android.app.NotificationManager
import android.app.ProgressDialog
import android.net.Uri
import android.support.v4.app.NotificationCompat
import com.github.salomonbrys.kodein.instance
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.ui.notifications.WykopNotificationManager
import io.github.feelfreelinux.wykopmobilny.utils.*
import java.io.File
import java.io.InputStream


const val EXTRA_INPUT_TYPE = "INPUT_TYLE"
const val EXTRA_ENTRY_ID = "ENTRY_ID"
const val EXTRA_RECEIVER = "PROFILE_RECEIVER"
const val USER_INPUT_NEW_ENTRY = 0
const val USER_INPUT_ENTRY_COMMENT = 1
const val USER_EDIT_ENTRY = 2
const val USER_EDIT_ENTRY_COMMENT = 3
const val USER_ACTION_INSERT_PHOTO = 4

fun Context.launchNewEntryUserInput(receiver: String?) {
    val intent = Intent(this, AddCommentActivity::class.java)
    intent.putExtra(EXTRA_INPUT_TYPE, USER_INPUT_NEW_ENTRY)
    intent.putExtra(EXTRA_RECEIVER, receiver)
    startActivity(intent)
}

fun Context.launchEntryCommentUserInput(entryId : Int, receiver : String?) {
    val intent = Intent(this, AddCommentActivity::class.java)
    intent.putExtra(EXTRA_INPUT_TYPE, USER_INPUT_ENTRY_COMMENT)
    intent.putExtra(EXTRA_ENTRY_ID, entryId)
    intent.putExtra(EXTRA_RECEIVER, receiver)
    startActivity(intent)
}

class AddCommentActivity : BaseActivity(), AddUserInputContract.View {
    override val inputType by lazy { intent.getIntExtra(EXTRA_INPUT_TYPE, -1) }
    override val receiver: String? by lazy { intent.getStringExtra(EXTRA_RECEIVER) }
    override val entryId: Int? by lazy { intent.getIntExtra(EXTRA_ENTRY_ID, -1) }

    private val notificationManager by kodein.instance<WykopNotificationManager>()
    private val notificationId by lazy { notificationManager.getNewId() }

    private val progressNotification : NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(this, "wykopmobilny-uploading")
                .setSmallIcon(R.drawable.ic_upload)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.sending_entry))
                .setOngoing(true)
    }

    override var photo: Uri? = null
    override var photoUrl: String? = null

    val presenter by lazy { AddUserInputPresenter(kodein.instanceValue()) }
    override var textBody: String?
        get() = body.text.toString()
        set(value) { body.setText(value, TextView.BufferType.EDITABLE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_comment)
        setSupportActionBar(toolbar)
        setToolbarTitle()
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
        val formatCallback : formatDialogCallback = {
            var text = ""
            textBody?.let { text = textBody!! }

            val selection = body.selectionStart
            val preString = text.substring(0, selection)
            val postString = text.substring(selection, text.length)

            textBody = preString + it + postString
            setEditTextSelection(selection + it.length)
        }

        when (it.itemId) {
            R.id.insert_photo -> showUploadPhotoDialog (
                    {
                        if (!it.isEmpty() && it.contains("http")) {
                            photo = null
                            photoUrl = it
                            imageCardView.isVisible = true
                            image.loadImage(it)
                        }
                    })
            R.id.insert_emoticon -> showLennyfaceDialog (formatCallback)
            R.id.format_bold -> showMarkdownBoldDialog (formatCallback)
            R.id.format_italic -> showMarkdownItalicDialog (formatCallback)
            R.id.format_quote -> showMarkdownQuoteDialog (formatCallback)
            R.id.insert_link -> showMarkdownLinkDialog (formatCallback)
            R.id.insert_code -> showMarkdownSourceCodeDialog (formatCallback)
            R.id.insert_spoiler -> showMarkdownSpoilerDialog (formatCallback)
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

    override fun setEditTextSelection(pos : Int) {
        body.setSelection(pos)
    }

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
                USER_ACTION_INSERT_PHOTO -> {
                    photo = data?.data
                    photoUrl = null
                    imageCardView.isVisible = true
                    image.setImageURI(photo)
                }
            }
        }
    }

    override fun getPhotoInputStreamWithName(): Pair<String, InputStream> =
        Pair(photo?.queryFileName(contentResolver)!!, contentResolver.openInputStream(photo))

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
        if (!hasUserEditedContent()) finish()
        else showUnsavedDialog()
    }

    private fun hasUserEditedContent() : Boolean = !textBody.isNullOrEmpty() || photo != null

    private fun showUnsavedDialog() { ExitConfirmationDialog(this, { finish() })?.show() }

    private fun showMarkdownBoldDialog(callback : formatDialogCallback) =
            EditTextFormatDialog(R.string.text_bold, this, { callback.invoke(it.markdownBold) })?.show()

    private fun showMarkdownItalicDialog(callback : formatDialogCallback) =
            EditTextFormatDialog(R.string.text_italic, this, { callback.invoke(it.markdownItalic) })?.show()

    private fun showMarkdownQuoteDialog(callback : formatDialogCallback) =
            EditTextFormatDialog(R.string.text_quote, this, { callback.invoke(it.markdownQuote) })?.show()

    private fun showMarkdownLinkDialog(callback : formatDialogCallback) =
            MarkDownLinkDialog(this, callback)?.show()

    private fun showMarkdownSourceCodeDialog(callback : formatDialogCallback) =
            EditTextFormatDialog(R.string.insert_code, this, { callback.invoke(it.markdownSourceCode) })?.show()

    private fun showMarkdownSpoilerDialog(callback : formatDialogCallback) =
            EditTextFormatDialog(R.string.insert_spoiler, this, { callback.invoke(it.markdownSpoiler) })?.show()

    private fun showLennyfaceDialog(callback : formatDialogCallback) =
            LennyfaceDialog(this, callback)?.show()

    private fun showUploadPhotoDialog(callback : formatDialogCallback)  {
        val galleryUploadCallback = {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,
                    getString(R.string.insert_photo_galery)), USER_ACTION_INSERT_PHOTO)
        }

        UploadPhotoDialog(this, galleryUploadCallback, callback)?.show()
    }

}