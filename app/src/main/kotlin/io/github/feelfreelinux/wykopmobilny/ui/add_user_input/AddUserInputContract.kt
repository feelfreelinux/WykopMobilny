package io.github.feelfreelinux.wykopmobilny.ui.add_user_input

import android.net.Uri
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.BaseView
import java.io.InputStream

interface AddUserInputContract {
    interface View : BaseView {
        val receiver : String?
        var textBody : String
        var photo : Uri?
        var showNotification : Boolean
        val entryId : Int?
        val inputType : Int
        var photoUrl : String?
        fun getPhotoInputStreamWithName() : Pair<String, InputStream>
        var selectionPosition : Int
        fun exitActivity()
    }
    interface Presenter : BasePresenter<View> {
        fun sendInput()
        var formatText : formatDialogCallback
    }
}