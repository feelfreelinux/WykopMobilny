package io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen

import io.github.feelfreelinux.wykopmobilny.api.user.UserApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.LoginCredentials
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Single

class LoginScreenPresenter(private val userManager: UserManagerApi,
                           private val subscriptionHelperApi: SubscriptionHelperApi,
                           private val userApi: UserApi) : BasePresenter<LoginScreenView>() {
    fun handleUrl(url: String) {
        extractToken(url)?.apply {
            userManager.loginUser(this)
            subscriptionHelperApi.subscribe(userApi.getUserSessionToken()
                    .flatMap { it ->
                        userManager.saveCredentials(it)
                        Single.just(it)
                    },
                    { view?.goBackToSplashScreen() },
                    { view?.showErrorDialog(IllegalStateException("Redirect url ($url) doesn't contain userData")) },
                    this)
        }
    }

    override fun unsubscribe() {
        subscriptionHelperApi.dispose(this)
        super.unsubscribe()
    }

    private fun extractToken(url: String): LoginCredentials? {
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