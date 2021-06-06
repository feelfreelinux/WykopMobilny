package io.github.wykopmobilny.ui.modules.input

import io.github.wykopmobilny.api.WykopImageFile
import io.github.wykopmobilny.base.BasePresenter

interface BaseInputPresenter {
    fun sendWithPhoto(photo: WykopImageFile, containsAdultContent: Boolean)
    fun sendWithPhotoUrl(photo: String?, containsAdultContent: Boolean)
}

abstract class InputPresenter<T : BaseInputView> : BasePresenter<T>(), BaseInputPresenter {
    abstract override fun sendWithPhoto(photo: WykopImageFile, containsAdultContent: Boolean)

    abstract override fun sendWithPhotoUrl(photo: String?, containsAdultContent: Boolean)
}
