package io.github.feelfreelinux.wykopmobilny.ui.splashscreen
import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.api.IApiPreferences

class SplashScreenPresenter(private val apiPreferences: IApiPreferences, private val apiManager: WykopApi) : BasePresenter<SplashScreenView>() {
    fun checkIsUserLoggedIn() {
        if (apiPreferences.isUserAuthorized()) {
            getUserToken()
        } else {
            view?.startLoginActivity()
        }
    }

    fun getUserToken() {
        apiManager.getUserSessionToken {
            it.fold({
                apiPreferences.userToken = it.userKey
                apiPreferences.avatarUrl = it.avatarBig
                view?.startNavigationActivity()
            }, { view?.showErrorDialog(it) })
        }
    }
}