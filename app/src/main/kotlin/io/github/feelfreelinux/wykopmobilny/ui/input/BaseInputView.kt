package io.github.feelfreelinux.wykopmobilny.ui.input

import android.net.Uri
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface BaseInputView : BaseView {
    var textBody: String
    var showNotification: Boolean
    var selectionPosition: Int
    fun exitActivity()
}