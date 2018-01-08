package io.github.feelfreelinux.wykopmobilny.ui.widgets.markdown_toolbar

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.*
import io.github.feelfreelinux.wykopmobilny.utils.*

class MarkdownDialogs(val context : Context) {
    fun showMarkdownBoldDialog(callback : formatDialogCallback) =
            EditTextFormatDialog(R.string.text_bold, context, { callback.invoke(it.markdownBold) })?.show()

    fun showMarkdownItalicDialog(callback : formatDialogCallback) =
            EditTextFormatDialog(R.string.text_italic, context, { callback.invoke(it.markdownItalic) })?.show()

    fun showMarkdownQuoteDialog(callback : formatDialogCallback) =
            EditTextFormatDialog(R.string.text_quote, context, { callback.invoke(it.markdownQuote) })?.show()

    fun showMarkdownLinkDialog(callback : formatDialogCallback) =
            MarkDownLinkDialog(context, callback)?.show()

    fun showMarkdownSourceCodeDialog(callback : formatDialogCallback) =
            EditTextFormatDialog(R.string.insert_code, context, { callback.invoke(it.markdownSourceCode) })?.show()

    fun showMarkdownSpoilerDialog(callback : formatDialogCallback) =
            EditTextFormatDialog(R.string.insert_spoiler, context, { callback.invoke(it.markdownSpoiler) })?.show()

    fun showLennyfaceDialog(callback : formatDialogCallback) =
            LennyfaceDialog(context, callback)?.show()

    fun showUploadPhotoDialog(formatCallback : formatDialogCallback, insertGalleryImageCallback : () -> Unit) =
            UploadPhotoDialog(context, insertGalleryImageCallback, formatCallback)?.show()
}