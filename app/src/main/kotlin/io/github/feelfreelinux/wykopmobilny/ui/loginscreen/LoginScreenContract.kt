package io.github.feelfreelinux.wykopmobilny.ui.loginscreen

interface LoginScreenContract {
    interface View {
        fun startNavigationActivity()
        fun setupWebView()
        fun hideWebView()
    }

    interface Presenter {
        fun checkIsUserLoggedIn()
        val userLoggedCallback : UserLoggedCallback
    }
}