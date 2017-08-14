package io.github.feelfreelinux.wykopmobilny.ui.loginscreen

import io.github.feelfreelinux.wykopmobilny.utils.ApiPreferences
import io.github.feelfreelinux.wykopmobilny.utils.WykopApi
import io.github.feelfreelinux.wykopmobilny.utils.setupSubscribeIOAndroid

class LoginScreenPresenter(val apiPreferences: ApiPreferences, val apiManager: WykopApi, var view: LoginScreenContract.View?) : LoginScreenContract.Presenter {
    override val userLoggedCallback : UserLoggedCallback = {
        login, token ->
        apiPreferences.login = login
        apiPreferences.userKey = token
        getUserToken()
    }

    override fun checkIsUserLoggedIn() {
        if (apiPreferences.isUserAuthorized()) getUserToken()
        else view?.setupWebView()
    }

    fun getUserToken() {
        view?.hideWebView()
        apiManager.getUserSessionToken()
                .setupSubscribeIOAndroid()
                .subscribe {
                    (result, _) ->
                    result?.let {
                        apiPreferences.userToken = result.userKey
                        apiPreferences.avatarUrl = result.avatarBig
                        view?.startNavigationActivity()
                    }
                }
    }

}