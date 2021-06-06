package io.github.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import io.github.wykopmobilny.R

fun exitConfirmationDialog(context: Context, callback: () -> Unit): AlertDialog? {
    context.createAlertBuilder().run {
        setTitle(R.string.confirm_exit)
        setPositiveButton(android.R.string.yes, { _, _ -> callback.invoke() })
        setNeutralButton(android.R.string.no, null)
        return create()
    }
}
