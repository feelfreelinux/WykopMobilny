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
import io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs.ExitConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.elements.markdown_toolbar.MarkdownToolbarListener
import io.github.feelfreelinux.wykopmobilny.ui.notifications.WykopNotificationManagerApi
import kotlinx.android.synthetic.main.activity_write_comment.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

abstract class BaseInputActivity<T : BaseInputPresenter> : AppCompatActivity(), BaseInputView, MarkdownToolbarListener {
    @Inject lateinit var notificationManager : WykopNotificationManagerApi
    private val notificationId by lazy { notificationManager.getNewId() }

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

    abstract var presenter : T
    override var textBody: String
        get() = body.text.toString()
        set(value) { body.setText(value, TextView.BufferType.EDITABLE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_comment)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        markupToolbar.markdownListener = this
    }


    override var selectionPosition : Int
        get() = body.selectionStart
        set(pos) { body.setSelection(pos) }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_comment, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.send -> {
                val typedInputStream = markupToolbar.getPhotoTypedInputStream()
                if (typedInputStream != null) {
                    presenter.sendWithPhoto(typedInputStream)
                } else {
                    presenter.sendWithPhotoUrl(markupToolbar.photoUrl)
                }
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
                    markupToolbar.photo = data?.data
                }
            }
        }
    }

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
        if (!markupToolbar.hasUserEditedContent()) exitActivity()
        else
            ExitConfirmationDialog(this) {
                exitActivity()
            }?.show()
    }

    override fun openGalleryImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.insert_photo_galery)), USER_ACTION_INSERT_PHOTO)
    }

    override fun showErrorDialog(e: Throwable) =
        showExceptionDialog(e)
}