package io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs

import android.app.AlertDialog
import android.content.Context
import io.github.feelfreelinux.wykopmobilny.R

fun UserNotLoggedInDialog(context : Context) : AlertDialog? {
    val alertBuilder = context.createAlertBuilder()

    alertBuilder.run {
        setTitle(context.getString(R.string.error_occured))
        setMessage(context.getString(R.string.user_not_logged_in))
        return create()
    }
}