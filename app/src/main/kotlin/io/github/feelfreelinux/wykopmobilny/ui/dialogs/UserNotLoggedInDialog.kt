package io.github.feelfreelinux.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import io.github.feelfreelinux.wykopmobilny.R

fun userNotLoggedInDialog(context: Context): AlertDialog? {
    context.createAlertBuilder().run {
        setTitle(context.getString(R.string.error_occured))
        setMessage(context.getString(R.string.user_not_logged_in))
        setPositiveButton(android.R.string.ok, null)
        return create()
    }
}