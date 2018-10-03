package io.github.feelfreelinux.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.WykopExceptionParser
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity

fun Context.showExceptionDialog(e: Throwable) {
    if (this is BaseActivity && isRunning) {
        exceptionDialog(this, e)?.show()
    }
}

private fun exceptionDialog(context: Context, e: Throwable): AlertDialog? {
    val message = when (e) {
        is WykopExceptionParser.WykopApiException -> {
            "${e.message} (${e.code})"
        }
        else -> if (e.message.isNullOrEmpty()) e.toString() else e.message
    }
    context.createAlertBuilder().run {
        setTitle(context.getString(R.string.error_occured))
        setMessage(message)
        setPositiveButton(android.R.string.ok, null)
        return create()
    }
}