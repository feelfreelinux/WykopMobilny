package io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen

import io.github.feelfreelinux.wykopmobilny.api.user.LoginApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.LoginCredentials
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Single

class LoginScreenPresenter(
		private val schedulers : Schedulers,
		private val userManager: UserManagerApi,
		private val userApi: LoginApi) : BasePresenter<LoginScreenView>() {
	fun handleUrl(url: String) {
		extractToken(url)?.apply {
			userManager.loginUser(this)

			compositeObservable.add(
					userApi.getUserSessionToken()
							.subscribeOn(schedulers.backgroundThread())
							.observeOn(schedulers.mainThread())
							.flatMap { it ->
								userManager.saveCredentials(it)
								Single.just(it)
							}
							.subscribe({ view?.goBackToSplashScreen() }, { view?.showErrorDialog(it) })
			)
		}
	}

	private fun extractToken(url: String): LoginCredentials? {
		if (url.contains("ConnectSuccess")) {
			url.apply {
				printout(this)
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
		return null
	}
}