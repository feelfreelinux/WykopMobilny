package io.github.feelfreelinux.wykopmobilny.ui.add_user_input

import android.net.Uri
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs.formatDialogCallback
import java.io.InputStream

interface AddUserInputView : BaseView {
    val receiver: String?
    var textBody: String
    var photo: Uri?
    var showNotification: Boolean
    val entryId: Int?
    val inputType: Int
    var photoUrl: String?
    fun getPhotoInputStreamWithName(): Pair<String, InputStream>
    var selectionPosition: Int
    fun exitActivity()
}