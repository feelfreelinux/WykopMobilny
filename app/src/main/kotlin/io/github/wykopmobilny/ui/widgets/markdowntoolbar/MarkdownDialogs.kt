package io.github.wykopmobilny.ui.widgets.markdowntoolbar

import android.content.Context
import io.github.wykopmobilny.ui.dialogs.FormatDialogCallback
import io.github.wykopmobilny.ui.dialogs.lennyfaceDialog

class MarkdownDialogs(val context: Context) {
    fun showLennyfaceDialog(callback: FormatDialogCallback) = lennyfaceDialog(context, callback).show()
}
