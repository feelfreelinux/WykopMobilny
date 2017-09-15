package io.github.feelfreelinux.wykopmobilny.ui.add_user_input

import io.github.feelfreelinux.wykopmobilny.api.ApiResultCallback
import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs.formatDialogCallback

class AddUserInputPresenter(private val apiManager: WykopApi) : BasePresenter<AddUserInputView>() {
    var formatText: formatDialogCallback = {
        view?.apply {
            textBody.apply {
                val prefix = substring(0, selectionPosition)
                textBody = prefix + it + substring(selectionPosition, length)
                view!!.selectionPosition = prefix.length + it.length
            }
        }
    }

    private val postSendCallback : ApiResultCallback<Any> = {
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
        if (view?.photo != null)
            apiManager.addNewEntry(view?.textBody!!, view?.getPhotoInputStreamWithName()!!, postSendCallback)
        else apiManager.addNewEntry(view?.textBody!!, view?.photoUrl, postSendCallback)
    }

    private fun createEntryComment() {
        if (view?.photo != null)
            apiManager.addNewEntryComment(view?.entryId!!, view?.textBody!!, view?.getPhotoInputStreamWithName()!!, postSendCallback)
        else apiManager.addNewEntryComment(view?.entryId!!, view?.textBody!!, view?.photoUrl, postSendCallback)
    }
}