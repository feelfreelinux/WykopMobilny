package io.github.feelfreelinux.wykopmobilny.ui.modules.input

import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter

interface BaseInputPresenter {
    fun sendWithPhoto(photo : TypedInputStream, containsAdultContent: Boolean)
    fun sendWithPhotoUrl(photo : String?, containsAdultContent: Boolean)
}

abstract class InputPresenter<T : BaseInputView> : BasePresenter<T>(), BaseInputPresenter {
    abstract override fun sendWithPhoto(photo : TypedInputStream, containsAdultContent : Boolean)

    abstract override fun sendWithPhotoUrl(photo : String?, containsAdultContent : Boolean)
}