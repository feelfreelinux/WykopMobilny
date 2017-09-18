package io.github.feelfreelinux.wykopmobilny.ui.splashscreen
import io.github.feelfreelinux.wykopmobilny.api.user.UserApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.api.enqueue

class SplashScreenPresenter(private val apiPreferences: CredentialsPreferencesApi, private val userApi: UserApi) : BasePresenter<SplashScreenView>() {
    fun checkIsUserLoggedIn() {
        if (apiPreferences.isUserAuthorized()) {
            getUserToken()
        } else {
            view?.startLoginActivity()
        }
    }

    fun getUserToken() {
        userApi.getUserSessionToken().enqueue(
                {
                    val user = it.body()!!

                    apiPreferences.userToken = user.userKey
                    apiPreferences.avatarUrl = user.avatarBig
                    view?.startNavigationActivity()
                }, { view?.showErrorDialog(it) }
        )
    }
}