package io.github.feelfreelinux.wykopmobilny.ui.modules.input

import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface BaseInputView : BaseView {
    var showProgressBar: Boolean
    var textBody : String
    fun exitActivity()
}