package io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation

import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface MainNavigationView : BaseView {

    fun openFragment(fragment : Fragment)
    fun closeDrawer()
    fun showUsersMenu(value : Boolean)
    fun openLoginActivity()
    fun restartActivity()
}