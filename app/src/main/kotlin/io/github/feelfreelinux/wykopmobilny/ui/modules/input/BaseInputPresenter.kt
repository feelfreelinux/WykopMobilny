package io.github.feelfreelinux.wykopmobilny.ui.modules.input

import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter

interface BaseInputPresenter {
    fun sendWithPhoto(photo : TypedInputStream)
    fun sendWithPhotoUrl(photo : String?)
}

abstract class InputPresenter<T : BaseInputView> : BasePresenter<T>(), BaseInputPresenter {
    abstract override fun sendWithPhoto(photo : TypedInputStream)

    abstract override fun sendWithPhotoUrl(photo : String?)
}