package io.github.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import io.github.wykopmobilny.R

fun userNotLoggedInDialog(context: Context): AlertDialog? {
    AlertDialog.Builder(context).run {
        setTitle(context.getString(R.string.error_occured))
        setMessage(context.getString(R.string.user_not_logged_in))
        setPositiveButton(android.R.string.ok, null)
        return create()
    }
}
