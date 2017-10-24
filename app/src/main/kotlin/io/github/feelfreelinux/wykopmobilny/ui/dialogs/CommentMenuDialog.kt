package io.github.feelfreelinux.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

interface CommentMenuDialogInterface {
    fun removeComment()
    fun editComment()
    fun addReceiver()
    fun copyContent()
}

fun CommentMenuDialog(context : Context, author : Author, userManagerApi: UserManagerApi, listener : CommentMenuDialogInterface): AlertDialog {
    val alertBuilder = context.createAlertBuilder()

    alertBuilder.run {
        if (userManagerApi.isUserAuthorized()) {
            if (userManagerApi.getUserCredentials()!!.login == author.nick) {
                val items = context.resources.getStringArray(R.array.entry_menu_canedit)
                setItems(items, { _, pos ->
                    when (pos) {
                        0 -> { listener.copyContent() }
                        1 -> { listener.editComment() }
                        2 -> {}
                        3 -> { ConfirmationDialog(context) { listener.removeComment() }.show() }
                    }
                })
            } else {
                val items = context.resources.getStringArray(R.array.entry_comment_menu)
                setItems(items, { _, pos ->
                    when (pos) {
                        0 -> { listener.copyContent() }
                        1 -> { listener.addReceiver() }
                    }
                })
            }
        } else {
            val items = context.resources.getStringArray(R.array.entry_menu_notlogged)
            setItems(items, { _, pos ->
                when (pos) {
                    0 -> {
                        listener.copyContent()
                    }
                }
            })
        }
        return create()
    }
}