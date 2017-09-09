package io.github.feelfreelinux.wykopmobilny.ui.splashscreen

import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface SplashScreenContract {
    interface View : BaseView {
        fun startLoginActivity()
        fun startNavigationActivity()
    }

    interface Presenter : BasePresenter<View> {
        fun checkIsUserLoggedIn()
    }
}