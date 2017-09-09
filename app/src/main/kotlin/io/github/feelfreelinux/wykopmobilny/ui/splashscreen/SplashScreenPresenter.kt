package io.github.feelfreelinux.wykopmobilny.ui.splashscreen
import io.github.feelfreelinux.wykopmobilny.base.Presenter
import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.utils.api.IApiPreferences

class SplashScreenPresenter(private val apiPreferences: IApiPreferences, private val apiManager: WykopApi) : Presenter<SplashScreenContract.View>(), SplashScreenContract.Presenter {
    override fun checkIsUserLoggedIn() {
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