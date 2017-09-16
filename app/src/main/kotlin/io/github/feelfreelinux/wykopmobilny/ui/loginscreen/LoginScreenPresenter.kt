package io.github.feelfreelinux.wykopmobilny.ui.loginscreen

import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi

class LoginScreenPresenter(private val apiPreferences: CredentialsPreferencesApi) : BasePresenter<LoginScreenView>() {
    fun handleUrl(url: String) {
        extractToken(url)?.apply {
            saveUserCredentials(first, second)
            view?.goBackToSplashScreen()
        }
    }
    fun extractToken(url: String) : Pair<String, String>? {
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

        return Pair(login, token)
    }

    fun saveUserCredentials(login : String, token : String) {
        apiPreferences.login = login
        apiPreferences.userKey = token
    }
}