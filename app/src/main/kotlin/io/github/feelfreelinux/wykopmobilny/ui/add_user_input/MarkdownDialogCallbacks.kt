package io.github.feelfreelinux.wykopmobilny.ui.add_user_input

import android.content.Context
import android.view.LayoutInflater
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.utils.*

interface MarkdownDialogCallbacks {
    fun showUnsavedDialog(callback : () -> Unit)
    fun showMarkdownBoldDialog(callback : formatDialogCallback): Unit?
    fun showMarkdownItalicDialog(callback : formatDialogCallback): Unit?
    fun showMarkdownQuoteDialog(callback : formatDialogCallback): Unit?
    fun showMarkdownLinkDialog(callback : formatDialogCallback): Unit?
    fun showMarkdownSourceCodeDialog(callback : formatDialogCallback): Unit?
    fun showMarkdownSpoilerDialog(callback : formatDialogCallback): Unit?
    fun showLennyfaceDialog(callback : formatDialogCallback): Unit?
    fun showUploadPhotoDialog(formatCallback : formatDialogCallback, insertGalleryImageCallback : () -> Unit): Unit?
    fun showAppExitConfirmationDialog(callback : () -> Unit)
}

class MarkdownDialogActions(val context : Context, private val layoutInflater : LayoutInflater) : MarkdownDialogCallbacks {
    override fun showUnsavedDialog(callback : () -> Unit) { ExitConfirmationDialog(context, callback)?.show() }

    override fun showMarkdownBoldDialog(callback : formatDialogCallback) =
            EditTextFormatDialog(R.string.text_bold, context, { callback.invoke(it.markdownBold) })?.show()

    override fun showMarkdownItalicDialog(callback : formatDialogCallback) =
            EditTextFormatDialog(R.string.text_italic, context, { callback.invoke(it.markdownItalic) })?.show()

    override fun showMarkdownQuoteDialog(callback : formatDialogCallback) =
            EditTextFormatDialog(R.string.text_quote, context, { callback.invoke(it.markdownQuote) })?.show()

    override fun showMarkdownLinkDialog(callback : formatDialogCallback) =
            MarkDownLinkDialog(context, layoutInflater, callback)?.show()

    override fun showMarkdownSourceCodeDialog(callback : formatDialogCallback) =
            EditTextFormatDialog(R.string.insert_code, context, { callback.invoke(it.markdownSourceCode) })?.show()

    override fun showMarkdownSpoilerDialog(callback : formatDialogCallback) =
            EditTextFormatDialog(R.string.insert_spoiler, context, { callback.invoke(it.markdownSpoiler) })?.show()

    override fun showLennyfaceDialog(callback : formatDialogCallback) =
            LennyfaceDialog(context, callback)?.show()

    override fun showUploadPhotoDialog(formatCallback : formatDialogCallback, insertGalleryImageCallback : () -> Unit) =
            UploadPhotoDialog(context, insertGalleryImageCallback, formatCallback)?.show()

    override fun showAppExitConfirmationDialog(callback : () -> Unit) { AppExitConfirmationDialog(context, callback)?.show() }
}