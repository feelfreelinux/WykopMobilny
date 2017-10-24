package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.comment

import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputView

interface EditEntryCommentView : BaseInputView {
    val entryId : Int
    val commentId : Int

}