package io.github.feelfreelinux.wykopmobilny.ui.add_user_input

import io.github.feelfreelinux.wykopmobilny.api.enqueue
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs.formatDialogCallback
import android.webkit.MimeTypeMap
import android.content.ContentResolver
import android.net.Uri


class AddUserInputPresenter(private val entriesApi: EntriesApi) : BasePresenter<AddUserInputView>() {
    var formatText: formatDialogCallback = {
        view?.apply {
            textBody.apply {
                val prefix = substring(0, selectionPosition)
                textBody = prefix + it + substring(selectionPosition, length)
                view!!.selectionPosition = prefix.length + it.length
            }
        }
    }

    private val postSendCallback = {
        view?.showNotification = false
    }

    private val preSendCallback = {
        view?.showNotification = true
        view?.exitActivity()
    }

    override fun subscribe(view: AddUserInputView) {
        super.subscribe(view)

        view.apply {
            // Add `@user: ` when replying to comment
            receiver?.let {
                if (textBody.isBlank()) {
                    textBody = "@$receiver: "
                    view.selectionPosition = textBody.length
                }
            }
        }
    }

    fun sendInput() {
        if (!view?.textBody.isNullOrEmpty()) {
            preSendCallback.invoke()

            when (view?.inputType!!) {
                USER_INPUT_NEW_ENTRY -> createNewEntry()
                USER_INPUT_ENTRY_COMMENT -> createEntryComment()
            }
        } else view?.exitActivity()

    }
    private fun createNewEntry() {
        if (view?.photo != null) {
            entriesApi.addEntry(view?.textBody!!, view?.getPhotoTypedInputStream()!!).enqueue(
                    { postSendCallback.invoke() },
                    { view?.showErrorDialog(it) }
            )
        }
        else {
            entriesApi.addEntry(view?.textBody!!, view?.photoUrl!!).enqueue(
                    { postSendCallback.invoke() },
                    { view?.showErrorDialog(it) }
            )
        }
    }

    private fun createEntryComment() {
        if (view?.photo != null) {
            entriesApi.addEntryComment(view?.textBody!!, view?.entryId!!, view?.getPhotoTypedInputStream()!!).enqueue(
                    { postSendCallback.invoke() },
                    { view?.showErrorDialog(it) }
            )
        }
        else {
            entriesApi.addEntryComment(view?.textBody!!, view?.entryId!!, view?.photoUrl!!).enqueue(
                    { postSendCallback.invoke() },
                    { view?.showErrorDialog(it) }
            )
        }
    }
}