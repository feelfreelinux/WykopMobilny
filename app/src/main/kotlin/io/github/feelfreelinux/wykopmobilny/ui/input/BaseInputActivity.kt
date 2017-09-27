package io.github.feelfreelinux.wykopmobilny.ui.input

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.notifications.WykopNotificationManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.getMimeType
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.queryFileName
import kotlinx.android.synthetic.main.activity_write_comment.*
import kotlinx.android.synthetic.main.activity_write_comment.view.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

abstract class BaseInputActivity<T : BaseInputPresenter> : AppCompatActivity(), BaseInputView {
    @Inject lateinit var notificationManager : WykopNotificationManagerApi
    private val notificationId by lazy { notificationManager.getNewId() }
    private val markdownDialogCallbacks by lazy { MarkdownDialogActions(this, layoutInflater) as MarkdownDialogCallbacks }

    companion object {
        val EXTRA_RECEIVER = "EXTRA_RECEIVER"
        val EXTRA_BODY = "EXTRA_BODY"
        val USER_ACTION_INSERT_PHOTO = 142
    }

    private val progressNotification : NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(this, "wykopmobilny-uploading")
                .setSmallIcon(R.drawable.ic_upload)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.sending_entry))
                .setOngoing(true)
    }

    var photo: Uri? = null
    var photoUrl: String? = null

    abstract var presenter : T
    override var textBody: String
        get() = body.text.toString()
        set(value) { body.setText(value, TextView.BufferType.EDITABLE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_comment)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        removeImage.setOnClickListener {
            image.setImageBitmap(null)
            imageCardView.isVisible = false
            photo = null
            photoUrl = null
        }

        intent.apply {
            getStringExtra(EXTRA_RECEIVER)?.apply {
                textBody += "$this: "
                selectionPosition = this.length + 2
            }

            getStringExtra(EXTRA_BODY)?.apply {
                textBody += this
                selectionPosition = this.length
            }
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


    override var selectionPosition : Int
        get() = body.selectionStart
        set(pos) {body.setSelection(pos)}

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_comment, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.send -> {
                if (photo != null) presenter.sendWithPhoto(getPhotoTypedInputStream())
                else presenter.sendWithPhotoUrl(if (photoUrl == null) "" else photoUrl!!)
            }
            android.R.id.home -> onBackPressed()
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
    fun getPhotoTypedInputStream(): TypedInputStream =
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

    override fun showErrorDialog(e: Throwable) =
        showExceptionDialog(e)
}