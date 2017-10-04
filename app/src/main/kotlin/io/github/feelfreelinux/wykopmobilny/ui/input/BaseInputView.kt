package io.github.feelfreelinux.wykopmobilny.ui.input

import android.net.Uri
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface BaseInputView : BaseView {
    var showNotification: Boolean
    var textBody : String
    fun exitActivity()
}