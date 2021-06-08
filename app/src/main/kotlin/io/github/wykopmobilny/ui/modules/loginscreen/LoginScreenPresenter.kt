package io.github.wykopmobilny.ui.modules.loginscreen

import io.github.wykopmobilny.api.scraper.ScraperApi
import io.github.wykopmobilny.api.user.LoginApi
import io.github.wykopmobilny.base.BasePresenter
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.utils.intoComposite
import io.github.wykopmobilny.utils.usermanager.LoginCredentials
import io.github.wykopmobilny.utils.usermanager.UserManagerApi

class LoginScreenPresenter(
    private val schedulers: Schedulers,
    private val userManager: UserManagerApi,
    private val scraperApi: ScraperApi,
    private val userApi: LoginApi,
) : BasePresenter<LoginScreenView>() {

    fun handleUrl(url: String) {
        val credentials = extractToken(url) ?: return
        userManager.loginUser(credentials)

        userApi.getUserSessionToken()
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .doOnSuccess {
                userManager.saveCredentials(it)
            }
            .subscribe(
                {
                    view?.goBackToSplashScreen()
                },
                {
                    view?.showErrorDialog(it)
                },
            )
            .intoComposite(compositeObservable)
    }

    private fun extractToken(url: String): LoginCredentials? {
        if (url.contains("ConnectSuccess")) {
            url.apply {
                // Commented a log cause it's static class , to make it coverage we have to use powermock or move to somewhere else.
                // printout(this)
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

            return if (login.isNotEmpty() && token.isNotEmpty()) {
                LoginCredentials(login, token)
            } else null
        }
        return null
    }

    fun importBlacklist() {
        scraperApi.getBlacklist()
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.importBlacklist(it) }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }
}
