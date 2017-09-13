package io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs

import android.app.AlertDialog
import android.content.Context
import io.github.feelfreelinux.wykopmobilny.R

fun ExitConfirmationDialog(context : Context, callback : () -> Unit) : AlertDialog? {
    val alertBuilder = context.createAlertBuilder()

    alertBuilder.run {
        setTitle(R.string.confirm_exit)
        setPositiveButton(android.R.string.yes, {_, _ -> callback.invoke() })
        setNeutralButton(android.R.string.no, null)
        return create()
    }
}