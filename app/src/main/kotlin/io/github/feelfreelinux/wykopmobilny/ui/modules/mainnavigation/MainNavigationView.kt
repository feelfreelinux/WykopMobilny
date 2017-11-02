package io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation

import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface MainNavigationView : BaseView {
    fun showUsersMenu(value : Boolean)
    fun restartActivity()
    fun showNotImplementedToast()
}