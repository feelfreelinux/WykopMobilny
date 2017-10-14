package io.github.feelfreelinux.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

interface EntryMenuDialogListener {
    fun removeEntry()
    fun editEntry()
    fun copyEntry()
}

fun EntryMenuDialog(context : Context, author : Author, userManagerApi: UserManagerApi, listener : EntryMenuDialogListener) : AlertDialog {
    val alertBuilder = context.createAlertBuilder()

    alertBuilder.run {
        if (userManagerApi.isUserAuthorized()) {
            if (userManagerApi.getUserCredentials()!!.login == author.nick) {
                val items = context.resources.getStringArray(R.array.entry_menu_canedit)
                setItems(items, { _, pos ->
                    when (pos) {
                        0 -> { listener.copyEntry() }
                        1 -> { listener.editEntry() }
                        2 -> {}
                        3 -> { ConfirmationDialog(context) { listener.removeEntry() }.show() }
                    }
                })
            } else {
                val items = context.resources.getStringArray(R.array.entry_menu)
                setItems(items, { _, pos ->
                    when (pos) {
                        0 -> { listener.copyEntry() }
                    }
                })
            }
        } else {
            val items = context.resources.getStringArray(R.array.entry_menu_notlogged)
            setItems(items, { _, pos ->
                when (pos) {
                    0 -> {
                        listener.copyEntry()
                    }
                }
            })
        }
        return create()
    }
}