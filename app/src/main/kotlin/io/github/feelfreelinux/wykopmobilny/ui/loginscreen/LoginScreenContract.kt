package io.github.feelfreelinux.wykopmobilny.ui.loginscreen

import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface LoginScreenContract {
    interface View : BaseView {
        fun startNavigationActivity()
        fun setupWebView()
        fun hideWebView()
    }

    interface Presenter : BasePresenter<View> {
        val userLoggedCallback : UserLoggedCallback
    }
}