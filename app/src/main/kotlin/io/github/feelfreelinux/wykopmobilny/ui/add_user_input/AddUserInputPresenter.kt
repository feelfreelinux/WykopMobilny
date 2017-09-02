package io.github.feelfreelinux.wykopmobilny.ui.add_user_input

import io.github.feelfreelinux.wykopmobilny.api.ApiResultCallback
import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.base.Presenter

class AddUserInputPresenter(private val apiManager: WykopApi) : AddUserInputContract.Presenter, Presenter<AddUserInputContract.View>() {
    override var formatText: formatDialogCallback = {
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

    override fun subscribe(view: AddUserInputContract.View) {
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

    override fun sendInput() {
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