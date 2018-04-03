package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.HashTagsSuggestionsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.UsersSuggestionsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.WykopSuggestionsTokenizer
import io.github.feelfreelinux.wykopmobilny.ui.widgets.markdown_toolbar.MarkdownToolbarListener
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.input_toolbar.view.*


interface InputToolbarListener {
    fun sendPhoto(photo : TypedInputStream, body : String, containsAdultContent: Boolean)
    fun sendPhoto(photo : String?, body : String, containsAdultContent : Boolean)
    fun openGalleryImageChooser()
}

class InputToolbar : ConstraintLayout, MarkdownToolbarListener {
    override var selectionPosition: Int
        get() = body.selectionStart
        set(value) { body.setSelection(value) }

    override var textBody: String
        get() =  body.text.toString()
        set(value) { body.setText(value) }

    lateinit var userManager : UserManagerApi
    lateinit var suggestApi : SuggestApi
    val usersSuggestionAdapter by lazy { UsersSuggestionsAdapter(context, suggestApi) }
    val hashTagsSuggestionAdapter by lazy { HashTagsSuggestionsAdapter(context, suggestApi) }

    var defaultText = ""
    var showToolbar = false

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
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.itemBackgroundColor, typedValue, true)
        setBackgroundColor(typedValue.data)

        // Inflate view
        View.inflate(context, R.layout.input_toolbar, this)

        markdownToolbar.markdownListener = this
        markdownToolbar.floatingImageView = floatingImageView

        // Setup listeners
        body.setOnFocusChangeListener {
            _, focused ->
            if (focused && !hasUserEditedContent()) {
                textBody = defaultText
                showMarkdownToolbar()
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
        body.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (textBody.length > 2 && send.isEnabled == false) {
                    enableSendButton()
                } else if (textBody.length < 3) disableSendButton()
            }
        })
        send.setOnClickListener {
            showProgress(true)
            val typedInputStream = markdownToolbar.getPhotoTypedInputStream()
            if (typedInputStream != null) {
                inputToolbarListener?.sendPhoto(typedInputStream, body.text.toString(), markdownToolbar.containsAdultContent)
            } else {
                inputToolbarListener?.sendPhoto(markdownToolbar.photoUrl, body.text.toString(), markdownToolbar.containsAdultContent)
            }
        }

        disableSendButton()
    }

    fun setup(userManagerApi: UserManagerApi, suggestionApi: SuggestApi) {
        userManager = userManagerApi
        suggestApi = suggestionApi
        show()
    }

    fun showMarkdownToolbar() {
        if (!hasUserEditedContent()) textBody = defaultText
        markdownToolbar.isVisible = true
        separatorButton.isVisible = true
        send.isVisible = true
    }

    fun closeMarkdownToolbar() {
        markdownToolbar.isVisible = false
        separatorButton.isVisible = false
        send.isVisible = false
    }

    fun showProgress(shouldShowProgress : Boolean) {
        progressBar.isVisible = shouldShowProgress
        send.isVisible = !shouldShowProgress
    }

    fun resetState() {
        getActivityContext()?.currentFocus?.apply {
            val imm = getActivityContext()?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(windowToken, 0)
        }

        textBody = defaultText
        selectionPosition = textBody.length
        markdownToolbar.apply {
            photo = null
            photoUrl = null
            containsAdultContent = false
        }

        if (textBody.length < 3) disableSendButton()
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
        if (textBody.length > 2) enableSendButton()
        selectionPosition = textBody.length
    }

    fun addQuoteText(quote : String, quoteAuthor : String) {
        defaultText = ""
        body.requestFocus()
        if(textBody.length > 0) textBody += "\n\n"
        textBody += "> ${quote.removeHtml().replace("\n", "\n> ")}\n@$quoteAuthor: "
        selectionPosition = textBody.length
        if (textBody.length > 2) enableSendButton()
    }

    fun setCustomHint(hint : String) {
        body.hint = hint
    }

    fun hasUserEditedContent() = textBody != defaultText && markdownToolbar.hasUserEditedContent()

    fun hide() {
        isVisible = false
    }

    fun disableSendButton() {
        send.alpha = 0.3f
        send.isEnabled = false
    }

    fun enableSendButton() {
        send.alpha = 1f
        send.isEnabled = true
    }

    fun show() {
        // Only show if user's logged in
        isVisible = userManager.isUserAuthorized()
    }
}