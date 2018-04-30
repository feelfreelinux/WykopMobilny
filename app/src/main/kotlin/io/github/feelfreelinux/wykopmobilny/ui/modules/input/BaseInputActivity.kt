package io.github.feelfreelinux.wykopmobilny.ui.modules.input

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.ExitConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.HashTagsSuggestionsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.UsersSuggestionsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.WykopSuggestionsTokenizer
import io.github.feelfreelinux.wykopmobilny.ui.widgets.markdown_toolbar.MarkdownToolbarListener
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeSpoilerHtml
import kotlinx.android.synthetic.main.activity_write_comment.*
import kotlinx.android.synthetic.main.toolbar.*
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import io.github.feelfreelinux.wykopmobilny.R.id.editText
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


abstract class BaseInputActivity<T : BaseInputPresenter> : BaseActivity(), BaseInputView, MarkdownToolbarListener {
    abstract var suggestionApi : SuggestApi
    val usersSuggestionAdapter by lazy { UsersSuggestionsAdapter(this, suggestionApi) }
    val hashTagsSuggestionAdapter by lazy { HashTagsSuggestionsAdapter(this, suggestionApi) }

    companion object {
        const val EXTRA_RECEIVER = "EXTRA_RECEIVER"
        const val EXTRA_BODY = "EXTRA_BODY"
        const val REQUEST_CODE = 106
        const val EDIT_ENTRY_COMMENT = 107
        const val EDIT_ENTRY = 108
        const val EDIT_LINK_COMMENT = 109
        const val USER_ACTION_INSERT_PHOTO = 142
    }

    abstract var presenter : T
    override var textBody: String
        get() = body.text.toString()
        set(value) { body.setText(value, TextView.BufferType.EDITABLE) }

    fun setupSuggestions() {
        body.setTokenizer(WykopSuggestionsTokenizer({
            if (body.adapter !is UsersSuggestionsAdapter)
                body.setAdapter(usersSuggestionAdapter)
        }, {
            if (body.adapter !is HashTagsSuggestionsAdapter)
                body.setAdapter(hashTagsSuggestionAdapter)
        }))
        body.threshold = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_comment)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        intent.apply {
            getStringExtra(EXTRA_RECEIVER)?.apply {
                textBody += "$this: "
                selectionStart = this.length + 2
            }

            getStringExtra(EXTRA_BODY)?.apply { // @TODO Replace it with some regex or parser, its way too hacky now
                if (contains("<a href=\"spoiler:")) {
                    textBody += removeSpoilerHtml().removeHtml()
                } else textBody += removeHtml()
                selectionStart = if (!startsWith("#")) textBody.length else {
                    textBody = "\n$textBody"
                    0
                }
            }
        }

        markupToolbar.markdownListener = this
        markupToolbar.floatingImageView = floatingImageView

        // show focus
        body.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(body, InputMethodManager.SHOW_IMPLICIT)
    }


    override var selectionStart: Int
        get() = body.selectionStart
        set(value) { body.setSelection(value) }
    override var selectionEnd: Int
        get() = body.selectionEnd
        set(value) { body.setSelection(value) }

    override fun setSelection(start : Int, end : Int) {
        body.setSelection(start, end)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_comment, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.send -> {
                val typedInputStream = markupToolbar.getPhotoTypedInputStream()
                if (typedInputStream != null) {
                    presenter.sendWithPhoto(typedInputStream, markupToolbar.containsAdultContent)
                } else {
                    presenter.sendWithPhotoUrl(markupToolbar.photoUrl, markupToolbar.containsAdultContent)
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
    override var showProgressBar: Boolean
        get() = progressBar.isVisible
        set(value) {
            progressBar.isVisible = value
            contentView.isVisible = !value
            markupToolbar.isVisible = !value
        }

    override fun exitActivity() {
        setResult(Activity.RESULT_OK)
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
}