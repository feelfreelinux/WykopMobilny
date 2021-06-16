package io.github.wykopmobilny.ui.widgets

import android.content.Context
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import io.github.wykopmobilny.R
import io.github.wykopmobilny.api.WykopImageFile
import io.github.wykopmobilny.api.suggest.SuggestApi
import io.github.wykopmobilny.databinding.InputToolbarBinding
import io.github.wykopmobilny.ui.suggestions.HashTagsSuggestionsAdapter
import io.github.wykopmobilny.ui.suggestions.UsersSuggestionsAdapter
import io.github.wykopmobilny.ui.suggestions.WykopSuggestionsTokenizer
import io.github.wykopmobilny.ui.widgets.markdowntoolbar.MarkdownToolbarListener
import io.github.wykopmobilny.utils.getActivityContext
import io.github.wykopmobilny.utils.layoutInflater
import io.github.wykopmobilny.utils.textview.removeHtml
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.wykopmobilny.utils.usermanager.isUserAuthorized

const val ZERO_WIDTH_SPACE = "\u200B\u200B\u200B\u200B\u200B"

interface InputToolbarListener {
    fun sendPhoto(photo: WykopImageFile, body: String, containsAdultContent: Boolean)
    fun sendPhoto(photo: String?, body: String, containsAdultContent: Boolean)
    fun openGalleryImageChooser()
    fun openCamera(uri: Uri)
}

class InputToolbar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs), MarkdownToolbarListener {

    // Inflate view
    private val binding = InputToolbarBinding.inflate(layoutInflater, this)

    override var selectionStart: Int
        get() = binding.body.selectionStart
        set(value) = binding.body.setSelection(value)

    override var selectionEnd: Int
        get() = binding.body.selectionEnd
        set(value) = binding.body.setSelection(value)

    override var textBody: String
        get() = binding.body.text.toString()
        set(value) = binding.body.setText(value)

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

        binding.markdownToolbar.markdownListener = this
        binding.markdownToolbar.floatingImageView = binding.floatingImageView
        binding.markdownToolbar.remoteImageInserted = { enableSendButton() }

        // Setup listeners
        binding.body.setOnFocusChangeListener { _, focused ->
            if (focused && !hasUserEditedContent()) {
                textBody = defaultText
                showMarkdownToolbar()
            }
        }

        binding.body.setTokenizer(
            WykopSuggestionsTokenizer(
                {
                    if (binding.body.adapter !is UsersSuggestionsAdapter) {
                        binding.body.setAdapter(usersSuggestionAdapter)
                    }
                },
                {
                    if (binding.body.adapter !is HashTagsSuggestionsAdapter) {
                        binding.body.setAdapter(hashTagsSuggestionAdapter)
                    }
                }
            )
        )
        binding.body.threshold = 3
        binding.body.doOnTextChanged { text, start, before, count ->
            if ((textBody.length > 2 || binding.markdownToolbar.photo != null || binding.markdownToolbar.photoUrl != null)) {
                if (!binding.send.isEnabled) {
                    enableSendButton()
                }
            } else if (textBody.length < 3) {
                disableSendButton()
            }
        }
        binding.send.setOnClickListener {
            showProgress(true)
            val wykopImageFile = binding.markdownToolbar.getWykopImageFile()
            if (wykopImageFile != null) {
                inputToolbarListener?.sendPhoto(
                    wykopImageFile,
                    if (binding.body.text.toString().isNotEmpty()) binding.body.text.toString() else ZERO_WIDTH_SPACE,
                    binding.markdownToolbar.containsAdultContent
                )
            } else {
                inputToolbarListener?.sendPhoto(
                    binding.markdownToolbar.photoUrl,
                    if (binding.body.text.toString().isNotEmpty()) binding.body.text.toString() else ZERO_WIDTH_SPACE,
                    binding.markdownToolbar.containsAdultContent
                )
            }
        }

        disableSendButton()
        if (showToolbar) showMarkdownToolbar()
        else closeMarkdownToolbar()
    }

    override fun setSelection(start: Int, end: Int) = binding.body.setSelection(start, end)

    override fun openCamera(uri: Uri) {
        inputToolbarListener?.openCamera(uri)
    }

    override fun openGalleryImageChooser() {
        inputToolbarListener?.openGalleryImageChooser()
    }

    fun setPhoto(photo: Uri?) {
        enableSendButton()
        binding.markdownToolbar.photo = photo
    }

    fun setup(userManagerApi: UserManagerApi, suggestionApi: SuggestApi) {
        userManager = userManagerApi
        suggestApi = suggestionApi
        show()
    }

    private fun showMarkdownToolbar() {
        if (!hasUserEditedContent()) textBody = defaultText
        binding.markdownToolbar.isVisible = true
        binding.separatorButton.isVisible = true
        binding.send.isVisible = true
        showToolbar = true
    }

    private fun closeMarkdownToolbar() {
        binding.markdownToolbar.isVisible = false
        binding.separatorButton.isVisible = false
        binding.send.isVisible = false
        showToolbar = false
    }

    fun showProgress(shouldShowProgress: Boolean) {
        binding.progressBar.isVisible = shouldShowProgress
        binding.send.isVisible = !shouldShowProgress
    }

    fun resetState() {
        getActivityContext()?.currentFocus?.apply {
            val imm = getActivityContext()?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(windowToken, 0)
        }

        textBody = ""
        selectionStart = textBody.length
        binding.markdownToolbar.apply {
            photo = null
            photoUrl = null
            containsAdultContent = false
        }

        if (textBody.length < 3 && !(binding.markdownToolbar.photo != null || binding.markdownToolbar.photoUrl != null)) disableSendButton()
        closeMarkdownToolbar()
        binding.body.isFocusableInTouchMode = false
        binding.body.isFocusable = false
        binding.body.isFocusableInTouchMode = true
        binding.body.isFocusable = true
    }

    fun setDefaultAddressant(user: String) {
        if (userManager.isUserAuthorized() && userManager.getUserCredentials()!!.login != user) {
            defaultText = "@$user: "
        }
    }

    fun addAddressant(user: String) {
        defaultText = ""
        binding.body.requestFocus()
        textBody += "@$user: "
        if (textBody.length > 2 || binding.markdownToolbar.photo != null || binding.markdownToolbar.photoUrl != null) enableSendButton()
        selectionStart = textBody.length
        showKeyboard()
    }

    fun addQuoteText(quote: String, quoteAuthor: String) {
        defaultText = ""
        binding.body.requestFocus()
        if (textBody.isNotEmpty()) textBody += "\n\n"
        textBody += "> ${quote.removeHtml().replace("\n", "\n> ")}\n@$quoteAuthor: "
        selectionStart = textBody.length
        if (textBody.length > 2 || binding.markdownToolbar.photo != null || binding.markdownToolbar.photoUrl != null) enableSendButton()
        showKeyboard()
    }

    fun setCustomHint(hint: String) {
        binding.body.hint = hint
    }

    fun hasUserEditedContent() = textBody != defaultText && binding.markdownToolbar.hasUserEditedContent()

    fun hide() {
        isVisible = false
    }

    fun disableSendButton() {
        binding.send.alpha = 0.3f
        binding.send.isEnabled = false
    }

    fun enableSendButton() {
        binding.send.alpha = 1f
        binding.send.isEnabled = true
    }

    private fun showKeyboard() {
        val imm = getActivityContext()!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.body, InputMethodManager.SHOW_IMPLICIT)
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

    class SavedState : BaseSavedState {

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
