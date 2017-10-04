package io.github.feelfreelinux.wykopmobilny.ui.elements.input_toolbar

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.ui.elements.markdown_toolbar.MarkdownToolbarListener
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.input_toolbar.view.*
import javax.inject.Inject


class InputToolbar : ConstraintLayout, MarkdownToolbarListener {
    override var selectionPosition: Int
        get() = body.selectionStart
        set(value) { body.setSelection(value) }

    override var textBody: String
        get() =  body.text.toString()
        set(value) { body.setText(value) }

    @Inject lateinit var userManager : UserManagerApi

    var defaultText = ""

    override fun openGalleryImageChooser() {
        // @TODO add image handler
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var sendPhotoListener : ((TypedInputStream, String) -> Unit)? = null
    var sendPhotoUrlListener : ((String?, String) -> Unit)? = null


    init {
        WykopApp.uiInjector.inject(this)

        // Only show if user's logged in
        isVisible = userManager.isUserAuthorized()

        // Inflate view
        View.inflate(context, R.layout.input_toolbar, this)

        markdownToolbar.markdownListener = this

        // Setup listeners
        show_markdown_menu.setOnClickListener {
            showMarkdownToolbar()
        }

        markdown_close.setOnClickListener {
            closeMarkdownToolbar()
        }

        body.setOnFocusChangeListener {
            _, focus -> if (focus) showMarkdownToolbar()
        }

        send.setOnClickListener {
            showProgress(true)
            val typedInputStream = markdownToolbar.getPhotoTypedInputStream()
            if (typedInputStream != null) {
                sendPhotoListener?.invoke(typedInputStream, body.text.toString())
            } else {
                sendPhotoUrlListener?.invoke(markdownToolbar.photoUrl, body.text.toString())
            }
        }
    }

    fun showMarkdownToolbar() {
        markdownToolbarHolder.isVisible = true
        show_markdown_menu.isVisible = false
    }

    fun closeMarkdownToolbar() {
        markdownToolbarHolder.isVisible = false
        show_markdown_menu.isVisible = true
    }

    fun showProgress(shouldShowProgress : Boolean) {
        progressBar.isVisible = shouldShowProgress
        send.isVisible = !shouldShowProgress
    }

    fun resetState() {
        textBody = defaultText
        selectionPosition = defaultText.length
        markdownToolbar.apply {
            photo = null
            photoUrl = null
        }
        closeMarkdownToolbar()
        body.clearFocus()
    }
}