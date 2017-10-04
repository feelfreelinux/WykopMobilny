package io.github.feelfreelinux.wykopmobilny.ui.input

import io.github.feelfreelinux.wykopmobilny.api.entries.AddResponse
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs.formatDialogCallback

interface BaseInputPresenter {
    val postSendCallback : (AddResponse) -> Unit
    val preSendCallback : () -> Unit?
    fun sendWithPhoto(photo : TypedInputStream)
    fun sendWithPhotoUrl(photo : String?)
}

abstract class InputPresenter<T : BaseInputView> : BasePresenter<T>(), BaseInputPresenter {

    override val postSendCallback : (AddResponse) -> Unit = {
        view?.showNotification = false
    }

    override val preSendCallback = {
        view?.showNotification = true
        view?.exitActivity()
    }

    abstract override fun sendWithPhoto(photo : TypedInputStream)

    abstract override fun sendWithPhotoUrl(photo : String?)
}