package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.ui.widgets.markdown_toolbar.MarkdownToolbarListener
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.input_toolbar.view.*
import javax.inject.Inject
import android.util.TypedValue
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.HashTagsSuggestionsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.UsersSuggestionsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.WykopSuggestionsTokenizer


interface InputToolbarListener {
    fun sendPhoto(photo : TypedInputStream, body : String)
    fun sendPhoto(photo : String?, body : String)
    fun openGalleryImageChooser()
}

class InputToolbar : ConstraintLayout, MarkdownToolbarListener {
    override var selectionPosition: Int
        get() = body.selectionStart
        set(value) { body.setSelection(value) }

    override var textBody: String
        get() =  body.text.toString()
        set(value) { body.setText(value) }

    @Inject lateinit var userManager : UserManagerApi
    @Inject lateinit var suggestApi : SuggestApi
    val usersSuggestionAdapter by lazy { UsersSuggestionsAdapter(context, suggestApi) }
    val hashTagsSuggestionAdapter by lazy { HashTagsSuggestionsAdapter(context, suggestApi) }

    var defaultText = ""

    override fun openGalleryImageChooser() {
        inputToolbarListener?.openGalleryImageChooser()
    }

    fun setPhoto(photo : Uri?) {
        markdownToolbar.photo = photo
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var inputToolbarListener : InputToolbarListener? = null

    init {
        WykopApp.uiInjector.inject(this)
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.cardViewColor, typedValue, true)
        setBackgroundColor(typedValue.data)
        show()

        // Inflate view
        View.inflate(context, R.layout.input_toolbar, this)

        markdownToolbar.markdownListener = this
        markdownToolbar.floatingImageView = floatingImageView

        // Setup listeners
        show_markdown_menu.setOnClickListener {
            showMarkdownToolbar()
        }

        markdown_close.setOnClickListener {
            closeMarkdownToolbar()
        }

        body.setOnFocusChangeListener {
            _, focused ->
            if (focused && !hasUserEditedContent()) {
                textBody = defaultText
            }
        }

        body.setTokenizer(WykopSuggestionsTokenizer({
            if (body.adapter !is UsersSuggestionsAdapter)
                body.setAdapter(usersSuggestionAdapter)
        }, {
            if (body.adapter !is HashTagsSuggestionsAdapter)
                body.setAdapter(hashTagsSuggestionAdapter)
        }))
        body.threshold = 3
        send.setOnClickListener {
            showProgress(true)
            val typedInputStream = markdownToolbar.getPhotoTypedInputStream()
            if (typedInputStream != null) {
                inputToolbarListener?.sendPhoto(typedInputStream, body.text.toString())
            } else {
                inputToolbarListener?.sendPhoto(markdownToolbar.photoUrl, body.text.toString())
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
        selectionPosition = textBody.length
        markdownToolbar.apply {
            photo = null
            photoUrl = null
        }
        closeMarkdownToolbar()
    }

    fun setDefaultAddressant(user : String) {
        if (userManager.isUserAuthorized() && userManager.getUserCredentials()!!.login != user) {
            defaultText = "@$user: "
        }
    }

    fun addAddressant(user : String) {
        defaultText = ""
        body.requestFocus()
        textBody += "@$user: "
        selectionPosition = textBody.length
    }

    fun setCustomHint(hint : String) {
        body.hint = hint
    }

    fun hasUserEditedContent() = textBody != defaultText && markdownToolbar.hasUserEditedContent()

    fun hide() {
        isVisible = false
    }

    fun show() {
        // Only show if user's logged in
        isVisible = userManager.isUserAuthorized()
    }
}