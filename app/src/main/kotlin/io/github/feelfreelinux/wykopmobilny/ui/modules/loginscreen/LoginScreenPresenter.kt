package io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen

import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.LoginCredentials
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

class LoginScreenPresenter(private val userManager: UserManagerApi) : BasePresenter<LoginScreenView>() {
    fun handleUrl(url: String) {
        extractToken(url)?.apply {
            userManager.loginUser(this)
            view?.goBackToSplashScreen()
        }
    }

    private fun extractToken(url: String) : LoginCredentials? {
        url.apply {
            if (!contains("/token/") and !contains("/login/")) {
                view?.showErrorDialog(IllegalStateException("Redirect url ($url) doesn't contain userData"))
                return null
            }
        }

        val login = url
                .split("/token/").first()
                .substringAfterLast("/login/")
        val token = url
                .split("/token/").last()
                .replace("/", "")

        return if (login.isNotEmpty() && token.isNotEmpty())
            LoginCredentials(login, token)
        else null
    }
}