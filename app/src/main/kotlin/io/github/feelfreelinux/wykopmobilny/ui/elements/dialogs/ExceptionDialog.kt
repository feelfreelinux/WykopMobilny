package io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs

import android.app.AlertDialog
import android.content.Context
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.WykopRequestBodyConverterFactory
import io.github.feelfreelinux.wykopmobilny.api.WykopUserNotLoggedInException

fun Context.showExceptionDialog(e : Throwable) {
    ExceptionDialog(this, e)?.show()
}

fun ExceptionDialog(context : Context, e: Throwable) : AlertDialog? {
    val message = when(e) {
        is WykopRequestBodyConverterFactory.ApiException -> { "${e.message} (${e.code})" }
        is WykopUserNotLoggedInException -> { context.getString(R.string.user_not_logged_in) }
        else -> if (e.message.isNullOrEmpty()) e.toString() else e.message
    }
    val alertBuilder = context.createAlertBuilder()

    alertBuilder.run {
        setTitle(context.getString(R.string.unexpected_error_occured))
        setMessage(message)
        return create()
    }
}