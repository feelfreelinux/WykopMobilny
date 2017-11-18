package io.github.feelfreelinux.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.WykopExceptionParser

fun Context.showExceptionDialog(e : Throwable) {
    ExceptionDialog(this, e)?.show()
}

fun ExceptionDialog(context : Context, e: Throwable) : AlertDialog? {
    val message = when(e) {
        is WykopExceptionParser.WykopApiException -> { "${e.message} (${e.code})" }
        else -> if (e.message.isNullOrEmpty()) e.toString() else e.message
    }
    val alertBuilder = context.createAlertBuilder()

    alertBuilder.run {
        setTitle(context.getString(R.string.error_occured))
        setMessage(message)
        setNeutralButton(android.R.string.ok, null)
        return create()
    }
}