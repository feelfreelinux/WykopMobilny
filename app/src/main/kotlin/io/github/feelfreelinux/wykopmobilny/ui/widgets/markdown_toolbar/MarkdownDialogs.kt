package io.github.feelfreelinux.wykopmobilny.ui.widgets.markdown_toolbar

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.FormatDialogCallback
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.lennyfaceDialog

class MarkdownDialogs(val context: Context) {
    fun showLennyfaceDialog(callback: FormatDialogCallback) = lennyfaceDialog(context, callback).show()
}
