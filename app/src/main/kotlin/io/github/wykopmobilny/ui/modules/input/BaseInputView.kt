package io.github.wykopmobilny.ui.modules.input

import io.github.wykopmobilny.base.BaseView

interface BaseInputView : BaseView {
    var showProgressBar: Boolean
    var textBody: String
    fun exitActivity()
}
