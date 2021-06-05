package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.WykopImageFile
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.HashTagsSuggestionsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.UsersSuggestionsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.WykopSuggestionsTokenizer
import io.github.feelfreelinux.wykopmobilny.ui.widgets.markdown_toolbar.MarkdownToolbarListener
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.input_toolbar.view.*

const val ZERO_WIDTH_SPACE = "\u200B\u200B\u200B\u200B\u200B"

interface InputToolbarListener {
    fun sendPhoto(photo: WykopImageFile, body: String, containsAdultContent: Boolean)
    fun sendPhoto(photo: String?, body: String, containsAdultContent: Boolean)
    fun openGalleryImageChooser()
    fun openCamera(uri: Uri)
}

class InputToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.constraintlayout.widget.ConstraintLayout(context, attrs, defStyleAttr), MarkdownToolbarListener {

    override var selectionStart: Int
        get() = body.selectionStart
        set(value) = body.setSelection(value)

    override var selectionEnd: Int
        get() = body.selectionEnd
        set(value) = body.setSelection(value)

    override var textBody: String
        get() = body.text.toString()
        set(value) = body.setText(value)

    lateinit var userManager: UserManagerApi
    lateinit var suggestApi: SuggestApi

    var defaultText = ""
    var showToolbar = false
    var inputToolbarListener: InputToolbarListener? = null
    var isCommentingPossible = true

    private val usersSuggestionAdapter by lazy { UsersSuggestionsAdapter(context, suggestApi) }
    private val hashTagsSuggestionAdapter by lazy { HashTagsSuggestionsAdapter(context, suggestApi) }

    init {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.itemBackgroundColor, typedValue, true)
        setBackgroundColor(typedValue.data)

        // Inflate view
        View.inflate(context, R.layout.input_toolbar, this)

        markdownToolbar.markdownListener = this
        markdownToolbar.floatingImageView = floatingImageView
        markdownToolbar.remoteImageInserted = {
            enableSendButton()
        }

        // Setup listeners
        body.setOnFocusChangeListener { _, focused ->
            if (focused && !hasUserEditedContent()) {
                textBody = defaultText
                showMarkdownToolbar()
            }
        }

        body.setTokenizer(
            WykopSuggestionsTokenizer(
                {
                    if (body.adapter !is UsersSuggestionsAdapter) {
                        body.setAdapter(usersSuggestionAdapter)
                    }
                },
                {
                    if (body.adapter !is HashTagsSuggestionsAdapter) {
                        body.setAdapter(hashTagsSuggestionAdapter)
                    }
                }
            )
        )
        body.threshold = 3
        body.doOnTextChanged { text, start, before, count ->
            if ((textBody.length > 2 || markdownToolbar.photo != null || markdownToolbar.photoUrl != null)) {
                if (!send.isEnabled) {
                    enableSendButton()
                }
            } else if (textBody.length < 3) {
                disableSendButton()
            }
        }
        send.setOnClickListener {
            showProgress(true)
            val wykopImageFile = markdownToolbar.getWykopImageFile()
            if (wykopImageFile != null) {
                inputToolbarListener?.sendPhoto(
                    wykopImageFile,
                    if (body.text.toString().isNotEmpty()) body.text.toString() else ZERO_WIDTH_SPACE,
                    markdownToolbar.containsAdultContent
                )
            } else {
                inputToolbarListener?.sendPhoto(
                    markdownToolbar.photoUrl,
                    if (body.text.toString().isNotEmpty()) body.text.toString() else ZERO_WIDTH_SPACE,
                    markdownToolbar.containsAdultContent
                )
            }
        }

        disableSendButton()
        if (showToolbar) showMarkdownToolbar()
        else closeMarkdownToolbar()
    }

    override fun setSelection(start: Int, end: Int) = body.setSelection(start, end)

    override fun openCamera(uri: Uri) {
        inputToolbarListener?.openCamera(uri)
    }

    override fun openGalleryImageChooser() {
        inputToolbarListener?.openGalleryImageChooser()
    }

    fun setPhoto(photo: Uri?) {
        enableSendButton()
        markdownToolbar.photo = photo
    }

    fun setup(userManagerApi: UserManagerApi, suggestionApi: SuggestApi) {
        userManager = userManagerApi
        suggestApi = suggestionApi
        show()
    }

    private fun showMarkdownToolbar() {
        if (!hasUserEditedContent()) textBody = defaultText
        markdownToolbar.isVisible = true
        separatorButton.isVisible = true
        send.isVisible = true
        showToolbar = true
    }

    private fun closeMarkdownToolbar() {
        markdownToolbar.isVisible = false
        separatorButton.isVisible = false
        send.isVisible = false
        showToolbar = false
    }

    fun showProgress(shouldShowProgress: Boolean) {
        progressBar.isVisible = shouldShowProgress
        send.isVisible = !shouldShowProgress
    }

    fun resetState() {
        getActivityContext()?.currentFocus?.apply {
            val imm = getActivityContext()?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(windowToken, 0)
        }

        textBody = ""
        selectionStart = textBody.length
        markdownToolbar.apply {
            photo = null
            photoUrl = null
            containsAdultContent = false
        }

        if (textBody.length < 3 && !(markdownToolbar.photo != null || markdownToolbar.photoUrl != null)) disableSendButton()
        closeMarkdownToolbar()
        body.isFocusableInTouchMode = false
        body.isFocusable = false
        body.isFocusableInTouchMode = true
        body.isFocusable = true
    }

    fun setDefaultAddressant(user: String) {
        if (userManager.isUserAuthorized() && userManager.getUserCredentials()!!.login != user) {
            defaultText = "@$user: "
        }
    }

    fun addAddressant(user: String) {
        defaultText = ""
        body.requestFocus()
        textBody += "@$user: "
        if (textBody.length > 2 || markdownToolbar.photo != null || markdownToolbar.photoUrl != null) enableSendButton()
        selectionStart = textBody.length
        showKeyboard()
    }

    fun addQuoteText(quote: String, quoteAuthor: String) {
        defaultText = ""
        body.requestFocus()
        if (textBody.length > 0) textBody += "\n\n"
        textBody += "> ${quote.removeHtml().replace("\n", "\n> ")}\n@$quoteAuthor: "
        selectionStart = textBody.length
        if (textBody.length > 2 || markdownToolbar.photo != null || markdownToolbar.photoUrl != null) enableSendButton()
        showKeyboard()
    }

    fun setCustomHint(hint: String) {
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

    private fun showKeyboard() {
        val imm = getActivityContext()!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(body, InputMethodManager.SHOW_IMPLICIT)
    }

    fun setIfIsCommentingPossible(value: Boolean) {
        isCommentingPossible = value
    }

    fun show() {
        // Only show if user's logged in and has permissions to do it
        // If OP blacklisted user, then user cannot respond to his entries
        isVisible = userManager.isUserAuthorized() && isCommentingPossible
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as? SavedState
        savedState?.let {
            textBody = savedState.text
            if (savedState.isOpened) {
                showMarkdownToolbar()
            } else {
                closeMarkdownToolbar()
            }
        }
        super.onRestoreInstanceState(savedState?.superState)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return SavedState(superState!!, showToolbar, textBody)
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>?) {
        super.dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>?) {
        super.dispatchThawSelfOnly(container)
    }

    class SavedState : View.BaseSavedState {

        val isOpened: Boolean
        val text: String

        constructor(superState: Parcelable, opened: Boolean, body: String) : super(superState) {
            isOpened = opened
            text = body
        }

        constructor(`in`: Parcel) : super(`in`) {
            isOpened = `in`.readInt() == 1
            text = `in`.readString()!!
        }

        override fun writeToParcel(destination: Parcel, flags: Int) {
            super.writeToParcel(destination, flags)
            destination.writeInt(if (isOpened) 1 else 0)
            destination.writeString(text)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}
