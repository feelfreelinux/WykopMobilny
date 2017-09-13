package io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs

import android.app.AlertDialog
import android.content.Context

fun Context.showExceptionDialog(e : Exception) {
    ExceptionDialog(this, e)?.show()
}

fun ExceptionDialog(context : Context, e: Exception) : AlertDialog? {
    val message = if (e.message.isNullOrEmpty()) e.toString() else e.message
    val alertBuilder = context.createAlertBuilder()

    alertBuilder.run {
        setTitle("Wystąpił nieoczekiwany problem")
        setMessage(message)
        setPositiveButton(android.R.string.ok, null)
        return create()
    }
}