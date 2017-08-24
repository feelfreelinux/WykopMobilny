package io.github.feelfreelinux.wykopmobilny.ui.loginscreen

import io.github.feelfreelinux.wykopmobilny.base.Presenter
import io.github.feelfreelinux.wykopmobilny.utils.api.ApiPreferences
import io.github.feelfreelinux.wykopmobilny.api.WykopApi

class LoginScreenPresenter(val apiPreferences: ApiPreferences, val apiManager: WykopApi) : Presenter<LoginScreenContract.View>(), LoginScreenContract.Presenter {

    override fun subscribe(view: LoginScreenContract.View) {
        super.subscribe(view)
        checkIsUserLoggedIn()
    }

    override val userLoggedCallback : UserLoggedCallback = {
        login, token ->
        apiPreferences.login = login
        apiPreferences.userKey = token
        getUserToken()
    }

    private fun checkIsUserLoggedIn() {
        if (apiPreferences.isUserAuthorized()) getUserToken()
        else view?.setupWebView()
    }

    fun getUserToken() {
        view?.hideWebView()
        apiManager.getUserSessionToken {
            it.fold({
                        apiPreferences.userToken = it.userKey
                        apiPreferences.avatarUrl = it.avatarBig
                        view?.startNavigationActivity()
                    },
                    { view?.showErrorDialog(it) })
        }
    }

}