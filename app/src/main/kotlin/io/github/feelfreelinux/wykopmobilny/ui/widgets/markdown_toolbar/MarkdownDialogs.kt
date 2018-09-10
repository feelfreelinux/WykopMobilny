package io.github.feelfreelinux.wykopmobilny.ui.widgets.markdown_toolbar

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.LennyfaceDialog
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.formatDialogCallback

class MarkdownDialogs(val context: Context) {
  fun showLennyfaceDialog(callback: formatDialogCallback) = LennyfaceDialog(context, callback).show()
}